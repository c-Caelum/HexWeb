package io.github.techtastic.hexweb.casting

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexweb.HexWeb.MOD_ID
import io.github.techtastic.hexweb.casting.actions.*
import io.github.techtastic.hexweb.casting.actions.spells.OpRequest

object HexWebActions {
    private val ACTIONS = DeferredRegister.create(MOD_ID, HexRegistries.ACTION)


    // JSON

    val CREATE_JSON = ACTIONS.register("create_json") { ActionRegistryEntry(
        HexPattern.fromAngles("edade", HexDir.NORTH_WEST),
        OpCreateJson
    )}
    val PARSE_JSON = ACTIONS.register("parse_json") { ActionRegistryEntry(
        HexPattern.fromAngles("edadeqdwedw", HexDir.NORTH_WEST),
        OpParseJson
    )}
    val SET_ELEMENT = ACTIONS.register("set_element") { ActionRegistryEntry(
        HexPattern.fromAngles("edadedaa", HexDir.NORTH_WEST),
        OpSetElement
    )}
    val GET_ELEMENT = ACTIONS.register("get_element") { ActionRegistryEntry(
        HexPattern.fromAngles("edadeedd", HexDir.NORTH_WEST),
        OpGetElement
    )}
    val HAS_ELEMENT = ACTIONS.register("has_element") { ActionRegistryEntry(
        HexPattern.fromAngles("edadee", HexDir.NORTH_WEST),
        OpHasElement
    )}


    // HTTP

    val REQUEST = ACTIONS.register("request") { ActionRegistryEntry(
        HexPattern.fromAngles("qqqqwqdqddqe", HexDir.NORTH_EAST),
        OpRequest
    )}
    val GET_RESPONSE = ACTIONS.register("get_response") { ActionRegistryEntry(
        HexPattern.fromAngles("qqqqwweaaead", HexDir.NORTH_EAST),
        OpGetResponse
    )}


    fun register() {
        ACTIONS.register()
    }
}