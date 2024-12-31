package dev.cammiescorner.enhanceddoors.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.enhanceddoors.common.GotAnyGrapes;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsComponents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;

public class AnimatedDoorRenderer implements BlockEntityRenderer<BlockEntity> {
	private final BlockEntityRendererProvider.Context context;

	public AnimatedDoorRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public void render(BlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		if(blockEntity.getBlockState().getBlock() instanceof DoorBlock)
			renderDoor(blockEntity, tickDelta, poseStack, multiBufferSource, i, j);
		if(blockEntity.getBlockState().getBlock() instanceof TrapDoorBlock)
			renderTrapDoor(blockEntity, tickDelta, poseStack, multiBufferSource, i, j);
	}

	public void renderDoor(BlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		BlockRenderDispatcher blockRenderer = context.getBlockRenderDispatcher();
		BlockState state = blockEntity.getBlockState();
		BlockState defaultState = state.setValue(DoorBlock.OPEN, false);
		Direction facing = state.getValue(DoorBlock.FACING);
		DoorHingeSide hingeSide = state.getValue(DoorBlock.HINGE);
		boolean isOpen = state.getValue(DoorBlock.OPEN);
		float openingProgress = blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).getProgress(tickDelta);

		if(!isOpen)
			openingProgress = 1 - openingProgress;

		float offset = -0.1875f * openingProgress;

		poseStack.pushPose();

		switch(facing) {
			case NORTH -> {
				poseStack.translate(hingeSide == DoorHingeSide.RIGHT ? 1f : 0f, 0f, 1f);
				poseStack.mulPose(Axis.YP.rotationDegrees((hingeSide == DoorHingeSide.LEFT ? 1 : -1) * 90 * openingProgress));
				poseStack.translate(hingeSide == DoorHingeSide.RIGHT ? -1f : 0f, 0f, -1f);
				poseStack.translate(facing.getStepX() * offset, facing.getStepY() * offset, facing.getStepZ() * offset);
			}
			case EAST -> {
				poseStack.translate(0f, 0f, hingeSide == DoorHingeSide.RIGHT ? 1f : 0f);
				poseStack.mulPose(Axis.YP.rotationDegrees((hingeSide == DoorHingeSide.LEFT ? 1 : -1) * 90 * openingProgress));
				poseStack.translate(0f, 0f, hingeSide == DoorHingeSide.RIGHT ? -1f : 0f);
				poseStack.translate(facing.getStepX() * offset, facing.getStepY() * offset, facing.getStepZ() * offset);
			}
			case SOUTH -> {
				poseStack.translate(hingeSide == DoorHingeSide.RIGHT ? 0f : 1f, 0f, 0f);
				poseStack.mulPose(Axis.YP.rotationDegrees((hingeSide == DoorHingeSide.LEFT ? 1 : -1) * 90 * openingProgress));
				poseStack.translate(hingeSide == DoorHingeSide.RIGHT ? 0f : -1f, 0f, 0f);
				poseStack.translate(facing.getStepX() * offset, facing.getStepY() * offset, facing.getStepZ() * offset);
			}
			case WEST -> {
				poseStack.translate(1f, 0f, hingeSide == DoorHingeSide.RIGHT ? 0f : 1f);
				poseStack.mulPose(Axis.YP.rotationDegrees((hingeSide == DoorHingeSide.LEFT ? 1 : -1) * 90 * openingProgress));
				poseStack.translate(-1f, 0f, hingeSide == DoorHingeSide.RIGHT ? 0f : -1f);
				poseStack.translate(facing.getStepX() * offset, facing.getStepY() * offset, facing.getStepZ() * offset);
			}
			default -> {}
		}

		((GotAnyGrapes) defaultState.getBlock()).setUseSuper(true);
		blockRenderer.renderSingleBlock(defaultState, poseStack, multiBufferSource, i, j);
		poseStack.translate(0f, 1f, 0f);
		blockRenderer.renderSingleBlock(defaultState.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), poseStack, multiBufferSource, i, j);
		((GotAnyGrapes) defaultState.getBlock()).setUseSuper(false);

		poseStack.popPose();
	}

	public void renderTrapDoor(BlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		BlockRenderDispatcher blockRenderer = context.getBlockRenderDispatcher();
		BlockState state = blockEntity.getBlockState();
		BlockState defaultState = state.setValue(TrapDoorBlock.OPEN, false);
		Half half = state.getValue(TrapDoorBlock.HALF);
		Direction facing = state.getValue(TrapDoorBlock.FACING);
		boolean isOpen = state.getValue(TrapDoorBlock.OPEN);
		float openingProgress = blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).getProgress(tickDelta);

		if(!isOpen)
			openingProgress = 1 - openingProgress;

		float offset = -0.1875f * openingProgress;

		poseStack.pushPose();

		((GotAnyGrapes) state.getBlock()).setUseSuper(true);
		blockRenderer.renderSingleBlock(state, poseStack, multiBufferSource, i, j);
		((GotAnyGrapes) state.getBlock()).setUseSuper(false);

		poseStack.popPose();
	}
}
