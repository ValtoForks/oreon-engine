package org.oreon.core.vk.core.image;

import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class VkImageLoader {

	public static ByteBuffer loadImage(String file){
		
		String relativePath = "./src/main/resources/" + file;

	    IntBuffer x = BufferUtils.createIntBuffer(1);
	    IntBuffer y = BufferUtils.createIntBuffer(1);
	    IntBuffer channels = BufferUtils.createIntBuffer(1);
	    
	    ByteBuffer image = stbi_load(relativePath, x, y, channels, STBI_rgb_alpha);
	    
	    return image;
	}
}
