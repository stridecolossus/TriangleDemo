package org.sarge.jove.demo.triangle;

import java.util.Set;

import org.sarge.jove.platform.vulkan.common.Queue;
import org.sarge.jove.platform.vulkan.core.Command.Buffer;
import org.sarge.jove.platform.vulkan.core.Command.Pool;
import org.sarge.jove.platform.vulkan.core.LogicalDevice;
import org.sarge.jove.platform.vulkan.core.Semaphore;
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
	public static ApplicationRunner render(LogicalDevice dev, Swapchain swapchain, Buffer render) {
		return args -> {
			// Start next frame
			final Semaphore semaphore = Semaphore.create(dev);
			final int index = swapchain.acquire(semaphore, null);

			// Render frame
			final Pool pool = render.pool();
			Work.of(render).submit(null);
			pool.waitIdle();

			// Present frame
			swapchain.present(pool.queue(), index, Set.of());

			// TODO
			Thread.sleep(1000);

			// Cleanup
			semaphore.destroy();
		};
	}
}
