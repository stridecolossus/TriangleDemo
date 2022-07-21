package org.sarge.jove.demo.triangle;

import java.util.List;

import org.sarge.jove.common.*;
import org.sarge.jove.platform.vulkan.*;
import org.sarge.jove.platform.vulkan.core.*;
import org.sarge.jove.platform.vulkan.render.*;
import org.sarge.jove.platform.vulkan.render.Subpass.Reference;
import org.springframework.context.annotation.*;

@Configuration
class PresentationConfiguration {
	@Bean
	public static Surface surface(Handle surface, PhysicalDevice dev) {
		return new Surface(surface, dev);
	}

	@Bean
	public static Swapchain swapchain(LogicalDevice dev, Surface surface) {
		return new Swapchain.Builder(dev, surface)
				.count(2)
				.clear(new Colour(0.3f, 0.3f, 0.3f, 1))
				.build();
	}

	@Bean
	public static RenderPass pass(LogicalDevice dev, Swapchain swapchain) {
		// Create colour attachment
		final Attachment attachment = new Attachment.Builder()
				.format(swapchain.format())
				.load(VkAttachmentLoadOp.CLEAR)
				.store(VkAttachmentStoreOp.STORE)
				.finalLayout(VkImageLayout.PRESENT_SRC_KHR)
				.build();

		// Create sub-pass
		final Subpass subpass = new Subpass.Builder()
				.colour(new Reference(attachment, VkImageLayout.COLOR_ATTACHMENT_OPTIMAL))
				.build();

		// Create render pass
		return RenderPass.create(dev, List.of(subpass));
	}

	@Bean
	public static FrameBuffer frame(Swapchain swapchain, RenderPass pass) {
		return new FrameBuffer.Builder()
				.pass(pass)
				.extents(swapchain.extents())
				.build(swapchain.attachments())
				.get(0);
	}
}
