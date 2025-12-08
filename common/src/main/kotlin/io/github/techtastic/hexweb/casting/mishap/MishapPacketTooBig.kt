package io.github.techtastic.hexweb.casting.mishap

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import net.minecraft.network.chat.Component
import java.util.UUID

class MishapPacketTooBig(val size: Int): Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context) = ctx.pigment

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context) = Component.translatable("hexweb.mishap.packet_too_big", size)

    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
    }
}