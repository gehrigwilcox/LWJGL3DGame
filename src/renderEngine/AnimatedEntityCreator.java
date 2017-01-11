package renderEngine;

import org.lwjgl.util.vector.Vector3f;

import colladaLoader.AnimatedModelData;
import colladaLoader.ColladaLoader;
import colladaLoader.JointData;
import colladaLoader.JointsData;
import entities.AnimatedEntity;
import models.RawModel;
import models.TexturedModel;
import models.animation.Joint;
import textures.ModelTexture;

public class AnimatedEntityCreator {
	
	public static final int MAX_WEIGHTS = 3;
	
	public static AnimatedEntity loadAnimatedEntity(String modelFileName, String textureFileName, Loader loader, Vector3f position, float rotX, float rotY, float rotZ){
		String colladaFile = "res/" + modelFileName;
		String textureFile = "res/" + textureFileName;
		
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(colladaFile, MAX_WEIGHTS);
		
		RawModel model = loader.loadToVAO(entityData.getMeshData());
		TexturedModel textured = new TexturedModel(model, new ModelTexture(loader.loadTexture(textureFileName)));
		
		JointsData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		
		return new AnimatedEntity(textured, position, rotX, rotY, rotZ, headJoint, skeletonData.jointCount);
	}
	
	private static Joint createJoints(JointData data){
		Joint j = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for(JointData child : data.children){
			j.addChild(createJoints(child));
		}
		return j;
	}

}
