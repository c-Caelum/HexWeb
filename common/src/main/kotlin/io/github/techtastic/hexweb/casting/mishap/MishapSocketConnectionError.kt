package io.github.techtastic.hexweb.casting.mishap

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import java.io.IOException

class MishapSocketConnectionError(val e: IOException): Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = ctx.pigment

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): MutableComponent = Component.literal(e.localizedMessage)

    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
    }
}