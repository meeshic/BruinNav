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
	private String streetName;
	private GeoSegment segment;
	private List<Address> attractionsOnThisSegment;
    
    StreetSegment(String streetName, GeoSegment segment){
        this.streetName = streetName;
        this.segment = segment;
        
        attractionsOnThisSegment = new ArrayList<>();
    }
    
    public void addAttraction(String attraction, GeoCoord location){
        attractionsOnThisSegment.add(new Address(attraction, location));
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
}