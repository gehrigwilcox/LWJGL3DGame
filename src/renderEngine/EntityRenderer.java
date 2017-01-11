package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import engineTester.MainGameLoop;
import entities.Entity;
import entities.Interactable;
import models.RawModel;
import models.TexturedModel;
import shaders.SingleColor;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class EntityRenderer {

	private StaticShader shader;
	private SingleColor highlight;
	private float highlightWidth = 0.01f;

	public EntityRenderer(StaticShader shader,SingleColor highlight,
			Matrix4f projectionMatrix) {
		this.shader = shader;
		this.highlight = highlight;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		
		highlight.start();
		highlight.loadProjectionMatrix(projectionMatrix);
		highlight.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
				
				
			}
			unbindTexturedModel();
		}
	}
	
	public void renderHighlight(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				
				if(entity instanceof Interactable){
					
					if(((Interactable) entity).renderHighlight){
						
						prepareHighlightInstance(entity);
						
						GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
								GL11.GL_UNSIGNED_INT, 0);
					}
				}
				
				
			}
			unbindTexturedModel();
		}
	}
	
	public void renderHitbox(List<Entity> entities){
		for(Entity e : entities){
			//GL11.glDrawElements(GL11.GL_TRIANGLES, );
		}
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if(texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareAnimatedTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if(texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unbindAnimatedTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
	

	private void prepareHighlightInstance(Entity entity) {
		Matrix4f tM = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale() + highlightWidth);
		highlight.loadTransformationMatrix(tM);
	}
	
	private RawModel hitboxModel(Entity e){
		
		float[] hitboxVerticies[] = {
				{e.getModel().getRawModel().getMinHeight().x,e.getModel().getRawModel().getMinHeight().y,e.getModel().getRawModel().getMinHeight().z}, //-x-y-z
				{e.getModel().getRawModel().getMaxHeight().x,e.getModel().getRawModel().getMinHeight().y,e.getModel().getRawModel().getMinHeight().z}, //x-y-z
				{e.getModel().getRawModel().getMinHeight().x,e.getModel().getRawModel().getMaxHeight().y,e.getModel().getRawModel().getMinHeight().z}, //-xy-z
				{e.getModel().getRawModel().getMinHeight().x,e.getModel().getRawModel().getMinHeight().y,e.getModel().getRawModel().getMaxHeight().z}, //-x-yz
				{e.getModel().getRawModel().getMaxHeight().x,e.getModel().getRawModel().getMaxHeight().y,e.getModel().getRawModel().getMinHeight().z}, //xy-z
				{e.getModel().getRawModel().getMaxHeight().x,e.getModel().getRawModel().getMinHeight().y,e.getModel().getRawModel().getMaxHeight().z}, //x-yz
				{e.getModel().getRawModel().getMinHeight().x,e.getModel().getRawModel().getMaxHeight().y,e.getModel().getRawModel().getMaxHeight().z}, //-xyz
				{e.getModel().getRawModel().getMaxHeight().x,e.getModel().getRawModel().getMaxHeight().y,e.getModel().getRawModel().getMaxHeight().z} //xyz
		};
		
		float[] hitboxModel = {
		                       
		};
		
		int vertexCount = 0;
		
		return new RawModel(MainGameLoop.loader.loadToVAO(hitboxModel), vertexCount);
	}

}
