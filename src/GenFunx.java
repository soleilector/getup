import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class GenFunx {
	
	public static Random random = new Random() {{
		setSeed(getCurrentTick());
	}};

	public static long getCurrentTick() {
		return System.currentTimeMillis();
	}
	
	public static int randomInRange_Random(int start, int end) {
	   // Random random = new Random();
	    //random.setSeed(getCurrentTick());
		//System.out.printf("Start: %d -++- End: %d\n", start, end);
	    int number = random.nextInt((end - start) + 1) + start; // see explanation below
	    return number;
	}
	
	public static int[] shuffleArray(int[] ar) {
		for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = random.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
		return ar;
	}
	
	public static ArrayList<Integer> shuffleArray(ArrayList<Integer> ar){
		for (int i = ar.size() - 1; i > 0; i--)
	    {
	      int index = random.nextInt(i + 1);
	      // Simple swap
	      int a = ar.get(index);
	      //ar[index] = ar[i];
	      ar.set(index, ar.get(i));
	      //ar[i] = a;
	      ar.set(i, a);
	    }
		return ar;
	}
	
	public static ArrayList<String> shuffleArrayStr(ArrayList<String> ar){
		for (int i = ar.size() - 1; i > 0; i--)
	    {
	      int index = random.nextInt(i + 1);
	      // Simple swap
	      String a = ar.get(index);
	      //ar[index] = ar[i];
	      ar.set(index, ar.get(i));
	      //ar[i] = a;
	      ar.set(i, a);
	    }
		return ar;
	}
	
	public static int countItems(String[] arr) {
		int ct = 0;
		
		for (String s : arr) { ct = ct + ((s!=null) ? 1 : 0); };
		
		return ct;
	}
	
	public static int countItems(int[] arr) {
		int ct = 0;
		
		for (int i : arr) { ct++; }; // 0 is default for null int, so dunno if actually 0 or a garbage value?
		
		return ct;
	}
	
	public static ArrayList<String> keysAsArray(HashMap<String,String> hash) {
		
		Set<String> keys = hash.keySet();
		String[] keysArr = keys.toArray(new String[keys.size()]);
		
		ArrayList<String> finalKeysArr = new ArrayList<>();
		
		for (String v : keysArr) {
			System.out.println("thiskey: "+v);
			finalKeysArr.add(v);
		}
		
		return finalKeysArr;
	}
	
	
	public static String[] nestHashKeysAsArray(HashMap<String, HashMap<String,String>> hash) {
		int ct = 0;
		String[] arr = new String[hash.size()];
		for (String key : hash.keySet()) {
			arr[ct] = key;
			ct++;
		}
		
		return arr;
	}
}
