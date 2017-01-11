package variables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoopVariables {
	
	/*This Class will be used to hold variables needed in the main game loop
	 * they are initially created null, but will soon contain things
	 * if they are created with a value, then they will most likely not be changed in the future
	 * or can have values before display is envoked
	 */

	//********Utils********\\
	
	public static Loader loader;
	public static FontType font;
	public static GUIText text;
	public static MasterRenderer renderer;
	public static GuiRenderer guiRenderer;
	
	//**********************\\
	
	//********Terrain Textures********\\
	
	public static TerrainTexture backgroundTexture;
	public static TerrainTexture rTexture;
	public static TerrainTexture gTexture;
	public static TerrainTexture bTexture;
	public static TerrainTexture blendMap;
	
	public static TerrainTexturePack texturePack;
	
	//**********************************\\
	
	//*********Textured Models***********\\
	
	public static RawModel playerModel;
	
	public static TexturedModel rocks;
	public static TexturedModel fern;
	public static TexturedModel bobble;
	public static TexturedModel lamp;
	public static TexturedModel barrelModel;
	public static TexturedModel crateModel;
	public static TexturedModel boulderModel;
	public static TexturedModel playerTextured;
	
	public static ModelTexture fernTextureAtlas;
	
	//***********************************\\
	
	//**************Terrain**************\\
	
	public static Terrain terrain;
	
	public static List<Terrain> terrains = new ArrayList<Terrain>();
	
	//***********************************\\
	
	//**************Entities**************\\
	
	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Entity> normalMapEntities = new ArrayList<Entity>();
	
	public static Entity barrel;
	public static Entity crate;
	public static Entity boulder;
	
	//************************************\\
	
	//**************Random Terrain Gen************\\
	
	public static Random seed = new Random(5666778);
	
	//********************************************\\
	
	//***************Lights*********************\\
	
	public static List<Light> lights = new ArrayList<Light>();
	
	public static Light sun;
	
	//******************************************\\
	
	//***************Player*********************\\
	
	public static Player player;
	
	//******************************************\\
	
	//***************Camera*********************\\
	
	public static Camera camera;
	
	//******************************************\\
	
	//***************GUI Textures***************\\
	
	public static List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
	
	//******************************************\\
	
	//***************Mouse Picker***************\\
	
	public static MousePicker picker;
	
	//******************************************\\
	
	//***************Water Renderer*************\\
	
	public static WaterFrameBuffers buffers;
	public static WaterShader waterShader;
	public static WaterRenderer waterRenderer;
	public static List<WaterTile> waters = new ArrayList<WaterTile>();
	public static WaterTile water;
	
	//******************************************\\
}
