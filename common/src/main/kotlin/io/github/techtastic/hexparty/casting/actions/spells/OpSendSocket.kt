package io.github.techtastic.hexparty.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import at.petrak.hexcasting.api.misc.MediaConstants
import io.github.techtastic.hexparty.SocketHandler
import io.github.techtastic.hexparty.blocks.hexpartyBlocks
import io.github.techtastic.hexparty.blocks.circles.impetuses.BlockEntitySocketImpetus
import io.github.techtastic.hexparty.casting.iota.JsonIota
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.nio.charset.StandardCharsets

object OpSendSocket: SpellAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        if (env !is CircleCastEnv) throw MishapNoSpellCircle()
        val imp = env.impetus!! as? BlockEntitySocketImpetus
            ?: throw MishapBadBlock(env.impetus!!.blockPos, hexpartyBlocks.SOCKET_IMPETUS.get().name)

        val socket = imp.getOrCreateSocket()
        val data = when (val data = args[0]) {
            is JsonIota -> data.json.asString.toByteArray(StandardCharsets.UTF_8)
            is StringIota -> data.string.toByteArray(StandardCharsets.UTF_8)
            else -> throw MishapInvalidIota(data, 1, Component.translatable("hexparty.mishap.socket.send.invalid"))
        }

        return SpellAction.Result(
            Spell(socket, data),
            MediaConstants.SHARD_UNIT,
            listOf(ParticleSpray(imp.blockPos.center, Vec3(0.0, 10.0, 0.0), 0.0, 0.1))
        )
    }

    data class Spell(val socket: SocketHandler.ManagedSocket, val data: ByteArray) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            socket.sendData(data)
        }
    }
}