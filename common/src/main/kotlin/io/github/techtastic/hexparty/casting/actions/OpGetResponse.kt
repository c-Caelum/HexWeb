package io.github.techtastic.hexparty.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import io.github.techtastic.hexparty.utils.HexpartyOperatorUtils.getResponse
import ram.talia.moreiotas.api.casting.iota.StringIota

object OpGetResponse: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val response = args.getResponse(0, argc)
        return listOf(DoubleIota(response.statusCode().toDouble()), StringIota.make(response.body()))
    }
}