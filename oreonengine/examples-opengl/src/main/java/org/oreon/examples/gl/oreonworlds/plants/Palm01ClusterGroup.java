package org.oreon.examples.gl.oreonworlds.plants;

import java.util.ArrayList;
import java.util.List;

import org.oreon.core.context.BaseContext;
import org.oreon.core.gl.memory.GLMeshVBO;
import org.oreon.core.gl.scenegraph.GLRenderInfo;
import org.oreon.core.gl.texture.GLTexture;
import org.oreon.core.gl.util.GLAssimpModelLoader;
import org.oreon.core.gl.wrapper.parameter.CullFaceDisable;
import org.oreon.core.instanced.InstancedCluster;
import org.oreon.core.instanced.InstancedHandler;
import org.oreon.core.instanced.InstancedObject;
import org.oreon.core.math.Vec3f;
import org.oreon.core.model.Model;
import org.oreon.core.model.Vertex;
import org.oreon.core.scenegraph.NodeComponentType;
import org.oreon.core.scenegraph.Renderable;
import org.oreon.examples.gl.oreonworlds.shaders.plants.PalmBillboardShader;
import org.oreon.examples.gl.oreonworlds.shaders.plants.PalmBillboardShadowShader;
import org.oreon.examples.gl.oreonworlds.shaders.plants.PalmShader;
import org.oreon.examples.gl.oreonworlds.shaders.plants.PalmShadowShader;

public class Palm01ClusterGroup extends InstancedObject{
	
	public Palm01ClusterGroup(){
		
		List<Renderable> objects = new ArrayList<>();
		
		List<Model<GLTexture>> models = GLAssimpModelLoader.loadModel("oreonworlds/assets/plants/Palm_01","Palma 001.obj");
		List<Model<GLTexture>> billboards = GLAssimpModelLoader.loadModel("oreonworlds/assets/plants/Palm_01","billboardmodel.obj");
		
		for (Model<GLTexture> model : models){
			
			GLMeshVBO meshBuffer = new GLMeshVBO();
			model.getMesh().setTangentSpace(false);
			model.getMesh().setInstanced(true);
			meshBuffer.addData(model.getMesh());

			GLRenderInfo renderInfo = new GLRenderInfo(PalmShader.getInstance(), new CullFaceDisable(), meshBuffer);
			GLRenderInfo shadowRenderInfo = new GLRenderInfo(PalmShadowShader.getInstance(), new CullFaceDisable(), meshBuffer);
	
			Renderable object = new Renderable();
			object.addComponent(NodeComponentType.MAIN_RENDERINFO, renderInfo);
			object.addComponent(NodeComponentType.SHADOW_RENDERINFO, shadowRenderInfo);
			object.addComponent(NodeComponentType.MATERIAL0, model.getMaterial());
			objects.add(object);
		}
		
		for (Model<GLTexture> billboard : billboards){	

			GLMeshVBO meshBuffer = new GLMeshVBO();
			billboard.getMesh().setTangentSpace(false);
			billboard.getMesh().setInstanced(true);
			
			for (Vertex vertex : billboard.getMesh().getVertices()){
				vertex.setPosition(vertex.getPosition().mul(135));
				vertex.getPosition().setX(vertex.getPosition().getX()*1.1f);
				vertex.getPosition().setZ(vertex.getPosition().getZ()*1.1f);
			}
			
			meshBuffer.addData(billboard.getMesh());
	
			GLRenderInfo renderInfo = new GLRenderInfo(PalmBillboardShader.getInstance(), new CullFaceDisable(), meshBuffer);
			GLRenderInfo shadowRenderInfo = new GLRenderInfo(PalmBillboardShadowShader.getInstance(), new CullFaceDisable(), meshBuffer);
			
			Renderable object = new Renderable();
			object.addComponent(NodeComponentType.MAIN_RENDERINFO, renderInfo);
			object.addComponent(NodeComponentType.SHADOW_RENDERINFO, shadowRenderInfo);
			object.addComponent(NodeComponentType.MATERIAL0, billboard.getMaterial());
			objects.add(object);
		}
	
		addCluster(new Palm01Cluster(8,new Vec3f(-166,0,-826),objects));
		addCluster(new Palm01Cluster(8,new Vec3f(-290,0,-666),objects));
//		addCluster(new Palm01Cluster(8,new Vec3f(-185,0,-850),getObjectData()));
//		addCluster(new Palm01Cluster(8,new Vec3f(-314,0,-566),getObjectData()));
//		addCluster(new Palm01Cluster(10,new Vec3f(-334,0,-667),getObjectData()));
//		addCluster(new Palm01Cluster(6,new Vec3f(-2049,0,2883),getObjectData()));
//		addCluster(new Palm01Cluster(4,new Vec3f(-355,0,-259),getObjectData()));
//		addCluster(new Palm01Cluster(4,new Vec3f(-160,0,-318),getObjectData()));
		
		setThread(new Thread(this));
		getThread().start();
	}

	public void run() {
		while(isRunning()){
			
			InstancedHandler.getInstance().getLock().lock();
			try {
				InstancedHandler.getInstance().getCondition().await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally{
				InstancedHandler.getInstance().getLock().unlock();
			}
			
			synchronized (getChildren()) {
				
				getChildren().clear();
				
				for (InstancedCluster cluster : getClusters()){
					if (cluster.getCenter().sub(BaseContext.getCamera().getPosition()).length() < 2000){
						cluster.updateUBOs();
						addChild(cluster);
					}
				}
			}
		}
	}
}
