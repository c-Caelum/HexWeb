package io.github.techtastic.hexparty.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.techtastic.hexparty.casting.iota.JsonIota
import io.github.techtastic.hexparty.casting.mishap.MishapCannotJson
import ram.talia.moreiotas.api.casting.iota.StringIota
import ram.talia.moreiotas.api.getString

object OpParseJson: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val str = args.getString(0, argc)
        val json = try { JsonParser.parseString(str) } catch (ignored: Exception) {
            throw MishapCannotJson(StringIota.make(str))
        }

        return listOf(JsonIota(if (json.isJsonObject) json.asJsonObject
        else {
            val temp = JsonObject()
            temp.add("value", json)
            temp
        }))
    }
}