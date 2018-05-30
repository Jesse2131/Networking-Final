import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Item implements Serializable{
	private int x,y,width,height;
	private boolean collected;

	public Item(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.collected = false;
	}

	public void drawMe(Graphics g, BufferedImage itemImage){
		if(collected == false){
			g.drawImage(itemImage,x,y,null);
		}
		/*else if(collected == true){
			g.drawString("Collected",x,y);
		}*/
		//g.drawImage(itemImage,x,y,null);
	}

	public int hashCode(){
		return x+y;
	}

	public boolean equals(Object o){
		Item i = (Item)o;
		return i.hashCode() == hashCode();
	}

	public String toString(){
		return x + " " +y;
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

	public boolean getCollected(){
		return collected;
	}

	public void setCollected(boolean c){
		collected = c;
	}
	public boolean checkCollision(Player p){
		int pX = p.getX();
		int pY = p.getY();
		int pWidth = p.getWidth();
		int pHeight = p.getHeight();
		
		if( pX + pWidth >= x && pX <= x + width && pY + pHeight >= y && pY <= y + height && p.getHasItem() == false && collected == false){
			System.out.println("hit");
			collected = true;
			p.setHasItem(true);
			return true;
		}
		return false;		
	}
}