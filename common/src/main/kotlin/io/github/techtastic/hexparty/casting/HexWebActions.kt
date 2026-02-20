package io.github.techtastic.hexparty.casting

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexparty.hexparty.MOD_ID
import io.github.techtastic.hexparty.casting.actions.*
import io.github.techtastic.hexparty.casting.actions.spells.OpRequest
import io.github.techtastic.hexparty.casting.actions.spells.OpSendSocket
import io.github.techtastic.hexparty.casting.actions.spells.OpSetSocket
import io.github.techtastic.hexparty.casting.actions.OpJsonToPatternList
import io.github.techtastic.hexparty.casting.actions.OpJsonFromPatternList
import io.github.techtastic.hexparty.casting.actions.spells.OpGetCopyparty
import io.github.techtastic.hexparty.casting.actions.spells.OpSetCopyparty

object hexpartyActions {
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
    val JSON_TO_PATTERNS =  ACTIONS.register("json_to_pattern_list") { ActionRegistryEntry(
        HexPattern.fromAngles("edadeeda", HexDir.NORTH_WEST),
        OpJsonToPatternList
    )}
    val JSON_FROM_PATTERNS = ACTIONS.register("json_from_pattern_list") { ActionRegistryEntry(
        HexPattern.fromAngles("edadeede", HexDir.NORTH_WEST),
        OpJsonFromPatternList
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
    val GET_RESPONSE_JSON = ACTIONS.register("response_json") { ActionRegistryEntry(
        HexPattern.fromAngles("qqqqwqdwaawe", HexDir.EAST),
        OpResponseJson
    )}
    val GET_COPYPARTY = ACTIONS.register("get_copyparty") { ActionRegistryEntry(
        HexPattern.fromAngles("wqqqqwqdqdwdqdeede",HexDir.NORTH_EAST),
        OpGetCopyparty
    )}
    val SET_COPYPARTY = ACTIONS.register("set_copyparty") { ActionRegistryEntry(
        HexPattern.fromAngles("wqqqqwqdqdwdqddqaq",HexDir.NORTH_EAST),
        OpSetCopyparty
    )}

    // SOCKETS

    val READ = ACTIONS.register("read") { ActionRegistryEntry(
        HexPattern.fromAngles("eqewdewqe", HexDir.NORTH_WEST),
        OpReadSocket
    ) }

    val SEND = ACTIONS.register("send") { ActionRegistryEntry(
        HexPattern.fromAngles("qewqawqeq", HexDir.NORTH_EAST),
        OpSendSocket
    ) }

    val GET_HOST = ACTIONS.register("get_host") { ActionRegistryEntry(
        HexPattern.fromAngles("eqewdewqeeaa", HexDir.NORTH_WEST),
        OpGetHostSocket
    ) }

    val GET_PORT = ACTIONS.register("get_port") { ActionRegistryEntry(
        HexPattern.fromAngles("eqewdewqewdd", HexDir.NORTH_WEST),
        OpGetPortSocket
    ) }

    val SET = ACTIONS.register("set") { ActionRegistryEntry(
        HexPattern.fromAngles("eqewdewqeeaadd", HexDir.NORTH_WEST),
        OpSetSocket
    ) }

    fun register() {
        ACTIONS.register()
    }
}