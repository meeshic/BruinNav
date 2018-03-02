class AttractionMapperImpl
{
    private MyMap<String, GeoCoord> map = new MyMap<String, GeoCoord>();
    
    // use MapLoader object to construct efficient data structure that allows
    // getGeoCoordForAttraction() to quickly find the GeoCoord that is associated
    // with the specified attraction name
    boolean init(MapLoaderImpl ml){
        
        for(int i=0; i<ml.getNumSegments(); i++){
            StreetSegment ss = ml.getSegment(i);
            
            for(Address a : ss.attractionsOnThisSegment)
                map.associate(a.attraction.toLowerCase(), a.location);
        }
        
        return true;
    }
    
    // Return GeoCoord associated with attraction name. Else, return null.
    // Note: getGeoCoordForAttraction() is case-insensitive
    GeoCoord getGeoCoordForAttraction(String attraction){
        //if(map.find(attraction.toLowerCase()) != null)
        //    System.out.println(map.find(attraction.toLowerCase()).toString());
        
        return map.find(attraction.toLowerCase());
    }
}