package io.github.techtastic.hexparty

import com.mojang.logging.LogUtils
import dev.architectury.platform.Platform
import io.github.techtastic.hexparty.blocks.hexpartyBlockEntities
import io.github.techtastic.hexparty.blocks.hexpartyBlocks
import io.github.techtastic.hexparty.config.hexpartyConfig
import io.github.techtastic.hexparty.interop.hexpartyConversions

object hexparty {
    const val MOD_ID: String = "hexparty"
    val LOGGER = LogUtils.getLogger()

    @JvmStatic
    fun init() {
        hexpartyBlocks.register()
        hexpartyBlockEntities.register()

        hexpartyConfig.setup()

        if (Platform.isModLoaded("mediatransport"))
            hexpartyConversions.register()
    }
}