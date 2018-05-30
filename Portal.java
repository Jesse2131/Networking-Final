import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Portal implements Serializable{
	private int x,y,width,height;
	private boolean inUse;
	public Portal(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.inUse = false;
	}

	public void drawMe(Graphics g, BufferedImage itemImage){
		g.drawImage(itemImage,x,y,null);
	}

	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}
	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public boolean getinUse(){
		return inUse;
	}

	public void setinUse(boolean c){
		inUse = c;
	}

	public boolean checkCollision(Player p){
		int pX = p.getX();
		int pY = p.getY();
		int pWidth = p.getWidth();
		int pHeight = p.getHeight();
		
		if( pX + pWidth >= x && pX <= x + width && pY + pHeight >= y && pY <= y + height && inUse == false){
			inUse = true;
			p.setSteppedOn(true);
			return true;
		}
		inUse = false;
		p.setSteppedOn(false);
		return false;		
	}
}