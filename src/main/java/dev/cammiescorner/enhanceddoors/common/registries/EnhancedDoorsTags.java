package dev.cammiescorner.enhanceddoors.common.registries;

import dev.cammiescorner.enhanceddoors.EnhancedDoors;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class EnhancedDoorsTags {
	public static final TagKey<Block> DONT_COUPLE = TagKey.create(Registries.BLOCK, EnhancedDoors.id("dont_couple"));
}
