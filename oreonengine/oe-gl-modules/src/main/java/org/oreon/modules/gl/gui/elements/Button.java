package org.oreon.modules.gl.gui.elements;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.oreon.core.context.EngineContext;
import org.oreon.core.gl.parameter.Default;
import org.oreon.core.gl.texture.Texture2D;
import org.oreon.core.math.Matrix4f;
import org.oreon.core.math.Quaternion;
import org.oreon.core.math.Transform;
import org.oreon.core.math.Vec2f;
import org.oreon.core.model.Mesh;
import org.oreon.modules.gl.gui.GUIElement;
import org.oreon.modules.gl.gui.GUIObjectLoader;
import org.oreon.modules.gl.gui.GUIVAO;
import org.oreon.modules.gl.gui.GuiShader;

public abstract class Button extends GUIElement{

	protected Texture2D buttonMap;
	protected Texture2D buttonClickMap;
	private boolean onClick = false;
	private Vec2f[] pos;
	
	public Button(){
		pos = new Vec2f[4];
		setOrthoTransform(new Transform());
	}
	
	public void init()
	{
		setShader(GuiShader.getInstance());
		setVao(new GUIVAO());
		setConfig(new Default());
		Mesh buttonMesh = GUIObjectLoader.load("gui/button.gui");
		getVao().addData(buttonMesh);
		getVao().update(texCoords);
		setOrthographicMatrix(new Matrix4f().Orthographic2D());
		setOrthographicMatrix(getOrthographicMatrix().mul(getOrthoTransform().getWorldMatrix()));
		Quaternion q0 = new Quaternion(0,0,0,0);
		Quaternion q1 = new Quaternion(0,0,0,0);
		Quaternion q2 = new Quaternion(0,0,0,0);
		Quaternion q3 = new Quaternion(0,0,0,0);
		q0 = getOrthoTransform().getWorldMatrix().mul(new Quaternion(buttonMesh.getVertices()[0].getPosition(),1));
		q1 = getOrthoTransform().getWorldMatrix().mul(new Quaternion(buttonMesh.getVertices()[1].getPosition(),1));
		q2 = getOrthoTransform().getWorldMatrix().mul(new Quaternion(buttonMesh.getVertices()[2].getPosition(),1));
		q3 = getOrthoTransform().getWorldMatrix().mul(new Quaternion(buttonMesh.getVertices()[3].getPosition(),1));
		pos[0] = new Vec2f(q0.getX(),q0.getY()+5);
		pos[1] = new Vec2f(q1.getX(),q1.getY()+5);
		pos[2] = new Vec2f(q2.getX()-7,q2.getY()-5);
		pos[3] = new Vec2f(q3.getX()-7,q3.getY()-5);
	}
	
	public void render()
	{
		getConfig().enable();
		getShader().bind();
		getShader().updateUniforms(getOrthographicMatrix());
		glActiveTexture(GL_TEXTURE1);
		if (onClick)
			buttonClickMap.bind();
		else
			buttonMap.bind();
		getShader().updateUniforms(1);
		getVao().draw();
		getConfig().disable();
	}
	
	public void update()
	{
		if(EngineContext.getInput().isButtonPushed(0))
		{
			if(onClick())
			{
				onClick = true;
				onClickActionPerformed();
			}
		}
		
		if(EngineContext.getInput().isButtonReleased(0)){
			onClick = false;
		}
	}
	
	public boolean onClick()
	{
		DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
		
		glfwGetCursorPos(EngineContext.getWindow().getId(), xPos, yPos);
		
		Vec2f mousePos = new Vec2f((float) xPos.get(),(float) yPos.get());
		
		if(pos[0].getX() < mousePos.getX() && 
		   pos[1].getX() < mousePos.getX() && 
		   pos[2].getX() > mousePos.getX() && 
		   pos[3].getX() > mousePos.getX() &&
		   pos[0].getY() < EngineContext.getWindow().getHeight() - mousePos.getY() && 
		   pos[3].getY() < EngineContext.getWindow().getHeight() - mousePos.getY() && 
		   pos[1].getY() > EngineContext.getWindow().getHeight() - mousePos.getY() && 
		   pos[2].getY() > EngineContext.getWindow().getHeight() - mousePos.getY()) {
			
			return true;
		}
		else
			return false;
	}
	
	public void onClickActionPerformed(){}
}
