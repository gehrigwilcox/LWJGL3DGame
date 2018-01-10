package entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import models.animation.Joint;
import renderEngine.Animation;
import renderEngine.Animator;

public class AnimatedEntity extends Entity{
	
	private final Joint rootJoint;
	private final int jointCount;
    
    private final Matrix4f[] jointTransforms;
	
	private final Animator animator;

	public AnimatedEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			Joint rootJoint, int jointCount) {
		super(model, position, rotX, rotY, rotZ, 1, 1);
		this.rootJoint = rootJoint;
		this.jointCount = jointCount;
		this.animator = new Animator(this);
        this.jointTransforms = new Matrix4f[jointCount];
		rootJoint.calcInverseBindTransform(
				new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1,0,0)));
	}
	
	public Joint getRootJoint(){
		return rootJoint;
	}
	
	public void doAnimation(Animation animation){
		animator.doAnimation(animation);
	}
	
	public void update(){
		animator.update();
	}
	
	/*public Matrix4f[] getJointTransforms(){
		Matrix4f[] jointMatricies = new Matrix4f[jointCount];
		addJointsToArray(rootJoint, jointMatricies);
		return jointMatricies;
	}*/
    
    public Matrix4f[] getJointTransforms(){
        addJointsToArray(rootJoint, jointTransforms);
        return jointTransforms;
        
    }
	
	private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatricies){
		jointMatricies[headJoint.index] = headJoint.getAnimatedTransform();
		for(Joint childJoint : headJoint.children){
			addJointsToArray(childJoint, jointMatricies);
		}
	}

}
