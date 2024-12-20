package dev.cammiescorner.enhanceddoors.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.enhanceddoors.common.blocks.entities.DoorBlockEntity;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsComponents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class DoorRenderer implements BlockEntityRenderer<DoorBlockEntity> {
	private final BlockEntityRendererProvider.Context context;

	public DoorRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public void render(DoorBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		BlockRenderDispatcher blockRenderer = context.getBlockRenderDispatcher();
		Level level = blockEntity.getLevel();
		BlockPos pos = blockEntity.getBlockPos();
		BlockState state = blockEntity.getBlockState();
		BlockState defaultState = state.setValue(DoorBlock.OPEN, false);
		DoorHingeSide hingeSide = state.getValue(DoorBlock.HINGE);
		boolean isOpen = state.getValue(DoorBlock.OPEN);
		float openingProgress = blockEntity.getComponent(EnhancedDoorsComponents.OPENING_PROGRESS).getProgress();

		poseStack.pushPose();

		// TODO render door
		blockRenderer.renderSingleBlock(defaultState, poseStack, multiBufferSource, i, j);
		blockRenderer.renderSingleBlock(defaultState.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), poseStack, multiBufferSource, i, j);

		poseStack.popPose();
	}
}
