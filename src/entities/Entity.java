package entities;

import org.lwjgl.util.vector.Vector3f;

import audio.Source;
import hitbox.AABB;
import models.TexturedModel;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	
	private Vector3f maxHeight;
	private Vector3f minHeight;
	private Vector3f middle;
	
	private AABB hitbox;
	
	private int textureIndex = 0;
	
	private Source source;
	
	public Vector3f lastPos;

	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ) {
		this(model,0,position,rotX,rotY,rotZ,1);
	}
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this(model,0,position,rotX,rotY,rotZ,scale);
	}
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale, int hold) {
		//this(model,0,position,rotX,rotY,rotZ,scale);
		this.textureIndex = 0;
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.maxHeight = new Vector3f(10,10,10);
		this.minHeight = new Vector3f(0,0,0);
	}
	
	public Entity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.textureIndex = index;
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.maxHeight = model.getRawModel().getMaxHeight();
		this.minHeight = model.getRawModel().getMinHeight();
		this.middle = model.getRawModel().getMiddle();
		
		this.maxHeight = new Vector3f(maxHeight.x * scale, maxHeight.y * scale, maxHeight.z * scale);
		this.minHeight = new Vector3f(minHeight.x * scale, minHeight.y * scale, minHeight.z * scale);
	}
	
	public float getTextureXOffset(){
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset(){
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}

	public void increasePosition(float dx, float dy, float dz) {
			lastPos.setX(position.x);//although annoying, this allows last position to be updated seperatly from curent pos
			lastPos.setY(position.y);
			lastPos.setZ(position.z);
			this.position.x += dx;
			this.position.y += dy;
			this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Source getSource(){
		return source;
	}

	public void setSourcePos(Vector3f pos){
		this.source.setPosition(pos.x, pos.y, pos.z);
	}
	
	public void playSound(int buffer){
		this.getSource().play(buffer);
	}
	
	public static void playSound(Entity entity, int buffer){
		entity.getSource().play(buffer);
	}
	
	public void renderHitbox(){
		
	}
	
	public void collide(Entity e){
		
		if(e instanceof Player){
			Vector3f lastPos = e.lastPos; //t=0
			Vector3f currentPos = e.getPosition(); //t=1
			Vector3f rate = new Vector3f(0,0,0);//just to keep it from null
			Vector3f newPos = null; //will always be changed from null
			
			float dist = 0.001f;//how far back in line to move
			
			if(lastPos != null){
				rate = new Vector3f(//the slope of line, also finds the direction of the line
						lastPos.x - currentPos.x,
						//lastPos.y - currentPos.y,
						0,
						lastPos.z - currentPos.z
						);
			}
			
			newPos = new Vector3f(//combines the slope of the line with the direction and position to find where to move
				lastPos.x + rate.x * dist,
				lastPos.y + rate.y * dist,
				lastPos.z + rate.z * dist
			);
			
			e.setPosition(newPos);
		}
	}
}
