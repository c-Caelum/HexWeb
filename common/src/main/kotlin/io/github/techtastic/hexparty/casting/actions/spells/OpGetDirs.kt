package io.github.techtastic.hexparty.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import io.github.techtastic.hexparty.HTTPRequestsHandler
import io.github.techtastic.hexparty.casting.iota.ResponseIota
import ram.talia.moreiotas.api.getString
import java.util.*

object OpGetDirs: ConstMediaAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val path = args.getString(0, argc)
        val password = args.getString(1,argc).plus("&ls")
        val headers = listOf<String>("user-agent",
            "Hexparty/1.0 " + env::class.simpleName + " " + (if(env is CircleCastEnv) {env.impetus?.blockPos} else {env.castingEntity?.uuid}?:"NONE"),
            "pw",password
        )
        val uuid = UUID.randomUUID()
        HTTPRequestsHandler.makeAndQueueRequest(uuid, path, headers.toTypedArray(), null, null)
        return listOf(ResponseIota(uuid))
    }
}