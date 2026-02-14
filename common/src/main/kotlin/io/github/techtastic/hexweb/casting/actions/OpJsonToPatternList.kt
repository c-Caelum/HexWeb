package io.github.techtastic.hexweb.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import io.github.techtastic.hexweb.utils.IotaParser
import com.google.gson.JsonObject
import io.github.techtastic.hexweb.casting.iota.JsonIota
import io.github.techtastic.hexweb.utils.HexWebOperatorUtils.getJsonObject
import kotlin.reflect.typeOf

object OpJsonToPatternList: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val json = args.getJsonObject(0,argc)

        return listOf(IotaParser.parse_json_full(json,env))
    }
}