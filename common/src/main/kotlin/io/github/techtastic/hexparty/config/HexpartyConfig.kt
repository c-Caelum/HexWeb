package io.github.techtastic.hexparty.config

import net.minecraftforge.common.ForgeConfigSpec


object HexpartyConfig {
    val BUILDER = ForgeConfigSpec.Builder()

    lateinit var ADDRESS_FILTERS: ForgeConfigSpec.ConfigValue<List<String>>
    lateinit var IS_WHITELIST: ForgeConfigSpec.ConfigValue<Boolean>
    lateinit var COPYPARTY_URL: ForgeConfigSpec.ConfigValue<String>
    lateinit var SPEC: ForgeConfigSpec

    fun setup() {
        BUILDER.push("Hexparty Configs")
        ADDRESS_FILTERS = BUILDER
            .comment("A list of filtered addresses which HTTP requests and the like can't (by default) reach!")
            .comment("Do note, these are regex patterns, so get creative!")
            .defineList("Address Filters", listOf("127\\.0\\.0\\.1")) { s -> s is String }

        IS_WHITELIST = BUILDER
            .comment("Whether or not the filter list is a whitelist instead of a blacklist.")
            .define("Filter Whitelist",false)

        COPYPARTY_URL = BUILDER
            .comment("The base url that http requests are appended to.")
            .define("Copyparty URL","https://copyparty.chloes.media/")

        BUILDER.pop()
        SPEC = BUILDER.build()
    }
}
