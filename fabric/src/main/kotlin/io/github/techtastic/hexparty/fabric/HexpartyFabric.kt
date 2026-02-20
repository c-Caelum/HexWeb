package io.github.techtastic.hexparty.fabric

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry
import io.github.techtastic.hexparty.Hexparty.MOD_ID
import io.github.techtastic.hexparty.Hexparty.init
import io.github.techtastic.hexparty.config.HexpartyConfig
import net.fabricmc.api.ModInitializer
import net.minecraftforge.fml.config.ModConfig

object HexpartyFabric : ModInitializer {
    override fun onInitialize() {
        init()

        val CONFIG = ForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.SERVER, HexpartyConfig.SPEC)
    }
}