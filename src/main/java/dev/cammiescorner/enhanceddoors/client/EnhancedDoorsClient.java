package dev.cammiescorner.enhanceddoors.client;

import dev.cammiescorner.enhanceddoors.client.renderers.blocks.DoorRenderer;
import dev.cammiescorner.enhanceddoors.client.renderers.blocks.TrapDoorRenderer;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class EnhancedDoorsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRenderers.register(EnhancedDoorsBlocks.DOOR_ENTITY, DoorRenderer::new);
		BlockEntityRenderers.register(EnhancedDoorsBlocks.TRAPDOOR_ENTITY, TrapDoorRenderer::new);
	}
}
