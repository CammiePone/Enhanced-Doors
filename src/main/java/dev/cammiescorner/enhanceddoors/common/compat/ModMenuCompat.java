package dev.cammiescorner.enhanceddoors.common.compat;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cammiescorner.enhanceddoors.EnhancedDoors;
import dev.cammiescorner.enhanceddoors.EnhancedDoorsConfig;

public class ModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			ResourcefulConfig config = EnhancedDoors.configurator.getConfig(EnhancedDoorsConfig.class);

			if(config == null)
				return null;

			return new ConfigScreen(null, config);
		};
	}
}
