package io.github.techtastic.hexparty.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import com.google.gson.JsonArray
import io.github.techtastic.hexparty.casting.mishap.MishapExpectedJsonTranslatable
import io.github.techtastic.hexparty.utils.hexpartyOperatorUtils.getJsonObject
import io.github.techtastic.hexparty.utils.IotaParser.JsonToIota

object OpJsonToPatternList: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val list = args.getJsonObject(0,argc)
        if (!(list.has("value")) || !(list.get("value").isJsonArray)) {
            throw MishapExpectedJsonTranslatable()
        }
        val temp = (list.get("value") as JsonArray).map {value ->
            value.JsonToIota(env)
        }

        return listOf(ListIota(temp))

    }
}