package org.oreon.core.gl.picking;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glGetTexImage;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.oreon.core.context.EngineContext;
import org.oreon.core.gl.context.GLContext;
import org.oreon.core.math.Quaternion;
import org.oreon.core.math.Vec2f;
import org.oreon.core.math.Vec3f;
import org.oreon.core.util.BufferUtil;

public class TerrainPicking {
	
	private FloatBuffer depthmapBuffer;
	private static TerrainPicking instance;
	private boolean isActive = true;

	public static TerrainPicking getInstance() 
	{
	    if(instance == null) 
	    {
	    	instance = new TerrainPicking();
	    }
	      return instance;
	}
	
	private TerrainPicking(){
		depthmapBuffer = BufferUtil.createFloatBuffer(EngineContext.getWindow().getWidth() * 
													  EngineContext.getWindow().getHeight());	
	}
	
	public void getTerrainPosition(){
		
		if (isActive() && glfwGetMouseButton(EngineContext.getWindow().getId(),1) == GLFW_PRESS){
			Vec3f pos = new Vec3f(0,0,0);
			DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
			glfwGetCursorPos(EngineContext.getWindow().getId(), xPos, yPos);
			Vec2f screenPos = new Vec2f((float) xPos.get(),(float) yPos.get());
			
			GLContext.getRenderContext().getSceneDepthMap().bind();
			glGetTexImage(GL_TEXTURE_2D,0,GL_DEPTH_COMPONENT,GL_FLOAT,depthmapBuffer);
			float depth = depthmapBuffer.get((int) (EngineContext.getWindow().getWidth() * screenPos.getY() + screenPos.getX()));
			
			// window coords
			Vec2f w = new Vec2f(screenPos.getX()/EngineContext.getWindow().getWidth(),
								screenPos.getY()/EngineContext.getWindow().getHeight());
			//ndc coords
			Vec3f ndc = new Vec3f(w.getX() * 2 - 1, w.getY() * 2 - 1, depth);
			float cw = EngineContext.getCamera().getProjectionMatrix().get(3,2) / (ndc.getZ() - EngineContext.getCamera().getProjectionMatrix().get(2,2)); 
			Vec3f clip = ndc.mul(cw);
			Quaternion clipPos = new Quaternion(clip.getX(),clip.getY(),clip.getZ(),cw);
			Quaternion worldPos =  EngineContext.getCamera().getViewProjectionMatrix().invert().mul(clipPos);
			worldPos = worldPos.div(worldPos.getW());
		
			pos.setX(worldPos.getX());
			pos.setY(worldPos.getY());
			pos.setZ(worldPos.getZ());
			
			System.out.println("TerrainPicking: " + pos);
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
