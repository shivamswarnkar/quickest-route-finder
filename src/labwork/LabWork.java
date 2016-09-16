/*
@SHIVAM SWARNKAR
LAB WORK
*/
package labwork;
import java.io.File;
import java.util.*;
import java.io.*;
/**
 *
 * @author RAMNARAYAN
 */
public class LabWork {

    /**
     * @param args the command line arguments
     */
    
    static Map flights = new HashMap();  //all fligths info
    static int DEFAULT = -1;
    
    public static void main(String[] args) throws FileNotFoundException {
       //reading info from map and storing it into flights container
        File file = new File("LabData.txt");
        Scanner inFile = new Scanner(file);
        String[] line;
        String fNum;
        String sur;
        String des;
        int time;
        
        while(inFile.hasNextLine()){
            line = inFile.nextLine().split("	");
            //for(String s: line){
            //System.out.println(s);}
            fNum = line[0];
            sur = line[1];
            des = line[2];
            time = Integer.parseInt(line[3]);
            Flight flight;
            
            if(!flights.containsKey(des)){
                flights.put(des, new Flight(des));
            }
            if(!flights.containsKey(sur)){
                flights.put(sur, new Flight(fNum, sur, des, time));
            }
            
            else{
                flight = (Flight) flights.get(sur);
                if((flight.code).equals("")){
                    flight.code = fNum;
                }
                flight.addDest(des, time);
            }
        }
        
        //test if file reading was sucessful and correct
       /* for(Object x: flights.values()){
            System.out.println(((Flight)x).name);
        }*/
       
       //finds the shortest path
        shortestPath();
    }
    
    static void shortestPath(){
        Scanner sin=new Scanner(System.in);
        System.out.println("Source: ");
        String source = sin.nextLine();
        while(!flights.containsKey(source)){
            System.out.println("INVALID SOURCE!! NOT FOUND!!, TRY AGAIN!!");
            System.out.println("Source: ");
            source = sin.nextLine();
        }
        
        String dest = "";
        
        //run dijsktra algo on the map
        djk((Flight) flights.get(source));
        
        //gives path for any destination from the given source
        while (!dest.equals("done") && !dest.equals("exit")){
            
            System.out.println("\nDestination: ");
            dest = sin.nextLine();
            
            while(!flights.containsKey(dest)){
                System.out.println("INVALID DESTINATION!! NOT FOUND!!, TRY AGAIN!!");
                System.out.println("DESTINATION: ");
                dest = sin.nextLine();
            }
            
            System.out.println("\n======Enter 'done' if you want to change source======\n");
            System.out.println("Shortest Path from "+source+" to "+dest+" :\n");
            System.out.print(source);
            
            //prints out the path and returns the total time cost of the path
            int total = printPath((Flight) flights.get(dest));
            if(total != -1){ //checks if there is a possible path or not
                System.out.println("Total time= "+total);
            }
            System.out.println("\n\nEnter 'done' to change the source or Enter 'exit' to stop the program, \nor press any key to continue with the same source....");
            dest = sin.nextLine();
        }
        if(dest.equals("done")){shortestPath();} 
    }
    
    static int printPath(Flight dest){
        //checks if there isn't a path
        if(dest.dist == DEFAULT){
            System.out.println("-> XXXXX ->"+dest.name);
            System.out.println("NO VALID PATH AVAILABLE");
            return -1;
        }
        
        Flight prev = dest;
        Stack st = new Stack();
        while(prev.dist != 0){
            st.push(prev);
            prev = prev.prev;
        }
        
        while(!st.empty()){System.out.print("->"+((Flight)st.pop()).name);}
        System.out.print("\n\n");
        return dest.dist;
    }
    
    static void djk(Flight source){
        for(Object x: flights.values()){
            ((Flight)x).reset();
        }
        
        Comparator<Flight> comparator = new FlightComparator();
        PriorityQueue<Flight> q = new PriorityQueue<Flight>(10, comparator);
        
        source.dist =0;
        source.visited = true;
        q.add(source);
        
        Flight visiting = source;
        Flight curr;
        while(!q.isEmpty()){
            visiting = q.remove();
            for(Destination x: visiting.dests ){
                curr = (Flight) flights.get(x.name);
                if(!curr.visited){
                    if (curr.dist == DEFAULT){
                        q.add(curr);
                        curr.dist = x.time + visiting.dist;
                        curr.prev = visiting;
                    }
                    else if(curr.dist > x.time+visiting.dist){
                        curr.dist = x.time + visiting.dist;
                        curr.prev = visiting;
                    }
                }
            }
            visiting.visited = true;
        }
    }
    
    static class FlightComparator implements Comparator<Flight>{
        @Override
        public int compare(Flight f1, Flight f2){
            if(f1.dist < f2.dist){
                return 1;
            }
            else if(f1.dist > f2.dist){
                return -1;
            }
            return 0;
        }
    }
    
    static class Flight{
        String code;
        String name;
        ArrayList<Destination> dests;
        
        int dist;
        Flight prev;
        boolean visited;
        
        Flight(String flightNum, String flightName, String des, int time){
            code =flightNum;
            name = flightName;
            dist = DEFAULT;
            prev = null;
            visited = false;
            dests = new ArrayList<Destination>();
            dests.add(new Destination(des, time));
        }
        
        Flight(String flightName){
            code ="";
            name = flightName;
            dist = DEFAULT;
            prev = null;
            visited = false;
            dests = new ArrayList<Destination>();
        }
        
        public void addDest(String name, int time){
            dests.add(new Destination(name, time));
        }
        
        public void reset(){
            dist = DEFAULT;
            prev = null;
            visited = false;
        }
    }
    
    static class Destination{
        String name;
        int time;
        Destination(String flightName, int t){
            name = flightName;
            time = t;
        }
    }
    
}



