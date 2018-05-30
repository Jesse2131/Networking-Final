import java.io.*;
import java.net.*;
//Swing
import javax.swing.*;
//Awt
import java.awt.*;
import java.awt.image.BufferedImage;
//Swing
import javax.swing.*;
//Awt Event
import java.awt.event.*;
//Util
import java.util.*; 
//Io
import java.io.*;
import javax.imageio.ImageIO;
//Sound
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import sun.audio.*;
public class ClientScreen extends JPanel implements ActionListener, KeyListener{ 

	//Server/Client info
	private String hostName;
	private int portNumber;
	private Socket serverSocket;
	private BufferedReader in;
	private PrintWriter out;
	
	private PushbackInputStream inObj;		
	private ObjectInputStream inObjREAL;	
	private ObjectOutputStream outObj;	
	
	//Keyboard
	private Input input;

	//Player Lists
	//private DLList<Player> playerList;
	private ArrayList<Player> playerList;
	private Player player;
	private String username;
	private int playerCode;
	private BufferedImage playerImage;

	//Login Screen
	private JTextField enterUserNameField;
	private JButton startGameButton;
	//End Game Screen
	private JButton restartGame;

	//Images
	private BufferedImage instructionsImage;


	//Level 1 and 2
		//Background
	private BufferedImage background1,background2;
	private int level;
		//Items
	//private DLList<Item> itemList;
	private MyHashMap<Item,String> itemList;
	private DLList<Item> itemKeys;
	private int swordX,swordY;
	private BufferedImage swordImage,enemyKnightImage;
		//Portal
	private Stack<Portal> portaList;
	private BufferedImage portalImage;
		//Enemys
	private LinkedList<Enemy> enemyList;
	private BufferedImage enemyImage;
		//Castle - Level 2
	private Castle castle;
	private BufferedImage castleImage;

	@SuppressWarnings("unchecked")
    public ClientScreen() throws IOException{

    	//Server Info
    	hostName = "localhost";

        portNumber = 4444;
        serverSocket = new Socket("10.210.30.185",portNumber);
        
		inObj = new PushbackInputStream(serverSocket.getInputStream());		
		inObjREAL = new ObjectInputStream(serverSocket.getInputStream());	
		outObj = new ObjectOutputStream(serverSocket.getOutputStream());
		
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		out = new PrintWriter(serverSocket.getOutputStream(),true);
        
		//KeyBoard
		input = new Input(this);

		//Player info
		playerList = new ArrayList<Player>();
		username = "";
		playerCode = -1;

		

		//Login Screen/ Screen = 0;
		//Start button
		startGameButton = new JButton("Start Game");
		startGameButton.setBounds(350,400,100,30);
		startGameButton.addActionListener(this);
		add(startGameButton);
		level = 0;

		//End Game Screen
		restartGame = new JButton("Restart");
		restartGame.setBounds(350,400,100,30);
		restartGame.addActionListener(this);


		//Level 1 Info
		//itemList = new DLList<Item>();
		itemList = new MyHashMap<Item,String>();
		itemKeys = new DLList<Item>();
		enemyList = new LinkedList<Enemy>();
		portaList = new Stack<Portal>();
		castle = new Castle(540,200,240,236);
		//Images
		try{
            instructionsImage = ImageIO.read(new File("Images/instructions.png"));
            background1 = ImageIO.read(new File("Images/Backgrounds/background1.png"));
            background2 = ImageIO.read(new File("Images/Backgrounds/background2.png"));
            playerImage = ImageIO.read(new File("Images/Player/Player.png"));
            swordImage = ImageIO.read(new File("Images/Items/sword.png"));
           	enemyKnightImage = ImageIO.read(new File("Images/Items/enemyKnight.png"));
            enemyImage = ImageIO.read(new File("Images/Enemies/enemy.png"));
            portalImage = ImageIO.read(new File("Images/portal.png"));
            castleImage = ImageIO.read(new File("Images/castle.png"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

		this.setLayout(null);
		this.setFocusable(true);
		addKeyListener(this);

    }
	public Dimension getPreferredSize() {
        //Sets the size of the panel
        return new Dimension(800,600);
    }

	public void paintComponent(Graphics g){

		super.paintComponent(g);
		Font big = new Font("Ariel",Font.BOLD,20);
		Font text = new Font("Ariel",Font.BOLD,17);
		Font small = new Font("Ariel",Font.PLAIN,15);
		if(level == 0){ //Lobby
			//Login Screen
			g.setColor(Color.gray);
			g.fillRect(0,0,800,600);
			//How many connections
			g.setColor(Color.black);
			g.setFont(big);
			g.drawString("Players: " + playerList.size(), 20,30);
			g.setFont(small);
			g.drawString("Instructions",20,50);
			//Instructions
			g.drawImage(instructionsImage,10,70,null);
			//Draw Players
			for(int i = 0; i<playerList.size();i++){
				playerList.get(i).drawMe(g,playerImage);
			}
		}
		else if(level == 1){ //Level 1      
			remove(startGameButton);
			//Background
			g.drawImage(background1,0,0,null);
			//Enemies
			for(int i=0; i<enemyList.size(); i++){
				enemyList.get(i).drawMe(g,enemyImage);
				//System.out.println("Screen" + enemyList.get(i).getX());
			}
			//Items
			for(int i=0; i<itemKeys.size(); i++){
				for(int d=0; d<itemKeys.size()+1; d++){
					//System.out.println("|"+itemList.getValues(itemKeys.get(i)).get(d).toString() + "|");
					if(itemList.getValues(itemKeys.get(i)).get(d).toString().equals("1")){
						if(itemKeys.get(i).getWidth() == 40){
							itemKeys.get(i).drawMe(g,swordImage);
						}
					}
				}
			}
			//Portal
			for(int i=0; i<portaList.size(); i++){
				portaList.get(i).drawMe(g,portalImage);
			}
			//Player
			for(int i = 0; i<playerList.size();i++){
				playerList.get(i).drawMe(g,playerImage);
			}	
			//Dialogue
			if(playerList.get(playerCode).getHasItem() == false){
				g.setFont(text);
				g.drawString("You have been sent to collect weapons for the kingdom. Grab a sword!",10,520);
			}
			else{
				g.setFont(text);
				g.drawString("Great! Now run to the portal!",10,520);
			}

		}
		else if(level == 2){ //Level 2
			g.drawImage(background2,0,0,null);
			//Enemies
			for(int i=0; i<enemyList.size(); i++){
				enemyList.get(i).drawMe(g,enemyImage);
				//System.out.println("Screen" + enemyList.get(i).getX());
			}
			//Items (Actually "enemies" this time)
			for(int i=0; i<itemKeys.size(); i++){
				for(int d=0; d<itemKeys.size()+1; d++){
					//System.out.println("|"+itemList.getValues(itemKeys.get(i)).get(d).toString() + "|");
					if(itemList.getValues(itemKeys.get(i)).get(d).toString().equals("2")){
						if(itemKeys.get(i).getWidth() == 55){
							itemKeys.get(i).drawMe(g,enemyKnightImage);
						}
					}
				}
			}
			//Princess
			castle.drawMe(g,castleImage);
			//Player
			for(int i = 0; i<playerList.size();i++){
				playerList.get(i).drawMe(g,playerImage);
			}
			//Dialogue
			if(playerList.get(playerCode).getHasItem() == false){
				g.setFont(text);
				g.drawString("Ah!! Kill that evil knight!",10,520);
			}
			else{
				g.setFont(text);
				g.drawString("Nice! You can return home now!",10,520);
			}
		}
		else if(level ==3){//End Game
			g.setColor(Color.gray);
			g.fillRect(0,0,800,600);
		}

		//Display Username
		//System.out.println("Player Code: " + playerCode);
		username = playerList.get(playerCode).getUsername();
		g.setFont(big);
		if(level != 0){
			g.drawString(username,20,30);	
		}
	}

	public void playHitSound(){
		try{
			URL url = this.getClass().getClassLoader().getResource("Sound/hitSound.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			clip.start();
		}
		catch (Exception exc){
		 	exc.printStackTrace(System.out);
		}
	}

	public void playPickUpItemSound(){
		try{
			URL url = this.getClass().getClassLoader().getResource("Sound/PickupItem.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			clip.start();
		}
		catch (Exception exc){
		 	exc.printStackTrace(System.out);
		}
	}

	public void playDestoryEnemySound(){
		try{
			URL url = this.getClass().getClassLoader().getResource("Sound/destoryEnemy.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			clip.start();
			System.out.println("Playing sound");
		}
		catch (Exception exc){
		 	exc.printStackTrace(System.out);
		}
	}

	public void playBackGroundMusic(){
		try{
			URL url = this.getClass().getClassLoader().getResource("Sound/bg.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			clip.start();
			clip.loop(1000);
		}
		catch (Exception exc){
		 	exc.printStackTrace(System.out);
		}
	}
	
	public static void music(){
		AudioPlayer MGP = AudioPlayer.player;
		AudioStream BGM;
		AudioData MD;

		ContinuousAudioDataStream loop = null;

		try{
			BGM = new AudioStream(new FileInputStream("Sound/bg.wav"));
			MD = BGM.getData();
			loop = new ContinuousAudioDataStream(MD);
		}
		catch(IOException e){
			System.out.println("cant find the file");
		}

		MGP.start(loop);
	}

	public void updatePlayer(int idCode, Player updatedPlayer){
		for(int i = 0; i<playerList.size();i++){
			if(playerList.get(i).getIDCode()==idCode){
				playerList.set(i,updatedPlayer);
			}
		}
	}
	public Player getPlayer(int idCode){
		for(int i = 0; i<playerList.size();i++){
			if(playerList.get(i).getIDCode()==idCode){
				return playerList.get(i);
			}
		}
		return null;
	}
	public boolean contains(int idCode){
		for(int i = 0; i<playerList.size();i++){
			if(playerList.get(i).getIDCode()==idCode){
				return true;
			}
		}
		return false;
	}
	public synchronized void setup() throws IOException{
        while(true){
			try {
				Thread.sleep(5);
			} catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}
			try{
				Object o = inObjREAL.readObject();
				if(o instanceof ArrayList){ //Player
					playerList = (ArrayList<Player>)o;
				}
				else if(o instanceof Integer){ //Playercode
					playerCode = (Integer)o;
				}
				else if(o instanceof DLList){//Item Keys
					itemKeys = (DLList<Item>)o;
				}
				else if(o instanceof MyHashMap){//Items
					itemList = (MyHashMap<Item,String>)o;
				}
				else if(o instanceof String){
					String msg = (String)o;
					if(msg.equals("1")){
						level = 1;
					}
					else if(msg.equals("2")){
						level = 2;
					}
					else if(msg.equals("End Game")){
						level = 3;
					}
					else if(msg.equals("Lobby")){
						remove(restartGame);
						add(startGameButton);
						level = 0;
					}
				}
				else if(o instanceof LinkedList){//Enemy
					LinkedList<Enemy> serverEnemy = (LinkedList<Enemy>)o;
					enemyList = serverEnemy;
				}
				else if(o instanceof Stack){//Portal
					Stack<Portal> serverPortal = (Stack<Portal>)o;
					portaList = serverPortal;
				}
				else if(o instanceof Castle){//Castle
					Castle serverCastle = (Castle)o;
					castle = serverCastle;
				}


				repaint();
			}
	        catch (ClassNotFoundException e) {
				System.err.println("Class does not exist" + e);
				System.exit(1);
			} 
	        catch (IOException e) {
				System.err.println("Setup: Couldn't get I/O for the connection to");
				System.exit(1);
			}	
			//Check Collision 
			if(level == 1){
				for(int i=0; i<itemKeys.size(); i++){//Items
					for(int p=0; p<playerList.size(); p++){
						if(itemKeys.get(i).getWidth() == 40){//Sword
							if(itemKeys.get(i).checkCollision(playerList.get(p)) == true){
								playPickUpItemSound();
								sendInfo(itemKeys);
							}
						}
					}
				}
				for(int i=0; i<enemyList.size(); i++){//Enemy
					if(enemyList.get(i).checkCollision(playerList.get(playerCode)) == true){
						playHitSound();
						int newY;
						if(enemyList.get(i).getY() <= 300){
							newY = enemyList.get(i).getY()+200;
						}
						else{
							newY = enemyList.get(i).getY()-200;
						}
						playerList.get(playerCode).setLocation(20,newY);
						sendInfo(playerList);
					}
				}
				for(int i=0; i<portaList.size(); i++){//Portal
					for(int p=0; p<playerList.size(); p++){
						portaList.get(i).checkCollision(playerList.get(p));
						if(checkAllPlayersOnPortal() == true && checkAllPlayersHaveItem() == true){
							changeToLevel2();
						}
					}
				}
			}
			else if(level == 2){
				for(int i=0; i<enemyList.size(); i++){//Enemy
					if(enemyList.get(i).checkCollision(playerList.get(playerCode)) == true){
						playHitSound();
						int newY;
						if(enemyList.get(i).getY() <= 300){
							newY = enemyList.get(i).getY()+200;
						}
						else{
							newY = enemyList.get(i).getY()-200;
						}
						playerList.get(playerCode).setLocation(20,newY);
						sendInfo(playerList);
					}
				}
				for(int i=0; i<playerList.size(); i++){
					castle.checkCollision(playerList.get(i));
					if(checkAllPlayersOnCastle() == true && checkAllPlayersHaveItem() == true){
						changeToEnd();
					}

				}
			}
		}
	}

	public boolean checkAllPlayersOnCastle(){
		for(int i=0; i<playerList.size(); i++){
			if(playerList.get(i).getHasSteppedOn() == false){
				return false;
			}
		}
		return true;
	}

	public boolean checkAllPlayersOnPortal(){
		for(int i=0; i<playerList.size(); i++){
			if(playerList.get(i).getHasSteppedOn() == false){
				return false;
			}
		}
	    return true;
	}

	public boolean checkAllPlayersHaveItem(){
		for(int i=0; i<playerList.size(); i++){
			if(playerList.get(i).getHasItem() == false){
				return false;
			}
		}
	    return true;
	}
	public void sendInfo(Object temp){
		try{
			outObj.reset();
			outObj.writeObject(temp);
		}
        catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to");
			System.exit(1);
		}
	}

	public void changeToLevel1(){ 
		try{
			outObj.reset();
			outObj.writeObject("Start Game");
		}
        catch (IOException ex) {
			System.err.println("Change To Level 1: Couldn't get I/O for the connection to");
			System.exit(1);
		}
	}

	public void changeToLevel2(){
		try{
			outObj.reset();
			outObj.writeObject("Level 2");
		}
        catch (IOException ex) {
			System.err.println("Change To Level 2: Couldn't get I/O for the connection to");
			System.exit(1);
		}
	}

	public void changeToEnd(){
		add(restartGame);
		try{
			outObj.reset();
			outObj.writeObject("End Game");
		}
        catch (IOException ex) {
			System.err.println("Change To Level 3: Couldn't get I/O for the connection to");
			System.exit(1);
		}
	}

	public void changeToLobby(){
		try{
			outObj.reset();
			outObj.writeObject("Lobby");
		}
        catch (IOException ex) {
			System.err.println("Change To Level 3: Couldn't get I/O for the connection to");
			System.exit(1);
		}
	}
	public void actionPerformed(ActionEvent e) {	
		if(e.getSource() == startGameButton){
			//playBackGroundMusic();
			music();
			changeToLevel1();	
		}
		else if(e.getSource() == restartGame){
			changeToLobby();
		}

		repaint();
    }

    public void keyPressed(KeyEvent e) {
        //System.out.println("key code: " + e.getKeyCode());
        if(e.getKeyCode() == 87){ //up 
            playerList.get(playerCode).moveUp();
        }
        if(e.getKeyCode() == 83){//Down
            playerList.get(playerCode).moveDown();
        }
        if(e.getKeyCode() == 65){//Left
            playerList.get(playerCode).moveLeft();
        }
        if(e.getKeyCode() == 68){//Right
            playerList.get(playerCode).moveRight();
        }
        if(e.getKeyCode() == 32){//Space
        	for(int i=0; i<itemKeys.size(); i++){//Items
        		for(int p=0; p<playerList.size(); p++){
        			if(itemKeys.get(i).getWidth() == 55){//Knight
        				if(itemKeys.get(i).checkCollision(playerList.get(p)) == true){
        					playDestoryEnemySound();
        					sendInfo(itemKeys);
        				}
        			}
        		}
        	}
        }
        if(e.getKeyCode() == 80){//Cheat Key
        	if(level == 1){
        		changeToLevel2();
        	}
        	else if(level == 2){
        		changeToEnd();
        	}
        }
 		//sendInfo(new String("player/"+playerCode + "/" + playerList.get(playerCode).getLocation() ));
        sendInfo(playerList);
        repaint();
 
    }
 
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
	
}