package io.github.techtastic.hexweb.blocks.circles.impetuses

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import io.github.techtastic.hexweb.HexWeb
import io.github.techtastic.hexweb.SocketHandler
import io.github.techtastic.hexweb.blocks.HexWebBlockEntities
import io.github.techtastic.hexweb.casting.mishap.MishapNoSocket
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.util.*

class BlockEntitySocketImpetus(pWorldPosition: BlockPos?, pBlockState: BlockState?) :
    BlockEntityAbstractImpetus(HexWebBlockEntities.SOCKET_IMPETUS.get(), pWorldPosition, pBlockState) {

    var host: String? = null
    var port: Int = -1
    private var socketId: UUID? = null

    private var executed = true

    fun getOrCreateSocket(): SocketHandler.ManagedSocket {
        socketId?.let { id -> SocketHandler.getSocket(id)?.let { socket ->
            if (host != socket.host || port != socket.port) {
                socket.close()
                socketId = null
            } else return socket
        } ?: run { socketId = null } }

        if (port in 1..65535) {
            socketId = SocketHandler.createSocket(host, port)
            socketId?.let { return SocketHandler.getSocket(it) ?: throw MishapNoSocket(it) }
        }
        throw MishapNoSocket(socketId)
    }

    override fun endExecution() {
        super.endExecution()
        executed = true
    }

    override fun loadModData(tag: CompoundTag) {
        super.loadModData(tag)
        if (tag.contains("hexweb\$host"))
            host = tag.getString("hexweb\$host")
        if (tag.contains("hexweb\$port"))
            port = tag.getInt("hexweb\$port")
    }

    override fun saveModData(tag: CompoundTag) {
        host?.let { tag.putString("hexweb\$host", it) }
        if (port != -1) tag.putInt("hexweb\$port", port)
        super.saveModData(tag)
    }

    companion object {
        fun serverTick(level: Level, pos: BlockPos, bs: BlockState, self: BlockEntitySocketImpetus) {
            if (self.port == -1 || !self.executed) return

            try {
                val socket = self.getOrCreateSocket()
                if (!socket.hasData()) return

                self.startExecution(null)
                self.executionState?.let { state ->
                    socket.getAllReceived()
                        ?.let { state.currentImage = state.currentImage.copy(listOf(ListIota(it.map(StringIota::make)))) }
                }
            } catch (mishap: Mishap) {
                HexWeb.LOGGER.warn("Failed to get/create Socket!", mishap)
                self.postMishap(Component.literal(mishap.localizedMessage))
            }
        }
    }
}