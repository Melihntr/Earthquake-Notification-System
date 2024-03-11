/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
//Melih Avcı 64220023
package earthquakenotificationsystem;

import java.io.File; 
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 
This program reads 2 files at the same time 
 
Stores them in DLL of their own

stores the information according to their time

notifies the watchers if they are close to the recent earthquakes

prints the earthquake with the miggest magnitude in the last 10 hours when asked

*/
public class EarthquakeNotificationSystem {
    private static int systemTime = 0;//time of my program
    private static int wTime = 0;//time for watcher list
    private static int eTime = 0;//time for earthquake list
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean t=true;
        
        WatcherList watcherList1= new WatcherList();//to store the watchers
        EarthquakeList earthquakeList1 = new EarthquakeList();//to store the earthquakes
        
        System.out.print("Enter the watcher file: ");//E:\\watcher-file.txt in my case
        String fileName1 = scanner.nextLine();
        System.out.print("Enter the earthquake file: ");//E:\\earthquake.xml in my case
        String fileName2 = scanner.nextLine();
        System.out.println(); 
        
        while (t==true) {
        watcherList1= loadWatcherData(fileName1,watcherList1,earthquakeList1);
        earthquakeList1 = loadEarthquakeData(fileName2,watcherList1,earthquakeList1);     
 
        //Increases the systemTime to read the next lines
        systemTime++;

             try {
                Thread.sleep(15); //to make the output look better
            } 
             catch (InterruptedException e) {
                e.printStackTrace();
            }
        //to exit the loop
        if(systemTime==200){
            t=false;
        }
        
           
        }
    
    }
    /*
    This method reads watcher-file line by line
    
    if time in the first line is bigger then our system time it doesn't get added to our list till the time comes
    
    if time in the given line is equal to our system time it gets drected to a if case to learn the action
    
    if action is add details get added to watcherList
    
    if action is remove the method checks if the watcher is in the list.If it is the watcher gets removed
    
    and the query largest prints the largest earthquake in last 6 hours
    */
    private static WatcherList loadWatcherData(String file,WatcherList watcherList, EarthquakeList earthquakeList)
    {
        try (Scanner scanner = new Scanner(new File(file))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                wTime = Integer.parseInt(parts[0]);
                //checks if the time is equal to take the action
                if(systemTime==wTime) {
                String action = parts[1];
                 
                    //Deciding on action
                    if (action.equals("add")) {
                        double latitude = Double.parseDouble(parts[2]);
                        double longitude = Double.parseDouble(parts[3]);
                        String watcherName = parts[4]; 
                    
                        watcherList.addWatcher(wTime, latitude, longitude, watcherName);
                        System.out.println(watcherName + " is added to the watcher-list at time " +wTime);
                        System.out.println();
                    
                    
                    } 
                    if (action.equals("delete")) {
                        String watcherName = parts[2];
                        System.out.println(watcherName + " is removed from the watcher-list at time " +wTime);
                        watcherList.deleteWatcher(watcherName);
                        System.out.println();
                    
                    }
                    if (action.equals("query-largest"))
                    {
                       earthquakeList.findLargestMagnitude();
                       System.out.println();
                    
                    }
                }
                wTime++;            
            }    
        }
        //Exception catching
        catch (NumberFormatException e)
        {
            System.out.print("" );
            wTime=0;           
        }
        catch (IndexOutOfBoundsException e) 
        {
            System.out.print("" );
            wTime=0;  
        } catch (IOException e) 
        {
            e.printStackTrace();
            wTime=0; 
        }
        return watcherList;
    }
    /*
    This method reads xml type earthquake file
    
    again it reads line by line and the and the line doesn't get added to the list till the systemTime equals to its time
    
    The method also notifies the watchers if there is a close earthquake to them
    
    I couldn't really find the exact distance magnitude relation to notify the watchers so I made my own. 
    */

    private static EarthquakeList loadEarthquakeData(String fileName,WatcherList watcherList,EarthquakeList earthquakeList)
    {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            StringBuilder xmlContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                xmlContent.append(scanner.nextLine());
            }
            String xml = xmlContent.toString();
            String regex = "<earthquake>.*?</earthquake>";
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(xml);
 
            while (matcher.find()) {
                String earthquakeData = matcher.group();
                int id = Integer.parseInt(extractValue(earthquakeData, "id"));
                eTime=wTime;
                eTime = Integer.parseInt(extractValue(earthquakeData, "time"));
                
                //checks if the time is equal to take the action
                if (systemTime ==eTime) {
                    String place = extractValue(earthquakeData, "place");
                    String coordinates =extractValue(earthquakeData, "coordinates");      
                    String[] parts = coordinates.split(",");//this part is here to read coordinates line seperately
                    double latitude=Double.parseDouble(parts[0]);//it converts coordinates string to two double values
                    double longitude=Double.parseDouble(parts[1]);//stored as latitude and longitude
                    Double magnitude = Double.parseDouble(extractValue(earthquakeData, "magnitude"));

                    earthquakeList.addRecord(id, eTime, place, coordinates, magnitude);
                    System.out.println("Earthquake at " +place +" is inserted into earthquake-list ");
                
                    double currentLat=watcherList.getHead().getLatitude();
                    double currentLong=watcherList.getHead().getLongitude();
                    int size = watcherList.getSize();
                    String name=watcherList.getHead().getName(); 
                    watcherList.setCurrent(watcherList.getHead().getNext());
               
                    //distance calculation  starts here
                    while(size!=0){
                        //distance formula
                        double distance=Math.sqrt(Math.pow(latitude - currentLat,2)+Math.pow(longitude - currentLong, 2));
                        if(distance<2*(Math.pow(magnitude,3.98))){ 
                            System.out.println("Earthquake at " +place +" is close to " +name);
                        }
                        name=watcherList.getCurrent().getName();
                        currentLat=watcherList.getCurrent().getLatitude();
                        currentLong=watcherList.getCurrent().getLatitude();
                        watcherList.setCurrent(watcherList.getCurrent().getNext());;
                
                         //I used size on the loop to make sure every single member of watcherlist gets checked
                        size--;
                        }
                System.out.println();
                }

                
                
            //This if loop deletes earthquakes that are older than 6hrs
                
                if(earthquakeList.head!=null  && systemTime>6) {
                
                    int t=systemTime-6;
                    if(earthquakeList.getHead().getTime()==t){
                        earthquakeList.removeEarthquakeByTime(t);   
                    }
                }    
            }
        }
        //Exception catching
        catch (NullPointerException e) 
        {
            System.out.println("");
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        catch (IndexOutOfBoundsException e)
        {
            System.out.print("" );
            wTime=0;  
        }
        return earthquakeList;
    }
    
    /*
    This method parses the xml file
    
    It gets rid of tags using patterns
    
    returns the part of the line that's not inside the tags as a string
    
    */
    
    public static String extractValue(String xml, String tagName) {
        String regex = "<" + tagName + ">(.*?)</" + tagName + ">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(xml);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
    

    
    
   
}

