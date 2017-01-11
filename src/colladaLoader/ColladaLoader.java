package colladaLoader;

import xmlLoader.XmlNode;
import xmlLoader.XmlParser;

public class ColladaLoader {
	
	public static AnimatedModelData loadColladaModel(String colladaFile, int maxWeights){
		XmlNode node = XmlParser.loadXmlFile(colladaFile);
		
		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
		SkinningData skinningData = skinLoader.extractSkinData();
		
		JointsLoader jointsLoader = new JointsLoader(node.getChild("library_visual_scenes"),
				skinningData.jointOrder);
		JointsData jointsData = jointsLoader.extractBoneData();
		
		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"),
				skinningData.verticesSkinData);
		MeshData meshData = g.extractModelData();
		
		return new AnimatedModelData(meshData, jointsData);
	}
	
	public static AnimationData loadColladaAnimation(String colladaFile){
		XmlNode node = XmlParser.loadXmlFile(colladaFile);
		AnimationLoader a = new AnimationLoader(node.getChild("library_animations"));
		AnimationData animData = a.extractAnimation();
		return animData;
	}

}
