package dev.cammiescorner.enhanceddoors.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.cammiescorner.enhanceddoors.EnhancedDoorsConfig;
import dev.cammiescorner.enhanceddoors.common.GotAnyGrapes;
import dev.cammiescorner.enhanceddoors.common.blocks.entities.DoorBlockEntity;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsComponents;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin extends Block implements EntityBlock, GotAnyGrapes {
	@Shadow @Final public static EnumProperty<DoubleBlockHalf> HALF;
	@Shadow @Final public static EnumProperty<DoorHingeSide> HINGE;
	@Shadow @Final public static DirectionProperty FACING;
	@Shadow @Final public static BooleanProperty OPEN;
	@Shadow @Final public static BooleanProperty POWERED;

	@Unique private final ThreadLocal<Boolean> A = ThreadLocal.withInitial(() -> false);

	@Shadow protected abstract void playSound(@Nullable Entity entity, Level level, BlockPos blockPos, boolean bl);
	@Shadow public abstract boolean isOpen(BlockState blockState);

	public DoorBlockMixin(Properties properties) { super(properties); }

	@Inject(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private void openSesameRedstone(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl, CallbackInfo ci, @Local(ordinal = 1) boolean bl2) {
		if(!EnhancedDoorsConfig.connectedDoors)
			return;

		Direction facing = blockState.getValue(FACING);
		BlockPos offset = blockPos.relative(blockState.getValue(HINGE) == DoorHingeSide.RIGHT ? facing.getCounterClockWise() : facing.getClockWise());
		BlockState offsetState = level.getBlockState(offset);

		if(blockState.is(EnhancedDoorsTags.DONT_COUPLE) || offsetState.is(EnhancedDoorsTags.DONT_COUPLE))
			return;

		if(offsetState.getBlock() instanceof DoorBlock && offsetState.getValue(OPEN) == blockState.getValue(OPEN) && offsetState.getValue(HINGE) != blockState.getValue(HINGE) && offsetState.getValue(FACING) == blockState.getValue(FACING)) {
			if(bl2 != offsetState.getValue(OPEN)) {
				playSound(null, level, offset, bl2);
				level.gameEvent(null, bl2 ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, offset);
			}

			level.setBlock(offset, offsetState.setValue(POWERED, bl2).setValue(OPEN, bl2), 2);

			if(level.getBlockEntity(offset.below(offsetState.getValue(HALF) == DoubleBlockHalf.UPPER ? 1 : 0)) instanceof DoorBlockEntity blockEntity)
				blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
		}

		if(level.getBlockEntity(blockPos.below(blockState.getValue(HALF) == DoubleBlockHalf.UPPER ? 1 : 0)) instanceof DoorBlockEntity blockEntity)
			blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
	}

	@Inject(method = "setOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private void openSesameVillagers(Entity entity, Level level, BlockState blockState, BlockPos blockPos, boolean bl, CallbackInfo ci) {
		if(!EnhancedDoorsConfig.connectedDoors)
			return;

		Direction facing = blockState.getValue(FACING);
		BlockPos offset = blockPos.relative(blockState.getValue(HINGE) == DoorHingeSide.RIGHT ? facing.getCounterClockWise() : facing.getClockWise());
		BlockState offsetState = level.getBlockState(offset);

		if(blockState.is(EnhancedDoorsTags.DONT_COUPLE) || offsetState.is(EnhancedDoorsTags.DONT_COUPLE))
			return;

		if(offsetState.getBlock() instanceof DoorBlock && offsetState.getValue(OPEN) == blockState.getValue(OPEN) && offsetState.getValue(HINGE) != blockState.getValue(HINGE) && offsetState.getValue(FACING) == blockState.getValue(FACING)) {
			offsetState = offsetState.cycle(OPEN);
			level.setBlock(offset, offsetState, Block.UPDATE_CLIENTS);
			playSound(entity, level, offset, offsetState.getValue(OPEN));
			level.gameEvent(entity, isOpen(offsetState) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, offset);

			if(level.getBlockEntity(offset.below(offsetState.getValue(HALF) == DoubleBlockHalf.UPPER ? 1 : 0)) instanceof DoorBlockEntity blockEntity)
				blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
		}

		if(level.getBlockEntity(blockPos.below(blockState.getValue(HALF) == DoubleBlockHalf.UPPER ? 1 : 0)) instanceof DoorBlockEntity blockEntity)
			blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private void openSesamePlayers(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
		if(!EnhancedDoorsConfig.connectedDoors)
			return;

		Direction facing = blockState.getValue(FACING);
		BlockPos offset = blockPos.relative(blockState.getValue(HINGE) == DoorHingeSide.RIGHT ? facing.getCounterClockWise() : facing.getClockWise());
		BlockState offsetState = level.getBlockState(offset);

		if(blockState.is(EnhancedDoorsTags.DONT_COUPLE) || offsetState.is(EnhancedDoorsTags.DONT_COUPLE))
			return;

		if(offsetState.getBlock() instanceof DoorBlock && offsetState.getValue(OPEN) != blockState.getValue(OPEN) && offsetState.getValue(HINGE) != blockState.getValue(HINGE) && offsetState.getValue(FACING) == blockState.getValue(FACING)) {
			offsetState = offsetState.cycle(OPEN);
			level.setBlock(offset, offsetState, Block.UPDATE_CLIENTS);
			playSound(player, level, offset, offsetState.getValue(OPEN));
			level.gameEvent(player, isOpen(offsetState) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, offset);

			if(level.getBlockEntity(offset.below(offsetState.getValue(HALF) == DoubleBlockHalf.UPPER ? 1 : 0)) instanceof DoorBlockEntity blockEntity)
				blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
		}

		if(level.getBlockEntity(blockPos.below(blockState.getValue(HALF) == DoubleBlockHalf.UPPER ? 1 : 0)) instanceof DoorBlockEntity blockEntity)
			blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? new DoorBlockEntity(blockPos, blockState) : null;
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return A.get() ? super.getRenderShape(blockState) : RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void setUseSuper(boolean bl) {
		A.set(bl);
	}
}
