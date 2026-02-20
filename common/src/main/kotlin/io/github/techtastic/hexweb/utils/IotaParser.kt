package io.github.techtastic.hexweb.utils

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.internal.JsonReaderInternalAccess
import com.mojang.datafixers.util.Either
import com.samsthenerd.duckyperiphs.hexcasting.utils.HexalObfMapState
import io.github.techtastic.hexweb.HexWeb
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import org.jblas.DoubleMatrix
import ram.talia.hexal.api.casting.iota.GateIota
import ram.talia.hexal.api.casting.iota.MoteIota
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager
import ram.talia.moreiotas.api.casting.iota.EntityTypeIota
import ram.talia.moreiotas.api.casting.iota.IotaTypeIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota
import ram.talia.moreiotas.api.casting.iota.MatrixIota
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.util.*


object IotaParser {
    fun JsonElement.is_String() : Boolean {
        return (this.isJsonPrimitive && (this as JsonPrimitive).isString)
    }
    fun JsonElement.is_Boolean() : Boolean {
        return (this.isJsonPrimitive && (this as JsonPrimitive).isBoolean)
    }
    fun JsonElement.JsonToIota(env : CastingEnvironment) : Iota {
        /*
        * This doesn't mishap when something goes wrong, it just errors (caught by hex).
        * So, don't malform your json!!!
        */
        if(this.isJsonPrimitive) {
            val x = this.asJsonPrimitive
            if (x.isNumber) {return DoubleIota(x.asDouble)}
            if (x.isBoolean) {return BooleanIota(x.asBoolean)}
            return StringIota.make(x.asString)
        }
        if (this.isJsonObject) {
            val jsonObj = this.asJsonObject


            if (jsonObj.has("null")) {
                return NullIota()
            }
            if (jsonObj.has("startDir") && jsonObj.has("angles")) {
                val angles = jsonObj.get("angles").asString
                val startDir = jsonObj.get("startDir").asString
                return PatternIota(HexPattern.fromAngles(angles, HexDir.fromString(startDir)))
            }
            if (jsonObj.has("x") && jsonObj.has("y") && jsonObj.has("z")) {
                val x = jsonObj.get("x").asDouble
                val y = jsonObj.get("y").asDouble
                val z = jsonObj.get("z").asDouble
                return Vec3Iota(Vec3(x, y, z))
            }
            if (jsonObj.has("uuid")) {
                val uuid = UUID.fromString(jsonObj.get("uuid").asString)
                val ent = env.world.getEntity(uuid)
                if (ent != null) {
                    if (ent is Player) {
                        return NullIota()
                    } else {
                        return EntityIota(ent)
                    }
                }
            }
            if (jsonObj.has("row") && jsonObj.has("col") && jsonObj.has("matrix")) {
                val col = jsonObj.get("col").asInt
                val row = jsonObj.get("row").asInt
                val mat = DoubleMatrix(row,col)
                val matrixTable = jsonObj.get("matrix").asJsonArray
                for (i in 1..col * row) {
                    if (matrixTable[i] != null) {
                        mat.put(i - 1, matrixTable[i].asDouble)
                    }
                }
            }
            if (jsonObj.has("moteUuid") && jsonObj.get("moteUuid").is_String() &&
            jsonObj.has("itemID") && jsonObj.get("itemID").is_String()) {
                val moteUUID = UUID.fromString(jsonObj.get("moteUuid").asString)
                val itemID = jsonObj.get("itemID").asString;
                val moteData = HexalObfMapState.getServerState(env.world.server).getMoteData(moteUUID)
                if (moteData == null || !itemID.equals(moteData.itemID())) {
                    return GarbageIota()
                }
                val storageUUID = moteData.uuid()
                val index = moteData.index()
                val manager = MediafiedItemManager
                if (manager != null) {
                    val mediafiedIndex = MediafiedItemManager.Index(storageUUID, index)
                    return MoteIota(mediafiedIndex)
                }
                return NullIota()
            }
            if (jsonObj.has("gate") && jsonObj.get("gate").is_String()) {
                val gateMap = HexalObfMapState.getServerState(env.world.server)
                val gateUuid = UUID.fromString(jsonObj.get("gate").asString)
                val gData = gateMap.getGateData(gateUuid)

                if (gData != null) {
                    if(gData.type == 0) {
                        return GateIota(gData.index, null);
                    }
                    if (gData.type == 1) {
                        return GateIota(gData.index, Either.left(gData.tVec))
                    }
                    if (gData.type == 2) {
                        val ent = env.world.getEntity(gData.entUuid()) ?: return NullIota()
                        val gatePair = Pair<Entity, Vec3>(ent,gData.tVec())
                        return GateIota(gData.index,Either.right(gatePair))
                    }
                }

                return NullIota()
            }
            if (jsonObj.has("iotaType") && jsonObj.get("iotaType").is_String()) {
                val typeKey = jsonObj.get("iotaType").asString
                if(!ResourceLocation.isValidResourceLocation(typeKey)) {return GarbageIota()}
                val typeLoc = ResourceLocation(typeKey)
                val type = HexIotaTypes.REGISTRY.get(typeLoc) ?: return GarbageIota()
                return IotaTypeIota(type)
            }
            if (jsonObj.has("entityType") && jsonObj.get("entityType").is_String()) {
                val typeKey = jsonObj.get("entityType").asString
                if (!ResourceLocation.isValidResourceLocation(typeKey)) {
                    return GarbageIota()
                }
                val typeLoc: ResourceLocation = ResourceLocation(typeKey)
                val type: EntityType<*> = BuiltInRegistries.ENTITY_TYPE.get(typeLoc) ?: return GarbageIota()
                return EntityTypeIota(type)
            }
            if (jsonObj.has("itemType") && jsonObj.get("itemType").is_String() &&
                jsonObj.has("isItem") && jsonObj.get("isItem").is_Boolean()) {
                val typeKey = jsonObj.get("itemType").asString
                val isItem = jsonObj.get("isItem").asBoolean
                if (!ResourceLocation.isValidResourceLocation(typeKey)) {return GarbageIota() }
                val typeLoc = ResourceLocation(typeKey)
                if (isItem) {
                    val type = BuiltInRegistries.ITEM.get(typeLoc)
                    if (type == null) return GarbageIota()
                    return ItemTypeIota(type)
                } else {
                    val type = BuiltInRegistries.BLOCK.get(typeLoc)
                    if (type == null) return GarbageIota()
                    return ItemTypeIota(type)
                }
            }
        }
        return GarbageIota()
    }
    fun ListIota.json_from_list(env: CastingEnvironment) : JsonArray {
        val json = JsonArray()
        this.list.forEach {value->
            if(value is ListIota){
                json.add(value.json_from_list(env))
            }
            if(value is Vec3Iota) {
                val temp = JsonObject()
                temp.add("x", JsonPrimitive(value.vec3.x))
                temp.add("y", JsonPrimitive(value.vec3.y))
                temp.add("z", JsonPrimitive(value.vec3.z))
                json.add(temp)
            }
            if(value is StringIota){
                json.add(JsonPrimitive(value.string))
            }
            if(value is PatternIota) {
                val temp = JsonObject()
                temp.add("angles", JsonPrimitive(value.pattern.anglesSignature()))
                temp.add("startDir",JsonPrimitive(value.pattern.startDir.toString()))
                json.add(temp)
            }
            if(value is DoubleIota) {
                json.add(JsonPrimitive(value.double))
            }
            if(value is BooleanIota) {
                json.add(JsonPrimitive(value.bool))
            }
            if(value is NullIota) {
                val temp = JsonObject()
                temp.add("null", JsonPrimitive(true))
                json.add(temp)
            }
            if(value is EntityIota) {
                val temp = JsonObject()
                temp.add("uuid", JsonPrimitive(value.entity.uuid.toString()))
                temp.add("name", JsonPrimitive(value.entity.name.toString()))
                json.add(temp)
            }
            if(value is MatrixIota) {
                val temp = JsonObject()
                temp.add("rows",JsonPrimitive(value.matrix.rows))
                temp.add("cols",JsonPrimitive(value.matrix.columns))
                val arr = JsonArray()
                for (i in 0..< (value.matrix.rows * value.matrix.columns)) {
                    arr.add(value.matrix[i])
                }
                temp.add("matrix",arr)
                json.add(temp)
            }
            if (value is ItemTypeIota) {
                val temp = JsonObject()
                val type = value.either
                val item = type.left()
                if (item.isPresent) {
                    val typeLoc = BuiltInRegistries.ITEM.getKey(item.get())
                    temp.add("itemType",JsonPrimitive(typeLoc.toString()))
                    temp.add("isItem",JsonPrimitive(true))
                }
                if (type.right().isPresent) {
                    val typeLoc = BuiltInRegistries.BLOCK.getKey(type.right().get());
                    temp.add("itemType", JsonPrimitive(typeLoc.toString()))
                    temp.add("isItem",JsonPrimitive(false))
                }
                json.add(temp)
            }
            if (value is EntityTypeIota) {
                val temp = JsonObject()
                val type = value.entityType
                val typeLoc = BuiltInRegistries.ENTITY_TYPE.getKey(type)
                temp.add("entityType",JsonPrimitive(typeLoc.toString()))
                json.add(temp)
            }
            if (value is IotaTypeIota) {
                val temp = JsonObject()
                val type = value.iotaType
                val typeLoc = HexIotaTypes.REGISTRY.getKey(type)
                temp.add("iotaType",JsonPrimitive(typeLoc.toString()))
                json.add(temp)
            }
            if (value is MoteIota) {
                val temp = JsonObject()
                val itemIndex = value.itemIndex
                val uuid = itemIndex.storage
                val index = itemIndex.index
                val itemID = value.item.toString()
                val moteData = HexalObfMapState.MoteData(uuid,index,itemID)
                val thisMoteUUID = HexalObfMapState.getServerState(env.world.server).getOrCreateMoteObfUUID(moteData)
                temp.add("moteUuid", JsonPrimitive(thisMoteUUID.toString()))
                temp.add("itemID", JsonPrimitive(itemID))
                temp.add("nexusUUID", JsonPrimitive(uuid.toString()))
                json.add(temp)
            }
            if (value is GateIota) {
                val temp = JsonObject()
                val gData = HexalObfMapState.GateDataFromIota(value)
                val thisGateUUID = HexalObfMapState.getServerState(env.world.server).getOrCreateGateUUID(gData)
                if (gData.type == 0) {
                    temp.add("gateType",JsonPrimitive("drifting"))
                } else {
                    val location_object = JsonObject()
                    location_object.add("x", JsonPrimitive(gData.tVec.x))
                    location_object.add("y", JsonPrimitive(gData.tVec.y))
                    location_object.add("z", JsonPrimitive(gData.tVec.z))
                    if (gData.type == 1) {
                        temp.add("gateType",JsonPrimitive("location"))
                        temp.add("location",location_object)
                    }
                    if (gData.type==2) {
                        temp.add("gateType",JsonPrimitive("entity"))
                        temp.add("entity",JsonPrimitive(gData.entUuid.toString()))
                        temp.add("offset",location_object)
                    }
                }
                json.add(temp)
            }
            if(value is GarbageIota) {
                val temp = JsonObject()
                temp.add("garbage", JsonPrimitive(true))
                json.add(temp)
            }
        }
        return json
    }
}