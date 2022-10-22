package org.sarge.jove.demo.triangle;

import org.sarge.jove.platform.desktop.Desktop;
import org.sarge.jove.platform.vulkan.core.*;
import org.sarge.jove.platform.vulkan.util.ValidationLayer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

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
				.extension(Handler.EXTENSION)
				.extensions(desktop.extensions())
				.layer(ValidationLayer.STANDARD_VALIDATION)
				.build(lib);
	}

	@Bean
	static Handler diagnostics(Instance instance) {
		return instance.handler().build();
	}
}
