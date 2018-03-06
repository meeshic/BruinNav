import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

class MapLoaderImpl{
    private List<StreetSegment> container = new ArrayList<StreetSegment>();
    
    boolean load(String mapFile){
        
        try{
            Scanner scanner = new Scanner(new File(mapFile));
            
            while(scanner.hasNextLine()){
                String streetName = scanner.nextLine();
                
                // fix strange map geo format
                String geoSegFormat = scanner.nextLine().replace(", ", ",");
                String[] geo = geoSegFormat.split(" ");
                String[] startCoord = geo[0].split(",");
                String[] endCoord = geo[1].split(",");
                GeoSegment seg = new GeoSegment(new GeoCoord(startCoord[0], startCoord[1]),new GeoCoord(endCoord[0], endCoord[1]));
                
                StreetSegment streetSeg = new StreetSegment(streetName, seg);

                // add attractions
                int numAttr = Integer.parseInt(scanner.nextLine());
                for(int i=0; i<numAttr; i++){
                    String[] attr = scanner.nextLine().split("\\|");
                    String[] coord = attr[attr.length-1].split(",");
                    streetSeg.attractionsOnThisSegment.add(new Address(attr[0], new GeoCoord(coord[0], coord[1])));
                }
                container.add(streetSeg);
            }
            scanner.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    int getNumSegments(){
        return container.size();
    }
    
    // Retrieve StreetSegment associated with specified segment number 'segNum'
    StreetSegment getSegment(int segNum){
        if(container.size() == 0 || segNum > container.size()-1) return null;
        
        return container.get(segNum);
    }
}