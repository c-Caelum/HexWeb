package io.github.techtastic.hexparty.casting

import at.petrak.hexcasting.common.lib.HexRegistries
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexparty.Hexparty.MOD_ID
import io.github.techtastic.hexparty.casting.iota.ResponseIota

object HexpartyIotaTypes {
    private val IOTAS = DeferredRegister.create(MOD_ID, HexRegistries.IOTA_TYPE)
    val RESPONSE = IOTAS.register("response", ResponseIota::TYPE)

    fun register() {
        IOTAS.register()
    }
}