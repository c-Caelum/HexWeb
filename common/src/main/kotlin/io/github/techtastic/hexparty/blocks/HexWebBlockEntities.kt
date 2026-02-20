package io.github.techtastic.hexparty.blocks

import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexparty.hexparty
import io.github.techtastic.hexparty.blocks.circles.impetuses.BlockEntitySocketImpetus
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType

object hexpartyBlockEntities {
    private val BLOCK_ENTITIES = DeferredRegister.create(hexparty.MOD_ID, Registries.BLOCK_ENTITY_TYPE)

    val SOCKET_IMPETUS = BLOCK_ENTITIES.register("impetus/socket") { BlockEntityType.Builder.of(
        ::BlockEntitySocketImpetus,
        hexpartyBlocks.SOCKET_IMPETUS.get()
    ).build(null) }

    fun register() {
        BLOCK_ENTITIES.register()
    }
}