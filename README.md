# BruinNav

Note: Project reformatted C++ -> Java
Geospatial data from Open Street Maps (OSM): https://www.openstreetmap.org

Simple navigation system that loads and indexes OSM data to build a turn-by-turn navigation system.

Important classes:
1. MyMap: implement a map-like structure using a binary search tree.
2. MapLoaderImpl: load and parse OSM data into StreetSegments
3. AttractionMapperImpl: input(attraction name) -> output(attraction location)
4. SegmentMapperImpl: input(location:lat,long) -> output(list<street>)
5. NavImpl: input(start attraction, end attraction) -> output(list<directions>)
  > A* search algorithm used to find optimal path from start to end attraction. The Euclidean distance 
    was used as the heuristic.

Observations:
- Upon basic testing with Google Maps, BruinNav gives a path that is ~ <= 0.3km similar to the one 
  given by Google Maps.
  
Future potential work:
- BruinNav only takes distance into consideration when computing the cost function. For more realistic
  use, the cost function could take into consideration other important parameters (i.e. traffic, speed 
  limit, local road, freeway, etc).
- The binary search tree (BST) used as the base for the map-like structure is not balanced. If decreased 
  computation time is desired, BST can be made to be balanced.
- There is a NavSegment (direction) for each GeoSegment. To give a more concise path, adjacent NavSegments 
  on the same street may be merged (i.e. x->y->z becomes x->z, where street(x)=street(y)=street(z)).
