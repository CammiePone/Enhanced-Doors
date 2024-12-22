package dev.cammiescorner.enhanceddoors;

import com.teamresourceful.resourcefulconfig.common.config.Configurator;
import dev.cammiescorner.enhanceddoors.common.ValidBlocksAccess;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsBlocks;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;

public class EnhancedDoors implements ModInitializer {
	public static final String MOD_ID = "enhanceddoors";
	public static final Configurator configurator = new Configurator();

	@Override
	public void onInitialize() {
		configurator.registerConfig(EnhancedDoorsConfig.class);
		EnhancedDoorsBlocks.register();

		BuiltInRegistries.BLOCK.forEach(block -> {
			if(block instanceof DoorBlock && !block.defaultBlockState().is(EnhancedDoorsTags.DONT_ANIMATE))
				((ValidBlocksAccess) EnhancedDoorsBlocks.DOOR_ENTITY).addBlockToDoorType(block);

			if(block instanceof TrapDoorBlock && !block.defaultBlockState().is(EnhancedDoorsTags.DONT_ANIMATE))
				((ValidBlocksAccess) EnhancedDoorsBlocks.TRAPDOOR_ENTITY).addBlockToDoorType(block);
		});

		RegistryEntryAddedCallback.event(BuiltInRegistries.BLOCK).register((i, resourceLocation, block) -> {
			if(block instanceof DoorBlock && !block.defaultBlockState().is(EnhancedDoorsTags.DONT_ANIMATE))
				((ValidBlocksAccess) EnhancedDoorsBlocks.DOOR_ENTITY).addBlockToDoorType(block);

			if(block instanceof TrapDoorBlock && !block.defaultBlockState().is(EnhancedDoorsTags.DONT_ANIMATE))
				((ValidBlocksAccess) EnhancedDoorsBlocks.TRAPDOOR_ENTITY).addBlockToDoorType(block);
		});
	}

	public static ResourceLocation id(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}
