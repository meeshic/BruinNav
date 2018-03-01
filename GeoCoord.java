public class GeoCoord implements Comparable<GeoCoord>
{
    double latitude;
    double longitude;
    String sLatitude;
    String sLongitude;
    
	GeoCoord(String latitude, String longitude)
	{
		this.latitude = Double.parseDouble(latitude);
		this.longitude = Double.parseDouble(longitude);

		sLatitude = latitude;
		sLongitude = longitude;
	}
    
    public boolean equals(Object o){
        GeoCoord c = (GeoCoord) o;
        
        return (this.latitude == c.latitude) && (this.longitude == c.longitude);
    }
    
    public int compareTo(GeoCoord c){
        if(this.latitude < c.latitude) return -1;
        if(this.latitude > c.latitude) return +1;
        if(this.longitude < c.longitude) return -1;
        if(this.longitude > c.longitude) return +1;
        return 0;        
    }
    
    public String toString(){
        return sLatitude + ", " + sLongitude;
    }
}