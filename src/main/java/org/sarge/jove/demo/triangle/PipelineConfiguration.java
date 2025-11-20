package org.sarge.jove.demo.triangle;

import java.io.IOException;
import java.nio.file.Paths;

import org.sarge.jove.platform.vulkan.*;
import org.sarge.jove.platform.vulkan.core.LogicalDevice;
import org.sarge.jove.platform.vulkan.pipeline.*;
import org.sarge.jove.platform.vulkan.pipeline.Shader.ShaderLoader;
import org.sarge.jove.platform.vulkan.render.*;
import org.springframework.context.annotation.*;

@Configuration
class PipelineConfiguration {
	private final LogicalDevice dev;

	public PipelineConfiguration(LogicalDevice dev) {
		this.dev = dev;
	}

	@Bean
	public Shader vertex() throws IOException {
		final var loader = new ShaderLoader(dev);
		return loader.load(Paths.get("src/main/resources/spv.triangle.vert"));
	}

	@Bean
	public Shader fragment() throws IOException {
		final var loader = new ShaderLoader(dev);
		return loader.load(Paths.get("src/main/resources/spv.triangle.frag"));
	}

	@Bean
	PipelineLayout pipelineLayout() {
		return new PipelineLayout.Builder().build(dev);
	}

	@Bean
	public Pipeline pipeline(RenderPass pass, Swapchain swapchain, Shader vertex, Shader fragment, PipelineLayout layout) {
		final var builder = new GraphicsPipelineBuilder();
		builder.layout(layout);
		builder.pass(pass);
		builder.viewport().viewportAndScissor(swapchain.extents().rectangle());
		builder.rasterizer().cull(VkCullMode.NONE);
		builder.shader(new ProgrammableShaderStage(VkShaderStage.VERTEX, vertex));
		builder.shader(new ProgrammableShaderStage(VkShaderStage.FRAGMENT, fragment));
		return builder.build(dev);
	}
}
