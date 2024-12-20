package dev.cammiescorner.enhanceddoors;

import dev.cammiescorner.enhanceddoors.common.ValidBlocksAccess;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsBlocks;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoorBlock;

public class EnhancedDoors implements ModInitializer {
	public static final String MOD_ID = "enhanceddoors";

	@Override
	public void onInitialize() {
		EnhancedDoorsBlocks.register();

		BuiltInRegistries.BLOCK.forEach(block -> {
			if(block instanceof DoorBlock && !block.defaultBlockState().is(EnhancedDoorsTags.DONT_ANIMATE))
				((ValidBlocksAccess) EnhancedDoorsBlocks.DOOR_ENTITY).addBlockToDoorType(block);
		});

		RegistryEntryAddedCallback.event(BuiltInRegistries.BLOCK).register((i, resourceLocation, block) -> {
			if(block instanceof DoorBlock && !block.defaultBlockState().is(EnhancedDoorsTags.DONT_ANIMATE))
				((ValidBlocksAccess) EnhancedDoorsBlocks.DOOR_ENTITY).addBlockToDoorType(block);
		});
	}

	public static ResourceLocation id(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}
