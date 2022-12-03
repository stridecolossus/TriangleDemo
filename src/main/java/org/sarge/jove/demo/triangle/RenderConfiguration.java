package org.sarge.jove.demo.triangle;

import org.sarge.jove.platform.vulkan.core.*;
import org.sarge.jove.platform.vulkan.core.Command.*;
import org.sarge.jove.platform.vulkan.pipeline.Pipeline;
import org.sarge.jove.platform.vulkan.render.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.*;

@Configuration
public class RenderConfiguration {
	@Bean
	public static Pool pool(LogicalDevice dev, @Qualifier("presentation") WorkQueue presentation) {
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
			new Work.Builder(pool)
					.add(render)
					.build()
					.submit(null);

			// Present frame
			pool.waitIdle();
			swapchain.present(pool.queue(), index, semaphore);

			// Bodge
			Thread.sleep(1000);

			// Cleanup
			semaphore.destroy();
		};
	}
}
