package models;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	
	private Vector3f maxHeight;
	private Vector3f minHeight;
	private Vector3f middle;
	
	public RawModel(int vaoID, int vertexCount, Vector3f maxHeight, Vector3f minHeight, Vector3f middle){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.maxHeight =  maxHeight;
		this.minHeight = minHeight;
		this.middle = middle;
	}
	
	public RawModel(int vaoID, int vertexCount){
		this(vaoID, vertexCount, null, null, null);
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public Vector3f getMaxHeight(){
		return maxHeight;
	}
	
	public Vector3f getMinHeight(){
		return minHeight;
	}
	
	public Vector3f getMiddle(){
		return middle;
	}
	
	public void bind(int... attributes){
		bind();
		for(int i : attributes){
			GL20.glEnableVertexAttribArray(i);
		}
	}
	
	public void unbind(int... attributes){
		for(int i : attributes){
			GL20.glDisableVertexAttribArray(i);
		}
		unbind();
	}
	
	private void bind(){
		GL30.glBindVertexArray(vaoID);
	}
	
	private void unbind(){
		GL30.glBindVertexArray(0);
	}

}
