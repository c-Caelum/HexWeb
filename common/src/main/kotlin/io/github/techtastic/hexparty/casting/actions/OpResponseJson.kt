package io.github.techtastic.hexparty.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import com.google.gson.JsonParser
import io.github.techtastic.hexparty.casting.mishap.MishapCannotJson
import io.github.techtastic.hexparty.utils.HexpartyOperatorUtils.getResponse
import io.github.techtastic.hexparty.utils.IotaParser.jsonToIota
import ram.talia.moreiotas.api.casting.iota.StringIota

object OpResponseJson: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val response = args.getResponse(0, argc)

        val json = try { JsonParser.parseString(response.body()) } catch (ignored: Exception) {
            throw MishapCannotJson(StringIota.make(response.body()))
        }
        return listOf(json.jsonToIota(env))
    }
}