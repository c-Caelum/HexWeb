package io.github.techtastic.hexparty.casting

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexparty.Hexparty.MOD_ID
import io.github.techtastic.hexparty.casting.actions.*
import io.github.techtastic.hexparty.casting.actions.spells.*

object HexpartyActions {
    private val ACTIONS = DeferredRegister.create(MOD_ID, HexRegistries.ACTION)
    // HTTP

    val REQUEST = ACTIONS.register("request") { ActionRegistryEntry(
        HexPattern.fromAngles("qqqqwqdqddqe", HexDir.NORTH_EAST),
        OpRequest
    )}
    val GET_RESPONSE = ACTIONS.register("get_response") { ActionRegistryEntry(
        HexPattern.fromAngles("qqqqwweaaead", HexDir.NORTH_EAST),
        _root_ide_package_.io.github.techtastic.hexparty.casting.actions.OpGetResponse
    )}
    val GET_RESPONSE_JSON = ACTIONS.register("response_json") { ActionRegistryEntry(
        HexPattern.fromAngles("qqqqwqdwaawe", HexDir.NORTH_EAST),
        _root_ide_package_.io.github.techtastic.hexparty.casting.actions.OpResponseJson
    )}
    val GET_COPYPARTY = ACTIONS.register("get_copyparty") { ActionRegistryEntry(
        HexPattern.fromAngles("wqqqqwqdqdwdqdeede",HexDir.NORTH_EAST),
        OpGetCopyparty
    )}
    val SET_COPYPARTY = ACTIONS.register("set_copyparty") { ActionRegistryEntry(
        HexPattern.fromAngles("wqqqqwqdqdwdqddqaq",HexDir.NORTH_EAST),
        OpSetCopyparty
    )}
    val GET_CODE = ACTIONS.register("get_code") { ActionRegistryEntry(
        HexPattern.fromAngles("qqqqwaedewed",HexDir.NORTH_EAST),
        OpGetCode
    )}


    fun register() {
        ACTIONS.register()
    }
}