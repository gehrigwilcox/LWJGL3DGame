package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import engineTester.MainGameLoop;
import entities.AnimatedEntity;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import normalMappingRenderer.NormalMappingRenderer;
import shaders.AnimatedEntityShader;
import shaders.SingleColor;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import toolbox.Maths;

public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;

	public static final float RED = 0.1f;
	public static final float GREEN = 0.4f;
	public static final float BLUE = 0.2f;

	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private SingleColor highlightShader = new SingleColor();
	
	private AnimatedEntityRenderer animatedEntityRenderer;
	private AnimatedEntityShader animatedShader = new AnimatedEntityShader();
	
	private NormalMappingRenderer normalMapRenderer;

	private SkyboxRenderer skyboxRenderer;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<AnimatedEntity>> animatedEntities = new HashMap<TexturedModel, List<AnimatedEntity>>();
	private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	public MasterRenderer(Loader loader) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, highlightShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
		animatedEntityRenderer = new AnimatedEntityRenderer(projectionMatrix);
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}
    
    public Matrix4f getProjectionViewMatrix(Camera camera){
        return Matrix4f.mul(projectionMatrix, Maths.createViewMatrix(camera), null);
    }

	public void renderScene(List<Entity> entities, List<Entity> normalEntities, List<Terrain> terrains, List<Light> lights,
			Camera camera, Vector4f clipPlane) {
		for (Terrain terrain : terrains) {
			processTerrain(terrain);
		}
		for (Entity entity : entities) {
			processEntity(entity);
		}
		for(Entity entity : normalEntities){
			processNormalMapEntity(entity);
		}
		render(lights, camera, clipPlane);
	}
	
	public void renderScene(List<Entity> entities, List<Entity> normalEntities, List<AnimatedEntity> animatedEntities, List<Terrain> terrains, List<Light> lights,
			Camera camera, Vector4f clipPlane) {
		for (Terrain terrain : terrains) {
			processTerrain(terrain);
		}
		for (Entity entity : entities) {
			processEntity(entity);
		}
		for(Entity entity : normalEntities){
			processNormalMapEntity(entity);
		}
		for(AnimatedEntity entity : animatedEntities){
			processAnimatedEntity(entity);
		}
		render(lights, camera, clipPlane);
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		
		prepare();
		
		animatedShader.start();
		animatedShader.loadLights(lights);
		animatedShader.loadProjectionViewMatrix(this.getProjectionViewMatrix(MainGameLoop.camera));
		animatedEntityRenderer.render(animatedEntities);
		animatedShader.stop();
		
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		/*
		Highlight code goes here
		*/
		
		highlightShader.start();
		
		shader.loadViewMatrix(camera);
		renderer.renderHighlight(entities);
		
		highlightShader.stop();
		
		
		normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera);
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		terrains.clear();
		entities.clear();
		normalMapEntities.clear();
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processAnimatedEntity(AnimatedEntity entity){
		TexturedModel entityModel = entity.getModel();
		List<AnimatedEntity> batch = animatedEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<AnimatedEntity> newBatch = new ArrayList<AnimatedEntity>();
			newBatch.add(entity);
			animatedEntities.put(entityModel, newBatch);
		}
	}
	
	public void processNormalMapEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = normalMapEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalMapEntities.put(entityModel, newBatch);
		}
	}
	
	public void processHitbox(){
		
	}

	public void cleanUp() {
		shader.cleanUp();
		highlightShader.cleanUp();
		terrainShader.cleanUp();
		normalMapRenderer.cleanUp();
		animatedShader.cleanUp();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

}
