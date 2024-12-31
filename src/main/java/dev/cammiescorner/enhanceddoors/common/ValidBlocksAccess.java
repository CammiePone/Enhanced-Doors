package dev.cammiescorner.enhanceddoors.common;

import net.minecraft.world.level.block.Block;

import java.util.Set;

public interface ValidBlocksAccess {
	default void addBlockToDoorType(Block block) { throw new UnsupportedOperationException(); }

	default Set<Block> getValidBlocks() { throw new UnsupportedOperationException(); }
}
