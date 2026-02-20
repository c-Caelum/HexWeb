package io.github.techtastic.hexparty.fabric

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry
import io.github.techtastic.hexparty.hexparty.MOD_ID
import io.github.techtastic.hexparty.hexparty.init
import io.github.techtastic.hexparty.config.hexpartyConfig
import net.fabricmc.api.ModInitializer
import net.minecraftforge.fml.config.ModConfig

object hexpartyFabric : ModInitializer {
    override fun onInitialize() {
        init()

        val CONFIG = ForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.SERVER, hexpartyConfig.SPEC)
    }
}