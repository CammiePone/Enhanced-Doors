package dev.cammiescorner.enhanceddoors.mixin;

import dev.cammiescorner.enhanceddoors.common.GotAnyGrapes;
import dev.cammiescorner.enhanceddoors.common.blocks.entities.DoorBlockEntity;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsComponents;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
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
	@Unique private final ThreadLocal<Boolean> A = ThreadLocal.withInitial(() -> false);

	public DoorBlockMixin(Properties properties) { super(properties); }

	// TODO couple the doors pls

	@Inject(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/DoorBlock;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)V"))
	private void openSesameRedstone(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl, CallbackInfo ci) {
		if(level.getBlockEntity(blockPos.below(blockState.getValue(HALF) == DoubleBlockHalf.UPPER ? 1 : 0)) instanceof DoorBlockEntity blockEntity)
			blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
	}

	@Inject(method = "setOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/DoorBlock;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)V"))
	private void openSesameVillagers(Entity entity, Level level, BlockState blockState, BlockPos blockPos, boolean bl, CallbackInfo ci) {
		if(level.getBlockEntity(blockPos.below(blockState.getValue(HALF) == DoubleBlockHalf.UPPER ? 1 : 0)) instanceof DoorBlockEntity blockEntity)
			blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).justOpened();
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/DoorBlock;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)V"))
	private void openSesamePlayers(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
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
