package org.oreon.core.vk.core.synchronization;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkCreateSemaphore;
import static org.lwjgl.vulkan.VK10.vkDestroySemaphore;

import java.nio.LongBuffer;

import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;
import org.oreon.core.vk.core.util.VkUtil;

import lombok.Getter;

public class VkSemaphore {
	
	@Getter
	private long handle;
	@Getter
	private LongBuffer pHandle;

	public VkSemaphore(VkDevice device) {
		
		VkSemaphoreCreateInfo semaphoreCreateInfo = VkSemaphoreCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO)
                .pNext(0)
                .flags(0);
		
		pHandle = memAllocLong(1);
		
		int err = vkCreateSemaphore(device, semaphoreCreateInfo, null, pHandle);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to create semaphore: " + VkUtil.translateVulkanResult(err));
		}
		
		handle = pHandle.get(0);
		
		semaphoreCreateInfo.free();
	}
	
	public void destroy(VkDevice device){
		
		vkDestroySemaphore(device, handle, null);
	}

}
