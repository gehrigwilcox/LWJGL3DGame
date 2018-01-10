package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import toolbox.Maths;

public class SingleColor extends ShaderProgram{

	private static final String FRAGMENT_FILE = "/shaders/singleColorShader.txt";
	private static final String VERTEX_FILE = "/shaders/vertexShader.txt";
	
	private int location_highlightColor;
	private int location_visibility;
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public SingleColor() {
		super(VERTEX_FILE,FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_highlightColor = super.getUniformLocation("highlightColor");
		location_visibility = super.getUniformLocation("visibility");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadHighlightColor(float r, float g, float b){
		super.loadVector(location_highlightColor, new Vector3f(r,g,b));
	}
	
	public void loadVisibility(float v){
		super.loadFloat(location_visibility, v);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}
