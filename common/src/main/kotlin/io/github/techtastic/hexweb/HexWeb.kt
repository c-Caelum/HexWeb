package io.github.techtastic.hexweb

import io.github.techtastic.hexweb.config.HexWebConfig

object HexWeb {
    const val MOD_ID: String = "hexweb"

    @JvmStatic
    fun init() {
        HexWebConfig.setup()
    }
}