package io.github.techtastic.hexweb.interop

import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import com.google.gson.JsonParser
import io.github.techtastic.hexweb.casting.HexWebIotaTypes
import io.github.techtastic.hexweb.casting.iota.JsonIota
import penguinencounter.mediatransport.conversions.Decoder
import penguinencounter.mediatransport.conversions.Encoder
import penguinencounter.mediatransport.conversions.Types
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

class HexWebConversions : Encoder, Decoder {
    override fun canEncode(it: Iota) = it is JsonIota

    override fun canDecode(type: Int) = type == 0x70

    override fun decode(type: Int, bytes: ByteArrayInputStream): Iota {
        when (type) {
            0x70 -> {
                val arr = ByteArray(8192)
                val read = bytes.read(arr)
                return JsonIota(JsonParser.parseString(arr.copyOf(read).toString(StandardCharsets.UTF_8)).asJsonObject)
            }
        }
        return GarbageIota()
    }

    override fun defineTypes(target: MutableMap<Int, IotaType<*>>) {
        target.define(0x70 to HexWebIotaTypes.JSON.get())
    }

    override fun encode(it: Iota): ByteArray {
        when (it) {
            is JsonIota -> it.json.asString.toByteArray(StandardCharsets.UTF_8)
        }
        return ByteArray(0)
    }

    companion object {
        fun register() {
            val converter = HexWebConversions()
            Encoder.converters.add(converter)
            Decoder.converters.add(converter)
            converter.defineTypes(Types.types)
        }
    }
}