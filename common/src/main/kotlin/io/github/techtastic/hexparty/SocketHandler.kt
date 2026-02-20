package io.github.techtastic.hexparty

import io.github.techtastic.hexparty.casting.mishap.MishapPacketTooBig
import io.github.techtastic.hexparty.casting.mishap.MishapSocketConnectionError
import io.github.techtastic.hexparty.utils.hexpartyOperatorUtils
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import kotlin.concurrent.Volatile

object SocketHandler {
    private val EXECUTOR = Executors.newSingleThreadExecutor()
    private val SOCKETS = ConcurrentHashMap<UUID, ManagedSocket>()

    fun getSocket(id: UUID) = SOCKETS[id]

    fun createSocket(host: String?, port: Int): UUID {
        host?.let { host -> hexpartyOperatorUtils.checkBlacklist("$host:$port") }
            ?: hexpartyOperatorUtils.checkBlacklist(":$port")

        val id = UUID.randomUUID()
        SOCKETS[id] = ManagedSocket(id, host, port)
        return id
    }

    class ManagedSocket(val id: UUID, val host: String?, val port: Int) {
        private val socket = Socket()
        private val received = ConcurrentLinkedQueue<ByteArray>()

        @Volatile
        private var closed = false

        init {
            EXECUTOR.submit {
                try {
                    val address = host?.let { host -> InetSocketAddress(host, port) } ?: InetSocketAddress(port)
                    socket.connect(address, 10000)
                    socket.soTimeout = 100
                    receiving()
                } catch (e: IOException) {
                    close()
                }
            }
        }

        private fun receiving() {
            EXECUTOR.submit {
                while (!closed && !socket.isClosed) {
                    try {
                        val input = socket.getInputStream()
                        if (input.available() > 0) {
                            val bytes = ByteArray(8192)
                            val read = input.read(bytes)
                            if (read > 0)
                                received.add(bytes.copyOf(read))

                            if (read == -1) {
                                close()
                                break
                            }
                        }
                    } catch (e: Exception) {
                        when (e) {
                            is SocketTimeoutException -> {}
                            else -> {
                                close()
                                break
                            }
                        }
                    }
                }
            }
        }

        fun sendData(data: ByteArray) {
            if (closed || socket.isClosed)
                throw MishapSocketConnectionError(IOException("Socket it closed"))

            if (data.size > 65536)
                throw MishapPacketTooBig(data.size)

           EXECUTOR.submit {
               try {
                   socket.getOutputStream().write(data)
                   socket.getOutputStream().flush()
               } catch (e: IOException) {
                   close()
                   throw MishapSocketConnectionError(e)
               }
           }
        }

        fun hasData() = received.isNotEmpty()

        fun receiveData(): ByteArray? = received.poll()

        fun getAllReceived(): List<String>? {
            if (received.isEmpty()) return null
            val list = mutableListOf<String>()
            var data = receiveData()
            while (data != null && list.size < 1023) {
                list.add(data.toString(StandardCharsets.UTF_8))
                data = receiveData()
            }
            return list
        }

        fun isOpen() = !closed && socket.isConnected && !socket.isClosed

        fun close() {
            if (closed) return
            closed = true

            try {
                socket.close()
            } catch (ignored: IOException) {}

            SOCKETS.remove(id)
        }
    }
}