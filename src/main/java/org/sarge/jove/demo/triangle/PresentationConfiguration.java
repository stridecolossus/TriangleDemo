package org.sarge.jove.demo.triangle;

import org.sarge.jove.common.*;
import org.sarge.jove.platform.desktop.Window;
import org.sarge.jove.platform.vulkan.*;
import org.sarge.jove.platform.vulkan.core.*;
import org.sarge.jove.platform.vulkan.render.*;
import org.springframework.context.annotation.*;

@Configuration
class PresentationConfiguration {
	@Bean
	static VulkanSurface surface(Window window, Instance instance, VulkanCoreLibrary lib) {
		return new VulkanSurface(window, instance, lib);
	}

	@Bean
	static VulkanSurface.Properties properties(VulkanSurface surface, PhysicalDevice device) {
		return surface.properties(device);
	}

	@Bean
	static Swapchain swapchain(LogicalDevice dev, VulkanSurface.Properties properties) {
		return new Swapchain.Builder()
				.count(2)
				.extent(new Dimensions(1024, 768))
				.format(new SurfaceFormatWrapper(VkFormat.B8G8R8A8_UNORM, VkColorSpaceKHR.SRGB_NONLINEAR_KHR))
				.clear(new Colour(0.3f, 0.3f, 0.3f, 1))
				.build(dev, properties);
	}

	@Bean
	static RenderPass pass(LogicalDevice dev, Swapchain swapchain) {
		final Subpass subpass = new Subpass.Builder()
				.colour(Attachment.colour(swapchain.format()))
				.build();

		return new RenderPass.Builder()
				.add(subpass)
				.build(dev);
	}

	@Bean
	static Framebuffer framebuffer(Swapchain swapchain, RenderPass pass) {
		return Framebuffer.create(
				pass,
				swapchain.extents().rectangle(),
				swapchain.attachments().subList(0, 1)
		);
	}
}
