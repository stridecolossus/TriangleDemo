package org.sarge.jove.demo.triangle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.sarge.jove.common.Dimensions;
import org.sarge.jove.common.Handle;
import org.sarge.jove.platform.desktop.Desktop;
import org.sarge.jove.platform.desktop.Window;
import org.sarge.jove.platform.vulkan.core.Instance;
import org.sarge.jove.platform.vulkan.core.PhysicalDevice;
import org.sarge.jove.platform.vulkan.core.Surface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("static-method")
class DesktopConfiguration {
	private final Desktop desktop = Desktop.create();
	private final Window window;

	public DesktopConfiguration() {
		window = new Window.Builder(desktop)
				.title("TriangleDemo")
				.size(new Dimensions(1024, 768))
				.property(Window.Property.DISABLE_OPENGL)
				.build();
	}

	@PostConstruct
	void validate() {
		if(!desktop.isVulkanSupported()) throw new RuntimeException("Vulkan not supported");
	}

	@Bean
	public Desktop desktop() {
		return desktop;
	}

	@Bean
	public Window window() {
		return window;
	}

	@Bean("surfaceHandle")
	Handle surface(Instance instance) {
		return window.surface(instance.handle());
	}

	@Bean
	public Surface surface(Handle handle, PhysicalDevice dev) {
		return new Surface(handle, dev);
	}

	@PreDestroy
	void destroy() {
		window.destroy();
		desktop.destroy();
	}
}
