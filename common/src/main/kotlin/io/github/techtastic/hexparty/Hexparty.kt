package io.github.techtastic.hexparty

import com.mojang.logging.LogUtils
import io.github.techtastic.hexparty.config.HexpartyConfig

object Hexparty {
    const val MOD_ID: String = "hexparty"
    val LOGGER = LogUtils.getLogger()

    @JvmStatic
    fun init() {
        HexpartyConfig.setup()
    }
}