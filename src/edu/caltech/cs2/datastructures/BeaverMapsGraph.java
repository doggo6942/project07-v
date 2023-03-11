package edu.caltech.cs2.datastructures;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.caltech.cs2.interfaces.*;
import java.io.FileReader;
import java.io.IOException;


public class BeaverMapsGraph extends Graph<Long, Double> {
    private IDictionary<Long, Location> ids;
    private ISet<Location> buildings;

    public BeaverMapsGraph() {
        super();
        this.buildings = new ChainingHashSet<>();
        this.ids = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    /**
     * Reads in buildings, waypoinnts, and roads file into this graph.
     * Populates the ids, buildings, vertices, and edges of the graph
     * @param buildingsFileName the buildings filename
     * @param waypointsFileName the waypoints filename
     * @param roadsFileName the roads filename
     */
    public BeaverMapsGraph(String buildingsFileName, String waypointsFileName, String roadsFileName) {
        this();

        // TODO (student): Write This
        // populating the buildings containers
        JsonElement bs = fromFile(buildingsFileName);
        for (JsonElement b : bs.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            //System.out.println("One of the locations is " + loc);
            this.buildings.add(loc);
            addVertex(loc);
            this.ids.put(loc.id, loc);

        }
        // populating the waypoints as Locations
        JsonElement ws = fromFile(waypointsFileName);
        for (JsonElement w : ws.getAsJsonArray()) {
            Location wloc = new Location(w.getAsJsonObject());
            addVertex(wloc);;
            this.ids.put(wloc.id, wloc);

        }
        // creating the edges, setting up the roads
        JsonElement rs = fromFile(roadsFileName);
        for (JsonElement r : rs.getAsJsonArray()) {
            JsonArray arr = r.getAsJsonArray();
            Long previouslocation = arr.get(0).getAsLong();
            Location previous = getLocationByID(previouslocation);
            for(int i = 1; i < arr.size(); i++){
                // this is not the first location in the entry
                Long currentLocation = arr.get(i).getAsLong();
                // get both location object
                Location current = getLocationByID(currentLocation);
                Double distance = previous.getDistance(current);
                addUndirectedEdge(previouslocation, currentLocation, distance);
                previouslocation = currentLocation;
                previous = current;
            }
        }
    }

    /**
     * Returns a deque of all the locations with the name locName.
     * @param locName the name of the locations to return
     * @return a deque of all location with the name locName
     */
    public IDeque<Location> getLocationByName(String locName) {
        // TODO (student): Write This
        IDeque<Location> locations = new ArrayDeque<>();
        for(Location loc : ids.values()){
           if(loc != null && loc.name != null && loc.name.equals(locName)){
               locations.add(loc);
           }
       }
        return locations;
    }

    /**
     * Returns the Location object corresponding to the provided id
     * @param id the id of the object to return
     * @return the location identified by id
     */
    public Location getLocationByID(long id) {
        // TODO (student): Write This
        return ids.get(id);
    }

    /**
     * Adds the provided location to this map.
     * @param n the location to add
     * @return true if n is a new location and false otherwise
     */
    public boolean addVertex(Location n) {
        // TODO (student): Write This
        ids.put(n.id, n);
        return addVertex(n.id);

    }

    /**
     * Returns the closest building to the location (lat, lon)
     * @param lat the latitude of the location to search near
     * @param lon the longitute of the location to search near
     * @return the building closest to (lat, lon)
     */
    public Location getClosestBuilding(double lat, double lon) {
        // Location closestBuilding = null;
        double minDistance = Double.POSITIVE_INFINITY;
        Location closestBuilding = null;
        for (Location building : buildings) {
            // Skip any non-building locations
            if (building.type != Location.Type.BUILDING) {
                continue;
            }
            // Calculate the distance between the current building and the given location
            double distance = building.getDistance(lat, lon);
            // If the distance is less than the current minimum, update the closest building and minimum distance
            if (distance < minDistance) {
                closestBuilding = building;
                minDistance = distance;
            }

        }
        return closestBuilding;
    }

    /**
     * Returns a set of locations which are reachable along a path that goes no further than `threshold` feet from start
     * @param start the location to search around
     * @param threshold the number of feet in the search radius
     * @return all locations within the provided `threshold` feet from start
     */
    public ISet<Location> dfs(Location start, double threshold) {
        // Create a set to hold the reachable locations
        ISet<Location> reachableLocations = new ChainingHashSet<>();

        // Create a set to keep track of visited locations
        ISet<Location> visitedLocations = new ChainingHashSet<>();

        // Initialize the search stack with the start location
        LinkedDeque<Location> searchStack = new LinkedDeque<>();
        searchStack.push(start);

        // Run the depth-first search algorithm until the search stack is empty
        while (!searchStack.isEmpty()) {
            // Get the next location to search
            // take the top of the stack
            Location currentLocation = searchStack.pop();

            // Check if the current location is within the threshold distance from the start location
            double distance = currentLocation.getDistance(start);
            if (distance <= threshold) {
                //  add it to the visited list and add it to the reachableLocation list
                reachableLocations.add(currentLocation);
                visitedLocations.add(currentLocation);
                // Get the list of locations adjacent to the current Location
                ISet<Long> neighbors = neighbors(currentLocation.id);
                // Add the locations that are not in the visited list to the top of the stack
                if(neighbors != null) {
                    for (Long id : neighbors
                    ) {
                        Location loc = getLocationByID(id);
                        if(!visitedLocations.contains(loc)){
                            searchStack.push(loc);
                        }
                    }
                }
            }
        }
        // Return the set of reachable locations
        return reachableLocations;
    }

    /**
     * Returns a list of Locations corresponding to
     * buildings in the current map.
     * @return a list of all building locations
     */
    public ISet<Location> getBuildings() {
        return this.buildings;
    }

    /**
     * Returns a shortest path (i.e., a deque of vertices) between the start
     * and target locations (including the start and target locations).
     * @param start the location to start the path from
     * @param target the location to end the path at
     * @return a shortest path between start and target
     */
    public IDeque<Location> dijkstra(Location start, Location target) {

        MinFourHeap<Long> pq = new MinFourHeap<>();
//
        ChainingHashDictionary<Long, Double> distances = new ChainingHashDictionary<>(MoveToFrontDictionary::new);

        // Create a dictionary to store the previous location in the shortest path
        ChainingHashDictionary<Long, Long> previous = new ChainingHashDictionary<>(MoveToFrontDictionary::new);

        //pq.enqueue(new IPriorityQueue.PQElement<>()start)

        //distances.put(start, 0.0);
        pq.enqueue(new IPriorityQueue.PQElement<>(start.id, 0.0));
        distances.put(start.id, 0.0);
        previous.put(start.id, null);
        // Run Dijkstra's Algorithm
        while (pq.size() != 0) {
            Long current = pq.dequeue().data;

            if (current.equals(target.id)) {
                break;
            }

            for (Long neighbor : neighbors(current)) {
                if (neighbor.equals(target.id) || !getBuildings().contains(getLocationByID(neighbor))) {
                    //distance btween neighboors + distance that was previously at
//                    if (adjacent(neighbor, current) == null) {
//                        System.out.println(neighbors(current));
//                        System.out.println(neighbor);
//                        System.out.println(current);
//                        return null;
//                    }
                    double distance = adjacent(current, neighbor) + distances.get(current);
                    if (distances.containsKey(neighbor)) {
                        if (distances.get(neighbor) > distance) {
                            distances.put(neighbor, distance);
                            //pair with null
                            previous.put(neighbor, current);
                            pq.decreaseKey(new IPriorityQueue.PQElement<>(neighbor, distance));
                        }
                    } else {
                        distances.put(neighbor, distance);
                        previous.put(neighbor, current);
                        pq.enqueue(new IPriorityQueue.PQElement<>(neighbor, distance));
                    }
                }
            }
        }

        //getLocations from ID
        IDeque<Location> paths = new LinkedDeque<>();
        Location current = target;
        while(!current.equals(start)) {
            paths.addFront(current);
            if(previous.get(current.id) == null) {
                return null;

            }
            current = getLocationByID(previous.get(current.id));

        }
        paths.addFront(start);

         return paths;
            }

    /**
     * Returns a JsonElement corresponding to the data in the file
     * with the filename filename
     * @param filename the name of the file to return the data from
     * @return the JSON data from filename
     */
    private static JsonElement fromFile(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            return JsonParser.parseReader(reader);
        } catch (IOException e) {
            return null;
        }
    }
}
