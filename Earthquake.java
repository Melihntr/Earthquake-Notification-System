/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package earthquakenotificationsystem;
//Melih Avcı 64220023
//This file includes Earthquake class and EarthquakeList class
 class Earthquake {
    private int id;
    private int time;
    private String place;
    private String coordinates;
    private double magnitude;
    private Earthquake prev;
    private Earthquake next;
    
    //Getters and setters
    public int getTime() {
        return time;
    }
    public int getId() {
        return id;
    }
    public Double getMagnitude() {
        return magnitude;
    }
    public String getPlace(){ 
        return place;
    }
    public Earthquake getPrev(){
        return prev;
    }
    public Earthquake getNext(){
        return next;
    }
    public void setNext(Earthquake next) {
        this.next = next;
    }
    public void setPrev(Earthquake prev) {
        this.prev = prev;
    }
    //Constructor
    public Earthquake(int id, int time, String place, String coordinates, double magnitude) {
        this.id = id;
        this.time = time;
        this.place = place;
        this.coordinates = coordinates;
        this.magnitude = magnitude;
        this.prev = null;
        this.next = null;
        
    }
}
//Earthquake list class
 class EarthquakeList {
    protected Earthquake head;
    private Earthquake tail;
    
    protected Earthquake getHead() {
        return head;
    }
    private void setHead(Earthquake head) {
        this.head = head;
    }
    private Earthquake getTail() {
        return tail;
    }
    private void setTail(Earthquake tail) {
        this.tail = tail;
    }

    public EarthquakeList() {
        head = null;
        tail = null;
    }
    //Adds a new earthquake record to the list
    public void addRecord(int id, int time, String place, String coordinates, double magnitude) {
        Earthquake newRecord = new Earthquake(id, time, place, coordinates, magnitude);
        if (getHead()== null) {
            setHead(newRecord);
            setTail(newRecord);
        } else {
            getTail().setNext(newRecord);
            newRecord.setPrev(getTail());
            setTail(newRecord);
        }
    }
    //Removes earthquake from list
    public void removeEarthquakeByTime(int earthquakeTime) {
        Earthquake current = getHead();

        while (current != null) {
            if (current.getTime()==earthquakeTime) {
                if (current.getPrev()!= null) {
                    current.getPrev().setNext(current.getNext());
                } else {
                    setHead(current.getNext());
                }

                if (current.getNext() != null) {
                    current.getNext().setPrev(current.getPrev());
                } else {
                    setTail(current.getPrev());
                }
                return; 
            }
            current = current.getNext();
        }
    }
    //Finds the earthquake with largest magnitude in the list 
    //Deletion is done by an another loop in main it's not in here
    public Earthquake findLargestMagnitude() {
        if (getHead() == null) {
            System.out.println("No record on list");
            return null;
        }

        Earthquake current = getHead();
        Earthquake largestMagnitudeEarthquake = current;

        while (current!= null) {
            if (current.getMagnitude() > largestMagnitudeEarthquake.getMagnitude()) {
                largestMagnitudeEarthquake = current;
            }
            current = current.getNext();
        }
        
        System.out.println("Largest earthquake in the past 6 hours: ");
        System.out.println("Magnitude " +largestMagnitudeEarthquake.getMagnitude() + " at " +largestMagnitudeEarthquake.getPlace());
        return largestMagnitudeEarthquake;//returns the largest earthquake
    }
       
}

