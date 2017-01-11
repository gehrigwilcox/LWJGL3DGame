package renderEngine;

import java.util.Map;

import models.animation.JointTransform;

public class KeyFrame {
	
    private final float timeStamp;
    private final Map<String, JointTransform> jointKeyFrames;
    
    public KeyFrame(float timeStamp, Map<String, JointTransform> jointKeyFrames){
        this.timeStamp = timeStamp;
        this.jointKeyFrames = jointKeyFrames;
    }
    
    protected float getTimeStamp(){
        return timeStamp;
    }
    
    protected Map<String, JointTransform> getJointKeyFrames(){
        return jointKeyFrames;
    }
}
