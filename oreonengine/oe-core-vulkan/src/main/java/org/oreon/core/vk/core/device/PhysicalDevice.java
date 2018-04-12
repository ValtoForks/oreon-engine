package org.oreon.core.vk.core.device;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkEnumeratePhysicalDevices;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceMemoryProperties;

import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.oreon.core.vk.core.queue.QueueFamilies;
import org.oreon.core.vk.core.swapchain.SwapChainCapabilities;
import org.oreon.core.vk.core.util.DeviceCapabilities;
import org.oreon.core.vk.core.util.VkUtil;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
public class PhysicalDevice {

	private VkPhysicalDevice handle;
	private VkPhysicalDeviceProperties properties;
	private VkPhysicalDeviceFeatures features;
	private QueueFamilies queueFamilies;
	private SwapChainCapabilities swapChainCapabilities;
	private VkPhysicalDeviceMemoryProperties memoryProperties;
	private List<String> supportedExtensionNames;
	
	public PhysicalDevice(VkInstance vkInstance, long surface) {

		IntBuffer pPhysicalDeviceCount = memAllocInt(1);
        int err = vkEnumeratePhysicalDevices(vkInstance, pPhysicalDeviceCount, null);
        if (err != VK_SUCCESS) {
            throw new AssertionError("Failed to get number of physical devices: " + VkUtil.translateVulkanResult(err));
        }
        
        log.info("Available Physical Devices: " + pPhysicalDeviceCount.get(0));
        
        PointerBuffer pPhysicalDevices = memAllocPointer(pPhysicalDeviceCount.get(0));
        err = vkEnumeratePhysicalDevices(vkInstance, pPhysicalDeviceCount, pPhysicalDevices);
        long physicalDevice = pPhysicalDevices.get(0);
       
        if (err != VK_SUCCESS) {
            throw new AssertionError("Failed to get physical devices: " + VkUtil.translateVulkanResult(err));
        }
        
        memFree(pPhysicalDeviceCount);
        memFree(pPhysicalDevices);
        
        handle =  new VkPhysicalDevice(physicalDevice, vkInstance);
        queueFamilies = new QueueFamilies(handle, surface);
        swapChainCapabilities = new SwapChainCapabilities(handle, surface);
        supportedExtensionNames = DeviceCapabilities.getPhysicalDeviceExtensionNamesSupport(handle);
        
        memoryProperties = VkPhysicalDeviceMemoryProperties.calloc();
        vkGetPhysicalDeviceMemoryProperties(handle, memoryProperties);
        
        properties = DeviceCapabilities.checkPhysicalDeviceProperties(handle);
        features = DeviceCapabilities.checkPhysicalDeviceFeatures(handle);
	}
	
	public void checkDeviceExtensionsSupport(PointerBuffer ppEnabledExtensionNames){
		
		for (int i=0; i<ppEnabledExtensionNames.limit(); i++){
			if (!supportedExtensionNames.contains(ppEnabledExtensionNames.getStringUTF8())){
				throw new AssertionError("Extension " + ppEnabledExtensionNames.getStringUTF8() + " not supported");
			}
		}
		
		ppEnabledExtensionNames.flip();
	}
	
	public void checkDeviceFormatAndColorSpaceSupport(int format, int colorSpace){
		
		swapChainCapabilities.checkVkSurfaceFormatKHRSupport(format, colorSpace);
	}
	
	public boolean checkDevicePresentationModeSupport(int presentMode){
		
		return swapChainCapabilities.checkPresentationModeSupport(presentMode);
	}
	
	public int getDeviceMinImageCount4TripleBuffering(){
		
		return swapChainCapabilities.getMinImageCount4TripleBuffering();
	}

}
