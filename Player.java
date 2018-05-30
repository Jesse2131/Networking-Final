import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Player implements Serializable{
	private int x,y,idCode; 
	private String username;
	private Boolean hasItem,hasSteppedOn;

	public Player(int x, int y, String username){
		this.x = x;
		this.y = y;
		this.username = username;
		idCode = -1;
		hasItem = false;
		hasSteppedOn = false;
	}

	public void drawMe(Graphics g,BufferedImage playerImage){
		g.drawImage(playerImage,x,y,null);
		g.setColor(Color.black);
		g.drawString(username,x,y-20);
	}

	public String toString(){
		return "" + idCode;
	}

	public String getLocation(){
		return x + "-" + y;
	}

	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getIDCode(){
		return idCode;
	}

	public void setIDCode(int id){
		this.idCode = id;
	}

	public String getUsername(){
		return username;
	}

	public void setUsername(String s){
		username = s;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getWidth(){
		return 52;
	}

	public int getHeight(){
		return 81;
	}
	public boolean getHasItem(){
		return hasItem;
	}

	public void setHasItem(boolean i){
		hasItem = i;
	}

	public boolean getHasSteppedOn(){
		return hasSteppedOn;
	}

	public void setSteppedOn(boolean s){
		hasSteppedOn = s;
	}
	
	public synchronized void moveRight(){
		if(x<=760){
			x+=10;
		}
	}

	public synchronized void moveLeft(){
		if(x>=0){
			x-=10;
		}
	}

	public synchronized void moveUp(){
		if(y>=0){
			y-=10;
		}
	}
	
	public synchronized void moveDown(){
		if(y<=550){
			y+=10;
		}
	}
}