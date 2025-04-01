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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
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

@Mixin(TrapDoorBlock.class)
public abstract class TrapDoorBlockMixin extends HorizontalDirectionalBlock implements EntityBlock, GotAnyGrapes {
	@Shadow @Final public static EnumProperty<Half> HALF;
	@Shadow @Final public static BooleanProperty POWERED;
	@Shadow @Final public static BooleanProperty OPEN;
	@Shadow @Final public static BooleanProperty WATERLOGGED;

	@Shadow public abstract void playSound(@Nullable Player player, Level level, BlockPos blockPos, boolean bl);

	@Unique private final ThreadLocal<Boolean> A = ThreadLocal.withInitial(() -> false);

	public TrapDoorBlockMixin(Properties properties) { super(properties); }

	@Inject(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private void openSesameRedstone(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl, CallbackInfo ci, @Local(ordinal = 1) boolean bl2) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		Direction facing = blockState.getValue(FACING);

		// TODO couple trapdoors in a way that doesnt infinitely loop
		if(blockEntity != null) {
			blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();

			for(int i = 0; i < 3; i++) {
				BlockPos offset = blockPos.relative(i == 0 ? facing : (i == 1 ? facing.getClockWise() : facing.getCounterClockWise()));
				BlockState offsetState = level.getBlockState(offset);

				if(!EnhancedDoorsConfig.connectedDoors || blockState.is(EnhancedDoorsTags.DONT_COUPLE) || offsetState.is(EnhancedDoorsTags.DONT_COUPLE))
					continue;

				if(offsetState.getBlock() instanceof TrapDoorBlock trapDoor && offsetState.getValue(OPEN) != blockState.getValue(OPEN))
					trapDoor.neighborChanged(offsetState, level, offset, trapDoor, blockPos, bl);
			}
		}
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private void openSesamePlayers(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		Direction facing = blockState.getValue(FACING);

		// TODO couple trapdoors in a way that doesnt infinitely loop
		if(blockEntity != null) {
			blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();

			for(int i = 0; i < 3; i++) {
				BlockPos offset = blockPos.relative(i == 0 ? facing : (i == 1 ? facing.getClockWise() : facing.getCounterClockWise()));
				BlockState offsetState = level.getBlockState(offset);

				if(!EnhancedDoorsConfig.connectedDoors || blockState.is(EnhancedDoorsTags.DONT_COUPLE) || offsetState.is(EnhancedDoorsTags.DONT_COUPLE))
					continue;

				if(offsetState.getBlock() instanceof TrapDoorBlock trapDoor && trapDoor.type.canOpenByHand() && offsetState.getValue(OPEN) != blockState.getValue(OPEN))
					trapDoor.use(offsetState, level, offset, player, interactionHand, blockHitResult);
			}
		}
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new DoorBlockEntity(blockPos, blockState);
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
