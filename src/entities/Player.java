package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import hitbox.AABB;
import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;
import weapons.Gun;

public class Player extends Entity {

	private static final float RUN_SPEED = 40;
	private static final float TURN_SPEED = /* for normal first person control*/ 30 /* for car mechanics*/ /*2*/;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 18;
	private static final float ACCELERATION = 10;
	
	public boolean stopMove = false;
	public boolean stopVelocity = false;

	public float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private float currentAcceleration = 0;
	
	public int mouseSensitivity = 5;

	private boolean isInAir = false;
	
	float dx,dy,dz;
	
	private Gun held;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.lastPos = position;
		
	}

	public void move(Terrain terrain) {
		if(!stopMove){
			
			checkInputs();
			
			//the following is for normal first person controls
			
			super.increaseRotation(0, -Mouse.getDX()/mouseSensitivity, 0);
			
			//the following is for car turning mechanics
			
			//super.increaseRotation(0, currentTurnSpeed, 0);
			
			
			dy = upwardsSpeed * DisplayManager.getFrameTimeSeconds();
			upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
			
			stopVelocity = false;
			
			
			increasePosition(dx,dy,dz);
			//increasePosition(dx, 0, dz);
			//increasePosition(0, dy, 0);
			
			//collisionDetection();
			
			float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
			if (super.getPosition().y < terrainHeight) {
				upwardsSpeed = 0;
				isInAir = false;
				super.getPosition().y = terrainHeight;
			}
			stopVelocity = false;
		}
		
		//for audio
		//setSourcePos(getPosition());
	}
	
	public void collisionDetection(){
		for(Entity e : MainGameLoop.entities){
			if(!(e instanceof Player)){
				if(AABB.testCollision(MainGameLoop.player, e)){
					e.collide(MainGameLoop.player);
				}
		}
	}
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			
			//the following is for normal first person controls
			
			this.currentSpeed = -RUN_SPEED;
			
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			//right
			
			//the following is for car turning mechanics
			//currentTurnSpeed = -TURN_SPEED;
			
			//the following is for normal first person controls
			
			currentTurnSpeed = TURN_SPEED;
			
			float distance2 = currentTurnSpeed * DisplayManager.getFrameTimeSeconds();
			dx = (float) (distance2 * Math.sin(Math.toRadians(super.getRotY() + 90)));
			dz = (float) (distance2 * Math.cos(Math.toRadians(super.getRotY() + 90)));
			//super.increasePosition(dx, 0, dz);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			//left
			
			//the following is for car turning mechanics
			currentTurnSpeed = TURN_SPEED;
			
			//the following is for normal first person controls
			
			
			float distance2 = currentTurnSpeed * DisplayManager.getFrameTimeSeconds();
			dx = (float) (distance2 * Math.sin(Math.toRadians(super.getRotY() - 90)));
			dz = (float) (distance2 * Math.cos(Math.toRadians(super.getRotY() - 90)));
			//super.increasePosition(dx, 0, dz);
		} else {
			this.currentTurnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
		
		//handleInteractables();
		
	}
	
	public void handleInteractables(){
		
		
		/*for(Interactable i : MainGameLoop.interactables){
			if(AABB.testCollision(EntityMousePicker.get3dPoint(new Vector2f(DisplayManager.WIDTH / 2, DisplayManager.HEIGHT / 2), Maths.createViewMatrix(MainGameLoop.camera), MainGameLoop.renderer.getProjectionMatrix(), DisplayManager.WIDTH, DisplayManager.HEIGHT), i)){
				
				System.out.println("LOOKING!!!");
			}
		}*/
		
	}

}
