package dev.cammiescorner.enhanceddoors.common.blocks.entities;

import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DoorBlockEntity extends BlockEntity {
	public DoorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(EnhancedDoorsBlocks.ANIMATED_DOOR_ENTITY, blockPos, blockState);
	}
}
