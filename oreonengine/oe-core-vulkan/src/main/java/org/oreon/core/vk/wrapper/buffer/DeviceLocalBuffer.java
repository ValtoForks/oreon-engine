package org.oreon.core.vk.wrapper.buffer;

import static org.lwjgl.vulkan.VK10.VK_BUFFER_USAGE_TRANSFER_DST_BIT;
import static org.lwjgl.vulkan.VK10.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT;

import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.oreon.core.vk.core.buffers.VkBuffer;

import lombok.Getter;

public class DeviceLocalBuffer {
	
	@Getter
	private VkBuffer buffer;

	public DeviceLocalBuffer(VkDevice device,
			 				 VkPhysicalDeviceMemoryProperties memoryProperties,
			 				 int size,
			 				 int usage) {
		
		buffer = new VkBuffer();
		buffer.create(device, size,
							VK_BUFFER_USAGE_TRANSFER_DST_BIT | usage);
		buffer.allocateBuffer(memoryProperties,
				  			  VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);
		buffer.bindBufferMemory();
	}
	
	public void destroy(){
		
		buffer.destroy();
	}
}
