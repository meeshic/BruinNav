public class GeoSegment implements Comparable<GeoSegment>
{
    GeoCoord start;
    GeoCoord end;
    
    GeoSegment(){}
    
    GeoSegment(GeoCoord s, GeoCoord e){
        start = s;
        end = e;
    }
    
    public int compareTo(GeoSegment gs){
        return (this.start.compareTo(gs.start) == 0) ? this.end.compareTo(gs.end) : this.start.compareTo(gs.start);
    }
    
    public String toString(){
        String startSeg = "start:" + start.latitude + ", end:" + start.longitude; 
        String endSeg = "start:" + end.latitude + ", end:" + end.longitude;
        String result = startSeg + "\n" + endSeg;
        
        return result;
    }
}