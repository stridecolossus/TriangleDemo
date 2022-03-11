package org.sarge.jove.demo.triangle;

import org.sarge.jove.platform.desktop.Desktop;
import org.sarge.jove.platform.vulkan.core.HandlerManager.Handler;
import org.sarge.jove.platform.vulkan.core.Instance;
import org.sarge.jove.platform.vulkan.core.VulkanLibrary;
import org.sarge.jove.platform.vulkan.util.ValidationLayer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class VulkanConfiguration {
	@Bean
	public static VulkanLibrary library() {
		return VulkanLibrary.create();
	}

	@Bean
	public static Instance instance(VulkanLibrary lib, Desktop desktop, @Value("${application.title}") String title) {
		return new Instance.Builder()
				.name(title)
				.extension(VulkanLibrary.EXTENSION_DEBUG_UTILS)
				.extensions(desktop.extensions())
				.layer(ValidationLayer.STANDARD_VALIDATION)
				.build(lib);
	}

	@Bean
	static Handler diagnostics(Instance instance) {
		return instance
				.manager()
				.builder()
				.init()
				.build();
	}
}
