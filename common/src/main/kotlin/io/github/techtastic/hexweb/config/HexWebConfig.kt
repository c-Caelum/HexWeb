package io.github.techtastic.hexweb.config

import net.minecraftforge.common.ForgeConfigSpec


object HexWebConfig {
    val BUILDER = ForgeConfigSpec.Builder()

    lateinit var ADDRESS_BLACKLIST: ForgeConfigSpec.ConfigValue<List<String>>
    lateinit var IOTA_TYPE_BLACKLIST: ForgeConfigSpec.ConfigValue<List<String>>

    lateinit var SPEC: ForgeConfigSpec

    fun setup() {
        BUILDER.push("HexWeb Configs")
        ADDRESS_BLACKLIST = BUILDER
            .comment("A blacklist of addresses HTTP requests and the like cannot be sent to!")
            .defineList("Address Blacklist", listOf("127\\.0\\.0\\.1")) { s -> s is String }
        BUILDER.pop()
        IOTA_TYPE_BLACKLIST = BUILDER
            .comment("A blacklist of Iota types to not deserialize into their Iotas!")
            .defineList("Iota Type Blacklist", listOf("hexcasting:entity", "hexical:pigment")) { s -> s is String }
        BUILDER.pop()
        SPEC = BUILDER.build()
    }
}