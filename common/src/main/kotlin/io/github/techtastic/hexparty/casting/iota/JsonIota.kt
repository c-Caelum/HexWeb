package io.github.techtastic.hexparty.casting.iota

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.techtastic.hexparty.casting.hexpartyIotaTypes
import io.github.techtastic.hexparty.utils.hexpartyOperatorUtils.toIota
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import ram.talia.moreiotas.api.casting.iota.StringIota
import kotlin.collections.component1

class JsonIota(val json: JsonObject): Iota(hexpartyIotaTypes.JSON.get(), json) {
    fun getPayload() = this.json

    override fun isTruthy() = !this.json.isJsonNull



    override fun toleratesOther(that: Iota) = typesMatch(this, that)
            && that is JsonIota && this.json == that.json

    override fun serialize(): Tag {
        val tag = CompoundTag()
        tag.putString("hexparty\$json", this.json.toString())
        return tag
    }

    override fun subIotas(): MutableIterable<Iota> {
        val sub = mutableListOf<Iota>()
        this.json.asMap().forEach { (key, value) ->
            sub.add(StringIota.make(key))
            sub.add(value.toIota(null))
        }
        return sub
    }
    override fun size() : Int {
        var size = 1;
        this.json.asMap().forEach{(key,value) ->
            size += StringIota.make(key).size() + value.toIota(null).size()
        }
        return size
    }

    companion object {
        val TYPE = object : IotaType<JsonIota>() {
            override fun deserialize(tag: Tag, world: ServerLevel) = Companion.deserialize(tag)

            override fun display(tag: Tag): Component {
                val json = Companion.deserialize(tag).json
                var comp = Component.literal("{")

                json.asMap().forEach { key, element ->
                    comp = comp.append("§d\"$key\"§f:").append(element.toIota(null).display())
                    if (json.asMap().keys.last() != key)
                        comp = comp.append(", ")
                }

                comp = comp.append("}")

                return comp
            }

            override fun color() = 0x136b18
        }

        fun deserialize(tag: Tag): JsonIota {
            val tag = tag as CompoundTag
            return JsonIota(JsonParser.parseString(tag.getString("hexparty\$json")).asJsonObject)
        }
    }
}