package dev.cammiescorner.enhanceddoors;

import com.teamresourceful.resourcefulconfig.common.annotations.Config;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;

@Config(EnhancedDoors.MOD_ID)
public final class EnhancedDoorsConfig {
	@ConfigEntry(
			id = "connectedDoors",
			type = EntryType.BOOLEAN,
			translation = "config." + EnhancedDoors.MOD_ID + ".connected_doors"
	)
	public static boolean connectedDoors = true;

	@ConfigEntry(
			id = "animateDoors",
			type = EntryType.BOOLEAN,
			translation = "config." + EnhancedDoors.MOD_ID + ".animate_doors"
	)
	public static boolean animateDoors = true;


	@ConfigEntry(
			id = "animationLength",
			type = EntryType.INTEGER,
			translation = "config." + EnhancedDoors.MOD_ID + ".animation_length"
	)
	public static int animationLength = 5;
}
