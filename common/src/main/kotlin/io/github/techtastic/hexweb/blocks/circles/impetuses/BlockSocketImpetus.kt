package io.github.techtastic.hexweb.blocks.circles.impetuses

import at.petrak.hexcasting.api.block.circle.BlockAbstractImpetus
import io.github.techtastic.hexweb.blocks.HexWebBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class BlockSocketImpetus(properties: Properties) : BlockAbstractImpetus(properties) {
    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) = BlockEntitySocketImpetus(blockPos, blockState)

    override fun <T : BlockEntity?> getTicker(
        pLevel: Level,
        pState: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (!pLevel.isClientSide)
            createTickerHelper(blockEntityType, HexWebBlockEntities.SOCKET_IMPETUS.get(), BlockEntitySocketImpetus::serverTick)
        else
            null
    }

    protected fun <E : BlockEntity?, A : BlockEntity?> createTickerHelper(
        type: BlockEntityType<A>, targetType: BlockEntityType<E>, ticker: BlockEntityTicker<in E>?
    ): BlockEntityTicker<A>? {
        return if (targetType === type) ticker as? BlockEntityTicker<A> else null
    }
}