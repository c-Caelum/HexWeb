package io.github.techtastic.hexweb.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import at.petrak.hexcasting.api.misc.MediaConstants
import io.github.techtastic.hexweb.blocks.HexWebBlocks
import io.github.techtastic.hexweb.blocks.circles.impetuses.BlockEntitySocketImpetus
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.nio.charset.StandardCharsets

object OpReadSocket: ConstMediaAction {
    override val argc: Int
        get() = (MediaConstants.DUST_UNIT * 5).toInt()

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env !is CircleCastEnv) throw MishapNoSpellCircle()
        val imp = env.impetus!! as? BlockEntitySocketImpetus
            ?: throw MishapBadBlock(env.impetus!!.blockPos, HexWebBlocks.SOCKET_IMPETUS.get().name)

        val socket = imp.getOrCreateSocket()
        return listOf(StringIota.make(socket.receiveData().toString(StandardCharsets.UTF_8)))
    }
}