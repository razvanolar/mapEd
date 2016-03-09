package mapEditor.application.main_part.app_utils.data_types;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 09.03.2016.
 */
public class CustomMap<K, V> {

  class Pair {
    K key;
    V value;

    public Pair(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }

  private List<Pair> values;

  public CustomMap() {
    this.values = new ArrayList<>();
  }

  public boolean put(K key, V value) {
    if (key == null || value == null)
      return false;
    Pair pair = null;
    for (Pair p : values) {
      if (p.key.equals(key)) {
        pair = p;
        break;
      }
    }
    if (pair != null) {
      pair.value = value;
    } else {
      return values.add(new Pair(key, value));
    }
    return true;
  }

  public V get(K key) {
    if (key == null)
      return null;

    for (Pair pair : values) {
      if (pair.key.equals(key))
        return pair.value;
    }
    return null;
  }

  public boolean contains(K key) {
    if (key == null)
      return false;

    for (Pair pair : values) {
      if (pair.key.equals(key))
        return true;
    }
    return false;
  }

  public boolean remove(K key) {
    if (key == null)
      return false;

    Pair pair = null;
    for (Pair p : values) {
      if (p.key.equals(key)) {
        pair = p;
        break;
      }
    }

    return pair != null && values.remove(pair);
  }

  public List<K> keys() {
    List<K> keys = new ArrayList<>();
    for (Pair p : values)
      keys.add(p.key);
    return keys;
  }

  public boolean isEmpty() {
    return values.isEmpty();
  }
}
