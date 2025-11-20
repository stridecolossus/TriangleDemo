package org.sarge.jove.demo.triangle;
import org.sarge.jove.platform.vulkan.VkQueueFlag;
import org.sarge.jove.platform.vulkan.core.*;
import org.sarge.jove.platform.vulkan.core.LogicalDevice.RequiredQueue;
import org.sarge.jove.platform.vulkan.core.PhysicalDevice.Selector;
import org.sarge.jove.platform.vulkan.render.Swapchain;
import org.springframework.context.annotation.*;

@Configuration
class DeviceConfiguration {
	private final Selector graphics;
	private final Selector presentation;

	public DeviceConfiguration(VulkanSurface surface) {
		this.graphics = Selector.queue(VkQueueFlag.GRAPHICS);
		this.presentation = new Selector(surface::isPresentationSupported);
	}

	@Bean
	public PhysicalDevice physical(Instance instance, VulkanCoreLibrary lib) {
		return PhysicalDevice
				.enumerate(instance, lib)
				.filter(graphics)
				.filter(presentation)
				.findAny()
				.orElseThrow(() -> new RuntimeException("No suitable physical device available"));
	}

	@Bean
	public LogicalDevice device(PhysicalDevice device, VulkanCoreLibrary lib) {
		return new LogicalDevice.Builder(device)
				.extension(Swapchain.EXTENSION)
				.layer(DiagnosticHandler.STANDARD_VALIDATION)
				.queue(new RequiredQueue(graphics.family(device)))
				//.queue(new RequiredQueue(presentation.family(device)))
				.build(lib);
	}

	@Bean
	public WorkQueue graphics(LogicalDevice dev, PhysicalDevice physical) {
		return dev.queues().get(graphics.family(physical)).getFirst();
	}

	@Bean
	public WorkQueue presentation(LogicalDevice dev, PhysicalDevice physical) {
		return dev.queues().get(presentation.family(physical)).getFirst();
	}
}
