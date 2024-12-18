package dev.cammiescorner.enhanceddoors.mixin;

import dev.cammiescorner.enhanceddoors.common.blocks.entities.DoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin extends Block implements EntityBlock {
	@Shadow @Final public static EnumProperty<DoubleBlockHalf> HALF;

	public DoorBlockMixin(Properties properties) { super(properties); }

	// TODO couple the doors pls

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? new DoorBlockEntity(blockPos, blockState) : null;
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
}
