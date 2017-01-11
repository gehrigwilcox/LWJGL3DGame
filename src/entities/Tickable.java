package entities;

import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import models.TexturedModel;

public class Tickable extends Entity{

	public Tickable(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, index, position, rotX, rotY, rotZ, scale);
		MainGameLoop.tickingEntities.add(this);
	}
	
	public Tickable(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		MainGameLoop.tickingEntities.add(this);
	}
	
	public void update(){
		
	}

}
