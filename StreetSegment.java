import java.util.List;
import java.util.ArrayList;

class Address
{
    String attraction;
    GeoCoord location;
    
    public Address(String attraction, GeoCoord location){
        this.attraction = attraction;
        this.location = location;
    }
}

public class StreetSegment implements Comparable<StreetSegment>
{
	String streetName;
	GeoSegment segment;
	List<Address> attractionsOnThisSegment;
    
    StreetSegment(String streetName, GeoSegment segment){
        this.streetName = streetName;
        this.segment = segment;
        
        attractionsOnThisSegment = new ArrayList<Address>();
    }
    
    public boolean equals(Object streetSeg){
        StreetSegment s = (StreetSegment) streetSeg;
        return (segment.start.equals(s.segment.start)) && (segment.end.equals(s.segment.end));
    }
    
    public int compareTo(StreetSegment s){
        if(segment.start == s.segment.start)
            return segment.end.compareTo(s.segment.end);
        
        return segment.start.compareTo(s.segment.start);
    }
    
    public String toString(){
        System.out.println("street name:" + streetName);
        System.out.println("geoSegment:"  + "\n" + segment.toString());
        System.out.println("number of attractions:" + attractionsOnThisSegment.size());
        for(Address a : attractionsOnThisSegment)
            System.out.println("attraction:" + a.attraction + ", location:" + a.location.toString()); 
        
        return "";
    }
}