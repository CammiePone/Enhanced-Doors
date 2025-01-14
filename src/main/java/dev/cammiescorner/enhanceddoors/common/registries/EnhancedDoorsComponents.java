package dev.cammiescorner.enhanceddoors.common.registries;

import dev.cammiescorner.enhanceddoors.EnhancedDoors;
import dev.cammiescorner.enhanceddoors.common.components.OpeningProgressComponent;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EnhancedDoorsComponents implements BlockComponentInitializer {
	public static final ComponentKey<OpeningProgressComponent> OPENING_PROGRESS = createComponent("opening_progress", OpeningProgressComponent.class);

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
		// A duck walked up to a lemonade stand
		// And he said to the man, running the stand
		// "Hey! (Bum bum bum) Got any grapes?"
		registry.registerFor(BlockEntity.class, OPENING_PROGRESS, OpeningProgressComponent::new);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(EnhancedDoors.id(name), component);
	}
}
