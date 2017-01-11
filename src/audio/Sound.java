package audio;

public class Sound {

	final int buffer;
	final String name;
	final String file;
	
	public Sound(int buffer, String name, String file){
		this.buffer = buffer;
		this.name = name;
		this.file = file;
	}
	
	public int getBuffer(){
		return this.buffer;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getFile(){
		return this.file;
	}
	
}
