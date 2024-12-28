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
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class TrapDoorRenderer implements BlockEntityRenderer<TrapDoorBlockEntity> {
	private final BlockEntityRendererProvider.Context context;

	public TrapDoorRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public void render(TrapDoorBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
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
