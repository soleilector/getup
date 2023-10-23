import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Map {
	final public static int CHANCE_TO_GEN = 100; // chance to generate a token at all
	final public static HashMap<String,Integer> TOKEN_TYPE_GEN_CHANCES = new HashMap<>() {{ // chances of generating a token in the rarity category
		put("legendary",1);
		put("srare",2);
		put("vrare",5);
		put("rare",10);
		put("uncommon",30);
		put("common",100); // <100> value for rarity category always allows a token to generate
	}}; 
	final public static String[] RARITY_PARSE_ORDER = {"rare","uncommon","common"}; // the order in which to parse rarity categories
	final public static int GEN_TOKEN_MAX_TIMES = 2;
	private static HashMap<String,String[]> grasslandTokens = new HashMap<>() {{ // the tokens available for each rarity category
		put(
				"common",
				new String[] {"thornbush"}
			);
		put(
				"uncommon",
				new String[] {"apple"}
			);
		put(
				"rare",
				new String[] {"dragon"}	
			);
	}};
	
	private final int MAX_TILES; // max tiles to generate for this new map
	private JButton[] mapTiles; // stores tiles buttons to serve as map tiles
	private String[] mapTokens; // stores tokens
	
	private String mapType = "grassland";
	
	private HashMap<String,HashMap<String,String[]>> mapObjects = new HashMap<>() {{
		put("grassland",grasslandTokens); // stores the possible tokens that can generate for this map
	}};
	
	Map(int maxTiles){ // constructor
		this.MAX_TILES = maxTiles;
		this.mapTokens = new String[maxTiles]; // stores token information about map
		this.mapTiles = new JButton[maxTiles];
	}
	
	public int getMaxTiles() {
		return this.MAX_TILES;
	}
	
	public void generateMapTiles(ActionListener onClick, JPanel mapContainer) { // just for generating mapTiles
		int eachRow = (int) Math.sqrt(this.MAX_TILES);
		int mapTileCount = 0;
		
		final int MAP_SIZE = mapContainer.getWidth();
		final int TILE_WIDTH = MAP_SIZE / eachRow; 
		System.out.println("\n------------Tile Size:"+TILE_WIDTH);
		
		for (int mapRow = 0; mapRow < eachRow; mapRow++) {
			for (int mapCol = 0; mapCol < eachRow; mapCol++) {
				// create tile
				JButton thisTile = new JButton() {
					{
						setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
						setPreferredSize(new Dimension(TILE_WIDTH, TILE_WIDTH));
						addActionListener(onClick);
						setBorder(null);
						setFocusPainted(false);
					}
				};

				thisTile.setName("Tile_" + String.valueOf(mapTileCount));
				mapContainer.add(thisTile);
				
				this.addTile(thisTile);

				mapTileCount++;

			}
		}
	}
	
	public void setMapType(String mapName) { // set the map's type
		this.mapType = mapName;
	}
	
	public String getMapType() {
		return this.mapType;
	}
	
	public int getCurrentNumOfTiles() { // current number of tiles
		int ct = 0; // count of tiles
		for (JButton i : getTiles()) { // for each jbutton
			ct = ct + ((i!=null) ? 1 : 0); // add count
		}
		return ct; // return count
	}
	
	public void addTile(JButton thisTile) { // add another tile
		System.out.println("\n\tsetting tile to :"+(getCurrentNumOfTiles()));
		mapTiles[getCurrentNumOfTiles()] = thisTile;
	}
	
	public void placeUser(int tileId) { // always sets this tile to user
		for (int thisTileId = 0; thisTileId < this.MAX_TILES; thisTileId++) {
			String thisToken = this.mapTokens[thisTileId];
			if (thisToken != null && thisToken.equals("user")) {
				this.mapTokens[thisTileId] = "none";
			}
		}
		this.mapTokens[tileId] = "user";
	}
	
	public boolean isTileFree(int tileId) { // is this specific tile free of any tokens
		//System.out.println("Tile Id#"+tileId+" has a token of "+getTileToken(tileId)+"\n\tTile is null?"+(getTileToken(tileId)==null));
		String thisToken = getTileToken(tileId);
		if (thisToken == null || thisToken.equals("none")) {
			//System.out.println("Returning "+thisToken+" as free");
			return true;
		}
		return false;
	}
	
	public void placeToken(int tileId, String token) {
		this.mapTokens[tileId] = token;
	}
	
	public boolean canPlaceUser(int tileId) { // check if tiles are taken away
		return false;
	}
	
	public JButton[] getTiles() {
		return mapTiles;
	}
	
	public JButton getTile(int tileId) {
		return mapTiles[tileId];
	}
	
	public String getTileToken(int tileId) {
		return mapTokens[tileId];
	}
	
	public String[] getMapTokens() {
		return mapTokens;
	}
	
	public ArrayList<Integer> getFreeTiles() { // not occupied by token or player
		ArrayList<Integer> freeTiles = new ArrayList<>();
		
		for (int tileId = 0; tileId < this.MAX_TILES; tileId++) { // for each tile
			if (isTileFree(tileId)) { // if nothing is on this tile...
				freeTiles.add(tileId); // add to free tiles list
				//System.out.println("Adding tile to free tiles: "+tileId+"/"+getTileToken(tileId));
			}
		}
		
		//System.out.println("Free tiles:"+freeTiles);
		
		return freeTiles;
	}
	
	public String generateMapToken(String mapName) { // FUTURE: give tile id so we can place certain spawns
		HashMap<String,String[]> mapTokens = mapObjects.get(mapName);
		
		if (mapTokens!=null) { // if there are tokens for this map
			for (int tokenRarityCatId = 0; tokenRarityCatId < RARITY_PARSE_ORDER.length;tokenRarityCatId++) {
				String rarity = RARITY_PARSE_ORDER[tokenRarityCatId];
				
				int rarityGenChance = TOKEN_TYPE_GEN_CHANCES.get(rarity);
				int randGenToken = GenFunx.randomInRange_Random(0, 100);
				//System.out.println("Parsing rarity tokens "+rarity+" under rarity "+rarityGenChance+" for rand "+randGenToken);
				
				if (randGenToken <= rarityGenChance) { // we can generate
					// all the code that is actually necessary to generate a map token
					String[] tokensArr = mapTokens.get(rarity); //GenFunx.nestHashKeysAsArray(mapTokens); // get map token names as array
					
					ArrayList<String> thisMapTokens = new ArrayList<>();
					for (String thisToken : tokensArr) {
						thisMapTokens.add(thisToken); // add this token to the array list
					}
					thisMapTokens = GenFunx.shuffleArrayStr(thisMapTokens);
					
					int randTokenId = GenFunx.randomInRange_Random(0,thisMapTokens.size()-1);
					
					return thisMapTokens.get(randTokenId);
				}
			}
		}
		
		return null;
	}
	
	/*
	public void fillFreeTiles() {
		ArrayList<Integer> freeMapTiles = this.getFreeTiles();
		
		int chanceToGenToken = 1;
		int rolledChance = GenFunx.randomInRange_Random(0, 100);
		
		if (rolledChance > chanceToGenToken) {
			System.out.println("Rolled chance unsuccessful: "+rolledChance);
		} else {
			for (int tileId : freeMapTiles) {
				String token = this.generateMapToken(mapType); // generate a token for each 
				if (token!=null) { // generated a token
					this.placeToken(tileId, token); // place token down
					System.out.println("Filled a free map tile");
				}
			}
		}
	}
	*/
	
	public void fillOneFreeTile() {
		ArrayList<Integer> freeMapTiles = this.getFreeTiles();
		int freeMapTileSize = freeMapTiles.size();
		
		int[] shuffledIds = new int[freeMapTileSize];
		for (int x=0;x<freeMapTileSize;x++) {
			int thisId = freeMapTiles.get(x);
			//System.out.println(mapTokens[thisId]);
			shuffledIds[x] = thisId;
			};
		shuffledIds = GenFunx.shuffleArray(shuffledIds);
		//System.out.println("Free tiles when filling: "+freeMapTiles);
		
		int tileId = GenFunx.randomInRange_Random(0, freeMapTileSize-1);
		int trueTileId = shuffledIds[tileId];
		
		//System.out.println("Filling tile: "+tileId+" of true Id "+trueTileId+" which has token: "+mapTokens[tileId]);
		String token = this.generateMapToken(mapType); // generate a token for each 
		if (token!=null) { // generated a token
			this.placeToken(trueTileId, token); // place token down
			//System.out.println("Filled a free map tile");
		} else {
			//System.out.println("Failed to find token: "+token);
		}
	}
	
	public void fillOneFreeTile(ArrayList<Integer> restriction) {
		ArrayList<Integer> freeMapTiles = this.getFreeTiles();
		// remove any restricted tiles from arraylist
		for (int x =0; x<freeMapTiles.size();x++) {
			for (int y =0; y < restriction.size(); y++) {
				if (freeMapTiles.get(x)==restriction.get(y)) {
					freeMapTiles.remove(x);
				}
			}
		}
		
		// continue
		int freeMapTileSize = freeMapTiles.size();
		
		/*
		int[] shuffledIds = new int[freeMapTileSize];
		for (int x=0;x<freeMapTileSize;x++) {
			int thisId = freeMapTiles.get(x);
			System.out.println(mapTokens[thisId]);
			shuffledIds[x] = thisId;
			};
		*/
		if (freeMapTileSize > 0) { // if there are more than 0 free tiles
			ArrayList<Integer> shuffledIds = GenFunx.shuffleArray(freeMapTiles);
			//System.out.println("Free tiles when filling: "+freeMapTiles);
			
			int tileId = GenFunx.randomInRange_Random(0, freeMapTileSize-1);
			int trueTileId = shuffledIds.get(tileId); //shuffledIds[tileId];
			
			//System.out.println("Filling tile: "+tileId+" of true Id "+trueTileId+" which has token: "+mapTokens[tileId]);
			String token = this.generateMapToken(mapType); // generate a token for each 
			if (token!=null) { // generated a token
				this.placeToken(trueTileId, token); // place token down
				//System.out.println("Filled a free map tile");
			} else {
				//System.out.println("Failed to find token: "+token);
			}
		}
	}
	
	public void rollDiceFillMap() {
		int shouldGen = GenFunx.randomInRange_Random(0, 100);
		System.out.println("Should gen: "+shouldGen);
		if (shouldGen <= Map.CHANCE_TO_GEN) {
			//System.out.println("Gonna generate a token...");
			fillOneFreeTile();
		}
	}
	
	public void rollDiceFillMap(ArrayList<Integer> restrictedIds) {
		int shouldGen = GenFunx.randomInRange_Random(0, 100);
		System.out.println("Should gen: "+shouldGen);
		if (shouldGen <= Map.CHANCE_TO_GEN) {
			//System.out.println("Gonna generate a token...");
			fillOneFreeTile(restrictedIds);
		}
	}
	
	public int fillMap(int moveSeshTokenGens) {
		if (moveSeshTokenGens <= Map.CHANCE_TO_GEN) {
			int shouldGen = GenFunx.randomInRange_Random(0, 100);
			//System.out.println("Should gen: "+shouldGen);
			if (shouldGen <= Map.CHANCE_TO_GEN) {
				//System.out.println("Gonna generate a token...");
				fillOneFreeTile();
			}
		}
		moveSeshTokenGens++;
		return moveSeshTokenGens;
	}
	
	public int fillMap(int moveSeshTokenGens, ArrayList<Integer> restrictedIds) {
		if (moveSeshTokenGens <= Map.CHANCE_TO_GEN) {
			int shouldGen = GenFunx.randomInRange_Random(0, 100);
			System.out.println("Should gen: "+shouldGen);
			if (shouldGen <= Map.CHANCE_TO_GEN) {
				//System.out.println("Gonna generate a token...");
				fillOneFreeTile(restrictedIds);
			}
		}
		moveSeshTokenGens++;
		return moveSeshTokenGens;
	}
}
