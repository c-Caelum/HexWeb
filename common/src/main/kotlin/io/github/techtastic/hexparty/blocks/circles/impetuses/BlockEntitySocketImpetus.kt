package io.github.techtastic.hexparty.blocks.circles.impetuses

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import io.github.techtastic.hexparty.hexparty
import io.github.techtastic.hexparty.SocketHandler
import io.github.techtastic.hexparty.blocks.hexpartyBlockEntities
import io.github.techtastic.hexparty.casting.mishap.MishapNoSocket
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.util.*

class BlockEntitySocketImpetus(pWorldPosition: BlockPos?, pBlockState: BlockState?) :
    BlockEntityAbstractImpetus(hexpartyBlockEntities.SOCKET_IMPETUS.get(), pWorldPosition, pBlockState) {

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
        if (tag.contains("hexparty\$host"))
            host = tag.getString("hexparty\$host")
        if (tag.contains("hexparty\$port"))
            port = tag.getInt("hexparty\$port")
    }

    override fun saveModData(tag: CompoundTag) {
        host?.let { tag.putString("hexparty\$host", it) }
        if (port != -1) tag.putInt("hexparty\$port", port)
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
                hexparty.LOGGER.warn("Failed to get/create Socket!", mishap)
                self.postMishap(Component.literal(mishap.localizedMessage))
            }
        }
    }
}