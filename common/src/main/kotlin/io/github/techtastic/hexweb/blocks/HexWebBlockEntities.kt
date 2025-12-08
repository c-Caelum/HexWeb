package io.github.techtastic.hexweb.blocks

import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexweb.HexWeb
import io.github.techtastic.hexweb.blocks.circles.impetuses.BlockEntitySocketImpetus
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType

object HexWebBlockEntities {
    private val BLOCK_ENTITIES = DeferredRegister.create(HexWeb.MOD_ID, Registries.BLOCK_ENTITY_TYPE)

    val SOCKET_IMPETUS = BLOCK_ENTITIES.register("impetus/socket") { BlockEntityType.Builder.of(
        ::BlockEntitySocketImpetus,
        HexWebBlocks.SOCKET_IMPETUS.get()
    ).build(null) }

    fun register() {
        BLOCK_ENTITIES.register()
    }
}