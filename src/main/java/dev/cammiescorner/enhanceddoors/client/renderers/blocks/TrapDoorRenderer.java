package dev.cammiescorner.enhanceddoors.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.enhanceddoors.common.GotAnyGrapes;
import dev.cammiescorner.enhanceddoors.common.blocks.entities.TrapDoorBlockEntity;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsComponents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;

public class TrapDoorRenderer implements BlockEntityRenderer<TrapDoorBlockEntity> {
	private final BlockEntityRendererProvider.Context context;

	public TrapDoorRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public void render(TrapDoorBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
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

		// TODO render trapdoor
		((GotAnyGrapes) defaultState.getBlock()).setUseSuper(true);
		blockRenderer.renderSingleBlock(defaultState, poseStack, multiBufferSource, i, j);
		((GotAnyGrapes) defaultState.getBlock()).setUseSuper(false);

		poseStack.popPose();
	}
}
