package renderEngine;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import colladaLoader.AnimationData;
import colladaLoader.ColladaLoader;
import colladaLoader.JointTransformData;
import colladaLoader.KeyFrameData;
import models.animation.JointTransform;
import toolbox.Quaternion;

public class AnimationCreator {
	
	public static Animation loadAnimation(String colladaFileName){
		AnimationData animationData = ColladaLoader.loadColladaAnimation("/res/" + colladaFileName);
		KeyFrame[] frames = new KeyFrame[animationData.keyFrames.length];
		int pointer = 0;
		for(KeyFrameData frameData : animationData.keyFrames){
			frames[pointer++] = createKeyFrame(frameData);
		}
		return new Animation(animationData.lengthSeconds, frames);
	}
	
	private static KeyFrame createKeyFrame(KeyFrameData data){
		Map<String, JointTransform> map = new HashMap<String, JointTransform>();
		for(JointTransformData jointData : data.jointTransforms){
			JointTransform jointTransform = createTransform(jointData);
			map.put(jointData.jointNameId, jointTransform);
		}
		return new KeyFrame(data.time, map);
	}
	
	private static JointTransform createTransform(JointTransformData data){
		Matrix4f mat = data.jointLocalTransform;
		Vector3f translation = new Vector3f(mat.m30, mat.m31, mat.m32);
		Quaternion rotation = new Quaternion(mat);
		return new JointTransform(translation, rotation);
	}

}
