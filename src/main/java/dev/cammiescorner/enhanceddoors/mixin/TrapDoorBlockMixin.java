package dev.cammiescorner.enhanceddoors.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.cammiescorner.enhanceddoors.common.GotAnyGrapes;
import dev.cammiescorner.enhanceddoors.common.blocks.entities.DoorBlockEntity;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsComponents;
import net.minecraft.core.BlockPos;
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

	@Unique private final ThreadLocal<Boolean> A = ThreadLocal.withInitial(() -> false);

	public TrapDoorBlockMixin(Properties properties) { super(properties); }

	// TODO couple trapdoors
	@Inject(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private void openSesameRedstone(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl, CallbackInfo ci, @Local(ordinal = 1) boolean bl2) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);

		if(blockEntity != null)
			blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private void openSesamePlayers(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);

		if(blockEntity != null)
			blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
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
