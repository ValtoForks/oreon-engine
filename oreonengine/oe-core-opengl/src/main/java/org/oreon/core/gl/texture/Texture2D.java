package org.oreon.core.gl.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import org.lwjgl.opengl.GL;
import org.oreon.core.texture.Texture;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;


public class Texture2D extends Texture{
	
	private int id;
	private int width;
	private int height;

	public Texture2D(){
	}
	
	public Texture2D(String file, int i){
		setPath(file);
		int[] data = ImageLoader.loadHeightmap(file);
		id = data[0];
		width = data[1];
		height = data[2];
	}
	
	public Texture2D(Texture2D texture){
		
		id = texture.getId();
		width = texture.getWidth();
		height = texture.getHeight();
	}
	
	public Texture2D(String file) {
		
		setPath(file);
		int[] data = ImageLoader.loadImage(file);
		id = data[0];
		width = data[1];
		height = data[2];
	}
	
	public void load(){
		
		int[] data = ImageLoader.loadImage(getPath());
		id = data[0];
		width = data[1];
		width = data[2];
	}
	
	public void loadHeightmap(String file){
		
		int[] data = ImageLoader.loadHeightmap(file);
		id = data[0];
		width = data[1];
		width = data[2];
	}
	
	public void generate(){
		
		id = glGenTextures();
	}
	
	public void delete(){
		
		glDeleteTextures(id);
	}
	
	public void bind(){
		
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void unbind(){
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void noFilter(){
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	}
	
	public void bilinearFilter(){
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	}
	
	public void trilinearFilter(){
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	}
	
	public void anisotropicFilter(){
		
		if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic){
			float maxfilterLevel = glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, maxfilterLevel);
		}
		else{
			System.out.println("anisotropic not supported");
		}
	}
	
	public void clampToEdge(){
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	}
	
	public void repeat(){
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	}
	
	public int getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
