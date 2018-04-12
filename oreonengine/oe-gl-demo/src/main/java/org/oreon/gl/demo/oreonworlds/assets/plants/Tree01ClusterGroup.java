package org.oreon.gl.demo.oreonworlds.assets.plants;

import java.util.ArrayList;
import java.util.List;

import org.oreon.core.context.EngineContext;
import org.oreon.core.gl.buffers.GLMeshVBO;
import org.oreon.core.gl.parameter.CullFaceDisable;
import org.oreon.core.gl.scenegraph.GLRenderInfo;
import org.oreon.core.gl.util.modelLoader.obj.OBJLoader;
import org.oreon.core.instanced.InstancedCluster;
import org.oreon.core.instanced.InstancedHandler;
import org.oreon.core.instanced.InstancedObject;
import org.oreon.core.math.Vec3f;
import org.oreon.core.model.Model;
import org.oreon.core.model.Vertex;
import org.oreon.core.scenegraph.ComponentType;
import org.oreon.core.scenegraph.Renderable;
import org.oreon.core.util.Util;
import org.oreon.gl.demo.oreonworlds.shaders.assets.plants.TreeBillboardShader;
import org.oreon.gl.demo.oreonworlds.shaders.assets.plants.TreeBillboardShadowShader;
import org.oreon.gl.demo.oreonworlds.shaders.assets.plants.TreeLeavesShader;
import org.oreon.gl.demo.oreonworlds.shaders.assets.plants.TreeShadowShader;
import org.oreon.gl.demo.oreonworlds.shaders.assets.plants.TreeTrunkShader;

public class Tree01ClusterGroup extends InstancedObject{
	
	public Tree01ClusterGroup(){
		
		Model[] models = new OBJLoader().load("oreonworlds/assets/plants/Tree_01","tree01.obj","tree01.mtl");
		Model[] billboards = new OBJLoader().load("oreonworlds/assets/plants/Tree_01","billboardmodel.obj","billboardmodel.mtl");
		
		List<Renderable> objects = new ArrayList<>();
		
		for (Model model : models){
			
			GLMeshVBO meshBuffer = new GLMeshVBO();
			
			if (model.equals(models[0])){
				model.getMesh().setTangentSpace(true);
				Util.generateTangentsBitangents(model.getMesh());
			}
			else
				model.getMesh().setTangentSpace(false);
			model.getMesh().setInstanced(true);
			
			for (Vertex vertex : model.getMesh().getVertices()){
				vertex.getPosition().setX(vertex.getPosition().getX()*1.2f);
				vertex.getPosition().setZ(vertex.getPosition().getZ()*1.2f);
			}
			
			meshBuffer.addData(model.getMesh());

			GLRenderInfo renderInfo;
			GLRenderInfo shadowRenderInfo;
			
			if (model.equals(models[0])){
				renderInfo = new GLRenderInfo(TreeTrunkShader.getInstance(), new CullFaceDisable(), meshBuffer);
				shadowRenderInfo = new GLRenderInfo(TreeShadowShader.getInstance(), new CullFaceDisable(), meshBuffer);
			}
			else{
				renderInfo = new GLRenderInfo(TreeLeavesShader.getInstance(), new CullFaceDisable(), meshBuffer);
				shadowRenderInfo = new GLRenderInfo(TreeShadowShader.getInstance(), new CullFaceDisable(), meshBuffer);
			}
			
			Renderable object = new Renderable();
			object.addComponent(ComponentType.MAIN_RENDERINFO, renderInfo);
			object.addComponent(ComponentType.SHADOW_RENDERINFO, shadowRenderInfo);
			object.addComponent(ComponentType.MATERIAL0, model.getMaterial());
			objects.add(object);
		}
		
		for (Model billboard : billboards){
			
			GLMeshVBO meshBuffer = new GLMeshVBO();
			
			billboard.getMesh().setTangentSpace(false);
			billboard.getMesh().setInstanced(true);
			
			for (Vertex vertex : billboard.getMesh().getVertices()){
				vertex.setPosition(vertex.getPosition().mul(7.4f));
				vertex.getPosition().setX(vertex.getPosition().getX()*1f);
				vertex.getPosition().setZ(vertex.getPosition().getZ()*1f);
			}
			
			meshBuffer.addData(billboard.getMesh());
	
			GLRenderInfo renderInfo = new GLRenderInfo(TreeBillboardShader.getInstance(), new CullFaceDisable(), meshBuffer);
			GLRenderInfo shadowRenderInfo = new GLRenderInfo(TreeBillboardShadowShader.getInstance(), new CullFaceDisable(), meshBuffer);
			
			Renderable object = new Renderable();
			object.addComponent(ComponentType.MAIN_RENDERINFO, renderInfo);
			object.addComponent(ComponentType.SHADOW_RENDERINFO, shadowRenderInfo);
			object.addComponent(ComponentType.MATERIAL0, billboard.getMaterial());
			objects.add(object);
		}
	
		addCluster(new Tree01Cluster(10,new Vec3f(-1002,0,1550),objects));
		addCluster(new Tree01Cluster(10,new Vec3f(-1085,0,1536),objects));
		addCluster(new Tree01Cluster(10,new Vec3f(-1121,0,1473),objects));
		addCluster(new Tree01Cluster(10,new Vec3f(-1114,0,1423),objects));
		addCluster(new Tree01Cluster(10,new Vec3f(-1074,0,1378),objects));
		addCluster(new Tree01Cluster(10,new Vec3f(-1138,0,1345),objects));
		addCluster(new Tree01Cluster(10,new Vec3f(-1039,0,1129),objects));
		addCluster(new Tree01Cluster(10,new Vec3f(-1011,0,1042),objects));
		addCluster(new Tree01Cluster(6,new Vec3f(-1181,0,1346),objects));
		addCluster(new Tree01Cluster(6,new Vec3f(-1210,0,1348),objects));
		addCluster(new Tree01Cluster(6,new Vec3f(-1211,0,1392),objects));
		
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
					if (cluster.getCenter().sub(EngineContext.getCamera().getPosition()).length() < 2000){
						cluster.updateUBOs();
						addChild(cluster);
					}
				}
				
			}
		}
	}
}
