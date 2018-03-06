import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;

class NavImpl{
    private ArrayList<NavSegment> directions;
    private MapLoaderImpl mapLoad = new MapLoaderImpl();
    private AttractionMapperImpl am = new AttractionMapperImpl();
    private SegmentMapperImpl sm = new SegmentMapperImpl();
    
    static enum NAV_RESULT
    {
        success, invalid_source, invalid_destination, no_route;
    }
    
    // to use in priority queue
    private class locationCost implements Comparable<locationCost>{
        public GeoCoord gc;
        public double g;
        public double h;
        public double f;
        
        locationCost(GeoCoord gc){
            this.gc = gc;
        }
        
        locationCost(GeoCoord gc, double g, double h){
            this.gc = gc;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
        
        public int compareTo(locationCost lc){
            if(this.f < lc.f) return -1;
            if(this.f > lc.f) return 1;
            else return 0;
        }
    }
    
    private class coordStreet {
        public GeoCoord gc;
        public String streetName;
        
        coordStreet(GeoCoord gc, String streetName){
            this.gc = gc;
            this.streetName = streetName;
        }
    }
    
    // initialize internal data structures and AttractionMapper or SegmentMapper variables
    boolean loadMapData(String mapFile){
        mapLoad.load(mapFile);
        am.init(mapLoad);
        sm.init(mapLoad);
        
        return true;
    }
    
    // startLocation: starting attraction name or address
    // endLocation: ending attraction name or address
    NAV_RESULT navigate(String startLocation, String endLocation){
        PriorityQueue<locationCost> queue = new PriorityQueue<locationCost>();
        MyMap<GeoCoord, Double> costMap = new MyMap<GeoCoord, Double>();
        // reached key from (<-) value
        MyMap<GeoCoord, coordStreet> fromMap = new MyMap<GeoCoord, coordStreet>();
        // associate GeoSegments with street name
        //MyMap<GeoSegment, String> geoStreet = new MyMap<GeoSegment, String>();
        
        
        GeoCoord startAttrCoord = am.getGeoCoordForAttraction(startLocation);
        GeoCoord endAttrCoord = am.getGeoCoordForAttraction(endLocation);
        
        if(startAttrCoord == null)
            return NAV_RESULT.invalid_source;
        if(endAttrCoord == null) 
            return NAV_RESULT.invalid_destination; 
        
        
        //System.out.println("distance between start and end:" + GeoTools.distanceEarthKM(startAttrCoord, endAttrCoord));
        
        StreetSegment streetSeg = sm.lookUpGeoCoord(startAttrCoord).get(0);
        GeoCoord streetStartCoord = streetSeg.segment.start;
        GeoCoord streetEndCoord = streetSeg.segment.end;
        
        // g: distance = start attr -> start street
        double gStart = GeoTools.distanceEarthKM(startAttrCoord, streetStartCoord);
        // h: distance = start street -> end attr
        double hStart = GeoTools.distanceEarthKM(streetStartCoord, endAttrCoord);
        // g: distance = start attr -> end street
        double gEnd = GeoTools.distanceEarthKM(startAttrCoord, streetEndCoord);
        // h: distance = end street -> end attr 
        double hEnd = GeoTools.distanceEarthKM(streetEndCoord, endAttrCoord);
        
        queue.add(new locationCost(streetStartCoord, gStart, hStart));
        queue.add(new locationCost(streetEndCoord, gEnd, hEnd));        
        costMap.associate(streetStartCoord, gStart+hStart);
        costMap.associate(streetEndCoord, gEnd+hEnd);
        fromMap.associate(streetStartCoord, new coordStreet(startAttrCoord, streetSeg.streetName));
        fromMap.associate(streetEndCoord, new coordStreet(startAttrCoord, streetSeg.streetName));

        // A* search algorithm
        while(!queue.isEmpty()){
            locationCost from = queue.poll();
            
            if(from.gc.equals(endAttrCoord)) break;
            
            for(StreetSegment currStreet : sm.lookUpGeoCoord(from.gc)){                    
                if(isEndAttraction(currStreet, endAttrCoord)){
                    fromMap.associate(endAttrCoord, new coordStreet(from.gc, currStreet.streetName));
                    //geoStreet.associate(new GeoSegment(from.gc, endAttrCoord), currStreet.streetName);
                    queue.add(new locationCost(endAttrCoord, from.g, 0));
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
                    fromMap.associate(streetStartCoord, new coordStreet(from.gc, currStreet.streetName));
                    //geoStreet.associate(new GeoSegment(from.gc, streetStartCoord), currStreet.streetName);
                }                
                
                if(fromMap.find(streetEndCoord) == null || (gEnd+hEnd) < costMap.find(streetEndCoord)){
                    queue.add(new locationCost(streetEndCoord, gEnd, hEnd));
                    costMap.associate(streetEndCoord, gEnd+hEnd);
                    fromMap.associate(streetEndCoord, new coordStreet(from.gc, currStreet.streetName));
                    //geoStreet.associate(new GeoSegment(from.gc, streetEndCoord), currStreet.streetName);
                }
            }
        }
       
        if(fromMap.find(endAttrCoord) == null) // end attraction is unreachable from starting attraction
            return NAV_RESULT.no_route;
            
        directions = new ArrayList<NavSegment>();
        
        // create first NavSegment
        coordStreet to = fromMap.find(endAttrCoord);
        //GeoCoord toCoord = fromMap.find(endAttrCoord);
        GeoCoord toCoord = to.gc;
        GeoSegment nextGeoSeg = new GeoSegment(toCoord, endAttrCoord);
        //String nextStreet = sm.lookUpGeoCoord(endAttrCoord).get(0).streetName;
        String nextStreet = to.streetName;
        NavSegment currNav = createProceedSeg(nextGeoSeg, nextStreet);
        directions.add(currNav);
        
        while(!currNav.getSegment().start.equals(startAttrCoord)){
            //GeoCoord fromCoord = fromMap.find(toCoord);
            coordStreet from = fromMap.find(toCoord);
            GeoCoord fromCoord = from.gc;
            GeoSegment prevGeoSeg = new GeoSegment(fromCoord, toCoord);
            //String prevStreet = geoStreet.find(prevGeoSeg);
            String prevStreet = from.streetName;
            
            // turn
            if(!prevStreet.equals(nextStreet)){
                directions.add(0, createTurnSeg(prevGeoSeg, nextGeoSeg, nextStreet));
            }
            
            currNav = createProceedSeg(prevGeoSeg, prevStreet);
            directions.add(0, currNav);
            nextStreet = prevStreet;
            toCoord = fromCoord;
        }
        return NAV_RESULT.success;
    }
    
    private NavSegment createProceedSeg(GeoSegment gs, String streetName){
        String direction = getDirection(gs);
        double distance = GeoTools.distanceEarthKM(gs.start, gs.end);
        NavSegment nav = new NavSegment();
        nav.initProceed(direction, streetName, distance, gs);
        return nav;
    }
    
    private NavSegment createTurnSeg(GeoSegment from, GeoSegment to, String streetName){
        String turnDirection = getDirection(from, to);
        NavSegment nav = new NavSegment();
        nav.initTurn(turnDirection, streetName);
        return nav;        
    }    
    
    public ArrayList<NavSegment> navDirections(){ return directions; }
    
    public void printDirections(){
        if(directions.size() == 0) return;
        int size = 0;
        
        double distanceTravelled = 0;
        
        for(NavSegment nv : directions){
            System.out.println("directions[" + size++ + "]:");
            System.out.println("type: " + nv.getCommandType());
            if(nv.getCommandType().equals(NavSegment.NAV_COMMAND.proceed)){
                System.out.println("start: " + nv.getSegment().start);
                System.out.println("end: " + nv.getSegment().end);
                System.out.println("distance: " + nv.getDistance());
                distanceTravelled += nv.getDistance();
            }
            
            System.out.println("direction: " + nv.getDirection());
            System.out.println("street: " + nv.getStreet());
            System.out.println("====================");
        }
        System.out.println("Travelled:" + distanceTravelled);
        System.out.println("REACHED DESTINATION");
    }
    
    private boolean isEndAttraction(StreetSegment street, GeoCoord endCoord){
        for(Address a : street.attractionsOnThisSegment){
            if(a.location.equals(endCoord)) return true;
        }
        
        return false;
    }
    
    private String getDirection(GeoSegment gs){
        double travelAngle = GeoTools.angleOfLine(gs);
        String direction = "";
        
        if(0 <= travelAngle && travelAngle <= 22.5)
            direction = "east";
        if(22.5 < travelAngle && travelAngle <= 67.5)
            direction = "northeast";
        if(67.5 < travelAngle && travelAngle <= 112.5) 
            direction = "north";
        if(112.5  < travelAngle && travelAngle <= 157.5)
            direction = "northwest";
        if(157.5  < travelAngle && travelAngle <= 202.5)
            direction = "west";
        if(202.5  < travelAngle && travelAngle <= 247.5)
            direction = "southwest";
        if(247.5  < travelAngle && travelAngle <= 292.5)
            direction = "south";
        if(292.5  < travelAngle && travelAngle <= 337.5)
            direction = "southeast";
        if(337.5  < travelAngle && travelAngle < 360)
            direction = "east";
        
        return direction;
    }
    
    private String getDirection(GeoSegment gs1, GeoSegment gs2){
        if(GeoTools.angleBetween2Lines(gs1, gs2) < 180)
            return "left";
        else
            return "right";
    }
}