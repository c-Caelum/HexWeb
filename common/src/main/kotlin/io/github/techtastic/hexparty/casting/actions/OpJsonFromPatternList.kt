package io.github.techtastic.hexparty.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.techtastic.hexparty.hexparty
import io.github.techtastic.hexparty.casting.iota.JsonIota
import io.github.techtastic.hexparty.utils.hexpartyOperatorUtils.toJson
import io.github.techtastic.hexparty.utils.IotaParser.json_from_list

object OpJsonFromPatternList : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val list = ListIota(args.getList(0,argc))
        val json = JsonObject()
        json.add("value",list.json_from_list(env))

        return listOf(JsonIota(json))
    }
}