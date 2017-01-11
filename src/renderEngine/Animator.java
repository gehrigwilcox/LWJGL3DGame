package renderEngine;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.AnimatedEntity;
import models.animation.Joint;
import models.animation.JointTransform;
import renderEngine.KeyFrame;

public class Animator {
	
	private final AnimatedEntity entity;
	
	private float animationTime = 0;
    private Animation currentAnimation;
    
    public Animator(AnimatedEntity entity){
        this.entity = entity;
    }
    
    public void doAnimation(Animation animation){
        this.animationTime = 0;
        this.currentAnimation = animation;
    }
    
    public void update(){
        if(currentAnimation == null){
            return;
        }
        increaseAnimationTime();
        Map<String, Matrix4f> currentPose = getCurrentAnimationPose();
        applyPoseToJoints(currentPose, entity.getRootJoint(),
                          new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0)));
    }
    
    private Map<String, Matrix4f> getCurrentAnimationPose(){
        KeyFrame[] frames = getPreviousAndNextFrames();
        float progression = calculateProgression(frames[0], frames[1]);
        return calculateCurrentPose(frames[0], frames[1], progression);
    }
    
    private void applyPoseToJoints(Map<String, Matrix4f> currentPose, Joint joint,
                                   Matrix4f parentTransform){
        Matrix4f currentLocalTransform = currentPose.get(joint.name);
        Matrix4f currentTransform = Matrix4f.mul(parentTransform, currentLocalTransform, null);
        for(Joint childJoint : joint.children){
            applyPoseToJoints(currentPose, childJoint, currentTransform);
        }
        Matrix4f.mul(currentTransform, joint.getInverseBindTransform(), currentTransform);
        joint.setAnimationTransform(currentTransform);
    }
    
    private KeyFrame[] getPreviousAndNextFrames(){
        KeyFrame previousFrame = null;
        KeyFrame nextFrame = null;
        
        for(KeyFrame frame : currentAnimation.getKeyFrames()){
            if(frame.getTimeStamp() > animationTime){
                nextFrame = frame;
                break;
            }
            previousFrame = frame;
        }
        if(previousFrame == null){
            previousFrame = nextFrame;
        }else if(nextFrame == null){
            nextFrame = previousFrame;
        }
        return new KeyFrame[] {previousFrame, nextFrame};
    }
    
    private float calculateProgression(KeyFrame previousFrame, KeyFrame nextFrame){
        float timeDifference = nextFrame.getTimeStamp() - previousFrame.getTimeStamp();
        return (animationTime - previousFrame.getTimeStamp()) / timeDifference;
    }
    
    private Map<String, Matrix4f> calculateCurrentPose(KeyFrame previousFrame, KeyFrame nextFrame,
                                                       float progression){
        Map<String, Matrix4f> currentPose = new HashMap<String, Matrix4f>();
        for(String jointName : previousFrame.getJointKeyFrames().keySet()){
            JointTransform previousPose = previousFrame.getJointKeyFrames().get(jointName);
            JointTransform nextPose = nextFrame.getJointKeyFrames().get(jointName);
            JointTransform jointPose = JointTransform.interpolate(previousPose, nextPose,
                                                                  progression);
            currentPose.put(jointName, jointPose.getLocalTransform());
        }
        return currentPose;
    }
    
    private void increaseAnimationTime(){
        animationTime += DisplayManager.getFrameTimeSeconds();
        if(animationTime > currentAnimation.getLength()){
            this.animationTime %= currentAnimation.getLength();
        }
    }

}
