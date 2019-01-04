package org.oreon.gl.components.filter.bloom;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.oreon.core.gl.pipeline.GLShaderProgram;
import org.oreon.core.gl.texture.GLTexture;
import org.oreon.core.util.ResourceLoader;

public class HorizontalBloomBlurShader extends GLShaderProgram{

	private static HorizontalBloomBlurShader instance = null;
	
	public static HorizontalBloomBlurShader getInstance() 
	{
	    if(instance == null) 
	    {
	    	instance = new HorizontalBloomBlurShader();
	    }
	      return instance;
	}
	
	protected HorizontalBloomBlurShader()
	{
		super();
		
		addComputeShader(ResourceLoader.loadShader("shaders/filter/bloom/horizontalGaussianBlur.comp"));
		
		compileShader();
		
		addUniform("sceneBrightnessSampler");
		addUniform("windowWidth");
		addUniform("windowHeight");
	}
	
	public void updateUniforms(GLTexture sceneBrightnessSampler, int width, int height)
	{
		glActiveTexture(GL_TEXTURE0);
		sceneBrightnessSampler.bind();
		setUniformi("sceneBrightnessSampler", 0);
		setUniformf("windowWidth", width);
		setUniformf("windowHeight", height);
	}
}
