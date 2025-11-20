package org.sarge.jove.demo.triangle;

import org.sarge.jove.platform.vulkan.VkSubpassContents;
import org.sarge.jove.platform.vulkan.core.*;
import org.sarge.jove.platform.vulkan.core.Command.*;
import org.sarge.jove.platform.vulkan.pipeline.Pipeline;
import org.sarge.jove.platform.vulkan.render.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.*;

@Configuration
public class RenderConfiguration {
	@Autowired
	private LogicalDevice device;

	@Bean
	public Pool pool(@Qualifier("presentation") WorkQueue presentation) {
		return Pool.create(device, presentation);
	}

	@Bean
	public Buffer sequence(Pool pool, Framebuffer frame, Pipeline pipeline) {
		return pool
				.allocate(1, true)
				.getFirst()
				.begin()
					.add(frame.begin(VkSubpassContents.INLINE))
    					.add(pipeline.bind())
    					.add(DrawCommand.draw(3, device))
					.add(frame.end())
				.end();
	}

	@Bean
	public ApplicationRunner render(Swapchain swapchain, Buffer render) {
		return args -> {
			// Start next frame
			final var semaphore = VulkanSemaphore.create(device);
			final int index = swapchain.acquire(semaphore, null);

			// Render frame and block
			Work.submit(render);

			// Present frame
			final WorkQueue queue = render.pool().queue();
			swapchain.present(queue, index, semaphore);

			// Bodge
			Thread.sleep(1000);

			// Cleanup
			semaphore.destroy();
		};
	}
}
