package dev.cammiescorner.enhanceddoors.client;

import dev.cammiescorner.enhanceddoors.client.renderers.blocks.AnimatedDoorRenderer;
import dev.cammiescorner.enhanceddoors.common.GotAnyGrapes;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EnhancedDoorsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRenderers.register(EnhancedDoorsBlocks.ANIMATED_DOOR_ENTITY, AnimatedDoorRenderer::new);

		for(BlockEntityType<?> type : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
			if(BlockEntityRenderers.PROVIDERS.containsKey(type))
				continue;

			if(type.getValidBlocks().stream().anyMatch(block -> block instanceof GotAnyGrapes))
				BlockEntityRenderers.register(type, AnimatedDoorRenderer::new);
		}

		RegistryEntryAddedCallback.event(BuiltInRegistries.BLOCK_ENTITY_TYPE).register((rawId, id, type) -> {
			if(BlockEntityRenderers.PROVIDERS.containsKey(type))
				return;

			if(type.getValidBlocks().stream().anyMatch(block -> block instanceof GotAnyGrapes))
				BlockEntityRenderers.register(type, AnimatedDoorRenderer::new);
		});
	}
}
