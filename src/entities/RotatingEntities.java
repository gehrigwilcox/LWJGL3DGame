package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class RotatingEntities extends Tickable{

	public RotatingEntities(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, index, position, rotX, rotY, rotZ, scale);
	}
	
	public RotatingEntities(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	@Override
	public void update(){
		this.increaseRotation(0, 1, 0);
	}

}
