package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import audio.AudioMaster;
import audio.Sound;
import entities.AnimatedEntity;
import entities.Camera;
import entities.Entity;
import entities.Interactable;
import entities.Light;
import entities.Player;
import entities.RotatingEntities;
import entities.Tickable;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GameStates;
import guis.GuiButton;
import guis.GuiRenderer;
import guis.GuiTexture;
import guis.Menu;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.OBJFileLoader;
import renderEngine.AnimatedEntityCreator;
import renderEngine.Animation;
import renderEngine.AnimationCreator;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.EntityMousePicker;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {
	
	//Misc Vars, so I can access them somewhere else
	
	public static Loader loader;
	public static FontType font;
	public static GUIText text;
	
	public static GameStates state = GameStates.LOADING;
	
	public static int crosshairTexture;
	public static GuiTexture crosshair;
	
	public static TerrainTexture backgroundTexture;
	public static TerrainTexture rTexture;
	public static TerrainTexture gTexture;
	public static TerrainTexture bTexture;
	public static TerrainTexture blendMap;
	
	public static TerrainTexturePack texturePack;
	
	public static Terrain terrain;
	
	public static RawModel playerRawModel;
	
	public static TexturedModel rocks;
	public static TexturedModel fern;
	public static TexturedModel bobble;
	public static TexturedModel lamp;
	public static TexturedModel barrelModel;
	public static TexturedModel crateModel;
	public static TexturedModel boulderModel;
	public static TexturedModel playerModel;
	
	public static ModelTexture fernTextureAtlas;
	
	public static Entity barrel;
	public static Entity boulder;
	public static Entity crate;
	public static Entity mouse;
	
	public static Light sun;
	
	public static Player player;
	
	public static Camera camera;
	
	public static MousePicker picker;
	
	public static EntityMousePicker EMP;
	
	public static GuiRenderer guiRenderer;
	
	public static WaterFrameBuffers buffers;
	public static WaterShader waterShader;
	
	public static WaterRenderer waterRenderer;
	
	public static WaterTile water;
	
	public static List<Terrain> terrains = new ArrayList<Terrain>();
	public static List<WaterTile> waters = new ArrayList<WaterTile>();
	public static List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
	
	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Entity> normalMapEntities = new ArrayList<Entity>();
	public static List<Tickable> tickingEntities = new ArrayList<Tickable>();
	public static List<Interactable> interactables = new ArrayList<Interactable>();
	
	public static List<Light> lights = new ArrayList<Light>();
	public static List<Sound> sounds = new ArrayList<Sound>();
	
	public static MasterRenderer renderer;
	
	public static int seed = 18358423;
	
	public static List<AnimatedEntity> animatedEntities = new ArrayList<AnimatedEntity>();

	public static AnimatedEntity test;
	
	public static Animation testAnimation;
	
	public static Runtime runtime = Runtime.getRuntime();
	
	
	public static long getTime(){
		return System.currentTimeMillis();
	}
	
	public static void preInit(){
		//for functions that are needed later on, can be modified later
		
		createDisplay();
		loader = new Loader();
		setupRenderer();
		setupText();
		setupTerrainTexture();
		setupEntityTexture();
		setupGui();
		setupGuis();
		setupWaterRenderer();
	}
	
	public static void init(){
		//for functions that *might* be needed later on, can modify and be modified
		addTerrain();
		setupEntities();
		setupLight();
		setupPlayer();
		addWater();
	}
	
	public static void postInit(){
		/*for functions that will never be needed later on, can modify but will never be modified
		 * nothing should be put here unless absolutely necessary
		 */
		
		state = GameStates.GAME;
	}
	
	public static void createDisplay(){
		DisplayManager.createDisplay();
	}
	
	public static void setupText(){
		TextMaster.init(loader);
		
		font = new FontType(loader.loadTexture("harrington"), "harrington", "harrington");
		text = new GUIText("This is some text!", 3f, font, new Vector2f(0f, 0f), 1f, true);
		text.setColour(1, 0, 0);
		text.remove();
	}
	
	public static void setupGuis(){
		crosshairTexture = loader.loadTexture("crosshair");
		crosshair = new GuiTexture(crosshairTexture, new Vector2f(0,0), new Vector2f(0.5f,0.6f));
		//guiTextures.add(crosshair);
	}
	
	public static void setupTerrainTexture(){
		backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		rTexture = new TerrainTexture(loader.loadTexture("mud"));
		gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		bTexture = new TerrainTexture(loader.loadTexture("path"));

		texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
	}
	
	public static void setupEntityTexture(){
		rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader),
				new ModelTexture(loader.loadTexture("rocks")));

		fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);

		fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader),
				fernTextureAtlas);

		bobble = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader),
				new ModelTexture(loader.loadTexture("pine")));
		bobble.getTexture().setHasTransparency(true);

		fern.getTexture().setHasTransparency(true);
		
		lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
				new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);
		
		setupNormalMapModels();
	}
	
	public static void setupNormalMapModels(){
		barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		
		crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
				new ModelTexture(loader.loadTexture("crate")));
		crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
		crateModel.getTexture().setShineDamper(10);
		crateModel.getTexture().setReflectivity(0.5f);
		
		boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
				new ModelTexture(loader.loadTexture("boulder")));
		boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
		boulderModel.getTexture().setShineDamper(10);
		boulderModel.getTexture().setReflectivity(0.5f);
	}
	
	public static void addTerrain(){
		terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain);
	}
	
	public static void setupEntities(){
		test = AnimatedEntityCreator.loadAnimatedEntity("cubes.dae", "cubes", loader, new Vector3f(0,0,0), 0, 0, 0);
		
		animatedEntities.add(test);
		//entities.add(test);
		
		testAnimation = AnimationCreator.loadAnimation("cubes.dae");
		
		test.doAnimation(testAnimation);
		
		barrel = new RotatingEntities(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
		boulder = new RotatingEntities(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f);
		crate = new RotatingEntities(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
		normalMapEntities.add(barrel);
		normalMapEntities.add(boulder);
		normalMapEntities.add(crate);
		setupRandomEntities();
		entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));
		entities.add(new Interactable(bobble, new Vector3f(80,5,-40), 0, 0, 0, 1f));
	}
	
	public static void setupRandomEntities(){
		Random random = new Random(seed);
		for (int i = 0; i < 60; i++) {
			if (i % 3 == 0) {
				float x = random.nextFloat() * 150;
				float z = random.nextFloat() * -150;
				if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
				} else {
					float y = terrain.getHeightOfTerrain(x, z);

					entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0,
							random.nextFloat() * 360, 0, 0.9f));
				}
			}
			if (i % 2 == 0) {

				float x = random.nextFloat() * 150;
				float z = random.nextFloat() * -150;
				if ((x > 50 && x < 100) || (z < -50 && z > -100)) {

				} else {
					float y = terrain.getHeightOfTerrain(x, z);
					entities.add(new Entity(bobble, 1, new Vector3f(x, y, z), 0,
							random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
				}
			}
		}
	}
	
	public static void setupLight(){
		sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);
	}
	
	public static void setupRenderer(){
		renderer = new MasterRenderer(loader);
	}
	
	public static void setupPlayer(){
		playerRawModel = OBJLoader.loadObjModel("person", loader);
		playerModel = new TexturedModel(playerRawModel, new ModelTexture(
				loader.loadTexture("playerTexture")));

		//player = new Player(playerModel, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
		player = new Player(playerModel, new Vector3f(0, 0, 0), 0, 100, 0, 0.6f);
		entities.add(player);
		setupCamera();
	}
	
	public static void setupCamera(){
		camera = new Camera(player);
		setupMousePicker();
		setupEntityMousePicker();
	}
	
	public static void setupMousePicker(){
		picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
	}
	
	public static void setupEntityMousePicker(){
		EMP = new EntityMousePicker(camera, renderer.getProjectionMatrix());
	}
	
	public static void setupGui(){
		guiRenderer = new GuiRenderer(loader);
	}
	
	public static void setupWaterRenderer(){
		buffers = new WaterFrameBuffers();
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
	}
	
	public static void addWater(){
		water = new WaterTile(75, -75, 0);
		waters.add(water);
	}
	
	public static void addSounds(){
		sounds.add(new Sound(AudioMaster.loadSound("audio/bounce.wav"), "bounce", "audio/bounce.wav"));
	}
	
	public static void mainGameLoop(){
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			updateEntityMovement();
			
			updateRender();
			
			//runtime.gc();
			
			/*
			 * memory usage
			 */
			//System.out.println(runtime.totalMemory() - runtime.freeMemory());
		}
	}
	
	public static void updateEntityMovement(){
		player.move(terrain);
		camera.move();
		picker.update();
		test.update();
		EMP.update();
		for(Tickable t : tickingEntities){
			t.update();
		}
		
		//Collision Detection in player.move();
	}
	
	public static void renderGUI(){
		guiRenderer.render(guiTextures);
		TextMaster.render();
	}
	
	public static void updateRender(){
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		
		//render hitbox
		
		
		//render reflection texture
		buffers.bindReflectionFrameBuffer();
		float distance = 2 * (camera.getPosition().y - water.getHeight());
		camera.getPosition().y -= distance;
		camera.invertPitch();
		//renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+1));
		renderer.renderScene(entities, normalMapEntities, animatedEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+1));
		camera.getPosition().y += distance;
		camera.invertPitch();
		
		//render refraction texture
		buffers.bindRefractionFrameBuffer();
		//renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
		renderer.renderScene(entities, normalMapEntities, animatedEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
		
		//render to screen
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		buffers.unbindCurrentFrameBuffer();	
		//renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
		renderer.renderScene(entities, normalMapEntities, animatedEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
		waterRenderer.render(waters, camera, sun);
		renderGUI();
		
		DisplayManager.updateDisplay();
		System.out.println(DisplayManager.getFrameTimeSeconds());
	}
	
	public static void MainMenu(){
		
		Menu main = new Menu(new GuiTexture(loader.loadTexture("transparent"), new Vector2f(0,0), new Vector2f(1,1)), new GuiButton(0,0,100,100,"hi", font));
		
		main.render();
		
		while(state == GameStates.MAIN_MENU && !Display.isCloseRequested()){
			main.update();
			renderGUI();
			DisplayManager.updateDisplay();
		}
		
		main.stopRender();
		
	}
	
	public static void Customize(){
		
		while(state == GameStates.CUSTOMIZE && !Display.isCloseRequested()){
			
		}
		
	}
	
	public static void ServerList(){
		
	}
	
	public static void Game(){
		Mouse.setGrabbed(true);
	}
	
	public static void cleanUp(){
		AudioMaster.cleanUp();
		TextMaster.cleanUp();
		buffers.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		
		switch(state){
			case LOADING:	
				
				//render loading screen
				
				preInit();
		
				init();
		
				postInit();
				
			case MAIN_MENU:
				
				//render main menu
				
				MainMenu();
				
			case CUSTOMIZE:
				
				//render customize screen
				
				Customize();
				
			case GAME:
				
				Mouse.setCursorPosition(DisplayManager.WIDTH / 2, DisplayManager.HEIGHT / 2);
				
				Mouse.setGrabbed(true);
				
				TextMaster.loadText(text);
				guiTextures.add(crosshair);
				
				mainGameLoop();
				
			default:
				
		}

		cleanUp();
	}


}
