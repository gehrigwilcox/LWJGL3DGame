package entities;

import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import models.TexturedModel;

public class Interactable extends Entity{

	public boolean renderHighlight = false; 
	
	public Interactable(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, 0, position, rotX, rotY, rotZ, scale);
		MainGameLoop.interactables.add(this);
	}
	
	public void onHover(){
		//highlight
		renderHighlight = true;
		
	}
	
	public void onUnHover(){
		renderHighlight = false;
	}
	
	public void Interact(){
		
	}

}
