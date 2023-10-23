import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class MapTokens {
	private static String TOKEN_EFFECT_PREFIX = "effect_";
	
	private static HashMap<String, String> tokenIcons = new HashMap<>() {{
		put("user","gerbil3.png");
		put("dragon","dragon.png");
		put("apple","apple.png");
		put("thornbush","thornbush.png");
	}};
	
	private static HashMap<String, Image> iconImages = new HashMap<>() {{
	}};
	
	private static HashMap<String,HashMap<String,String>> tokenInfo = new HashMap<>() {{
		put("apple",
				new HashMap<String,String>(){{
					put("type","edible");
					put(TOKEN_EFFECT_PREFIX+"heal","1");
				}}
			);
		put("dragon",
				new HashMap<String,String>(){{
					put("type","enemy");
					put("sociality","aggressive");
					put("attack","2");
					put("exp","40");
					put("turns","5");
				}}
		);
		put("thornbush",
				new HashMap<String,String>(){{
					put("type","foliage");
					put("exp","5");
				}}
			);
	}};
	
	public static Image getIconImage(String tokenName) {
		Image thisImage = iconImages.get(tokenName);
		
		if (thisImage!=null) { return thisImage; }
		
		for (String thisTokenName : tokenIcons.keySet()) {
			if (thisTokenName.equals(tokenName)) { // matches what we want
				String tokenURI = tokenIcons.get(thisTokenName);
				try {
					iconImages.put(tokenName, ImageIO.read(Main.class.getResource(tokenURI)));
					return iconImages.get(tokenName);
				} catch (Exception e) { return null; }
				
			}
		}
		
		return null;
	}
	
	public static HashMap<String,String> getTokenEffects(String tokenName) {
		HashMap<String, String> thisTokenInfo = getTokenInfo(tokenName);
		if (thisTokenInfo!=null) {
			System.out.println("This token has info... let's see if it has any effects...");
			HashMap<String, String> tokenEffects = new HashMap<>();
			
			for (String prop : thisTokenInfo.keySet()) {
				if (prop.contains(TOKEN_EFFECT_PREFIX)) {
					String effectName = prop.substring(TOKEN_EFFECT_PREFIX.length());
					System.out.println("Indexing effect "+effectName+"...");
					tokenEffects.put(effectName, thisTokenInfo.get(prop));
				}
			}
			
			return tokenEffects;
		}
		return null;
	}
	
	public static String getTokenType(String tokenName) {
		HashMap<String,String> thisTokenInfo = tokenInfo.get(tokenName);
		if (thisTokenInfo!=null) {
			return thisTokenInfo.get("type");
		}
		return null;
	}
	public static String getTokenSociality(String tokenName) {
		HashMap<String,String> thisTokenInfo = tokenInfo.get(tokenName);
		if (thisTokenInfo!=null) {
			return thisTokenInfo.get("sociality");
		}
		return null;
	}
	public static String getTokenHeal(String tokenName) {
		HashMap<String,String> thisTokenInfo = tokenInfo.get(tokenName);
		if (thisTokenInfo!=null) {
			return thisTokenInfo.get("heal");
		}
		return null;
	}
	
	public static int getTokenEXP(String tokenName) {
		HashMap<String,String> thisTokenInfo = tokenInfo.get(tokenName);
		if (thisTokenInfo!=null) {
			return Integer.valueOf(thisTokenInfo.get("exp"));
		}
		return 0;
	}
	
	public static int getTurnReward(String tokenName) {
		HashMap<String,String> thisTokenInfo = tokenInfo.get(tokenName);
		if (thisTokenInfo!=null) {
			return Integer.valueOf(thisTokenInfo.get("turns"));
		}
		return 0;
	}
	
	public static int getTokenAttack(String tokenName) {
		HashMap<String,String> thisTokenInfo = tokenInfo.get(tokenName);
		if (thisTokenInfo!=null) {
			return Integer.valueOf(thisTokenInfo.get("attack"));
		}
		return 0;
	}
	public static String getTokenHealth(String tokenName) {
		HashMap<String,String> thisTokenInfo = tokenInfo.get(tokenName);
		if (thisTokenInfo!=null) {
			return thisTokenInfo.get("health");
		}
		return null;
	}
	
	public static HashMap<String,String> getTokenInfo(String tokenName) {
		return tokenInfo.get(tokenName);
	}
	
}
