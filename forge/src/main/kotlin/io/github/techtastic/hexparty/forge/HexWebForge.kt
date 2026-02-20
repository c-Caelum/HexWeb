package io.github.techtastic.hexparty.forge

import dev.architectury.platform.forge.EventBuses
import io.github.techtastic.hexparty.hexparty
import io.github.techtastic.hexparty.hexparty.init
import io.github.techtastic.hexparty.config.hexpartyConfig
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import thedarkcolour.kotlinforforge.KotlinModLoadingContext

@Mod(hexparty.MOD_ID)
class hexpartyForge {
    init {
        EventBuses.registerModEventBus(hexparty.MOD_ID, KotlinModLoadingContext.get().getKEventBus())

        init()

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, hexpartyConfig.SPEC)
    }
}
