import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

public class Quests {
	private static HashMap<Integer,HashMap<String,String>> quests = new HashMap<>(){{
		put(0,
				new HashMap<String, String>(){{
					put("Object","Dog");
					put("Turns","1");
				}}
			);
		put(1, new HashMap<String,String>(){{
				put("Object","Cat");
				put("Turns","1");
			}}
		);
		put(2, new HashMap<String,String>(){{
						put("Object","Person");
						put("Turns","1");
					}}
				);
		put(3, new HashMap<String,String>(){{
			put("Object","Fish");
			put("Turns","1");
		}}
	);
		put(4, new HashMap<String,String>(){{
			put("Object","Snake");
			put("Turns","1");
		}}
	);
		put(5, new HashMap<String,String>(){{
			put("Object","Fish");
			put("Turns","1");
		}}
	);
		put(6, new HashMap<String,String>(){{
			put("Object","Clock");
			put("Turns","1");
		}}
	);
		put(7, new HashMap<String,String>(){{
			put("Object","Bird");
			put("Turns","1");
		}}
	);
		put(8, new HashMap<String,String>(){{
			put("Object","Natural Landscape");
			put("Turns","1");
		}}
	);
		put(10, new HashMap<String,String>(){{
			put("Object","Flower");
			put("Turns","1");
		}}
	);
		put(11, new HashMap<String,String>(){{
						put("Object","Tree");
						put("Turns","1");
					}}
				);
		put(12, new HashMap<String,String>(){{
			put("Object","Handwriting");
			put("Turns","1");
		}}
	);
		put(13, new HashMap<String,String>(){{
			put("Object","Building");
			put("Turns","1");
		}}
	);
		put(
				14, 
				new HashMap<String,String>(){{
					put("Object","Taj Mahal");
					put("Turns","1");
				}}
			);
		put(
				14, 
				new HashMap<String,String>(){{
					put("Object","Great Pyramid of Giza");
					put("Turns","1");
				}}
			);
		put(
				15, 
				new HashMap<String,String>(){{
					put("Object","Collosseum");
					put("Turns","1");
				}}
			);
		put(
				16, 
				new HashMap<String,String>(){{
					put("Object","Van Gogh");
					put("Turns","1");
				}}
			);
		put(
				17, 
				new HashMap<String,String>(){{
					put("Object","Painting");
					put("Turns","1");
				}}
			);
		put(
				18, 
				new HashMap<String,String>(){{
					put("Object","Christ the Redeemer");
					put("Turns","1");
				}}
			);
		put(
				19, 
				new HashMap<String,String>(){{
					put("Object","Stonehenge");
					put("Turns","1");
				}}
			);
		put(
				20, 
				new HashMap<String,String>(){{
					put("Object","Waterfall");
					put("Turns","1");
				}}
			);
		put(
				21, 
				new HashMap<String,String>(){{
					put("Object","Temple of Artemis");
					put("Turns","1");
				}}
			);
		put(
				22, 
				new HashMap<String,String>(){{
					put("Object","Whale");
					put("Turns","1");
				}}
			);
		put(
				23, 
				new HashMap<String,String>(){{
					put("Object","Fox");
					put("Turns","1");
				}}
			);
		put(
				24, 
				new HashMap<String,String>(){{
					put("Object","Dolphin");
					put("Turns","1");
				}}
			);
		put(
				25, 
				new HashMap<String,String>(){{
					put("Object","Pattern");
					put("Turns","1");
				}}
			);
		put(
				26, 
				new HashMap<String,String>(){{
					put("Object","Fractal Art");
					put("Turns","1");
				}}
			);
	}};
	
	public static HashMap<Integer, HashMap<String,String>> getAllQuests() {
		return quests;
	}
	
	public static HashMap<String, String> getQuestInfo(String questName) { // if quest info
		
		for (int questId : quests.keySet()) {
			HashMap<String, String> thisQuestInfo = quests.get(questId);
			System.out.println("Evaluating object "+thisQuestInfo.get("Object"));
			if (thisQuestInfo.get("Object").equals(questName)) {
				thisQuestInfo.put("QuestId", String.valueOf(questId));
				return thisQuestInfo;
			}
		}
		
		return null;
	}
	
	public static HashMap<String, String> getQuestInfo(int questId) {
		HashMap<String, String> thisQuestInfo = quests.get(questId);
		if (thisQuestInfo!=null) {
			thisQuestInfo.put("QuestId", String.valueOf(questId));
		}
		//System.out.println("Quest["+questId+"]"+thisQuestInfo);
		return thisQuestInfo;
	}
	
	public static int maxQuests(){
		int ct = 0;
		for (int questId : quests.keySet()) {
			ct++;
		}
		return ct;
	}
	
	public static long getQuestSeed() {
		return (long) Math.floor(System.currentTimeMillis()/(1000)); // returns ms since 1970
	}

}
