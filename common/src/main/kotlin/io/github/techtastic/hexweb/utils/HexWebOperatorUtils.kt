package io.github.techtastic.hexweb.utils

import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.getString
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import com.google.gson.*
import com.mojang.serialization.JsonOps
import io.github.techtastic.hexweb.HTTPRequestsHandler
import io.github.techtastic.hexweb.casting.iota.JsonIota
import io.github.techtastic.hexweb.casting.iota.ResponseIota
import io.github.techtastic.hexweb.casting.mishap.MishapBlacklistUrl
import io.github.techtastic.hexweb.casting.mishap.MishapResponseError
import io.github.techtastic.hexweb.casting.mishap.MishapTooEarly
import io.github.techtastic.hexweb.config.HexWebConfig
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.server.level.ServerLevel
import org.apache.commons.codec.binary.Hex
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.net.http.HttpResponse

object HexWebOperatorUtils {
    fun List<Iota>.getJsonObject(idx: Int, argc: Int): JsonObject {
        if (idx >= 0 && idx <= this.lastIndex) {
            val iota = this.get(idx)
            if (iota is JsonIota)
                return iota.getPayload()
            throw MishapInvalidIota.ofType(iota, if (argc == 0) idx else argc - (idx + 1), "json")
        }
        throw MishapNotEnoughArgs(idx + 1, this.size)
    }

    fun List<Iota>.getResponse(idx: Int, argc: Int): HttpResponse<String> {
        if (idx >= 0 && idx <= this.lastIndex) {
            val iota = this.get(idx)
            if (iota is ResponseIota) {
                val either = HTTPRequestsHandler.getResponse(iota.getPayload()) ?: throw MishapTooEarly()
                if (either.right().isPresent) {
                    HTTPRequestsHandler.clearResponse(iota.getPayload())
                    throw MishapResponseError(either.right().get())
                }
                return try {
                    val response = either.left().orElseThrow()
                    HTTPRequestsHandler.clearResponse(iota.getPayload())
                    response
                } catch (ignored: Exception) {
                    throw MishapTooEarly()
                }
            }
            throw MishapInvalidIota.ofType(iota, if (argc == 0) idx else argc - (idx + 1), "response")
        }
        throw MishapNotEnoughArgs(idx + 1, this.size)
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
        IotaType.getTypeFromTag(nbt)?.let { type ->
            if (HexWebConfig.IOTA_TYPE_BLACKLIST.get().contains(nbt.getString(HexIotaTypes.KEY_TYPE)!!)) return GarbageIota()
            return type.deserialize(nbt, level) ?: GarbageIota()
        }

        return JsonIota(json)
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
        HexWebConfig.ADDRESS_BLACKLIST.get().forEach { rule ->
            Regex.fromLiteral(rule).find(url)?.let { throw MishapBlacklistUrl(rule) }
        }
    }
}
