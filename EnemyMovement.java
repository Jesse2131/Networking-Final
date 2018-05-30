import java.io.*;
import java.net.*;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.*;
@SuppressWarnings("unchecked")
public class EnemyMovement implements Runnable{
	Manager manager;
	LinkedList<Enemy> enemyList;

	public EnemyMovement(LinkedList<Enemy> eList, Manager manager){
		this.manager = manager;
		enemyList = eList;
	}
	
	public void run(){
		System.out.println("Enemy Thread Started: " + Thread.currentThread().getName());
		while(true){
			try {
				Thread.sleep(10);
			} catch(Exception e) {
	
			}
			synchronized(enemyList){
				for(int i=0; i<enemyList.size(); i++){
					enemyList.get(i).move();
					manager.broadcastEnemy();
				}
			}

		}
	}
}