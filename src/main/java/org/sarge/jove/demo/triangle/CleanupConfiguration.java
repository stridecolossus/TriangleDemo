package org.sarge.jove.demo.triangle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.sarge.jove.platform.vulkan.core.Instance;
import org.sarge.jove.platform.vulkan.core.LogicalDevice;
import org.sarge.jove.platform.vulkan.core.Shader;
import org.sarge.jove.platform.vulkan.core.Surface;
import org.sarge.jove.platform.vulkan.pipeline.Pipeline;
import org.sarge.jove.platform.vulkan.render.RenderPass;
import org.sarge.jove.platform.vulkan.render.Swapchain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class CleanupConfiguration {
	@Autowired private LogicalDevice dev;
	@Autowired private Surface surface;
	@Autowired private Swapchain swapchain;
	@Autowired private RenderPass pass;
	@Autowired private Shader vertex, fragment;
	@Autowired private Pipeline pipeline;

	@PostConstruct
	void handler() {
		final Instance instance = dev.parent().instance();
		instance.handler().init().attach();
	}

	@PreDestroy
	void cleanup() {
		// Release pipeline
		vertex.destroy();
		fragment.destroy();
		pass.destroy();
		pipeline.destroy();
		pipeline.layout().destroy();

		// Release rendering surface
		swapchain.destroy();
		surface.destroy();

		// Release devices
		dev.destroy();
		dev.parent().instance().destroy();
	}
}
