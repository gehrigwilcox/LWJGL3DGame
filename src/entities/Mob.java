package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Mob extends Tickable{

	public Mob(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, index, position, rotX, rotY, rotZ, scale);
	}
	
	public Mob(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	@Override
	public void update(){
		//run through AI and output to the move function
	}
	
	public void move(Vector3f dist){
		
	}

}
