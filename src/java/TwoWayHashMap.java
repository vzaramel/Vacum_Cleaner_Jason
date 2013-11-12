import java.util.Hashtable;
import java.util.Map;


public class TwoWayHashMap{

  private Map<String,Integer> forward = new Hashtable<String, Integer>();
  private Map<Integer,String> backward = new Hashtable<Integer, String>();

  public synchronized void add(String key, Integer value) {
    forward.put(key, value);
    backward.put(value, key);
  }

  public synchronized Integer LiteralToInt(String key) {
    return forward.get(key);
  }

  public synchronized String IntToLiteral(Integer key) {
    return backward.get(key);
  }
}
