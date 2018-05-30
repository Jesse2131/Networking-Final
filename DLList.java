import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
public class DLList<E> implements Serializable{
    private Node<E> dummy;
    private int size;

    public DLList(){
        dummy = new Node<E>(null);
        dummy.setNext(null);
        dummy.setPrev(null);
        size = 0;
    }

    public Node<E> getNode(int ind){
        Node<E> current;
        if(ind < size/2){
            current=dummy.next();
            for(int j=0; j<ind; j++){
                current=current.next();
            }
            return current;
        }
        else{
            current=dummy.prev();
            for(int i=size-1; i>ind; i--){
                current = current.prev();
            }
            return current;
        }
    }
        
    public void add(E obj){
        Node<E> newNode = new Node<E>(obj);
        if(size==0){
            dummy.setNext(newNode);
            dummy.setPrev(newNode);
            newNode.setNext(null);
            newNode.setPrev(null);
            size++;
        }
        else{
            Node<E> current = dummy.prev();
            current.setNext(newNode);
            dummy.setPrev(newNode);
            newNode.setNext(dummy);
            newNode.setPrev(current);
            size++;
        }
    }

    public E get(int index){
        Node<E> current;
        if(index < size/2){
            current = dummy.next();
            for(int i=0; i<index; i++){
                current = current.next();
            }
            return current.get();
        }
        else{
            current = dummy.prev();
            for(int i=size-1; i>index; i--){
                current = current.prev();
            }
            return current.get();
        }
    }

    public void remove(E obj){
        Node<E> current = dummy.next();
        Node<E> previous = dummy.prev();
        if(current.get().equals(obj)){
            dummy.setNext(current.next());
            size--;
        }
        else{
            for(int i=0; i<size; i++){
                if(current.get().equals(obj)){
                    Node<E> temp = current.next();
                    temp.setPrev(previous);
                    previous.setNext(current.next());
                    break;
                }
                previous = current;
                current = current.next();
            }
            size--;
        }
    }

    public String toString(){
        String str = "";
        Node<E> current = dummy.next();
        for(int i=0; i<size; i++){
            str+=current.get();
            current = current.next();
        }
            return str;
    }

    public void set(int i, E obj){
        Node<E> newNode = getNode(i);
        newNode.setData(obj);
    }
    public int size(){
        return size;
    }
}