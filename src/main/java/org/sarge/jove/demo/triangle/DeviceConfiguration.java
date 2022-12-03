package org.sarge.jove.demo.triangle;

import org.sarge.jove.common.Handle;
import org.sarge.jove.platform.vulkan.VkQueueFlag;
import org.sarge.jove.platform.vulkan.core.*;
import org.sarge.jove.platform.vulkan.core.LogicalDevice.RequiredQueue;
import org.sarge.jove.platform.vulkan.core.PhysicalDevice.Selector;
import org.sarge.jove.platform.vulkan.render.Swapchain;
import org.sarge.jove.platform.vulkan.util.ValidationLayer;
import org.springframework.context.annotation.*;

@Configuration
class DeviceConfiguration {
	private final Selector graphics = Selector.of(VkQueueFlag.GRAPHICS);
	private final Selector presentation;

	public DeviceConfiguration(Handle surface) {
		presentation = Selector.of(surface);
	}

	@Bean
	public PhysicalDevice physical(Instance instance) {
		return PhysicalDevice
				.devices(instance)
				.filter(graphics)
				.filter(presentation)
				.findAny()
				.orElseThrow(() -> new RuntimeException("No suitable physical device available"));
	}

	@Bean
	public LogicalDevice device(PhysicalDevice dev) {
		return new LogicalDevice.Builder(dev)
				.extension(Swapchain.EXTENSION)
				.layer(ValidationLayer.STANDARD_VALIDATION)
				.queue(new RequiredQueue(graphics.select(dev)))
				.queue(new RequiredQueue(presentation.select(dev)))
				.build();
	}

	@Bean
	public WorkQueue graphics(LogicalDevice dev) {
		return dev.queue(graphics.select(dev.parent()));
	}

	@Bean
	public WorkQueue presentation(LogicalDevice dev) {
		return dev.queue(presentation.select(dev.parent()));
	}
}
