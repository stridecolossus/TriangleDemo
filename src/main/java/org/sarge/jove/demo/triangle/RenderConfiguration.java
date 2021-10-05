package org.sarge.jove.demo.triangle;

import java.util.Set;

import org.sarge.jove.platform.vulkan.common.Command.Buffer;
import org.sarge.jove.platform.vulkan.common.Command.Pool;
import org.sarge.jove.platform.vulkan.common.Queue;
import org.sarge.jove.platform.vulkan.core.LogicalDevice;
import org.sarge.jove.platform.vulkan.core.Work;
import org.sarge.jove.platform.vulkan.pipeline.Pipeline;
import org.sarge.jove.platform.vulkan.render.DrawCommand;
import org.sarge.jove.platform.vulkan.render.FrameBuffer;
import org.sarge.jove.platform.vulkan.render.Swapchain;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RenderConfiguration {
	@Bean
	public static Pool pool(LogicalDevice dev, @Qualifier("presentation") Queue presentation) {
		return Pool.create(dev, presentation);
	}

	@Bean
	public static Buffer sequence(Pool pool, FrameBuffer frame, Pipeline pipeline) {
		return pool
				.allocate()
				.begin()
					.add(frame.begin())
					.add(pipeline.bind())
					.add(DrawCommand.draw(3))
					.add(FrameBuffer.END)
				.end();
	}

	@Bean
	public static ApplicationRunner render(Swapchain swapchain, Buffer render) {
		return args -> {
			// Start next frame
			final int index = swapchain.acquire(null, null);

			// Render frame
			//final VulkanLibrary lib = dev.library();
			Work.of(render).submit(null);

			// TODO
			final Pool pool = render.pool();
			pool.waitIdle();
//			queue.waitIdle(lib);

			// Present frame
			// TODO - present should accept frame index?
			swapchain.present(pool.queue(), Set.of());

			// TODO
			Thread.sleep(1000);
		};
	}
}
