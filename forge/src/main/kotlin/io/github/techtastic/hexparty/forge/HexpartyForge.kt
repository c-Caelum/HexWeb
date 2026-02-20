package io.github.techtastic.hexparty.forge

import dev.architectury.platform.forge.EventBuses
import io.github.techtastic.hexparty.Hexparty
import io.github.techtastic.hexparty.Hexparty.init
import io.github.techtastic.hexparty.config.HexpartyConfig
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import thedarkcolour.kotlinforforge.KotlinModLoadingContext

@Mod(Hexparty.MOD_ID)
class HexpartyForge {
    init {
        EventBuses.registerModEventBus(Hexparty.MOD_ID, KotlinModLoadingContext.get().getKEventBus())

        init()

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, HexpartyConfig.SPEC)
    }
}
