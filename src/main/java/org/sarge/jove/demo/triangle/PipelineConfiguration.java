package org.sarge.jove.demo.triangle;

import java.io.*;

import org.sarge.jove.io.*;
import org.sarge.jove.platform.vulkan.VkShaderStage;
import org.sarge.jove.platform.vulkan.core.LogicalDevice;
import org.sarge.jove.platform.vulkan.pipeline.*;
import org.sarge.jove.platform.vulkan.render.*;
import org.springframework.context.annotation.*;

@Configuration
class PipelineConfiguration {
	private final LogicalDevice dev;
	private final ResourceLoaderAdapter<InputStream, Shader> loader;

	public PipelineConfiguration(LogicalDevice dev, DataSource src) {
		this.dev = dev;
		this.loader = new ResourceLoaderAdapter<>(src, new Shader.Loader(dev));
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
	PipelineLayout pipelineLayout() {
		return new PipelineLayout.Builder().build(dev);
	}

	@Bean
	public Pipeline pipeline(RenderPass pass, Swapchain swapchain, Shader vertex, Shader fragment, PipelineLayout layout) {
		return new GraphicsPipelineBuilder(pass)
				.viewport(swapchain.extents().rectangle())
				.shader(new ProgrammableShaderStage(VkShaderStage.VERTEX, vertex))
				.shader(new ProgrammableShaderStage(VkShaderStage.FRAGMENT, fragment))
				.build(dev, layout);
	}
}
