package org.oreon.gl.demo.oreonworlds.shaders;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.oreon.core.context.EngineContext;
import org.oreon.core.gl.shaders.GLShader;
import org.oreon.core.scenegraph.Renderable;
import org.oreon.core.util.ResourceLoader;
import org.oreon.modules.gl.water.Water;
import org.oreon.modules.gl.water.WaterConfiguration;

public class OceanWireframeShader extends GLShader{

private static OceanWireframeShader instance = null;
	

	public static OceanWireframeShader getInstance() 
	{
	    if(instance == null) 
	    {
	    	instance = new OceanWireframeShader();
	    }
	      return instance;
	}
	
	protected OceanWireframeShader()
	{
		super();
		
		addVertexShader(ResourceLoader.loadShader("oreonworlds/shaders/ocean/Ocean_VS.glsl"));
		addTessellationControlShader(ResourceLoader.loadShader("oreonworlds/shaders/ocean/Ocean_TC.glsl"));
		addTessellationEvaluationShader(ResourceLoader.loadShader("oreonworlds/shaders/ocean/Ocean_TE.glsl"));
		addGeometryShader(ResourceLoader.loadShader("oreonworlds/shaders/ocean/OceanGrid_GS.glsl"));
		addFragmentShader(ResourceLoader.loadShader("oreonworlds/shaders/ocean/OceanGrid_FS.glsl"));
		compileShader();
		
		addUniform("viewProjectionMatrix");
		addUniform("worldMatrix");
		addUniform("eyePosition");
		
		addUniform("displacementScale");
		addUniform("choppiness");
		addUniform("texDetail");
		addUniform("tessFactor");
		addUniform("tessSlope");
		addUniform("tessShift");
		addUniform("Dy");
		addUniform("Dx");
		addUniform("Dz");
		
		for (int i=0; i<6; i++)
		{
			addUniform("frustumPlanes[" + i +"]");
		}
	}
	
	public void updateUniforms(Renderable object)
	{
		setUniform("viewProjectionMatrix", EngineContext.getCamera().getViewProjectionMatrix());
		setUniform("worldMatrix", object.getWorldTransform().getWorldMatrix());
		setUniform("eyePosition", EngineContext.getCamera().getPosition());
		
		for (int i=0; i<6; i++)
		{
			setUniform("frustumPlanes[" + i +"]", EngineContext.getCamera().getFrustumPlanes()[i]);
		}
		
		Water ocean = (Water) object;
		WaterConfiguration configuration = ocean.getWaterConfiguration();
		
		setUniformf("displacementScale", configuration.getDisplacementScale());
		setUniformf("choppiness", configuration.getChoppiness());
		setUniformi("texDetail", configuration.getTexDetail());
		setUniformi("tessFactor", configuration.getTessellationFactor());
		setUniformf("tessSlope", configuration.getTessellationSlope());
		setUniformf("tessShift", configuration.getTessellationShift());
		
		glActiveTexture(GL_TEXTURE0);
		ocean.getFft().getDy().bind();
		setUniformi("Dy", 0);
		glActiveTexture(GL_TEXTURE1);
		ocean.getFft().getDx().bind();
		setUniformi("Dx", 1);
		glActiveTexture(GL_TEXTURE2);
		ocean.getFft().getDz().bind();
		setUniformi("Dz", 2);
	}
}
