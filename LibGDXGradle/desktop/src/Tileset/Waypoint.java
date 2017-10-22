package Tileset;

import java.io.Serializable;

public class Waypoint extends GameObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6898938584372061984L;

	public Waypoint(ObjectType type, String imgPath) {
		super(type, imgPath);
	}
	
	public Waypoint clone() {
		return new Waypoint(getType(), getImgPath());
	}
	
}
