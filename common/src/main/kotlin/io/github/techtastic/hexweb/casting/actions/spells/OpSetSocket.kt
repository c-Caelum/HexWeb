package io.github.techtastic.hexweb.casting.actions.spells

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import io.github.techtastic.hexweb.blocks.HexWebBlocks
import io.github.techtastic.hexweb.blocks.circles.impetuses.BlockEntitySocketImpetus
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.casting.iota.StringIota


object OpSetSocket: SpellAction {
    override val argc: Int
        get() = 3

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getBlockPos(0, argc)
        val host = (args.getOrNull(1) as? StringIota)?.string
        val port = args.getIntBetween(2, 1, 65535, argc)
        env.assertPosInRange(pos)

        val be = env.world.getBlockEntity(pos)
        if (be !is BlockEntitySocketImpetus)
             throw MishapBadBlock(pos, HexWebBlocks.SOCKET_IMPETUS.get().name)

        return SpellAction.Result(
            Spell(be, host, port),
            0L,
            listOf(ParticleSpray.burst(pos.center, 5.0))
        )
    }

    data class Spell(val be: BlockEntitySocketImpetus, val host: String?, val port: Int) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            be.host = host
            be.port = port
            be.setChanged()
        }
    }
}