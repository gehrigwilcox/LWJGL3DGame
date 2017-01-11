package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Light;

public class AnimatedEntityShader extends ShaderProgram{

	private static final int MAX_JOINTS = 50;
	private static final int MAX_LIGHTS = StaticShader.MAX_LIGHTS;
	
	private static final String VERTEX_SHADER = "src/shaders/animatedEntityVertex.txt";
	//private static final String FRAGMENT_SHADER = "src/shaders/fragmentShader.txt";
	private static final String FRAGMENT_SHADER = "src/shaders/animatedEntityFragment.txt";
	
	private int location_jointTransforms[];
	private int location_projectionViewMatrix;
	private int location_transformationMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	
	public AnimatedEntityShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	@Override
	protected void getAllUniformLocations() {
		
		location_projectionViewMatrix = super.getUniformLocation("projectionViewMatrix");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_jointTransforms = new int[MAX_JOINTS];
		
		for(int i = 0; i<MAX_JOINTS; i++){
			location_jointTransforms[i] = super.getUniformLocation("jointTransforms[" + i + "]");
		}
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++){
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
		super.bindAttribute(2, "in_normal");
		super.bindAttribute(3, "in_jointIndices");
		super.bindAttribute(4, "in_weights");
	}
	
	public void loadProjectionViewMatrix(Matrix4f projectionMatrix){
		super.loadMatrix(location_projectionViewMatrix, projectionMatrix);
	}
	
	public void loadTransformationMatrix(Matrix4f transformationMatrix){
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}
	
	public void loadJointTransforms(Matrix4f[] jointTransforms){
		for(int i = 0; i<MAX_JOINTS; i++){
			if(i<jointTransforms.length){
				super.loadMatrix(location_jointTransforms[i], jointTransforms[i]);
			}else{
				super.loadMatrix(location_jointTransforms[i], Matrix4f.setIdentity(new Matrix4f()));
			}
		}
	}
	
	public void loadLights(List<Light> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}else{
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

}
