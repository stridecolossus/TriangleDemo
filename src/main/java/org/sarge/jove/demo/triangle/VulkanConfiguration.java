package org.sarge.jove.demo.triangle;

import org.sarge.jove.foreign.DefaultRegistry;
import org.sarge.jove.platform.desktop.Desktop;
import org.sarge.jove.platform.vulkan.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
class VulkanConfiguration {
	@Bean
	public static VulkanCoreLibrary library() {
		return Vulkan.create();
	}

	@Bean
	public static Instance instance(VulkanCoreLibrary lib, Desktop desktop, @Value("${application.title}") String title) {
		return new Instance.Builder()
				.name(title)
				.extension(DiagnosticHandler.EXTENSION)
				.layer(DiagnosticHandler.STANDARD_VALIDATION)
				.extensions(desktop.extensions())
				.build(lib);
	}

	@Bean
	static DiagnosticHandler diagnostics(Instance instance) {
		return new DiagnosticHandler.Builder().build(instance, DefaultRegistry.create());
	}
}
