import java.io.*;
import java.net.*;
import java.util.*;

public class Manager{
    private ArrayList<ServerThread> threadList;
    //Items
    private DLList<Item> itemKeys;
    private MyHashMap<Item,String> itemList;
    private static int yLocation;
    //Player
    private ArrayList<Player> playerList;
    //Enemy
    private LinkedList<Enemy> enemyList;
    EnemyMovement movementThread;
    Thread enemyThread;
    //Portal
    private Stack<Portal> portalList;
    //Castle
    private Castle castle;
    private String level;

    public Manager(){
        threadList = new ArrayList<ServerThread>();
        itemKeys = new DLList<Item>();
        itemList = new MyHashMap<Item,String>();
        playerList = new ArrayList<Player>();
        enemyList = new LinkedList<Enemy>();
        movementThread = new EnemyMovement(enemyList,this);
        enemyThread = new Thread(movementThread);
        portalList = new Stack<Portal>();
        castle = new Castle(560,220,240,236);
        yLocation = 20;

        level = "0";
    }

    public void addThread(ServerThread t){
        threadList.add(t);

        //Quest 1 Info
        //Create Item
        //itemList.add(new Item(400,yLocation,40,43));
        Item sword = new Item(400,yLocation,40,43);
        Item knight = new Item(400,yLocation,55,61);
        itemKeys.add(sword);
        itemKeys.add(knight);
        itemList.put(sword,"1");//Sword
        itemList.put(knight,"2");//Enemy Knight
        //Create Portal
        portalList.add(new Portal(600,yLocation,80,80));


        yLocation+=170;
        broadcastItemKeys();
        broadcastItem();
        broadcastPortal();
    }

    public void removeThread(ServerThread t){
        threadList.remove(t);
    }

    //Player
    public int addPlayer(){
        int ran = (int)(Math.random()*600);
        int playerNum = playerList.size()+1;
        playerList.add(new Player(ran,40,"Player: " + playerNum));
        return playerList.size()-1;
    }
    public void updatePlayerList(ArrayList<Player> list){
        playerList = list;
        broadcastPlayer();
    }
    public synchronized void broadcastPlayer(){
        for(int i = 0; i<threadList.size(); i++){
            threadList.get(i).sendInfo(playerList);
        }
    }

    //Item
    public void updateItemKeyList(DLList<Item> list){
        itemKeys = list;
        broadcastItemKeys();
    }

    public synchronized void broadcastItemKeys(){
        for(int i = 0; i<threadList.size(); i++){
            threadList.get(i).sendInfo(itemKeys);
        }
    }

    public void updateItemList(MyHashMap<Item,String> list){
        itemList = list;
        broadcastItem();
    }

    public synchronized void broadcastItem(){
        for(int i = 0; i<threadList.size(); i++){
            threadList.get(i).sendInfo(itemList);
        }
    }

    //Portal
    public void updatePortalList(Stack<Portal> list){
        portalList = list;
        broadcastPortal();
    }

    public synchronized void broadcastPortal(){
        for(int i = 0; i<threadList.size(); i++){
            threadList.get(i).sendInfo(portalList);
        }
    }

    //Castle
    public void updateCastle(Castle c){
        castle = c;
        broadcastCastle();
    }

    public synchronized void broadcastCastle(){
        for(int i = 0; i<threadList.size(); i++){
            threadList.get(i).sendInfo(castle);
        }
    }

    //Enemy
    public void addEnemy(){
        enemyList.add(new Enemy());
    }

    public void updateEnemyList(LinkedList<Enemy> list){
        enemyList = list;
        broadcastEnemy();
    }

    public synchronized void broadcastEnemy(){
        for(int i = 0; i<threadList.size(); i++){
            threadList.get(i).sendInfo(enemyList);
        }
    }

    public LinkedList<Enemy> getEnemyList(){
        return enemyList;
    }

    //Level
    public synchronized void broadcastLevel(){
    	for(int i=0; i<threadList.size(); i++){
    		threadList.get(i).sendInfo(level);
    	}
    }

    public synchronized void resetLevel1(){
        //Reset Items
        for(int i=0; i<itemList.size(); i++){
            ((Item)itemList.getKeys().get(i)).setCollected(false);
        }
        broadcastItem();
        for(int i=0; i<itemKeys.size(); i++){
            itemKeys.get(i).setCollected(false);
            /*
            if(itemList.getValues(itemKeys.get(i)).toString().equals("1")){
                itemKeys.get(i).setCollected(false);
            }*/
        }
        broadcastItemKeys();
        //Reset Players
        for(int i=0; i<playerList.size(); i++){
            playerList.get(i).setLocation(20,20);
            playerList.get(i).setHasItem(false);
            playerList.get(i).setSteppedOn(false);
        }
        broadcastPlayer();
        //Reset Enemy
        for(int i=0; i<enemyList.size(); i++){
            enemyList.get(i).setLocation(800,(int)(Math.random()*500));
        }
        broadcastEnemy();
    }

    public synchronized void resetLevel2(){
        //Reset Items
        for(int i=0; i<itemList.size(); i++){
            ((Item)itemList.getKeys().get(i)).setCollected(false);
        }
        broadcastItem();
        for(int i=0; i<itemKeys.size(); i++){
            itemKeys.get(i).setCollected(false);
        }
        broadcastItemKeys();
        //Reset Players
        for(int i=0; i<playerList.size(); i++){
            playerList.get(i).setLocation(20,20);
            playerList.get(i).setHasItem(false);
            playerList.get(i).setSteppedOn(false);
        }
        broadcastPlayer();
    }

    public String getLevel(){
        return level;
    }

    public void setLevel(String l){
        level = l;
    }

    public void startGame(){
        //Add enemy/thread
        if(enemyList.size() == 0){
            this.addEnemy();
        }
        if(!enemyThread.isAlive()){
            enemyThread.start();
        }

        broadcastEnemy();

        //Adjust Player Location
        for(int i=0; i<playerList.size(); i++){
            playerList.get(i).setLocation(20,20);
        }
        broadcastPlayer();
        //Level
    	level = "1";
    	broadcastLevel();

    }

    public void startLevel2(){
        resetLevel1();

        //Level
        level = "2";
        broadcastLevel();
    }

    public void endGame(){
        level = "End Game";
        broadcastLevel();
    }

    public void restartGame(){
        resetLevel2();
        level = "Lobby";
        broadcastLevel();
    }
}