package org.sarge.jove.demo.triangle;

import java.io.IOException;

import org.sarge.jove.common.Colour;
import org.sarge.jove.platform.vulkan.VkAttachmentLoadOp;
import org.sarge.jove.platform.vulkan.VkAttachmentStoreOp;
import org.sarge.jove.platform.vulkan.VkCullMode;
import org.sarge.jove.platform.vulkan.VkImageLayout;
import org.sarge.jove.platform.vulkan.VkShaderStage;
import org.sarge.jove.platform.vulkan.core.LogicalDevice;
import org.sarge.jove.platform.vulkan.core.Shader;
import org.sarge.jove.platform.vulkan.core.Shader.ShaderLoader;
import org.sarge.jove.platform.vulkan.core.Surface;
import org.sarge.jove.platform.vulkan.pipeline.Pipeline;
import org.sarge.jove.platform.vulkan.pipeline.PipelineLayout;
import org.sarge.jove.platform.vulkan.render.Attachment;
import org.sarge.jove.platform.vulkan.render.RenderPass;
import org.sarge.jove.platform.vulkan.render.Swapchain;
import org.sarge.jove.util.DataSource;
import org.sarge.jove.util.ResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PipelineConfiguration {
	private final LogicalDevice dev;
	private final ResourceLoader<String, Shader> loader;

	/**
	 * Constructor.
	 * @param dev
	 */
	public PipelineConfiguration(LogicalDevice dev, DataSource src) {
		this.dev = dev;
		this.loader = ResourceLoader.of(src, new ShaderLoader(dev));
	}

	@Bean
	public Swapchain swapchain(Surface surface) {
		return new Swapchain.Builder(dev, surface)
				.count(2)
				.clear(new Colour(0.3f, 0.3f, 0.3f, 1))
				.build();
	}

	@Bean
	public RenderPass pass() {
		// TODO - helper
		final Attachment attachment = new Attachment.Builder()
				.format(Swapchain.DEFAULT_FORMAT)
				.load(VkAttachmentLoadOp.CLEAR)
				.store(VkAttachmentStoreOp.STORE)
				.finalLayout(VkImageLayout.PRESENT_SRC_KHR)
				.build();

		return new RenderPass.Builder()
				.subpass()
					.colour(attachment, VkImageLayout.COLOR_ATTACHMENT_OPTIMAL)
					.build()
				.build(dev);
	}

	@Bean
	public Shader vertex() throws IOException {
		return loader.load("spv.triangle.vert");
	}

	@Bean
	public Shader fragment() throws IOException {
		return loader.load("spv.triangle.frag");
	}

	@Bean
	public Pipeline pipeline(RenderPass pass, Swapchain swapchain, Shader vertex, Shader fragment) {
		final var layout = new PipelineLayout.Builder(dev).build();

		return new Pipeline.Builder()
				.layout(layout)
				.pass(pass)
				.viewport(swapchain.extents())
				.rasterizer()
					.cull(VkCullMode.NONE) // TODO
					.build()
				.shader()
					.stage(VkShaderStage.VERTEX)
					.shader(vertex)
					.build()
				.shader()
					.stage(VkShaderStage.FRAGMENT)
					.shader(fragment)
					.build()
				.build(dev);
	}
}
