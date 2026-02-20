package io.github.techtastic.hexparty.blocks

import at.petrak.hexcasting.common.lib.HexBlocks
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexparty.hexparty
import io.github.techtastic.hexparty.blocks.circles.impetuses.BlockSocketImpetus
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockBehaviour

object hexpartyBlocks {
    private val BLOCKS = DeferredRegister.create(hexparty.MOD_ID, Registries.BLOCK)
    private val ITEMS = DeferredRegister.create(hexparty.MOD_ID, Registries.ITEM)

    val SOCKET_IMPETUS = BLOCKS.register("impetus/socket") { BlockSocketImpetus(BlockBehaviour.Properties.copy(HexBlocks.IMPETUS_LOOK)) }

    fun register() {
        BLOCKS.register()
        ITEMS.register(SOCKET_IMPETUS.id) { BlockItem(SOCKET_IMPETUS.get(), Item.Properties()) }
        ITEMS.register()
    }
}