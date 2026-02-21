package io.github.techtastic.hexparty.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import io.github.techtastic.hexparty.HTTPRequestsHandler
import io.github.techtastic.hexparty.casting.iota.ResponseIota
import kotlin.jvm.optionals.getOrNull

object OpGetCode : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val response = args.getOrElse(0 ) {throw MishapNotEnoughArgs(argc,0) }
        if (response !is ResponseIota) {throw MishapInvalidIota.ofType(response,0,"HTTP response") }
        val either = HTTPRequestsHandler.getResponse(response.getPayload())
        val code : Int = if (either != null) {either.left().getOrNull()?.statusCode() ?: -1} else {-1}
        return listOf(DoubleIota(code.toDouble()))
    }
}