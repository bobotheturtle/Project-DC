package Tileset.PlayerSubclass;

import com.badlogic.gdx.graphics.Texture;

import State.Coordinates;
import Tileset.GameObject;
import Tileset.Player;

public class PlayerWarrior extends Player {
	
	private static String imageName = "72_16x16_Tileset.png"; // Reusing the enemy sprite sorry!!
	private static Texture texture = GameObject.getTexture(imageName, 0, 144, 16, 16).getTexture();
	
	public PlayerWarrior(Coordinates coords) {
		super(coords, texture, 100, 25); // coords, hp, damage
		
	}
}
