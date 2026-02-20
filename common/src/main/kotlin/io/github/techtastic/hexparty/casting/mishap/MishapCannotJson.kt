package io.github.techtastic.hexparty.casting.mishap

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.network.chat.Component

class MishapCannotJson(val iota: Iota): Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context) = FrozenPigment.DEFAULT.get()

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context) = Component.translatable("hexparty.mishap.cannot_json", iota.display())

    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
    }
}