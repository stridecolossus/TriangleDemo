package org.sarge.jove.demo.triangle;

import org.sarge.jove.common.*;
import org.sarge.jove.platform.vulkan.core.*;
import org.sarge.jove.platform.vulkan.render.*;
import org.springframework.context.annotation.*;

@Configuration
class PresentationConfiguration {
	@Bean
	public static Surface surface(Handle surface, PhysicalDevice dev) {
		return new Surface(surface, dev);
	}

	@Bean
	public static Swapchain swapchain(LogicalDevice dev, Surface surface) {
		return new Swapchain.Builder(surface)
				.count(2)
				.clear(new Colour(0.3f, 0.3f, 0.3f, 1))
				.build(dev);
	}

	@Bean
	public static RenderPass pass(LogicalDevice dev, Swapchain swapchain) {
		final Attachment attachment = Attachment.colour(swapchain.format());
		return new Subpass().colour(attachment).create(dev);
	}

	@Bean
	public static FrameBuffer frame(Swapchain swapchain, RenderPass pass) {
		return FrameBuffer.create(pass, swapchain.extents(), swapchain.attachments().subList(0, 1));
	}
}
