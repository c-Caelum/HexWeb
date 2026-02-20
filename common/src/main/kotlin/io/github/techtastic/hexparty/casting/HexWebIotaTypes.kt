package io.github.techtastic.hexparty.casting

import at.petrak.hexcasting.common.lib.HexRegistries
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexparty.hexparty.MOD_ID
import io.github.techtastic.hexparty.casting.iota.JsonIota
import io.github.techtastic.hexparty.casting.iota.ResponseIota

object hexpartyIotaTypes {
    private val IOTAS = DeferredRegister.create(MOD_ID, HexRegistries.IOTA_TYPE)

    val JSON = IOTAS.register("json", JsonIota::TYPE)
    val RESPONSE = IOTAS.register("response", ResponseIota::TYPE)

    fun register() {
        IOTAS.register()
    }
}