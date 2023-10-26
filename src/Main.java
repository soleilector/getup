// Imports the Google Cloud client library

//import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.SystemParametersOrBuilder;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.protobuf.ByteString;

import java.util.HashMap;
import java.util.Scanner;
//import java.util.Calendar;

// GUI
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import javax.swing.JPasswordField;
import javax.swing.JLabel;

public class Main {
	// ================= MAIN VARS
	private static User thisUser;
	private static Map thisMap;
	private static Dimension computerSize = new Dimension(1366, 768);
	private static FlowLayout centerLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);
	private static FlowLayout statusBarLayout = new FlowLayout(FlowLayout.CENTER,0,10);
	private static int statusBarYSize = 50;
	private static FlowLayout leftLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);

	// ======== HEADER COMPONENTS GUI
	private static Color mainColor = new Color(0xfff4ab);
	private static Color mainFontColor = Color.black;
	
	private static JLabel headerLabel = new JLabel("Get Up",
			JLabel.CENTER) {
		{
			setBackground(mainColor);
			setForeground(mainFontColor);
			setPreferredSize(new Dimension(computerSize.width, 68));
			setFont(new Font("Verdana",Font.BOLD,36));
			setLayout(centerLayout);
		}
	};

	// ====== STATUS BAR COMPONENTS GUI
	private static JLabel turnsLabel = new JLabel("Turns: 0") {
		{
			setLayout(centerLayout);
			setBackground(mainColor);
			setForeground(Color.white);
			setPreferredSize(new Dimension(100, 30));
		}
	};

	private static JButton addMemoryBTN = new JButton("Add Memory") {
		{
			setLayout(centerLayout);
			setPreferredSize(new Dimension(150, 30));

		}
	};

	private static JPanel statusBar = new JPanel() {
		{
			setLayout(centerLayout);
			setBackground(new Color(0x5D4040));
			setPreferredSize(new Dimension(computerSize.width, 30));
			add(turnsLabel);
			add(addMemoryBTN);
		}
	};

	// ======= QUESTS GUI COMPONENTS
	private static ArrayList<JButton> questButtons = new ArrayList<JButton>();
	private static ArrayList<JLabel> questLabels = new ArrayList<JLabel>();

	private static JLabel questsHeader = new JLabel("Quests", JLabel.CENTER) {
		{
			setLayout(centerLayout);
			setBackground(new Color(0x5D4040));
			setForeground(Color.WHITE);
			setPreferredSize(new Dimension(548, 30));
		}
	};

	private static JPanel questsHeaderPanel = new JPanel() {
		{
			setLayout(centerLayout);
			setBackground(new Color(0x5D4040));
			setPreferredSize(new Dimension(548, 30));
			add(questsHeader);
		}
	};

	private static JPanel questsListPanel = new JPanel() {
		{
			setLayout(leftLayout);
			setBackground(new Color(0xDBCAB0));
			setPreferredSize(new Dimension(548, 370));
		}
	};

	// ===== MESSAGE PANEL COMPONENTS GUI
	//e
	
	private static JPanel memoryLoadingPanel = new JPanel() {
		{
			setLayout(centerLayout);
			setBackground(Color.BLACK);
			setPreferredSize(new Dimension(computerSize.width, 270));
			setVisible(false);
		}
	};
	
	private static JPanel messagePanel = new JPanel() {
		{
			setLayout(centerLayout);
			setBackground(new Color(0xDBCAB0));
			setPreferredSize(new Dimension(computerSize.width, 270));
		}
	};

	private static JLabel messageLabel = new JLabel("Some test text...", JLabel.CENTER) {
		{
			setLayout(centerLayout);
			setBackground(new Color(0xDBCAB0));
			setPreferredSize(new Dimension(1366, 300));
			setForeground(Color.black);
		}
	};

	// ===== STATUS GUI COMPONENTS
	private static Font statusFont = new Font("Lucida Console",Font.BOLD,18);
	
	private static JLabel healthBarLabel = new JLabel() {
		{
			setPreferredSize(new Dimension(100, 30));
			setLayout(centerLayout);
			setText("Health: ");
			setBackground(Color.CYAN);
			setFont(statusFont);
			setForeground(mainFontColor);
		}
	};
	
	private final static int HEALTH_BAR_MAX_WIDTH = 300;
	private static JPanel healthBarFG = new JPanel() {
		{
			setPreferredSize(new Dimension(HEALTH_BAR_MAX_WIDTH, 30));
			setLayout(centerLayout);
			setBackground(Color.GREEN);
		}
	};

	private static JPanel healthBarPanel = new JPanel() {
		{
			setPreferredSize(new Dimension(300, 30));
			setLayout(leftLayout);
			setBackground(Color.gray);
			add(healthBarFG);
		}
	};

	private static JPanel healthPanel = new JPanel() {
		{
			setPreferredSize(new Dimension(548, statusBarYSize));
			setBackground(mainColor);
			setLayout(statusBarLayout);
			add(healthBarLabel);
			add(healthBarPanel);
		}
	};
	
	private static JLabel expBarLabel = new JLabel() {
		{
			setPreferredSize(new Dimension(100, 30));
			setLayout(centerLayout);
			setText("EXP: ");
			setBackground(Color.CYAN);
			setFont(statusFont);
			setForeground(mainFontColor);
		}
	};

	private final static int EXP_BAR_MAX_WIDTH = 300;
	private static JPanel expBarFG = new JPanel() {
		{
			setPreferredSize(new Dimension(100, 30));
			setLayout(centerLayout);
			setBackground(Color.GREEN);
		}
	};

	private static JPanel expBarPanel = new JPanel() {
		{
			setPreferredSize(new Dimension(300, 30));
			setLayout(leftLayout);
			setBackground(Color.gray);
			add(expBarFG);
		}
	};

	private static JPanel expPanel = new JPanel() {
		{
			setPreferredSize(new Dimension(548, statusBarYSize));
			setBackground(mainColor);
			setLayout(statusBarLayout);
			add(expBarLabel);
			add(expBarPanel);
		}
	};
	
	private static JLabel dmgStatLabel = new JLabel() {
		{
			setPreferredSize(new Dimension(100, 30));
			setLayout(statusBarLayout);
			setText("Nost: 0");
			setBackground(Color.CYAN);
			setFont(statusFont);
			setForeground(mainFontColor);
		}
	};
	
	private static JPanel dmgStatPanel = new JPanel() {
		{
			setPreferredSize(new Dimension(548, statusBarYSize));
			setBackground(mainColor);
			setLayout(centerLayout);
			add(dmgStatLabel);
		}
	};

	// ===== MAP COMPONENTS
	private static JButton[] mapTiles = new JButton[9];
	private static String[] mapTileInfo = new String[9];

	private static JPanel mapContainer = new JPanel() {
		{
			setPreferredSize(new Dimension(270, 270));
			setLayout(centerLayout);
		}
	};

	private static JPanel statusContainer = new JPanel() {
		{
			setLayout(centerLayout);
			setPreferredSize(new Dimension(548, 400));
			setBackground(mainColor);
			add(healthPanel);
			add(expPanel);
			add(dmgStatPanel);
		}
	};

	private static JPanel questsContainer = new JPanel() {
		{
			setLayout(centerLayout);
			setPreferredSize(new Dimension(548, 400));
			setBackground(Color.white);
			add(questsHeaderPanel);
			add(questsListPanel);
		}
	};

	private static JPanel mapPanel = new JPanel() {
		{
			setLayout(centerLayout);
			setBackground(mainColor);
			setPreferredSize(new Dimension(computerSize.width, 400));
			add(statusContainer);
			add(mapContainer);
			add(questsContainer);
		}
	};

	// =============== MAIN PANELS
	private static JPanel navPanel = new JPanel() {
		{
			setLayout(centerLayout);
			setBackground(Color.BLACK);
			setPreferredSize(new Dimension(computerSize.width, 300));
			add(statusBar);
			add(messagePanel);
			add(memoryLoadingPanel);
		}
	};

	private static JPanel homePanel = new JPanel() {
		{
			setPreferredSize(computerSize);
			setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			setBackground(mainColor);
			add(headerLabel);
			add(mapPanel);
			add(navPanel);
		}
	};

	static JFrame mainMenuFrame = new JFrame() { // main menu frame
		{
			setSize(computerSize);
			setTitle("Get Up!");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			add(homePanel);
		}
	};

	// ===== UNIVERSAL ACTION LISTENER
	private static boolean canClickBTN = true;
	
	private static ActionListener pressBTN = new ActionListener() { // press a button
		@Override
		public void actionPerformed(ActionEvent e) {
			Object thing = e.getSource();
			System.out.println("Button clicked...");

			if (!canClickBTN) {
				return;
			}
			
			System.out.println("\t\tRegistered button click...");
			canClickBTN = false;
			
			int tokenGens = 0; // for generating only a specific amount of tokens per interaction...
			for (int mapTileId = 0; mapTileId < thisMap.getCurrentNumOfTiles(); mapTileId++) {
				//System.out.println("eekA");
				if (thing == thisMap.getTile(mapTileId)) { // if the source was a map tile
					//System.out.println("eekB");
					boolean userHasEnoughTurns = (thisUser.getTurns()>0);
					int tileMoveCost = thisUser.getMoveCost(thisUser.getMoveType(mapTileId, thisMap));
					//String thisTileToken = thisMap.getTileToken(mapTileId);
					if (!userHasEnoughTurns) { messageLabel.setText("You have no more turns! Complete some quests for more turns!"); }
					if (thisUser.getTurns() < tileMoveCost) { messageLabel.setText("You don't have enough turns. Complete some quests for more turns!"); }
					if (userHasEnoughTurns && tileMoveCost > -1) {
						//thisUser.getMoveType(mapTileId,thisMap);
						if (thisUser.getPosition() != mapTileId ) { // if 
							if (thisMap.isTileFree(mapTileId)) { // if user is not on this tile and it is free
								messageLabel.setText("Play!");
								thisUser.setPosition(mapTileId,thisMap); // set the position of user to the map tile
								thisUser.giveTurn(-tileMoveCost); // remove a turn from the user
								
								tokenGens = thisMap.fillMap(tokenGens);
							} else { // user is not on this tile and it is not free...
								//HashMap<String,String> thisTokenInfo = MapTokens.getTokenInfo(thisTileToken);
								User.interactStates successfulInteraction = thisUser.doMapInteraction(thisMap, mapTileId);
								
								if (successfulInteraction == User.interactStates.SUCCESS) {
									refreshHealthBar();
									refreshEXPBar();
									messageLabel.setText("Play!");
								} else {
									switch (successfulInteraction) {
										case HEALTH:
											messageLabel.setText("Your health is too low to continue! Complete a quest to gain health!");
									}
								}
							}
						}
					}
				}
			}
			
			for (int cquestBTNId = 0; cquestBTNId < questButtons.size(); cquestBTNId++) {
				JButton iButton = questButtons.get(cquestBTNId);
				if (thing == iButton) {
					int questId = Integer.valueOf(iButton.getName().substring(7));
					System.out.println("Claiming quest:"+questId);
					thisUser.claimQuest(questId);
					questsListPanel.remove(iButton);
					questsListPanel.revalidate();
					questsListPanel.repaint();
					
					System.out.println("Whee");
					break;
				}
			}

			if (thing == addMemoryBTN) {
				messagePanel.setVisible(false);
				memoryLoadingPanel.setVisible(true);
				
				ArrayList<String> objectsFound = PhotoScanner.scanPhoto(); // scan a photo
				if (objectsFound != null) { // if no objects were found
					System.out.println("Objects were found when adding a memory...");
					for (int obId = 0; obId < objectsFound.size(); obId++) {
						String objectName = objectsFound.get(obId);
						System.out.println("Testing if "+objectName+" is a quest object.");
						if (thisUser.isQuestObject(objectName)) {
							System.out.printf("Found quest object: %s\n", objectName);
							//thisUser.giveTurn(1);
							thisUser.completeQuest(objectName);
							
							// now find and remove label from
							updateQuests();
							
							// add images to memories box
							
							
							break;
						}
					}
				}
				
				messagePanel.setVisible(true);
				memoryLoadingPanel.setVisible(false);
			}

			canClickBTN = true;
		}
	};

	public static void main(String[] args) {
		addMemoryBTN.addActionListener(pressBTN);
		messageLabel.setText("Play.");
		messagePanel.add(messageLabel);
		mainMenuFrame.setVisible(true);
		
		thisUser = new User(new HashMap<String, String>() {
			{
				put("quests_0","1");
				put("quests_1","1");
				put("turns","50");
				put("map","grassland");
				put("health","5");
				put("exp","0");
			}
		});
		//thisMap = generateMap(thisUser.getMapName(),9);
		thisMap = new Map(9) {{
			generateMapTiles(pressBTN,mapContainer);
			placeUser(thisUser.getPosition());
			setMapType(thisUser.getMapName());
		}};

		long currentTick = System.currentTimeMillis();
		long timeToElapse = 1000 * Settings.generateQuestsEvery(); // time to elapse from a start time
		
		long startTick = currentTick; // get tick to start counting from
		// can be set by a pre-saved value
		long endTick = startTick+timeToElapse; // get tick to end count at
		System.out.printf("stick %d\netick %d",startTick,endTick);
		

		initializeQuests();
		refreshQuests();
		refreshHealthBar();
		refreshEXPBar();
		while (true) {
			updateMap();
			turnsLabel.setText("Turns: "+String.valueOf(thisUser.getTurns()));
			
			// turn refresh quests
			currentTick = System.currentTimeMillis();
			long secondsElapsed = (currentTick-startTick)/1000; // how many seconds have elapsed
			
			boolean hasTimeElapsed = secondsElapsed >= Settings.generateQuestsEvery();			
			
			if (hasTimeElapsed) { // if we have elapsed 60 seconds since start time
				// reset
				startTick = currentTick;
				endTick = currentTick + timeToElapse;
				
				refreshQuests();
				updateQuests();
				//System.out.println("Finished generated new quests.");
			}
			
			long x = 10;
			try {
				Thread.sleep(x);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private static void updateQuests() {
		//ArrayList<JButton> buttonCache = new ArrayList<>();
		//ArrayList<JLabel> buttonLabel = new ArrayList<>();
		
		questsListPanel.removeAll();
		
		initializeQuests();
		
		questsListPanel.revalidate();
		questsListPanel.repaint();
	}
	
	private static void initializeQuests() {
		HashMap<String, String>[] userQuests = thisUser.getUQuests(); //
		HashMap<String, String>[] userCQuests = thisUser.getCQuests();
		
		for (int userQuestId = 0; userQuestId < userCQuests.length; userQuestId++) {
			HashMap<String, String> thisUserQuest = userCQuests[userQuestId];
			if (thisUserQuest != null) {
				System.out.println(thisUserQuest);
				int questId = Integer.parseInt(thisUserQuest.get("questId"));
				String thisObject = thisUserQuest.get("object");

				//System.out.printf("QuestID: [U%d][Q%d]\n\tobject: %s", userQuestId, questId, thisObject);

				JButton userQuestBTN = new JButton(thisObject) {{
						setLayout(centerLayout);
						setOpaque(true);
						setPreferredSize(new Dimension(500, 30));
						setText(thisObject);
					}
				};
				
				userQuestBTN.addActionListener(pressBTN);

				userQuestBTN.setName("CQuest_"+questId);
				questButtons.add(userQuestBTN);
				questsListPanel.add(userQuestBTN);
			}
		}
		
		// List unclaimable quests
		for (int userQuestId = 0; userQuestId < userQuests.length; userQuestId++) {
			HashMap<String, String> thisUserQuest = userQuests[userQuestId];
			if (thisUserQuest != null) {
				System.out.println(thisUserQuest);
				int questId = Integer.parseInt(thisUserQuest.get("questId"));
				String thisObject = thisUserQuest.get("object");

				//System.out.printf("QuestID: [U%d][Q%d]\n\tobject: %s", userQuestId, questId, thisObject);

				JLabel userQuestLabel = new JLabel(thisObject, JLabel.CENTER) {
					{
						setLayout(centerLayout);
						setOpaque(true);
						setPreferredSize(new Dimension(500, 30));
						setText(thisObject);
					}
				};

				userQuestLabel.setName("UQuest_"+questId);
				questLabels.add(userQuestLabel);
				questsListPanel.add(userQuestLabel);
			}
		}
	}
	
	
	private static void updateMap() {
		Image userIcon = MapTokens.getIconImage("user");
		String[] mapTokens = thisMap.getMapTokens();
		JButton[] mapTiles = thisMap.getTiles();
		
		for (int tileId = 0; tileId < mapTiles.length; tileId++) {
			JButton thisTileBTN = mapTiles[tileId];
			String thisToken = mapTokens[tileId];
			
			thisTileBTN.setBackground(Color.white);
			
			if (thisToken != null && !thisToken.equals("none")) {
				Image thisIcon = MapTokens.getIconImage(thisToken);
				
				if (thisIcon != null) {
					//System.out.println("Token found and icon placed");
					thisTileBTN.setIcon(new ImageIcon(thisIcon));
				} else {
					System.out.print("Token present; no image found for :"+thisToken);
					break;
				}
			} else {
				//System.out.println("No token");
				thisTileBTN.setIcon(null);
			}
		}
	};
	
	private static void refreshQuests() {
		ArrayList<Integer> newQuestIds = thisUser.generateQuests(); // generate new QuestIds
		thisUser.setQuests(newQuestIds);
	}
	
	private static void refreshHealthBar() {
		float healthRatio = (float) thisUser.getHealth()/ (float) thisUser.getMaxHealth();
		float newHealthBarSize = healthRatio * (float) HEALTH_BAR_MAX_WIDTH;
		
		healthBarFG.setPreferredSize(new Dimension((int) newHealthBarSize,30));
		
		healthBarFG.revalidate();
		healthBarFG.repaint();
	}
	
	private static void refreshEXPBar() {
		float expRatio = (float) thisUser.getEXP() / (float) User.getMaxEXP(thisUser.getLevel());
		float newEXPBarSize = expRatio * (float) EXP_BAR_MAX_WIDTH;
		
		expBarFG.setPreferredSize(new Dimension( (int) newEXPBarSize, 30));
		
		expBarFG.revalidate();
		expBarFG.repaint();
		expBarLabel.setText("EXP ("+thisUser.getLevel()+")");
	}
}