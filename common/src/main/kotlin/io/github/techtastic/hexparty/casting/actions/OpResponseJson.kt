package io.github.techtastic.hexparty.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBool
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import io.github.techtastic.hexparty.casting.mishap.MishapCannotJson
import io.github.techtastic.hexparty.casting.mishap.MishapExpectedJsonTranslatable
import io.github.techtastic.hexparty.utils.HexpartyOperatorUtils.getResponse
import io.github.techtastic.hexparty.utils.IotaParser.JsonToIota
import ram.talia.moreiotas.api.casting.iota.StringIota

object OpResponseJson: ConstMediaAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val is_decoded = args.getBool(0,argc)
        val response = args.getResponse(1, argc)

        val json = try { JsonParser.parseString(response.body()) } catch (ignored: Exception) {
            throw MishapCannotJson(StringIota.make(response.body()))
        }
        return listOf(json.JsonToIota(env))
    }
}