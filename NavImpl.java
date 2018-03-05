enum NAV_RESULT
{
    success, invalid_source, invalid_destination, no_route;
}

class NavImpl{
    private List<NavSegment> directions;
    private MapLoaderImpl mapLoad = new MapLoaderImpl();
    private AttractionMapperImpl am = new AttractionMapperImpl();
    private SegmentMapperImpl sm = new SegmentMapperImpl();
    
    // to use in priority queue
    private class locationCost implements Comparable<locationCost>{
        public GeoCoord gc;
        public int g;
        public int h;
        public int f;
        
        locationCost(GeoCoord gc){
            this.gc = gc;
        }
        
        locationCost(GeoCoord gc, int g, int h){
            this.gc = gc;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
        
        public int compareTo(locationCost lc){
            if(this.f == lc.f) return 0;
            if(this.f < lc.f) return -1;
            if(this.f > lc.f) return 1;
        }
    }
    
    // initialize internal data structures and AttractionMapper or SegmentMapper variables
    boolean loadMapData(String mapFile){
        mapLoad.load(mapFile);
        am.init(mapLoad);
        sm.init(mapLoad);
    }
    
    // startLocation: starting attraction name or address
    // endLocation: ending attraction name or address
    NAV_RESULT navigate(String startLocation, String endLocation){
        directions = new ArrayList<NavSegment>();
        
        PriorityQueue<locationCost> queue = new PriorityQueue<locationCost>();
        MyMap<GeoCoord, Double> costMap = new MyMap<GeoCoord, Double>();
        // reached key from (<-) value
        MyMap<GeoCoord, GeoCoord> fromMap = new MyMap<GeoCoord, GeoCoord>();
        
        
        GeoCoord startAttrCoord = am.getGeoCoordForAttraction(startLocation);
        GeoCoord endAttrCoord = am.getGeoCoordForAttraction(endLocation);
        
        GeoCoord streetStartCoord = sm.lookUpGeoCoord(startAttrCoord).segment.start;
        GeoCoord streetEndCoord = sm.lookUpGeoCoord(startAttrCoord).segment.end;
        
        double gStart = GeoTools.distanceEarthKM(startAttrCoord, streetStartCoord);
        double hStart = GeoTools.distanceEarthKM(streetStartCoord, endAttrCoord);
        double gEnd = GeoTools.distanceEarthKM(startAttrCoord, streetEndCoord);
        double hEnd = GeoTools.distanceEarthKM(streetEndCoord, endAttrCoord);
        
        queue.add(new locationCost(streetStartCoord, gStart, hStart));
        queue.add(new locationCost(streetEndCoord, gEnd, hEnd));        
        costMap.associate(streetStartCoord, gStart+hStart);
        costMap.associate(streetEndCoord, gEnd+hEnd);
        fromMap.associate(streetStartCoord, startAttrCoord);
        fromMap.associate(streetEndCoord, startAttrCoord);

        // A* search algorithm
        while(!queue.isEmpty()){
            locationCost from = queue.poll();
            
            if(from.gc.equals(endAttrCoord)) break;
            
            for(StreetSegment currStreet : sm.lookUpGeoCoord(from.gc)){
                if(isEndAttraction(currStreet)){
                    fromMap.associate(endAttrCoord, from.gc);
                    queue.add(new locationCost(endAttrCoord, from.g));
                    break;
                }
                
                
                streetStartCoord = currStreet.segment.start;
                streetEndCoord = currStreet.segment.end;
                gStart = from.g + GeoTools.distanceEarthKM(streetStartCoord, from.gc);
                hStart = GeoTools.distanceEarthKM(streetStartCoord, endAttrCoord);
                gEnd = from.g + GeoTools.distanceEarthKM(streetEndCoord, from.gc);
                hEnd = GeoTools.distanceEarthKM(streetEndCoord, endAttrCoord);
                
                if(fromMap.find(streetStartCoord) == null || (gStart+hStart) < costMap.find(streetStartCoord)){
                    queue.add(new locationCost(streetStartCoord, gStart, hStart));
                    costMap.associate(streetStartCoord, gStart+hStart);
                    fromMap.associate(streetStartCoord, startAttrCoord);
                }
                
                
                if(fromMap.find(streetEndCoord) == null || (gEnd+hEnd) < costMap.find(streetEndCoord)){
                    queue.add(new locationCost(streetEndCoord, gEnd, hEnd));
                    costMap.associate(streetEndCoord, gEnd+hEnd);
                    fromMap.associate(streetEndCoord, startAttrCoord);
                }
            }
        }
        
        
        // TODO: iterate over fromMap starting from endAttrCoord -> value -> ... -> startAttrCoord and build NavSegments
        if(fromMap.find(endAttrCoord) == null) // end attraction is unreachable from starting attraction
        
        // use GeoTools to build proceed or turn style navsegments
        
        
    }
    
}