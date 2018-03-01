public class GeoSegment
{
    GeoCoord start;
	GeoCoord end;
    
    GeoSegment(){}
    
    GeoSegment(GeoCoord s, GeoCoord e){
        start = s;
        end = e;
    }
    
    public String toString(){
        String startSeg = "start:" + start.sLatitude + ", end:" + start.sLongitude; 
        String endSeg = "start:" + end.sLatitude + ", end:" + end.sLongitude; 
        
        String result = startSeg + "\n" + endSeg;
        
        return result;
    }
}