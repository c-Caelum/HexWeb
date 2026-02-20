package io.github.techtastic.hexparty.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.techtastic.hexparty.casting.iota.JsonIota
import io.github.techtastic.hexparty.utils.hexpartyOperatorUtils.getResponse

object OpGetResponse: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val response = args.getResponse(0, argc)

        val json = JsonObject()
        json.addProperty("code", response.statusCode())
        json.addProperty("body", response.body())
        return listOf(JsonIota(json))
    }
}