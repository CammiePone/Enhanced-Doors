package dev.cammiescorner.enhanceddoors.common.blocks.entities;

import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TrapDoorBlockEntity extends BlockEntity {
	public TrapDoorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(EnhancedDoorsBlocks.TRAPDOOR_ENTITY, blockPos, blockState);
	}
}
