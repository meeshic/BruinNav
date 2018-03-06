import java.util.List;
import java.util.ArrayList;

class SegmentMapperImpl{
    private MyMap<GeoCoord, List<StreetSegment>> map = new MyMap<GeoCoord, List<StreetSegment>>();
    
    
    // create Map: GeoCoord -> List<StreetSegment>
    boolean init(MapLoaderImpl ml){
        if(ml == null) return false;
        
        for(int i=0; i<ml.getNumSegments(); i++){
            StreetSegment ss = ml.getSegment(i);
            
            // Street start GeoCoord
            if(map.find(ss.segment.start) != null){
                map.find(ss.segment.start).add(ss);
            }
            else{
                List<StreetSegment> list = new ArrayList<StreetSegment>();
                list.add(ss);
                map.associate(ss.segment.start, list);
            }
            
            // Street end GeoCoord
            if(map.find(ss.segment.end) != null){
                map.find(ss.segment.end).add(ss);
            }
            else{
                List<StreetSegment> list = new ArrayList<StreetSegment>();
                list.add(ss);
                map.associate(ss.segment.end, list);
            }
            
            // Street's attraction GeoCoord(s)
            for(Address a : ss.attractionsOnThisSegment){
                if(map.find(a.location) != null)
                    map.find(a.location).add(ss);
                else{
                    List<StreetSegment> list = new ArrayList<StreetSegment>();
                    list.add(ss);
                    map.associate(a.location, list);
                }
            }
        }
        return true;
    }
    
    List<StreetSegment> lookUpGeoCoord(GeoCoord geoCoord){
        return map.find(geoCoord);
    }
}