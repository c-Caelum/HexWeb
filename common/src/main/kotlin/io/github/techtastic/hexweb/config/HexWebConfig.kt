package io.github.techtastic.hexweb.config

import net.minecraftforge.common.ForgeConfigSpec


object HexWebConfig {
    val BUILDER = ForgeConfigSpec.Builder()

    lateinit var ADDRESS_FILTERS: ForgeConfigSpec.ConfigValue<List<String>>
    lateinit var IS_WHITELIST: ForgeConfigSpec.ConfigValue<Boolean>
    lateinit var SPEC: ForgeConfigSpec

    fun setup() {
        BUILDER.push("HexWeb Configs")
        ADDRESS_FILTERS = BUILDER
            .comment("A list of filtered addresses which HTTP requests and the like can't (by default) reach!")
            .comment("Do note, these are regex patterns, so get creative!")
            .defineList("Address Filters", listOf("127\\.0\\.0\\.1")) { s -> s is String }

        IS_WHITELIST = BUILDER
            .comment("Whether or not the filter list is a whitelist instead of a blacklist.")
            .define("Filter Whitelist",false)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }
}
