package src.com.revature.utils;

public class KeyNode <K, V> {
    final int hashValue;
    final K key;
    V value;
    KeyNode<K,V> next;

    KeyNode (K key, V value) {
        hashValue = hashCode();
        this.key = key;
        this.value = value;
    }
}
