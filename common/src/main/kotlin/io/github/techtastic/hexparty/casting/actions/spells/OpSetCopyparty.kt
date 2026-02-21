package io.github.techtastic.hexparty.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import io.github.techtastic.hexparty.HTTPRequestsHandler
import io.github.techtastic.hexparty.casting.iota.ResponseIota
import io.github.techtastic.hexparty.utils.IotaParser.json_from_iota
import io.github.techtastic.hexparty.utils.IotaParser.json_from_list
import ram.talia.moreiotas.api.getString
import java.util.*

object OpSetCopyparty: ConstMediaAction {
    override val argc: Int
        get() = 3

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val path = args.getString(0, argc).plus("/hex.json")
        val method = "PUT"
        val iotas = args.get(1).json_from_iota(env).toString()
        val password = args.getString(2,argc)
        val headers = listOf<String>("user-agent",
            "Hexparty/1.0 " + env::class.simpleName + " " + (if(env is CircleCastEnv) {env.impetus?.blockPos} else {env.castingEntity?.uuid}?:"NONE"),
            "Replace","1",
            "pw",password
        )
        val uuid = UUID.randomUUID()
        HTTPRequestsHandler.makeAndQueueRequest(uuid, path, headers.toTypedArray(), method, iotas)
        return listOf(ResponseIota(uuid))
    }
}