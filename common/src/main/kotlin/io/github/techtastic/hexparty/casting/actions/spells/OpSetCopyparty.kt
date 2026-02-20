package io.github.techtastic.hexparty.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import io.github.techtastic.hexparty.HTTPRequestsHandler
import io.github.techtastic.hexparty.casting.iota.ResponseIota
import io.github.techtastic.hexparty.utils.IotaParser.json_from_list
import ram.talia.moreiotas.api.getString
import java.util.*

object OpSetCopyparty: ConstMediaAction {
    override val argc: Int
        get() = 3

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val path = args.getString(0, argc)
        val method = "PUT"
        val iotas = ListIota(args.getList(1,argc)).json_from_list(env).toString()
        val password = args.getString(2,argc)
        val headers = listOf<String>("user-agent",
            "Hexparty/1.0 " + env::class.simpleName + " " + (if(env is CircleCastEnv) {env.impetus?.blockPos} else {env.castingEntity?.uuid}?:"NONE"))
        val uuid = UUID.randomUUID()
        HTTPRequestsHandler.makeAndQueueRequest(uuid, path, headers.toTypedArray(), method, iotas,password)
        return listOf(ResponseIota(uuid))
    }
}