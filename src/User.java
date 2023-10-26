import java.util.HashMap;
import java.util.Random;

import io.grpc.netty.shaded.io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class User {
	public static final int MAX_LEVEL = 99;
	private HashMap<String,String> playerData = new HashMap<>() {{
		put("coins","0");
		put("skin","1");
		put("turns","3");
		put("map","grassland");
		put("mapTile","5");
		put("quests_1","1");
		put("quests_0","1");
		put("health","5");
		put("damage","1");
		put("max_health","0");
		put("exp","0");
	}};
	
	public static int getMaxEXP(int level) {
		return (int) (Math.pow((level * 10), 2)/2);
		//return ((level*10)^2)/2;
	}
	
	User(HashMap<String,String> thisPlayerData) {
		for (String prop : playerData.keySet()) {
			String entryData = thisPlayerData.get(prop);
			
			if (!prop.equals("max_health") && !prop.equals("map") && !prop.equals("health")) {
				playerData.put(
						prop, 
						((entryData == null || entryData.equals("")) ? "0" : entryData)
					);
				System.out.printf("\n\t\tSetting plrData value [%s] to %s",prop,((entryData == null || entryData.equals("")) ? "0" : entryData));
			} else {
				if (prop.equals("max_health")) {
					playerData.put(prop, 
							((entryData == null || entryData.equals("")) ? "10" : entryData)
						);
				} else if(prop.equals("map")){
					playerData.put(prop,
							((entryData == null || entryData.equals("")) ? "grassland" : entryData)
						);
				} else if (prop.equals("health")){
					playerData.put(prop,
							((entryData == null || entryData.equals("")) ? "10" : entryData)
						);
				}else {
					playerData.put(prop,
							((entryData == null || entryData.equals("")) ? "none" : entryData)
						);
				}
				System.out.printf("\n\t\tSetting plrData value [%s] to %s",prop,playerData.get(prop));
			}
		}
	}
	
	User(){
		
	}
	
	public void setQuests(ArrayList<Integer> newQuests) {
		System.out.println("\t\tset quests called...");
		for (String propName : playerData.keySet()) {
			if (propName.contains("quests_")) {
				int questId = Integer.parseInt(propName.substring(7));
				
				// search for questId in newQuests
				boolean setQuest = false;
				for (int x=0;x<newQuests.size();x++) {
					if (newQuests.get(x)==questId) { // this is a quest we are setting to keep
						//playerData.put("quests_"+questId, "1");
						setQuestStatus(questId,"1");
						setQuest=true;
						break;
					}
				}
				if (!setQuest) {
					setQuestStatus(questId,"0");
				}
			}
		}
	}
	
	public void claimQuest(int questId) {
		giveTurn(5);
		giveHealth(3);
	}
	
	private void setQuestStatus(int questId,String value) {
		playerData.put("quests_"+questId, value);
		System.out.println(questId+"=>"+value);
	}
	
	private void completeQuest(int questId) {
		for (int x=0;x<Quests.maxQuests();x++) {
			// add check to see if player has completed this recently
			String playerQuestData = playerData.get("quests_"+x);
			if (x==questId && (!playerQuestData.equals("") || playerQuestData==null)) {
				setQuestStatus(x,"claimable");
			}
		}
	}
	
	public void completeQuest(String object) { // complete quest
		for (int x=0;x<Quests.maxQuests();x++) { // for every quest
			// add check to see if player has completed this recently
			HashMap<String,String> questInfo = Quests.getQuestInfo(x);
			if (questInfo!=null) { // if quest information does not exist
				//System.out.println(questInfo);
				int questId = Integer.parseInt(questInfo.get("QuestId"));
				if ((questId)==x) {
					if (questInfo.get("Object").equals(object)) {
						setQuestStatus(questId,"claimable");
					}
				}
			}
		}
	}
	
	public ArrayList<Integer> generateQuests() {
		Date dateThing = new Date();
		long seedTick = Quests.getQuestSeed();
		System.out.println("\nSeed: "+seedTick+"\n");
		
		int maxQuests = Quests.maxQuests();
		int questsToGen = 3;
		
		Random random = new Random();
        random.setSeed(seedTick);
        
        HashMap<Integer, HashMap<String,String>> allQuests = Quests.getAllQuests();
        
        ArrayList<Integer> newQuestIdList = new ArrayList<>();
        
        while (newQuestIdList.size() < questsToGen) { // keep trying to generate a nonduplicate questId
        	//System.out.println("attempting to find quest for new quests...");
        	//import java.util.concurrent.ThreadLocalRandom;

        	// nextInt is normally exclusive of the top value,
        	// so add 1 to make it inclusive
        	int tryQuestId = ThreadLocalRandom.current().nextInt(0, maxQuests);
			
			HashMap<String,String> tryQuestInfo = allQuests.get(tryQuestId);
			if (tryQuestInfo!=null) {
				//System.out.println("\tquest info exists...\n\t\tchecking for duplicate...");
				// check if its a quest we've already had duplicates
				boolean isDupe = false;
				
				HashMap<String,String>[] claimableQuests = this.getCQuests(); // check claimed quest for dupe
				for (int x = 0; x < claimableQuests.length; x++) {
					HashMap<String, String> cQuestData = claimableQuests[x];
					if (cQuestData!=null && Integer.parseInt(cQuestData.get("questId"))==tryQuestId) {
						isDupe=true;
						break;
					}
				}
				
				for (int qId : newQuestIdList) { // check quest  
					if (qId==tryQuestId) { isDupe=true; break; }
				}
				
				if (!isDupe) { // if this is not a duplicate number
					//System.out.println("\n\tquest isn't a duplicate...\n\tassigning quest to our list");
					newQuestIdList.add(tryQuestId);
				}
				else {
					//System.out.println("\n\tquestwas a dupe... trying new random questId...");
				}
			}
        }
        
        return newQuestIdList;
        
        //System.out.println("Random Integer Number - " + random.nextInt());
	}
	
	public int getCoins() {
		return Integer.parseInt(this.playerData.get("coins"));
	}
	
	public int getTurns() {
		return Integer.parseInt(this.playerData.get("turns"));
	}
	
	public int getSkin() {
		return Integer.parseInt(this.playerData.get("skin"));
	}
	
	public int getPosition() {
		return Integer.parseInt(this.playerData.get("mapTile"));
	}
	
	public String getMapName() {
		return this.playerData.get("map");
	}
	
	public void setPosition(int pos) {
		this.playerData.put("mapTile", String.valueOf(pos));
	}
	
	public void setPosition(int pos, Map thisMap) {
		setPosition(pos);
		thisMap.placeUser(pos);
	}
	
	public int getMoveCost(String moveType) {
		HashMap<String,Integer> moveCosts = new HashMap<>() {{
			put("direct",1);
			put("diag",3);
			put("none",-1);
		}};
		
		return moveCosts.get(moveType);
	}
	
	public HashMap<String, String>[] getUQuests() {
		int maxQuests = Quests.maxQuests();
		
		HashMap<String, String>[] userQuests = new HashMap[maxQuests];
		
		for (int questId = 0, userQuestCt = 0; questId < maxQuests; questId++) {
			HashMap<String, String> thisQuestInfo = Quests.getQuestInfo(questId); // get this quest's info
			
			if (thisQuestInfo!=null) { // quest exists in our database
				String userQuestData = playerData.get("quests_"+questId);
				if (userQuestData==null || userQuestData.equals("")) { // create quest data if it doesn't exist in user data
					System.out.println("noquest");
					//playerData.put("quests_"+questId, "0");
					setQuestStatus(questId,"0");
				} else if (!userQuestData.equals("claimable") && userQuestData.equals("1")) { // user has data on quest					
					
					//String questLastCompleted = userQuestData;
					String questObject = thisQuestInfo.get("Object");
					
					String convert = String.valueOf(questId);
					HashMap<String, String> userQuestInfo = new HashMap<>() {{
						put("questId",convert);
						put("object",questObject);
					}};
					
					userQuests[userQuestCt] = userQuestInfo;
					userQuestCt++;
					
					System.out.println("Omigawhd");
				} else if (userQuestData.equals("0")) {
					//System.out.println("QuestID: "+questId);
					//System.out.println("\t"+(!userQuestData.equals("claimable")));
					//System.out.println("\t"+userQuestData.equals("1"));
					//System.out.println((!userQuestData.equals("claimable") && userQuestData.equals("1")));
					//System.out.println("\t"+userQuestData+"\n");
				}
				
			} else { // quest doesn't exist, so remove broken data from user...
				
			}
		}
		
		return userQuests;
	}
	
	public HashMap<String, String>[] getCQuests() {
		int maxQuests = Quests.maxQuests();
		
		HashMap<String, String>[] userClaimableQuests = new HashMap[maxQuests];
		
		for (int questId = 0, userQuestCt = 0; questId < maxQuests; questId++) {
			HashMap<String, String> thisQuestInfo = Quests.getQuestInfo(questId); // get this quest's info
			
			if (thisQuestInfo!=null) { // quest exists in our database
				String userQuestData = playerData.get("quests_"+questId);
				if (userQuestData==null || userQuestData.equals("")) { // create quest data if it doesn't exist in user data
					System.out.println("noquest");
					setQuestStatus(questId,"0");
				} else if (userQuestData.equals("claimable")) { // user has data on quest					
					
					//String questLastCompleted = userQuestData;
					String questObject = thisQuestInfo.get("Object");
					
					String convert = String.valueOf(questId);
					HashMap<String, String> userQuestInfo = new HashMap<>() {{
						put("questId",convert);
						put("object",questObject);
					}};
					
					userClaimableQuests[userQuestCt] = userQuestInfo;
					userQuestCt++;
				}
				
			} else { // quest doesn't exist, so remove broken data from user...
				
			}
		}
		
		return userClaimableQuests;
	}
	
	public String getMoveType(int moveToTile, Map thisMap) {
		int currentTileId = this.getPosition();
		
		final int maxTiles = thisMap.getMaxTiles();
		final int tilesPerRow = (int) Math.sqrt(maxTiles);
		final int numRows = maxTiles / tilesPerRow;
		
		//int 
		// find the row the user is in
		for (int rowId = 1; rowId <= numRows; rowId++) {
			int lastTileIdInRow = (rowId * tilesPerRow) - 1;
			int firstTileIdInRow = (lastTileIdInRow - tilesPerRow) + 1;
			System.out.printf(
					"Row ID: %d\n\tLast Tile ID in Row: %d\n\tFirst Tile ID in Row: %d\n\tCurrent Tile: %d\n\tMove To Tile: %d\n",
					rowId,
					lastTileIdInRow,
					firstTileIdInRow,
					currentTileId,
					moveToTile
					);
			
			HashMap<String, Integer> potentialTiles = new HashMap<>() {{
				put("left",currentTileId - 1);
				put("right",currentTileId + 1);
				put("up",(currentTileId - tilesPerRow));
				put("down",(currentTileId + tilesPerRow));
			}};
			
			potentialTiles.put("diagUpLeft", potentialTiles.get("up") - 1);
			potentialTiles.put("diagUpRight", potentialTiles.get("up") + 1);
			potentialTiles.put("diagDownLeft", potentialTiles.get("down") - 1);
			potentialTiles.put("diagDownRight", potentialTiles.get("down") + 1);
			
			
			if (currentTileId >= firstTileIdInRow && currentTileId <= lastTileIdInRow) { // user is in this row
				System.out.printf(
						"\n\tUSER IN ROW\n\t\tTile To Left: %d\n\t\tTile to Right: %d\n\t\tTile Upwards: %d\n\t\tTile Downwards: %d\n"
						+"\n\t\tTile Diag Up Left: %d\n\t\tTile Diag Up Right: %d\n\t\tTile Diag Down Left: %d\n\t\tDiag Down Right: %d\n",
						potentialTiles.get("left"), //currentTileId - 1,
 						potentialTiles.get("right"), //currentTileId + 1,
						potentialTiles.get("up"), //(currentTileId - tilesPerRow),
						potentialTiles.get("down"), //(currentTileId + tilesPerRow),
						potentialTiles.get("diagUpLeft"), //(currentTileId - tilesPerRow) - 1,
						potentialTiles.get("diagUpRight"), //(currentTileId - tilesPerRow) + 1,
						potentialTiles.get("diagDownLeft"), //(currentTileId + tilesPerRow) - 1,
						potentialTiles.get("diagDownRight") //(currentTileId + tilesPerRow) + 1
						);
				boolean higherThanMin = moveToTile >= firstTileIdInRow;
				boolean lowerThanMax = moveToTile <= lastTileIdInRow;
				if (higherThanMin && lowerThanMax) { // user wants to move to another tile in this row
					System.out.println("\n================== USER WANTS TO MOVE IN SAME ROW");
					System.out.printf("\t\t\t%d %d %d\n\t\t\t%b %b\n",
							firstTileIdInRow,
							lastTileIdInRow,
							moveToTile,
							higherThanMin,
							lowerThanMax
							);
					if (potentialTiles.get("left") == moveToTile || potentialTiles.get("right") == moveToTile) { // user wants to move to direct left or direct right
						System.out.println("\n================== USER WANTS TO MOVE LEFT OR RIGHT");
						return "direct";
					}
				} else if (potentialTiles.get("up") == moveToTile || potentialTiles.get("down") == moveToTile) { // user wants to move up or down
					System.out.println("\n================== USER WANTS TO MOVE UP OR DOWN");
					return "direct";
				} else {
					System.out.println("\n================== USER WANTS TO MOVE OTHERWISE");
					int previousLastIdInRow = ((rowId-1) * tilesPerRow) - 1;
					int previousFirstIdInRow = (previousLastIdInRow - tilesPerRow) + 1;
					
					int nextLastIdInRow = ((rowId+1) * tilesPerRow) - 1;
					int nextFirstIdInRow = (nextLastIdInRow - tilesPerRow) + 1;
					
					System.out.printf("\n\tPrevious First Id: %d\n\tPrevious Last Idd: %d\n\tNext First Id: %d\n\tNext Last Id: %d\n",
							previousFirstIdInRow,
							previousLastIdInRow,
							nextFirstIdInRow,
							nextLastIdInRow
							);
					
					if (moveToTile >= previousFirstIdInRow && moveToTile <= previousLastIdInRow) {
						System.out.println("\n================= USER WANTS TO MOVE DIAG UP");
						if (moveToTile == potentialTiles.get("diagUpRight") || moveToTile == potentialTiles.get("diagUpLeft")) {
							return "diag";
						}
					} else if (moveToTile >= nextFirstIdInRow && moveToTile <= nextLastIdInRow) {
						System.out.println("\n================= USER WANTS TO MOVE DIAG DOWN");
						if (moveToTile == potentialTiles.get("diagDownRight") || moveToTile == potentialTiles.get("diagDownLeft")) {
							return "diag";
						}
					}
				}
			}
		}		
		
		return "none";
	}
	
	public void giveTurn(int turns) {
		this.playerData.put(
					"turns",
					String.valueOf(
								this.getTurns() + ((turns!=0) ? turns : 1)
							)
				);
	}
	
	public void giveHealth(int health) {
		int currentHealth = Integer.valueOf(this.playerData.get("health"));
		currentHealth += health;
		
		final int MAX_HEALTH = this.getMaxHealth();
		this.playerData.put(
				"health",
				String.valueOf(
						currentHealth < 0 ? 0 : (
								currentHealth > MAX_HEALTH ? MAX_HEALTH : currentHealth
							)
					)
				);
	}
	
	public void giveEXP(int exp) {
		int currentEXP = Integer.valueOf(this.playerData.get("exp"));
		currentEXP += exp;
		
		final int MAX_HEALTH = this.getMaxHealth();
		this.playerData.put(
				"exp",
				String.valueOf(
						currentEXP < 0 ? 0 : (
								currentEXP > this.getMaxEXP(99) ? this.getMaxEXP(99) : currentEXP
							)
					)
				);
	}
	
	public int getMaxHealth() {
		//System.out.println("Max Health (S):"+this.playerData.get("max_health"));
		//System.out.println("Max Health: "+Integer.valueOf(this.playerData.get("max_health")));
		return Integer.valueOf(this.playerData.get("max_health"));
	}
	
	
	public int getHealth() {
		return Integer.valueOf(this.playerData.get("health"));
	}
	
	public int getEXP() {
		return Integer.valueOf(this.playerData.get("exp"));
	}
	
	public int getLevel() {
		for (int level = 0; level < MAX_LEVEL; level++) {
			int currentEXP = this.getEXP();
			int levelMaxEXP = this.getMaxEXP(level);
			
			System.out.println("+++I: "+level+" ["+(currentEXP >= levelMaxEXP)+"]");
			System.out.println("\t\t"+currentEXP+"/"+levelMaxEXP);
			
			if (currentEXP < levelMaxEXP) {
				
				System.out.println("Our current level is "+level+" with exp of "+currentEXP+"/"+levelMaxEXP);
				
				return level;
			}
		}
		return 0;
	}
	
	 public boolean isQuestObject(String objectName) { // text if an object is a quest object
		  HashMap<String, String>[] userQuests = getUQuests(); // 
		  
		  System.out.println("Searching unclaimed quests for quest object ["+userQuests.length+"]");
		  userQuests = getUQuests(); // 
		  for (int userQuestId = 0; userQuestId < userQuests.length; userQuestId++) {
			  HashMap<String,String> thisUserQuest = userQuests[userQuestId];
			 System.out.println("\t"+userQuestId+"//"+userQuests.length);
			 if (thisUserQuest!=null) {
				 System.out.println("\t\tQuest info not null");
				 System.out.println(thisUserQuest);
				  int questId = Integer.parseInt(thisUserQuest.get("questId"));
				  String thisObject = thisUserQuest.get("object");
				  
				  if (objectName.equals(thisObject)) { return true; }
			 }
		  }
		  return false;
	  }
	 
	 public static enum interactStates { HEALTH, SUCCESS };
	 
	 public interactStates doMapInteraction(Map thisMap, int mapTileId) {
		 String thisTileToken = thisMap.getTileToken(mapTileId);
		 String thisTokenType = MapTokens.getTokenType(thisTileToken);
		 ArrayList<Integer> restrictedTileIds;
		 
		 if (this.getHealth() <= 0) { return interactStates.HEALTH; }
		 
		 this.giveTurn(-1);
		 
		 switch(thisTokenType) {
			case "edible":
				
				HashMap<String,String> tokenEffects = MapTokens.getTokenEffects(thisTileToken);
				if (tokenEffects!=null) {
					for (String effect : tokenEffects.keySet()) { // iterate through each effect of this token
						int effectValue = Integer.valueOf(tokenEffects.get(effect)); // value of effect
						
						switch(effect) {
							case "heal":
								this.giveHealth(effectValue);
							case "transform":
								
							default:
								//
						}
					}
				}
				
				thisMap.placeToken(mapTileId,"none");
				
				restrictedTileIds = new ArrayList<>();
				restrictedTileIds.add(mapTileId);
				thisMap.rollDiceFillMap(restrictedTileIds);
				
				break;
			case "enemy":
				this.giveHealth(-MapTokens.getTokenAttack(thisTileToken));
				this.giveEXP(MapTokens.getTokenEXP(thisTileToken));
				this.giveTurn(MapTokens.getTurnReward(thisTileToken));
				
				thisMap.placeToken(mapTileId,"none");
				
				restrictedTileIds = new ArrayList<>();
				restrictedTileIds.add(mapTileId);
				thisMap.rollDiceFillMap(restrictedTileIds);
				
				break;
			case "foliage":
				thisMap.placeToken(mapTileId,"none");
				
				this.giveEXP(MapTokens.getTokenEXP(thisTileToken));
				
				restrictedTileIds = new ArrayList<>();
				restrictedTileIds.add(mapTileId);
				thisMap.rollDiceFillMap(restrictedTileIds);
				break;
			default:
				//
			
		}
		 System.out.println("Current Health: "+this.getHealth());
		 return interactStates.SUCCESS;
	 }
}
