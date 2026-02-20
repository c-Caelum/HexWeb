package io.github.techtastic.hexparty.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import io.github.techtastic.hexparty.casting.mishap.MishapInvalidJsonKey
import io.github.techtastic.hexparty.utils.hexpartyOperatorUtils.getJsonObject
import io.github.techtastic.hexparty.utils.hexpartyOperatorUtils.toIota
import ram.talia.moreiotas.api.getString

object OpGetElement: ConstMediaAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val json = args.getJsonObject(0, argc)
        val key = args.getString(1, argc)
        if (json.has(key))
            return listOf(json.get(key).toIota(env.world))
        throw MishapInvalidJsonKey(key)
    }
}