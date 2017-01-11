package guis;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;

public class GuiButton {

	
	private float x,y,w,h;
	
	private String text;
	
	private GUIText render;
	
	private FontType font;
	
	private boolean hover = false;
	
	public GuiButton(float x, float y, float w, float h, String text, FontType font){
		this.x = x / Display.getWidth();
		this.y = y / Display.getHeight();
		this.w = w;
		this.h = h;
		this.font = font;
		this.text = text;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getW(){
		return w;
	}
	
	public float getH(){
		return h;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public void onHover(){
		hover = true;
		//System.out.println("Hover");
		//rendering code
		
	}
	
	public void render(){
		//render = new GUIText(text, h, font, new Vector2f(x - x * w, y - y * h), w, true);
		render = new GUIText(text, 3f, font, new Vector2f(x, y), 1f, false);
		//for font size, I need to get the length of the text, and get the size of each character based on the 
		//font being used, then I can specify how large of a font size is needed to fit in a specified space
		TextMaster.loadText(render);
		render.setColour(1, 0, 0);
	}
	
	public void update(int DX, int DY){
		
		DY = Math.abs(DY - Display.getHeight());
		
		//System.out.println(DX + ":" + DY);
		
		if(isIntersecting(DX,DY)){
			onHover();
		}else{
			hover = false;
		}
		
	}
	
	public boolean isIntersecting(int DX, int DY){
		
		float width = 0.5f * (this.w + 1);
		float height = 0.5f * (this.h + 1);
		float dx = (this.x + this.w / 2) - DX;
		float dy = (this.y - this.h / 2) + DY;
		
		return (Math.abs(dx) <= width && Math.abs(dy) <= height);
	}
	
	public boolean onClick(){
		return hover;
	}
	
	public void stopRender(){
		render.remove();
	}
}
