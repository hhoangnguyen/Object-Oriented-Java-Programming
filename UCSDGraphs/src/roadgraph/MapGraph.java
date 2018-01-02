/**
 * @author UCSD MOOC development team and YOU
 * <p>
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between
 */
package roadgraph;


import java.util.*;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 *
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between
 *
 */
public class MapGraph {
    // Maintain both nodes and edges as you will need to
    // be able to look up nodes by lat/lon or by roads
    // that contain those nodes.
    private HashMap<GeographicPoint, MapNode> pointNodeMap;
    private HashSet<MapEdge> edges;
    private boolean debug = true;


    /**
     * Create a new empty MapGraph
     */
    public MapGraph() {
        pointNodeMap = new HashMap<GeographicPoint, MapNode>();
        edges = new HashSet<MapEdge>();
    }

    /**
     * Get the number of vertices (road intersections) in the graph
     * @return The number of vertices in the graph.
     */
    public int getNumVertices() {
        return pointNodeMap.values().size();
    }

    /**
     * Return the intersections, which are the vertices in this graph.
     * @return The vertices in this graph as GeographicPoints
     */
    public Set<GeographicPoint> getVertices() {
        return pointNodeMap.keySet();
    }

    /**
     * Get the number of road segments in the graph
     * @return The number of edges in the graph.
     */
    public int getNumEdges() {
        return edges.size();
    }


    /** Add a node corresponding to an intersection at a Geographic Point
     * If the location is already in the graph or null, this method does
     * not change the graph.
     * @param location  The location of the intersection
     * @return true if a node was added, false if it was not (the node
     * was already in the graph, or the parameter is null).
     */
    public boolean addVertex(GeographicPoint location) {
        if (location == null) {
            return false;
        }
        MapNode n = pointNodeMap.get(location);
        if (n == null) {
            n = new MapNode(location);
            pointNodeMap.put(location, n);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a directed edge to the graph from pt1 to pt2.
     * Precondition: Both GeographicPoints have already been added to the graph
     * @param from The starting point of the edge
     * @param to The ending point of the edge
     * @param roadName The name of the road
     * @param roadType The type of the road
     * @param length The length of the road, in km
     * @throws IllegalArgumentException If the points have not already been
     *   added as nodes to the graph, if any of the arguments is null,
     *   or if the length is less than 0.
     */
    public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
                        String roadType, double length) throws IllegalArgumentException {

        MapNode n1 = pointNodeMap.get(from);
        MapNode n2 = pointNodeMap.get(to);

        // check nodes are valid
        if (n1 == null)
            throw new NullPointerException("addEdge: pt1:" + from + "is not in graph");
        if (n2 == null)
            throw new NullPointerException("addEdge: pt2:" + to + "is not in graph");

        MapEdge edge = new MapEdge(roadName, roadType, n1, n2, length);
        edges.add(edge);
        n1.addEdge(edge);

    }

    /**
     * Get a set of neighbor nodes from a mapNode
     * @param node  The node to get the neighbors from
     * @return A set containing the MapNode objects that are the neighbors
     * 	of node
     */
    private Set<MapNode> getNeighbors(MapNode node) {
        return node.getNeighbors();
    }

    /** Find the path from start to goal using breadth first search
     *
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest (unweighted)
     *   path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return bfs(start, goal, temp);
    }

    /** Find the path from start to goal using breadth first search
     *
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest (unweighted)
     *   path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start,
                                     GeographicPoint goal,
                                     Consumer<GeographicPoint> nodeSearched) {
        /* Note that this method is a little long and we might think
		 * about refactoring it to break it into shorter methods as we
		 * did in the Maze search code in week 2 */

        // Setup - check validity of inputs
        if (start == null || goal == null)
            throw new NullPointerException("Cannot find route from or to null node");
        MapNode startNode = this.getMapNodeByLocation(start);
        MapNode endNode = this.getMapNodeByLocation(goal);

        // setup to begin BFS
        HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
        Queue<MapNode> toExplore = new LinkedList<MapNode>();
        HashSet<MapNode> visited = new HashSet<MapNode>();
        toExplore.add(startNode);
        MapNode next = null;

        while (!toExplore.isEmpty()) {
            next = toExplore.remove();

            // hook for visualization
            nodeSearched.accept(next.getLocation());

            if (next.equals(endNode)) break;
            Set<MapNode> neighbors = getNeighbors(next);
            for (MapNode neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, next);
                    toExplore.add(neighbor);
                }
            }
        }
        if (!next.equals(endNode)) {
            System.out.println("No path found from " + start + " to " + goal);
            return null;
        }
        // Reconstruct the parent path
        List<GeographicPoint> path =
                reconstructPath(parentMap, startNode, endNode);

        return path;

    }

    /**
     * Get MapNode by Geographic location
     * @param loc, Geographic location
     * @return MapNode
     */
    private MapNode getMapNodeByLocation(GeographicPoint loc) {
        MapNode mapNode = pointNodeMap.get(loc);

        if (mapNode == null) {
            System.err.println("Node " + mapNode + " does not exist");
            return null;
        }

        return mapNode;
    }

    /** Reconstruct a path from start to goal using the parentMap
     *
     * @param parentMap the HashNode map of children and their parents
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    private List<GeographicPoint>
    reconstructPath(HashMap<MapNode, MapNode> parentMap,
                    MapNode start, MapNode goal) {
        LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
        MapNode current = goal;

        while (!current.equals(start)) {
            path.addFirst(current.getLocation());
            current = parentMap.get(current);
        }

        // add start
        path.addFirst(start.getLocation());
        return path;
    }


    /** Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        // You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return dijkstra(start, goal, temp);
    }

    /**
     * Must reset distances due to Testing Bug here:
     * https://www.coursera.org/learn/advanced-data-structures/discussions/weeks/4/threads/1MJDknYzEeeAFw6nEgpmmA
     */
    private void resetAllDistances() {
        for (GeographicPoint point : this.getVertices()) {
            MapNode mapNode = pointNodeMap.get(point);
            mapNode.setDistance(Double.POSITIVE_INFINITY);
            mapNode.setPredictedDistance(Double.POSITIVE_INFINITY);
        }
    }

    /** Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start,
                                          GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        // Initialize priority queue, visited, parentMap
        PriorityQueue<MapNode> toExplore = new PriorityQueue<>();
        HashSet<MapNode> visited = new HashSet<>();
        HashMap<MapNode, MapNode> parentMap = new HashMap<>();

        this.resetAllDistances();

        // Get start and end MapNode
        MapNode startNode = this.getMapNodeByLocation(start);
        MapNode endNode = this.getMapNodeByLocation(goal);

        // flag to stop if found endNode
        boolean found = false;

        // set startNode distance and add to the queue
        if (startNode != null) startNode.setDistance(0.0);
        toExplore.add(startNode);

        int nodeCount = 0;

        while (!toExplore.isEmpty()) {
            // dequeue from front of queue
            MapNode curNode = toExplore.remove();
            nodeCount++;
            //if (debug) System.out.println("DIJKSTRA visiting" + curNode);

            // Hook for visualization.
            nodeSearched.accept(curNode.getLocation());

            if (!visited.contains(curNode)) {
                // add to visited HashSet
                visited.add(curNode);

                // if found, break out of while loop
                if (curNode.equals(endNode)) {
                    found = true;
                    break;
                }

                // loop through each out-going edges to get neighbor MapNodes
                for (MapEdge edge : curNode.getEdges()) {
                    MapNode neighborNode = edge.getEndNode();

                    // if neighborNode is not yet visited
                    if (!visited.contains(neighborNode)) {
                        double distanceFromCurNode = curNode.getDistance() + edge.getLength();

                        // and path through curNode to neighborNode is shorter
                        if (distanceFromCurNode < neighborNode.getDistance()) {
                            // update distance, parentMap, and add to the queue
                            neighborNode.setDistance(distanceFromCurNode);
                            parentMap.put(neighborNode, curNode);
                            toExplore.add(neighborNode);
                        }
                    }
                }
            }
        }
        if (debug) System.out.println("Dijkstra - Nodes visited in search: " + nodeCount);

        // if not found, return null
        if (!found) {
            System.out.println("Dijkstra: No path exists");
            return null;
        }

        // Reconstruct the parent path
        return reconstructPath(parentMap, startNode, endNode);
    }

    /** Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return aStarSearch(start, goal, temp);
    }

    /** Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start,
                                             GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        // Initialize priority queue, visited, parentMap
        PriorityQueue<MapNode> toExplore = new PriorityQueue<>();
        HashSet<MapNode> visited = new HashSet<>();
        HashMap<MapNode, MapNode> parentMap = new HashMap<>();

        this.resetAllDistances();

        // Get start and end MapNode
        MapNode startNode = this.getMapNodeByLocation(start);
        MapNode endNode = this.getMapNodeByLocation(goal);

        // startNode location
        GeographicPoint endNodeLocation = (endNode != null) ? endNode.getLocation() : new GeographicPoint(0, 0);

        // flag to stop if found endNode
        boolean found = false;

        // set startNode distance and add to the queue
        if (startNode != null) {
            startNode.setDistance(0.0);
            startNode.setPredictedDistance(0.0);
        }
        toExplore.add(startNode);

        int nodeCount = 0;

        while (!toExplore.isEmpty()) {
            // dequeue from front of queue
            MapNode curNode = toExplore.remove();
            nodeCount++;
            //if (debug) System.out.println("A* visiting" + curNode + "\n");

            // Hook for visualization.
            nodeSearched.accept(curNode.getLocation());

            if (!visited.contains(curNode)) {
                // add to visited HashSet
                visited.add(curNode);

                // if found, break out of while loop
                if (curNode.equals(endNode)) {
                    found = true;
                    break;
                }

                // loop through each out-going edges to get neighbor MapNodes
                for (MapEdge edge : curNode.getEdges()) {
                    MapNode neighborNode = edge.getEndNode();

                    // if neighborNode is not yet visited
                    if (!visited.contains(neighborNode)) {
                        /*
                        Calculate estimate distance f(n) = g(n) + h(n) where
                        g(n) = distance at curNode + length from curNode to neighborNode
                        h(n) = heuristic distance from endNode to neighborNode
                         */
                        double distanceFromCurNode = curNode.getDistance() + edge.getLength();
                        double heuristicDistanceFromEndNodeToNeighborNode = endNodeLocation.distance(neighborNode.getLocation());
                        double predictedDistance = distanceFromCurNode + heuristicDistanceFromEndNodeToNeighborNode;

                        // and path through curNode to neighborNode is shorter
                        if (predictedDistance < neighborNode.getPredictedDistance()) {
                            // update distance, predictedDistance, parentMap, and add to the queue
                            neighborNode.setDistance(distanceFromCurNode);
                            neighborNode.setPredictedDistance(predictedDistance);
                            parentMap.put(neighborNode, curNode);
                            toExplore.add(neighborNode);
                        }
                    }
                }
            }
        }
        if (debug) System.out.println("A* - Nodes visited in search: " + nodeCount);

        // if not found, return null
        if (!found) {
            System.out.println("AStar: No path exists");
            return null;
        }

        // Reconstruct the parent path
        return reconstructPath(parentMap, startNode, endNode);
    }


    public static void main(String[] args) {
        MapGraph simpleTestMap = new MapGraph();
        GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);

        GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
        GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);

        System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
        List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
        List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);

        MapGraph testMap = new MapGraph();
        GraphLoader.loadRoadMap("data/maps/utc.map", testMap);

        // A very simple test using real data
        testStart = new GeographicPoint(32.869423, -117.220917);
        testEnd = new GeographicPoint(32.869255, -117.216927);
        System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
        testroute = testMap.dijkstra(testStart,testEnd);
        testroute2 = testMap.aStarSearch(testStart,testEnd);


        // A slightly more complex test using real data
        testStart = new GeographicPoint(32.8674388, -117.2190213);
        testEnd = new GeographicPoint(32.8697828, -117.2244506);
        System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
        testroute = testMap.dijkstra(testStart,testEnd);
        testroute2 = testMap.aStarSearch(testStart,testEnd);
    }

}