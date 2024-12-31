package dev.cammiescorner.enhanceddoors.common.components;

import dev.cammiescorner.enhanceddoors.EnhancedDoorsConfig;
import dev.cammiescorner.enhanceddoors.common.registries.EnhancedDoorsComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class OpeningProgressComponent implements AutoSyncedComponent, ClientTickingComponent {
	private final @Nullable BlockEntity door;
	private long openingTicks = 0;
	private long prevOpeningTicks = 0;

	public OpeningProgressComponent(BlockEntity door) {
		this.door = door;
	}

	@Override
	public void clientTick() {
		prevOpeningTicks = openingTicks;

		if(openingTicks > 0)
			openingTicks--;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		openingTicks = tag.getLong("OpenedAt");
		prevOpeningTicks = openingTicks;
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putLong("OpenedAt", openingTicks);
	}

	public float getProgress(float tickDelta) {
		if(!EnhancedDoorsConfig.animateDoors)
			return 1f;

		return 1 - Mth.clamp(Mth.lerp(tickDelta, prevOpeningTicks, openingTicks) / (float) EnhancedDoorsConfig.animationLength, 0f, 1f);
	}

	public void justOpened() {
		openingTicks = EnhancedDoorsConfig.animationLength;
		door.syncComponent(EnhancedDoorsComponents.OPENING_PROGRESS);
	}
}
