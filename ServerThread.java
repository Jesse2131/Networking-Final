import java.net.*;
import java.io.*;
import java.util.*;
public class ServerThread implements Runnable{
    private Socket clientSocket;
    private Manager m1;
    private PrintWriter out; //not using this
  
	private ObjectOutputStream outObj;
	private PushbackInputStream inObj;
	private ObjectInputStream inObjREAL;
	private int threadCode;

	@SuppressWarnings("unchecked")
    public ServerThread(Socket clientSocket, Manager m1, int referenceNum){
        this.clientSocket = clientSocket;
		this.m1 = m1;
        this.threadCode = referenceNum;
		
		try{
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			outObj = new ObjectOutputStream(clientSocket.getOutputStream());
        } 
        catch (IOException ex){
            m1.removeThread(this);
            System.out.println("Server Thread: Error listening for a connection");
            System.out.println(ex.getMessage());
        }
    }
    public void run(){
        System.out.println(Thread.currentThread().getName() + ": connection opened");
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //not using rn
            inObj = new PushbackInputStream(clientSocket.getInputStream());		
			inObjREAL = new ObjectInputStream(clientSocket.getInputStream());

			//Create Player 
			int id = m1.addPlayer();
			sendInfo(id);
			
			m1.broadcastPlayer();
            while(true){
                if(inObj.available()>0){
					try{
						Object o = inObjREAL.readObject();

						if(o instanceof String){
							String msg = (String)o;
							if(msg.equals("Start Game")){
								m1.startGame();
							}
							else if(msg.equals("Level 2")){
								m1.startLevel2();
							}
							else if(msg.equals("End Game")){
								m1.endGame();
							}
							else if(msg.equals("Lobby")){
								m1.restartGame();
							}
						}
						else if(o instanceof ArrayList){//Player list
							ArrayList<Player> pl= (ArrayList<Player>)o;
							m1.updatePlayerList(pl);
						}
						else if(o instanceof MyHashMap){
							MyHashMap<Item,String> serverItem = (MyHashMap<Item,String>)o;
							m1.updateItemList(serverItem);
						}
						else if(o instanceof DLList){//Item Keys
							DLList<Item> itemKeys = (DLList<Item>)o;
							m1.updateItemKeyList(itemKeys);
						}
						else if(o instanceof LinkedList){//Enemy List
							LinkedList<Enemy> enemyList = (LinkedList<Enemy>)o;
							m1.updateEnemyList(enemyList);
						}
						else if(o instanceof Stack){//Portal
							Stack<Portal> portalList = (Stack<Portal>)o;
							m1.updatePortalList(portalList);
						}
						else if(o instanceof Castle){
							Castle c = (Castle)o;
							m1.updateCastle(c);
						}
					}
					catch (ClassNotFoundException e) {
						System.err.println("Class does not exist" + e);
						System.exit(1);
					}
					catch (IOException ex){
						m1.removeThread(this);
						System.out.println("Run: Error listening for a connection");
						System.out.println(ex.getMessage());
					}
                    
               }
  
            }            
           
        } 
        catch (IOException ex){
            m1.removeThread(this);
            System.out.println("Run Catch: Error listening for a connection");
            System.out.println(ex.getMessage());
        }
    }
  
    public synchronized void sendInfo(Object temp){
    	try{
    		outObj.reset();
    		outObj.writeObject(temp);
    	}
    	catch (IOException ex){
    		System.out.println("Send: Error listening for a connection");
    		System.out.println(ex.getMessage());
    	}
    }
}