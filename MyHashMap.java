import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
public class MyHashMap<K,V> implements Serializable{
	DLList<V>[] table;
	DLList<K> keyList;
	@SuppressWarnings("unchecked")
	public MyHashMap(){
		table = new DLList[10000];
		keyList = new DLList<K>();
	}

	public void put(K key,V value){
		int index = key.hashCode()%table.length;
		if(table[index] == null){
			table[index] = new DLList<V>();
			keyList.add(key);
		}
		table[index].add(value);
	}

	public DLList<V> getValues(K key){
		return table[key.hashCode()];
	}

	public DLList getKeys(){
		return keyList;
	}

	public String toString(){
		String str = "";
		for(int i=0; i<keyList.size(); i++){
			str += keyList.get(i).toString() + "-" +  table[keyList.get(i).hashCode()] ;
		}
		return str;
	}

	public void removeValue(K key, V value){
		table[key.hashCode()].remove(value);
	}

	public void removeAllValues(K key){
		table[key.hashCode()] = new DLList<V>();
		keyList.remove(key);
	}

	public int size(){
		return table.length;
	}
}