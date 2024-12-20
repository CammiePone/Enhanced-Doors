package dev.cammiescorner.enhanceddoors.common.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;

public class OpeningProgressComponent implements AutoSyncedComponent {
	private final BlockEntity door;
	private long openingTicks = 0;

	public OpeningProgressComponent(BlockEntity door) {
		this.door = door;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		openingTicks = tag.getLong("OpenedAt");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putLong("OpenedAt", openingTicks);
	}

	public float getProgress() {
		return Mth.clamp((door.getLevel().getGameTime() - openingTicks) / 10f, 0f, 1f);
	}

	public void justOpened() {
		openingTicks = door.getLevel().getGameTime();
	}
}
