package guis;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import engineTester.MainGameLoop;

public class Menu {

	public ArrayList<GuiButton> buttons = new ArrayList<GuiButton>();
	public GuiTexture background;
	
	public Menu(GuiTexture background, GuiButton... button){
		for(GuiButton b : button){
			buttons.add(b);
		}
		this.background = background;
	}
	
	public void render(){
		renderBackground();
		renderButtons();
	}
	
	private void renderButtons(){
		for(GuiButton b : buttons){
			b.render();
		}
	}
	
	private void renderBackground(){
		MainGameLoop.guiTextures.add(background);
	}
	
	public void update(){
		updateButtons();
		updateBackground();
		if(Mouse.isButtonDown(0)){
			onClick();
		}
	}
	
	private void updateButtons(){
		if(Mouse.isInsideWindow()){
			
			int DX = Math.abs(Mouse.getX());
			int DY = Math.abs(Mouse.getY());
			for(GuiButton b : buttons){
				b.update(DX, DY);
			}
		}
	}
	
	private void updateBackground(){
		
	}
	
	private void onClick(){
		for(GuiButton b : buttons){
			if(b.onClick()){
				stopRender();
				return;
			}
		}
	}
	
	public void stopRender(){
		stopRenderButtons();
		stopRenderBackground();
	}
	
	private void stopRenderButtons(){
		for(GuiButton b : buttons){
			b.stopRender();
		}
	}
	
	private void stopRenderBackground(){
		MainGameLoop.guiTextures.remove(background);
		MainGameLoop.state = GameStates.GAME;
	}
}
