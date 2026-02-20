package io.github.techtastic.hexparty.interop

import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import com.google.gson.JsonParser
import io.github.techtastic.hexparty.casting.hexpartyIotaTypes
import io.github.techtastic.hexparty.casting.iota.JsonIota
import penguinencounter.mediatransport.conversions.Decoder
import penguinencounter.mediatransport.conversions.Encoder
import penguinencounter.mediatransport.conversions.Types
import penguinencounter.mediatransport.conversions.buildData
import penguinencounter.mediatransport.conversions.unpackData
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

class hexpartyConversions : Encoder, Decoder {
    override fun canEncode(it: Iota) = it is JsonIota

    override fun canDecode(type: Int) = type == 0x70

    override fun decode(type: Int, bytes: ByteArrayInputStream): Iota =
        when (type) {
            0x70 -> unpackData(bytes) {
                val size = readInt()
                try {
                    val read = readNBytes(size)
                    if (read.size != size) GarbageIota()
                    JsonIota(JsonParser.parseString(String(read, Charsets.UTF_8)).asJsonObject)
                } catch (_: MishapInvalidIota) {
                    GarbageIota()
                }
            }
            else -> GarbageIota()
        }

    override fun defineTypes(target: MutableMap<Int, IotaType<*>>) {
        target.define(0x70 to hexpartyIotaTypes.JSON.get())
    }

    override fun encode(it: Iota): ByteArray =
        when (it) {
            is JsonIota -> buildData {
                writeByte(0x70)

                val bytes = it.json.asString.toByteArray(Charsets.UTF_8)
                writeInt(bytes.size)
                write(bytes)
            }
            else -> throw IllegalStateException()
        }

    companion object {
        fun register() {
            val converter = hexpartyConversions()
            Encoder.converters.add(converter)
            Decoder.converters.add(converter)
            converter.defineTypes(Types.types)
        }
    }
}