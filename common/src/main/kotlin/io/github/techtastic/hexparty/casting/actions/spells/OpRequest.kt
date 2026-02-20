package io.github.techtastic.hexparty.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import io.github.techtastic.hexparty.HTTPRequestsHandler
import io.github.techtastic.hexparty.casting.iota.ResponseIota
import io.github.techtastic.hexparty.utils.HexpartyOperatorUtils.getBodyString
import io.github.techtastic.hexparty.utils.HexpartyOperatorUtils.getHeaders
import ram.talia.moreiotas.api.getString
import ram.talia.moreiotas.api.getStringOrNull
import java.util.*

object OpRequest: ConstMediaAction {
    override val argc: Int
        get() = 4

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val url = args.getString(0, argc)
        val method = args.getStringOrNull(1, argc)
        val headers = (args.getHeaders(2, argc)?: listOf<String>()).plus(listOf("user-agent",
            "Hexparty/1.0 " + env::class.simpleName + " " + (if(env is CircleCastEnv) {env.impetus?.blockPos} else {env.castingEntity?.uuid}?:"NONE")
        ))
        val body = args.getBodyString(3, argc)

        val uuid = UUID.randomUUID()
        HTTPRequestsHandler.makeAndQueueRequest(uuid, url, headers.toTypedArray(), method, body,null)
        return listOf(ResponseIota(uuid))
    }
}