package org.sarge.jove.demo.triangle;

import java.util.function.Predicate;

import org.sarge.jove.common.Handle;
import org.sarge.jove.platform.desktop.Desktop;
import org.sarge.jove.platform.vulkan.VkQueueFlag;
import org.sarge.jove.platform.vulkan.api.VulkanLibrary;
import org.sarge.jove.platform.vulkan.common.Queue;
import org.sarge.jove.platform.vulkan.common.Queue.Family;
import org.sarge.jove.platform.vulkan.common.ValidationLayer;
import org.sarge.jove.platform.vulkan.core.Instance;
import org.sarge.jove.platform.vulkan.core.LogicalDevice;
import org.sarge.jove.platform.vulkan.core.PhysicalDevice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("static-method")
class VulkanConfiguration {
	private static final Predicate<Family> GRAPHICS = Family.predicate(VkQueueFlag.GRAPHICS);

	private final VulkanLibrary lib = VulkanLibrary.create();

	@Bean
	public Instance instance(Desktop desktop) {
		return new Instance.Builder()
				.name("TriangleDemo")
				.extension(VulkanLibrary.EXTENSION_DEBUG_UTILS)
				.extensions(desktop.extensions())
				.layer(ValidationLayer.STANDARD_VALIDATION)
				.build(lib);
	}

	@Bean
	PhysicalDevice physical(Instance instance, Handle surface) {
		return PhysicalDevice
				.devices(instance)
				.filter(PhysicalDevice.filter(GRAPHICS))
				.filter(dev -> dev.presentation(surface).isPresent())
				.findAny()
				.orElseThrow();
	}

	@Bean
	Family graphicsFamily(PhysicalDevice dev) {
		return dev.families().stream().filter(GRAPHICS).findAny().orElseThrow();
	}

	@Bean
	public LogicalDevice device(PhysicalDevice dev, Family graphics, Family present) {
		return new LogicalDevice.Builder(dev)
				.extension(VulkanLibrary.EXTENSION_SWAP_CHAIN)
				.layer(ValidationLayer.STANDARD_VALIDATION)
				.queue(graphics)
				.queue(present)
				.build();
	}

	@Bean
	public Queue graphics(LogicalDevice dev, Family graphics) {
		return dev.queue(graphics);
	}

	@Bean
	public Queue presentation(LogicalDevice dev, Family present) {
		return dev.queue(present);
	}
}
