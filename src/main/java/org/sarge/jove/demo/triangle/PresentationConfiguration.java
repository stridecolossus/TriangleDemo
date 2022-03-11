package org.sarge.jove.demo.triangle;

import java.util.List;

import org.sarge.jove.common.Colour;
import org.sarge.jove.platform.vulkan.VkAttachmentLoadOp;
import org.sarge.jove.platform.vulkan.VkAttachmentStoreOp;
import org.sarge.jove.platform.vulkan.VkImageLayout;
import org.sarge.jove.platform.vulkan.core.LogicalDevice;
import org.sarge.jove.platform.vulkan.core.Surface;
import org.sarge.jove.platform.vulkan.render.Attachment;
import org.sarge.jove.platform.vulkan.render.FrameBuffer;
import org.sarge.jove.platform.vulkan.render.RenderPass;
import org.sarge.jove.platform.vulkan.render.Subpass;
import org.sarge.jove.platform.vulkan.render.Subpass.Reference;
import org.sarge.jove.platform.vulkan.render.Swapchain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PresentationConfiguration {
	@Bean
	public static Swapchain swapchain(LogicalDevice dev, Surface.Properties props) {
		return new Swapchain.Builder(dev, props)
				.count(2)
				.clear(new Colour(0.3f, 0.3f, 0.3f, 1))
				.build();
	}

	@Bean
	public static RenderPass pass(LogicalDevice dev) {
		// Create colour attachment
		// TODO - helper
		final Attachment attachment = new Attachment.Builder()
				.format(Swapchain.DEFAULT_FORMAT)
				.load(VkAttachmentLoadOp.CLEAR)
				.store(VkAttachmentStoreOp.STORE)
				.finalLayout(VkImageLayout.PRESENT_SRC_KHR)
				.build();

		// Create render pass
		// TODO - helper x 2?
		final Subpass subpass = new Subpass.Builder()
				.colour(new Reference(attachment, VkImageLayout.COLOR_ATTACHMENT_OPTIMAL))
				.build();
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
