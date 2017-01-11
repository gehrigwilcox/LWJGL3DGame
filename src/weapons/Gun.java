package weapons;

public enum Gun {
	
	PISTOL(13), MACHINE_GUN(15), SEMIAUTOMATIC(5), SHOTGUN(10);
	
	private int speed;
	
	private Gun(int speed){
		this.speed = speed;
	}
}
