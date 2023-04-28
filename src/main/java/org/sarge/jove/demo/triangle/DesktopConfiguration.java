package org.sarge.jove.demo.triangle;

import org.sarge.jove.common.*;
import org.sarge.jove.platform.desktop.*;
import org.sarge.jove.platform.vulkan.core.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
class DesktopConfiguration {
	@Bean
	public static Desktop desktop() {
		final Desktop desktop = Desktop.create();
		if(!desktop.isVulkanSupported()) throw new RuntimeException("Vulkan not supported");
		return desktop;
	}

	@Bean
	public static Window window(Desktop desktop, @Value("${application.title}") String title) {
		return new Window.Builder()
				.title(title)
				.size(new Dimensions(1024, 768))
				.hint(Window.Hint.RESIZABLE, false)
				.hint(Window.Hint.CLIENT_API, 0)
				.build(desktop);
	}

	@Bean("surface-handle")
	public static Handle surface(Instance instance, Window window) {
		return window.surface(instance.handle());
	}
}
