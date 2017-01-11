package hitbox;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;

public class AABB {

	private Entity myEntity;
	
	
	//entitys position is where the center of the model goes
	
	public AABB(Entity myEntity){
		this.myEntity = myEntity;
	}
	
	public AABB(){
		this(null);
	}
	
	public static boolean testCollision(Entity entity1, Entity entity2){
		//this needs to be filtered where only entities that can move run this check, not all entities
		Vector3f e1Max = entity1.getModel().getRawModel().getMaxHeight();//need to multiply all these by the entities scale
		Vector3f e1Min = entity1.getModel().getRawModel().getMinHeight();
		
		Vector3f e2Max = entity2.getModel().getRawModel().getMaxHeight();
		Vector3f e2Min = entity2.getModel().getRawModel().getMinHeight();
		
		
		//reacts only with somethings
		float d1x = (entity2.getPosition().x + e2Min.x) - (e1Max.x + entity1.getPosition().x);
		float d1y = (entity2.getPosition().y + e2Min.y) - (e1Max.y + entity1.getPosition().y);
		float d1z = (entity2.getPosition().z + e2Min.z) - (e1Max.z + entity1.getPosition().z);
		float d2x = (entity1.getPosition().x + e1Min.x) - (e2Max.x + entity2.getPosition().x);
		float d2y = (entity1.getPosition().y + e1Min.y) - (e2Max.y + entity2.getPosition().y);
		float d2z = (entity1.getPosition().z + e1Min.z) - (e2Max.z + entity2.getPosition().z );
		
		if(d1x > 0.0f || d1y > 0.0f || d1z > 0.0f)
			return false;
		if(d2x > 0.0f || d2y > 0.0f || d2z > 0.0f)
			return false;
		return true;
	}
	
	public static boolean testCollision(Vector3f pos, Entity e){
		Vector3f eMax = e.getModel().getRawModel().getMaxHeight();
		Vector3f eMin = e.getModel().getRawModel().getMinHeight();
		
		Vector3f eMaxPos = new Vector3f(eMax.x + e.getPosition().x,eMax.y + e.getPosition().y,eMax.z + e.getPosition().z);
		Vector3f eMinPos = new Vector3f(eMin.x + e.getPosition().x,eMin.y + e.getPosition().y,eMin.z + e.getPosition().z);
		
		return (pos.x >= eMinPos.x && pos.x <= eMaxPos.x) &&
				(pos.y >= eMinPos.y && pos.y <= eMaxPos.y) &&
				(pos.z >= eMinPos.z && pos.z <= eMaxPos.z);
	}
	
	public static boolean testCollision(Entity e, AABB box){
		return false;
	}
	
	public static boolean testCollision(AABB box1, AABB box2){
		return false;
	}
}
