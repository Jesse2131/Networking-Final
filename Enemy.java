import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Enemy implements Serializable{
	private int x,y,width,height;

	public Enemy(){
		x = 700;
		y = (int)(Math.random()*500);
		width = 40;
		height = 27;
	}

	public void drawMe(Graphics g,BufferedImage enemyImage){
		g.drawImage(enemyImage,x,y,null);
		g.setColor(Color.black);
	}

	public String getLocation(){
		return x + " " + y;
	}

	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int width(){
		return width;
	}

	public int height(){
		return height;
	}

	public void move(){
		x-=3;
		if(x<0){
			x = 800;
			y = (int)(Math.random()*500);
		}
	}

	public boolean checkCollision(Player p){
		int pX = p.getX();
		int pY = p.getY();
		int pWidth = p.getWidth();
		int pHeight = p.getHeight();
		
		if( pX + pWidth >= x && pX <= x + width && pY + pHeight >= y && pY <= y + height){
			return true;
		}
		return false;		
	}
}