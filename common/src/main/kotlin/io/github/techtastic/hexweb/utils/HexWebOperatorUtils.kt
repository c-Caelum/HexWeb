package io.github.techtastic.hexweb.utils

import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import com.google.gson.*
import com.mojang.serialization.JsonOps
import io.github.techtastic.hexweb.HTTPRequestsHandler
import io.github.techtastic.hexweb.HexWeb
import io.github.techtastic.hexweb.casting.iota.JsonIota
import io.github.techtastic.hexweb.casting.iota.ResponseIota
import io.github.techtastic.hexweb.casting.mishap.MishapDisallowedUrl
import io.github.techtastic.hexweb.casting.mishap.MishapResponseError
import io.github.techtastic.hexweb.casting.mishap.MishapTooEarly
import io.github.techtastic.hexweb.config.HexWebConfig
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.player.Player
import ram.talia.hexal.api.linkable.PlayerLinkstore
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.net.http.HttpResponse
import kotlin.jvm.optionals.getOrNull

object HexWebOperatorUtils {
    val CANNOT_DESERIALIZE = TagKey.create(HexRegistries.IOTA_TYPE, ResourceLocation(HexWeb.MOD_ID, "cannot_deserialize"));
    val rule = Regex(HexWebConfig.ADDRESS_FILTERS.get().joinToString(separator = "|") { "($it)" })

    fun List<Iota>.getJsonObject(idx: Int, argc: Int): JsonObject {
        val iota = this.getOrNull(idx) ?: throw MishapNotEnoughArgs(idx + 1, this.size)
        if (iota is JsonIota)
            return iota.getPayload()
        throw MishapInvalidIota.ofType(iota, if (argc == 0) idx else argc - (idx + 1), "json")
    }

    fun List<Iota>.getHeaders(idx: Int, argc: Int): List<String>? {
        val iota = this.getOrElse(idx) { NullIota() }
        if (iota is NullIota) return null
        if (iota is ListIota) {
            val list = iota.list
            if (!list.nonEmpty) return null
            if (list.size() % 2 != 0 || list.any { iota -> iota !is StringIota || iota.string == "user-agent"})
                throw MishapInvalidIota.ofType(iota, if (argc == 0) idx else argc - (idx + 1), "headers.list")
            return list.map { iota -> (iota as StringIota).string }
        }
        if (iota is JsonIota) {
            val json = iota.json.asMap()
            if (json.isEmpty()) return null
            if (json.values.any { !it.isJsonPrimitive || it.asJsonPrimitive.isString })
                throw MishapInvalidIota.ofType(iota, if (argc == 0) idx else argc - (idx + 1), "headers.json")
            val list = mutableListOf<String>()
            json.forEach { (key, value) ->
                list.add(key)
                list.add(value.asJsonPrimitive.asString)
            }
            return list
        }
        // Handle Maps
        // Handle Dict
        throw MishapInvalidIota.ofType(iota, if (argc == 0) idx else argc - (idx + 1), "headers")
    }

    fun List<Iota>.getBodyString(idx: Int, argc: Int): String? {
        val iota = this.getOrElse(idx) { NullIota() }
        if (iota is NullIota) return null
        if (iota is JsonIota)
            return iota.getPayload().asString
        if (iota is StringIota)
            return iota.string
        throw MishapInvalidIota.ofType(iota, if (argc == 0) idx else argc - (idx + 1), "body")
    }

    fun List<Iota>.getResponse(idx: Int, argc: Int): HttpResponse<String> {
        val iota = this.getOrNull(idx) ?: throw MishapNotEnoughArgs(idx + 1, this.size)
        if (iota is ResponseIota) {
            val either = HTTPRequestsHandler.getResponse(iota.getPayload()) ?: throw MishapTooEarly()
            either.right().ifPresent {
                HTTPRequestsHandler.clearResponse(iota.getPayload())
                throw MishapResponseError(either.right().get())
            }
            return either.left().getOrNull()?.let {
                HTTPRequestsHandler.clearResponse(iota.getPayload())
                it
            } ?: throw MishapTooEarly()
        }
        throw MishapInvalidIota.ofType(iota, if (argc == 0) idx else argc - (idx + 1), "response")
    }

    fun JsonElement.toIota(level: ServerLevel?): Iota {
        if (this.isJsonNull) return NullIota()

        if (this.isJsonPrimitive) {
            if (this.asJsonPrimitive.isBoolean) return BooleanIota(this.asBoolean)
            if (this.asJsonPrimitive.isNumber) return DoubleIota(this.asNumber.toDouble())
            return StringIota.make(this.asString)
        }

        if (this.isJsonArray) return ListIota(this.asJsonArray.map { it.toIota(level) })

        val json = this.asJsonObject
        val nbt = JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, json) as? CompoundTag
        HexWeb.LOGGER.warn("$nbt")
        return IotaType.getTypeFromTag(nbt)?.let { type ->

            if (preventDeserialization(type)) GarbageIota()
            type.deserialize(nbt, level) ?: GarbageIota()

        } ?: JsonIota(json)
    }

    fun Iota.toJson(): JsonElement {
        if (this is BooleanIota) return JsonPrimitive(this.bool)
        if (this is DoubleIota) return JsonPrimitive(this.double)
        if (this is StringIota) return JsonPrimitive(this.string)
        if (this is NullIota) return JsonNull.INSTANCE
        if (this is JsonIota) return this.json
        if (this is ListIota) {
            val json = JsonArray()
            this.list.forEach { json.add(it.toJson()) }
            return json
        }

        return NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, this.serialize())
    }



    fun checkBlacklist(url: String) {
        if (rule.containsMatchIn(url) != HexWebConfig.IS_WHITELIST.get()) {
            throw MishapDisallowedUrl(url)
        }
    }

    fun preventDeserialization(type: IotaType<*>) =
        HexIotaTypes.REGISTRY.getHolder(HexIotaTypes.REGISTRY
            .getId(type)).get().`is`(CANNOT_DESERIALIZE)
}
