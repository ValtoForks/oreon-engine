package org.oreon.modules.gl.postprocessfilter.dofblur;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL42.glTexStorage2D;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import java.nio.ByteBuffer;

import org.oreon.core.context.EngineContext;
import org.oreon.core.gl.buffers.GLFramebuffer;
import org.oreon.core.gl.surface.FullScreenQuad;
import org.oreon.core.gl.texture.Texture2D;
import org.oreon.core.gl.texture.Texture2DMultisample;

public class DepthOfFieldBlur {
	
	private Texture2D horizontalBlurSceneTexture;
	private Texture2D verticalBlurSceneTexture;
	private DepthOfFieldHorizontalBlurShader horizontalBlurShader;
	private DepthOfFieldVerticalBlurShader verticalBlurShader;
	
	private GLFramebuffer lowResFbo;
	private FullScreenQuad fullScreenQuad;
	private Texture2D lowResSceneSampler;
	
	
		
	public DepthOfFieldBlur() {
		
		horizontalBlurShader = DepthOfFieldHorizontalBlurShader.getInstance();
		verticalBlurShader = DepthOfFieldVerticalBlurShader.getInstance();
		
		fullScreenQuad = new FullScreenQuad();
		
		horizontalBlurSceneTexture = new Texture2D();
		horizontalBlurSceneTexture.generate();
		horizontalBlurSceneTexture.bind();
		glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA16F, EngineContext.getWindow().getWidth(), EngineContext.getWindow().getHeight());
		
		verticalBlurSceneTexture = new Texture2D();
		verticalBlurSceneTexture.generate();
		verticalBlurSceneTexture.bind();
		glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA16F, EngineContext.getWindow().getWidth(), EngineContext.getWindow().getHeight());
		
		lowResSceneSampler = new Texture2D();
		lowResSceneSampler.generate();
		lowResSceneSampler.bind();
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F,
						(int)(EngineContext.getWindow().getWidth()/1.2f),
						(int)(EngineContext.getWindow().getHeight()/1.2f),
						0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		lowResSceneSampler.bilinearFilter();
		lowResSceneSampler.clampToEdge();
		
		lowResFbo = new GLFramebuffer();
		lowResFbo.bind();
		lowResFbo.createColorTextureAttachment(lowResSceneSampler.getId(), 0);
		lowResFbo.checkStatus();
		lowResFbo.unbind();
	}
	
	public void render(Texture2DMultisample depthmap, Texture2D lightScatteringMask, Texture2D sceneSampler, int width, int height) {
		
		getLowResFbo().bind();
		fullScreenQuad.setTexture(sceneSampler);
		glViewport(0,0,(int)(width/1.2f),(int)(height/1.2f));
		fullScreenQuad.render();
		getLowResFbo().unbind();
		glViewport(0,0, width, height);
		
		horizontalBlurShader.bind();
		glBindImageTexture(0, sceneSampler.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(1, lowResSceneSampler.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(2, lightScatteringMask.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(3, horizontalBlurSceneTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		horizontalBlurShader.updateUniforms(depthmap);
		glDispatchCompute(EngineContext.getWindow().getWidth()/8, EngineContext.getWindow().getHeight()/8, 1);	
		glFinish();
		
		verticalBlurShader.bind();
		glBindImageTexture(0, horizontalBlurSceneTexture.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(1, lightScatteringMask.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(2, verticalBlurSceneTexture.getId(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		verticalBlurShader.updateUniforms(depthmap);
		glDispatchCompute(EngineContext.getWindow().getWidth()/8, EngineContext.getWindow().getHeight()/8, 1);	
		glFinish();
	}

	public Texture2D getVerticalBlurSceneTexture() {
		return verticalBlurSceneTexture;
	}

	public GLFramebuffer getLowResFbo() {
		return lowResFbo;
	}
}
