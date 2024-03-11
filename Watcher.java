/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package earthquakenotificationsystem;
//Melih Avcı 64220023
//This file includes Watcher class and WatcherList class
public class Watcher {
    private int time;
    private String name;
    private double latitude;
    private double longitude;
    private Watcher prev;
    private Watcher next;
   
    //Getters and setters
    public int getTime() {
        return time;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getName() {
        return name;
    }
    public Watcher getNext(){
        return next;
    }
    public Watcher getPrev() {
        return prev;
    }
    public void setNext(Watcher next) {
        this.next = next;
    }
    public void setPrev(Watcher prev) {
        this.prev = prev;
    }
    
    
    //Constructor
    public Watcher(int time, String name, double latitude, double longitude) {
        this.time=time;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prev = null;
        this.next = null;
        
    }
   
}
//Watcher list class
class WatcherList {
    private Watcher head;
    private Watcher tail;
    private Watcher current;

    public WatcherList() {
        this.head = null;
        this.tail = null;
        this.current=null;
        
        
    }
    //Getters and setters
    protected Watcher getHead() {
        return head;
    }
    private void setHead(Watcher head) {
        this.head = head;
    }
    protected Watcher getCurrent() {
        return current;
    }
    protected void setCurrent(Watcher current) {
        this.current = current;
    }
    private Watcher getTail() {
        return tail;
    }
    private void setTail(Watcher tail) {
        this.tail = tail;
    }
    
    //Adds a watcher to the list and stores the information in given order
    public void addWatcher(int time, double latitude, double longitude,String name) {
        Watcher newWatcher = new Watcher(time,name, latitude, longitude);
        if (getHead() == null) {
            setHead(newWatcher);
            setCurrent(getHead());
            setTail(newWatcher);
        } else {
            setCurrent(getHead().getNext());
            getTail().setNext(newWatcher);
            newWatcher.setPrev(getTail());
            setTail(newWatcher);
        }
    }
    //Deletes a watcher from the list
    public void deleteWatcher(String name) {
        Watcher current = getHead();

        while (current != null) {
            if (current.getName().equals(name)) {
                if (current.getPrev() != null) {
                    current.getPrev().setNext( current.getNext());
                } else {
                    setHead(current.getNext());
                }

                if (current.getNext() != null) {
                    current.getNext().setPrev(current.getPrev()) ; 
                } else {
                    setTail(current.getPrev()) ;
                }

                return; // Watcher found and removed
            }
            current = current.getNext();
        }
    } 
        //returns the current size of the list
        public int getSize() {
        int size = 0;
        Watcher current =getHead();

        while (current != null) {
            size++;
            current = current.getNext();
        }
 
        return size;
    }
}




