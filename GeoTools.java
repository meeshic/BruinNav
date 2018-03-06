import java.lang.Math;

// Haversine formula: http://stackoverflow.com/questions/10198985/calculating-the-distance-between-2-latitudes-and-longitudes-that-are-saved-in-a

public class GeoTools{

    static final double PI = 3.14159265358979323846;
    static final double EARTH_RADIUS_KM = 6371.0;

    // This function converts decimal degrees to radians
    static double deg2rad(double deg) {
        return (deg * PI / 180);
    }

    //  This function converts radians to decimal degrees
    static double rad2deg(double rad) {
        return (rad * 180 / PI);
    }

    /**
    * Returns the distance between two points on the Earth.
    * Direct translation from http://en.wikipedia.org/wiki/Haversine_formula
    * @param lat1d Latitude of the first point in degrees
    * @param lon1d Longitude of the first point in degrees
    * @param lat2d Latitude of the second point in degrees
    * @param lon2d Longitude of the second point in degrees
    * @return The distance between the two points in kilometers
    */
    static double distanceEarthKM(GeoCoord g1, GeoCoord g2) {
        double lat1r, lon1r, lat2r, lon2r, u, v;
        lat1r = deg2rad(g1.latitude);
        lon1r = deg2rad(g1.longitude);
        lat2r = deg2rad(g2.latitude);
        lon2r = deg2rad(g2.longitude);
        u = Math.sin((lat2r - lat1r) / 2);
        v = Math.sin((lon2r - lon1r) / 2);
        return 2.0 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(u * u + Math.cos(lat1r) * Math.cos(lat2r) * v * v));
    }

    static double distanceEarthMiles(GeoCoord g1, GeoCoord g2) {
        double milesPerKm = 0.621371;
        return distanceEarthKM(g1, g2) * milesPerKm;
    }


    static double angleBetween2Lines(GeoSegment line1, GeoSegment line2)
    {
        /*
        double angle1 = atan2(line1.start.latitude - line1.end.latitude, line1.start.longitude - line1.end.longitude);
        double angle2 = atan2(line2.start.latitude - line2.end.latitude, line2.start.longitude - line2.end.longitude);
        */
        double angle1 = Math.atan2(line1.end.latitude - line1.start.latitude, line1.end.longitude - line1.start.longitude);
        double angle2 = Math.atan2(line2.end.latitude - line2.start.latitude, line2.end.longitude - line2.start.longitude);

        double result = (angle2 - angle1) * 180 / 3.14;
        if (result < 0)
            result += 360;

        return result;
    }


    static double angleOfLine(GeoSegment line1)
    {
        double angle = Math.atan2(line1.end.latitude - line1.start.latitude, line1.end.longitude - line1.start.longitude);
        double result = angle * 180 / 3.14;
        if (result < 0)
            result += 360;

        return result;
    }
}