package io.github.techtastic.hexparty.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import at.petrak.hexcasting.api.misc.MediaConstants
import io.github.techtastic.hexparty.blocks.hexpartyBlocks
import io.github.techtastic.hexparty.blocks.circles.impetuses.BlockEntitySocketImpetus
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.nio.charset.StandardCharsets

object OpReadSocket: ConstMediaAction {
    override val argc: Int
        get() = MediaConstants.DUST_UNIT.toInt()

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env !is CircleCastEnv) throw MishapNoSpellCircle()
        val imp = env.impetus!! as? BlockEntitySocketImpetus
            ?: throw MishapBadBlock(env.impetus!!.blockPos, hexpartyBlocks.SOCKET_IMPETUS.get().name)

        val socket = imp.getOrCreateSocket()
        socket.receiveData()?.toString(StandardCharsets.UTF_8)?.let { return listOf(StringIota.make(it)) }
        return listOf(NullIota())
    }
}