package org.oreon.core.gl.deferred;

import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.glTexImage2DMultisample;

import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

import org.oreon.core.context.EngineContext;
import org.oreon.core.gl.texture.Texture2DMultisample;

public class GBufferMultisample {

	private Texture2DMultisample albedoTexture;
	private Texture2DMultisample normalTexture;
	private Texture2DMultisample worldPositionTexture;
	private Texture2DMultisample specularEmissionTexture;
	private Texture2DMultisample lightScatteringMask;
	private Texture2DMultisample depthTexture;
	
	public GBufferMultisample(int width, int height) {
		
		albedoTexture = new Texture2DMultisample();
		albedoTexture.generate();
		albedoTexture.bind();
		glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, EngineContext.getConfig().getMultisamples(), GL_RGBA16F, width, height, true);
		
		worldPositionTexture = new Texture2DMultisample();
		worldPositionTexture.generate();
		worldPositionTexture.bind();
		glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, EngineContext.getConfig().getMultisamples(), GL_RGBA32F, width, height, true);
		
		normalTexture = new Texture2DMultisample();
		normalTexture.generate();
		normalTexture.bind();
		glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, EngineContext.getConfig().getMultisamples(), GL_RGBA32F, width, height, true);
		
		specularEmissionTexture = new Texture2DMultisample();
		specularEmissionTexture.generate();
		specularEmissionTexture.bind();
		glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, EngineContext.getConfig().getMultisamples(), GL_RGBA16F, width, height, true);
		
		lightScatteringMask = new Texture2DMultisample();
		lightScatteringMask.generate();
		lightScatteringMask.bind();
		glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, EngineContext.getConfig().getMultisamples(), GL_RGBA16F, width, height, true);
		
		depthTexture = new Texture2DMultisample();
		depthTexture.generate();
		depthTexture.bind();
		glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, EngineContext.getConfig().getMultisamples(), GL_DEPTH_COMPONENT32F, width, height, true);
	}
		
	public Texture2DMultisample getAlbedoTexture() {
		return albedoTexture;
	}
	public void setAlbedoTexture(Texture2DMultisample colorTexture) {
		this.albedoTexture = colorTexture;
	}
	public Texture2DMultisample getNormalTexture() {
		return normalTexture;
	}
	public void setNormalTexture(Texture2DMultisample normalTexture) {
		this.normalTexture = normalTexture;
	}
	public Texture2DMultisample getSpecularEmissionTexture() {
		return specularEmissionTexture;
	}
	public void setSpecularEmissionTexture(Texture2DMultisample specularEmissionTexture) {
		this.specularEmissionTexture = specularEmissionTexture;
	}
	public Texture2DMultisample getWorldPositionTexture() {
		return worldPositionTexture;
	}
	public void setWorldPositionTexture(Texture2DMultisample worldPositionTexture) {
		this.worldPositionTexture = worldPositionTexture;
	}

	public Texture2DMultisample getDepthTexture() {
		return depthTexture;
	}

	public void setDepthTexture(Texture2DMultisample depthmap) {
		this.depthTexture = depthmap;
	}

	public Texture2DMultisample getLightScatteringMask() {
		return lightScatteringMask;
	}

	public void setLightScatteringMask(Texture2DMultisample lightScatteringMask) {
		this.lightScatteringMask = lightScatteringMask;
	}
}
