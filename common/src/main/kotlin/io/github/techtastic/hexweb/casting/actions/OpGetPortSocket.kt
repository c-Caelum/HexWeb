package io.github.techtastic.hexweb.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import io.github.techtastic.hexweb.blocks.HexWebBlocks
import io.github.techtastic.hexweb.blocks.circles.impetuses.BlockEntitySocketImpetus
import io.github.techtastic.hexweb.casting.actions.spells.OpSetSocket

object OpGetPortSocket: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getBlockPos(0, OpSetSocket.argc)
        env.assertPosInRange(pos)

        val be = env.world.getBlockEntity(pos)
        if (be is BlockEntitySocketImpetus)
            return listOf(DoubleIota(be.port.toDouble()))
        throw MishapBadBlock(pos, HexWebBlocks.SOCKET_IMPETUS.get().name)
    }
}