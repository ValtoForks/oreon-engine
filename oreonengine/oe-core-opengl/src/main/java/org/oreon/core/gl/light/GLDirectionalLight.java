package org.oreon.core.gl.light;

import org.oreon.core.context.EngineContext;
import org.oreon.core.gl.buffers.GLUBO;
import org.oreon.core.light.DirectionalLight;
import org.oreon.core.util.BufferUtil;
import org.oreon.core.util.Constants;

public class GLDirectionalLight extends DirectionalLight{

	private static GLDirectionalLight instance = null;
	
	private GLUBO ubo_light;
	private GLUBO ubo_matrices;
	
	public static GLDirectionalLight getInstance(){
		if(instance == null) 
	    {
	    	instance = new GLDirectionalLight();
	    }
	      return instance;
	}
	
	protected GLDirectionalLight(){
		
		super(); 
		
		setUbo_light(new GLUBO());
		getUbo_light().setBinding_point_index(Constants.DirectionalLightUniformBlockBinding);
		getUbo_light().bindBufferBase();
		getUbo_light().allocate(getLightBufferSize());
		setFloatBufferLight(BufferUtil.createFloatBuffer(getLightBufferSize()));
		getFloatBufferLight().put(BufferUtil.createFlippedBuffer(getDirection()));
		getFloatBufferLight().put(intensity);
		getFloatBufferLight().put(BufferUtil.createFlippedBuffer(getAmbient()));
		getFloatBufferLight().put(0);
		getFloatBufferLight().put(BufferUtil.createFlippedBuffer(getColor()));
		getFloatBufferLight().put(0);
		getUbo_light().updateData(getFloatBufferLight(), getLightBufferSize());
		
		setUbo_matrices(new GLUBO());
		getUbo_matrices().setBinding_point_index(Constants.LightMatricesUniformBlockBinding);
		getUbo_matrices().bindBufferBase();
		getUbo_matrices().allocate(getMatricesBufferSize());
	
		getUbo_matrices().updateData(getFloatBufferMatrices(), getMatricesBufferSize());
	}
	
	public void update(){
		
		super.update();
		
		if (EngineContext.getCamera().isCameraRotated() || 
				EngineContext.getCamera().isCameraMoved()){
			
			getUbo_matrices().updateData(getFloatBufferMatrices(), getMatricesBufferSize());
		}
	}

	public GLUBO getUbo_light() {
		return ubo_light;
	}

	public void setUbo_light(GLUBO ubo_light) {
		this.ubo_light = ubo_light;
	}

	public GLUBO getUbo_matrices() {
		return ubo_matrices;
	}

	public void setUbo_matrices(GLUBO ubo_matrices) {
		this.ubo_matrices = ubo_matrices;
	}
}
