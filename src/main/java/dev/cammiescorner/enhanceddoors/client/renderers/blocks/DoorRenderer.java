package dev.cammiescorner.enhanceddoors.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.enhanceddoors.common.blocks.entities.DoorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class DoorRenderer implements BlockEntityRenderer<DoorBlockEntity> {
	private final BlockEntityRendererProvider.Context context;

	public DoorRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public void render(DoorBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		// TODO animate the doors pls
	}
}
