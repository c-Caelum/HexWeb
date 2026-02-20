package io.github.techtastic.hexparty.casting.iota

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import io.github.techtastic.hexparty.HTTPRequestsHandler
import io.github.techtastic.hexparty.casting.HexpartyIotaTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.server.level.ServerLevel
import java.util.UUID

class ResponseIota(val uuid: UUID): Iota(HexpartyIotaTypes.RESPONSE.get(), uuid) {
    fun getPayload() = this.uuid

    override fun isTruthy() = HTTPRequestsHandler.getResponse(uuid)?.left()?.isPresent ?: false

    override fun toleratesOther(that: Iota) = false

    override fun serialize(): Tag {
        val tag = CompoundTag()
        tag.putUUID("hexparty\$uuid", this.uuid)
        return tag
    }

    companion object {
        val TYPE = object : IotaType<ResponseIota>() {
            override fun deserialize(tag: Tag, world: ServerLevel) = Companion.deserialize(tag)

            override fun display(tag: Tag): Component {
                val iota = Companion.deserialize(tag)
                return Component.translatable("hexparty.iota.response", iota.uuid)
                    .withStyle(Style.EMPTY.withColor(this.color()))
            }

            override fun color() = 0x18d97c
        }

        fun deserialize(tag: Tag): ResponseIota {
            val tag = tag as CompoundTag
            return ResponseIota(tag.getUUID("hexparty\$uuid"))
        }
    }
}