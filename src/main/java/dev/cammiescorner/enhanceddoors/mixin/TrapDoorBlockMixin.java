package dev.cammiescorner.enhanceddoors.mixin;

import dev.cammiescorner.enhanceddoors.common.GotAnyGrapes;
import dev.cammiescorner.enhanceddoors.common.blocks.entities.TrapDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TrapDoorBlock.class)
public abstract class TrapDoorBlockMixin extends HorizontalDirectionalBlock implements EntityBlock, GotAnyGrapes {
	@Shadow @Final public static EnumProperty<Half> HALF;
	@Shadow @Final public static BooleanProperty POWERED;
	@Shadow @Final public static BooleanProperty OPEN;

	@Unique private final ThreadLocal<Boolean> A = ThreadLocal.withInitial(() -> false);

	public TrapDoorBlockMixin(Properties properties) { super(properties); }

	// TODO couple trapdoors

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrapDoorBlockEntity(blockPos, blockState);
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return A.get() ? super.getRenderShape(blockState) : RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void setUseSuper(boolean bl) {
		A.set(bl);
	}
}
