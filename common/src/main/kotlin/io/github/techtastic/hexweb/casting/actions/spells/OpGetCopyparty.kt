package io.github.techtastic.hexweb.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import io.github.techtastic.hexweb.HTTPRequestsHandler
import io.github.techtastic.hexweb.HexWeb
import io.github.techtastic.hexweb.casting.iota.ResponseIota
import ram.talia.moreiotas.api.getString
import java.util.*

object OpGetCopyparty: ConstMediaAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val path = args.getString(0, argc)
        val password = args.getString(1,argc)
        val headers = listOf<String>("user-agent",
            "Hexparty/1.0 " + env::class.simpleName + " " + (if(env is CircleCastEnv) {env.impetus?.blockPos} else {env.castingEntity?.uuid}?:"NONE"))
        val uuid = UUID.randomUUID()
        HTTPRequestsHandler.makeAndQueueRequest(uuid, path, headers.toTypedArray(), null, null,password)
        return listOf(ResponseIota(uuid))
    }
}