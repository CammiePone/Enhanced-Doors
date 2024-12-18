package dev.cammiescorner.enhanceddoors.mixin;

import com.google.common.collect.ImmutableSet;
import dev.cammiescorner.enhanceddoors.common.ValidBlocksAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin implements ValidBlocksAccess {
	@Shadow @Final @Mutable private Set<Block> validBlocks;

	@Override
	public void addBlockToDoorType(Block block) {
		validBlocks = ImmutableSet.<Block>builder().addAll(validBlocks).add(block).build();
	}
}