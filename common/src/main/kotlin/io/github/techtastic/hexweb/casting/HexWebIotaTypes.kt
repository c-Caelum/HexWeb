package io.github.techtastic.hexweb.casting

import at.petrak.hexcasting.common.lib.HexRegistries
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexweb.HexWeb.MOD_ID
import io.github.techtastic.hexweb.casting.iota.JsonIota
import io.github.techtastic.hexweb.casting.iota.ResponseIota

object HexWebIotaTypes {
    private val IOTAS = DeferredRegister.create(MOD_ID, HexRegistries.IOTA_TYPE)

    val JSON = IOTAS.register("json", JsonIota::TYPE)
    val RESPONSE = IOTAS.register("response", ResponseIota::TYPE)

    fun register() {
        IOTAS.register()
    }
}