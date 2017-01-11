package toolbox;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engineTester.MainGameLoop;
import entities.Camera;
import entities.Interactable;
import hitbox.AABB;

public class EntityMousePicker {
	
	private Camera camera;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	
	private Vector3f currentRay;
	
	private Interactable closest;
	
	private float closestDistance;
	
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600; //600

	private Vector3f currentEntityPos;
	
	public EntityMousePicker(Camera cam, Matrix4f projection){
		camera = cam;
		projectionMatrix = projection;
		viewMatrix = Maths.createViewMatrix(camera);
	}
	
	public void update(){
		
		if(closest != null)
			closest.renderHighlight = false;
		
		
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		
		for(Interactable i : MainGameLoop.interactables){
			
			if(canLook(i)){
				
				float xDiff = camera.getPosition().x - i.getPosition().x;  //do I need to add the rotation of the camera somewhere?
				float yDiff = camera.getPosition().y - i.getPosition().y;
				float zDiff = camera.getPosition().z - i.getPosition().z;
				
				float Dist = ( (float)
						Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2) + Math.pow(zDiff, 2))
						);
			
				Vector3f point = getPointOnRay(currentRay, Dist);

				
				if(AABB.testCollision(point, i)){
					
					if(closest != null){
					
						if(Dist < closestDistance){
							closest = i;
							closestDistance = Dist;
						}
					
					} else {
						closest = i;
					}
				
					closest.renderHighlight = true;
				
					//if(interactKey.isPressed)
					//closest.interact
				
				}
			
			}
		}
	}
	
	private Vector3f calculateMouseRay() {
		float crosshairX = 640;
		float crosshairY = 360;
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(crosshairX, crosshairY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}

	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	//******************************************************************************
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}

	/*private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}*/
	
	private boolean canLook(Interactable i){
		
		
		//if object is within ray range
		if(Math.abs(Math.abs(i.getPosition().x) - Math.abs(camera.getPosition().x)) + Math.abs(Math.abs(i.getPosition().y) - Math.abs(camera.getPosition().y)) + Math.abs(Math.abs(i.getPosition().z) - Math.abs(camera.getPosition().z)) <= RAY_RANGE){
			return true;
		}
		
		return false;
	}
	
	
	
	public Vector3f getRay(int MX, int MY){
		
		MY = Display.getHeight() - MY;
		
		int MZNear = 0;
		int MZFar = 1;
		
		return null;
	}
	
}
