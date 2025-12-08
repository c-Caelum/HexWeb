package io.github.techtastic.hexweb

import com.mojang.logging.LogUtils
import dev.architectury.platform.Platform
import io.github.techtastic.hexweb.blocks.HexWebBlockEntities
import io.github.techtastic.hexweb.blocks.HexWebBlocks
import io.github.techtastic.hexweb.config.HexWebConfig
import io.github.techtastic.hexweb.interop.HexWebConversions

object HexWeb {
    const val MOD_ID: String = "hexweb"
    val LOGGER = LogUtils.getLogger()

    @JvmStatic
    fun init() {
        HexWebBlocks.register()
        HexWebBlockEntities.register()

        HexWebConfig.setup()

        if (Platform.isModLoaded("mediatransport"))
            HexWebConversions.register()
    }
}