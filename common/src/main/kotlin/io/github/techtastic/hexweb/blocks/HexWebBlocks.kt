package io.github.techtastic.hexweb.blocks

import at.petrak.hexcasting.common.lib.HexBlocks
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexweb.HexWeb
import io.github.techtastic.hexweb.blocks.circles.impetuses.BlockSocketImpetus
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockBehaviour

object HexWebBlocks {
    private val BLOCKS = DeferredRegister.create(HexWeb.MOD_ID, Registries.BLOCK)
    private val ITEMS = DeferredRegister.create(HexWeb.MOD_ID, Registries.ITEM)

    val SOCKET_IMPETUS = BLOCKS.register("impetus/socket") { BlockSocketImpetus(BlockBehaviour.Properties.copy(HexBlocks.IMPETUS_LOOK)) }

    fun register() {
        BLOCKS.register()
        ITEMS.register(SOCKET_IMPETUS.id) { BlockItem(SOCKET_IMPETUS.get(), Item.Properties()) }
        ITEMS.register()
    }
}