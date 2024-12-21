package dev.cammiescorner.enhanceddoors.common.registries;

import dev.cammiescorner.enhanceddoors.EnhancedDoors;
import dev.cammiescorner.enhanceddoors.common.blocks.entities.DoorBlockEntity;
import dev.cammiescorner.enhanceddoors.common.blocks.entities.TrapDoorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.LinkedHashMap;

public class EnhancedDoorsBlocks {
	public static final LinkedHashMap<BlockEntityType<?>, ResourceLocation> BLOCK_ENTITIES = new LinkedHashMap<>();

	public static final BlockEntityType<DoorBlockEntity> DOOR_ENTITY = create("animated_door", FabricBlockEntityTypeBuilder.create(DoorBlockEntity::new).build());
	public static final BlockEntityType<TrapDoorBlockEntity> TRAPDOOR_ENTITY = create("animated_trapdoor", FabricBlockEntityTypeBuilder.create(TrapDoorBlockEntity::new).build());

	public static void register() {
		BLOCK_ENTITIES.keySet().forEach(blockEntity -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BLOCK_ENTITIES.get(blockEntity), blockEntity));
	}

	private static <T extends BlockEntityType<?>> T create(String name, T blockEntity) {
		BLOCK_ENTITIES.put(blockEntity, EnhancedDoors.id(name));
		return blockEntity;
	}
}
