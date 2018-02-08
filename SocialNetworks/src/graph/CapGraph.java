/**
 *
 */
package graph;

import java.util.*;

/**
 * @author Huy Hoang-Nguyen
 * <p>
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 */
public class CapGraph implements Graph {
    private HashSet<Edge> edges;
    private HashMap<Integer, Vertex> vertices;

    public CapGraph() {
        this.edges = new HashSet<>();
        this.vertices = new HashMap<>();
    }

    /* (non-Javadoc)
     * @see graph.Graph#addVertex(int)
     */
    @Override
    public void addVertex(int num) {
        Vertex vertex = new Vertex(num);
        this.vertices.put(num, vertex);
    }

    /* (non-Javadoc)
     * @see graph.Graph#addEdge(int, int)
     */
    @Override
    public void addEdge(int from, int to) {
        // create new vertices
        Vertex vertexFrom = this.vertices.get(from);
        Vertex vertexTo = this.vertices.get(to);

        // create edge
        Edge edge = new Edge(vertexFrom, vertexTo);

        // save edges' reference
        vertexFrom.addOutgoingEdge(edge);
        vertexTo.addIncomingEdge(edge);

        this.edges.add(edge);
    }

    /* (non-Javadoc)
     * @see graph.Graph#getEgonet(int)
     * egonet of a node is a sub-graph of all its neighbors and edges between them
     */
    @Override
    public Graph getEgonet(int center) {
        Graph egonet = new CapGraph();

        // add the first vertex
        egonet.addVertex(center);

        Vertex centerVertex = this.vertices.get(center);
        egonet = this.addEgonetNeighbors(egonet, centerVertex);

        return this.addEdgesForEgoNeighbors(egonet);
    }

    /**
     * Add neighbor vertices of provided vertex to current graph by looping through
     * outgoing and incoming edges from provided vertex
     * This method mutates both graph and vertex params
     *
     * @param graph
     * @param vertex
     * @return Graph, mutated graph
     */
    private Graph addEgonetNeighbors(Graph graph, Vertex vertex) {
        // loop through all outgoing neighbors
        for (Edge edge : vertex.getOutgoingEdges()) {
            graph.addVertex(edge.getTo().getValue());
            graph.addEdge(edge.getFrom().getValue(), edge.getTo().getValue());
        }

        // loop through all incoming neighbors
        for (Edge edge : vertex.getIncomingEdges()) {
            graph.addVertex(edge.getFrom().getValue());
            graph.addEdge(edge.getFrom().getValue(), edge.getTo().getValue());
        }

        return graph;
    }

    /**
     * Loop through each vertex, follow its outgoing edge and check if to's value is in graph vertices
     * if not, add it
     * This method mutates graph param
     *
     * @param graph
     * @return Graph, mutated graph
     */
    private CapGraph addEdgesForEgoNeighbors(Graph graph) {
        // this is a CapGraph, casting it will allow us to use private methods
        CapGraph egoGraph = (CapGraph) graph;

        Set<Integer> vertexValues = egoGraph.getVertices().keySet();
        for (Integer curValue : vertexValues) {
            // get the vertex for this value from original graph
            Vertex curVertex = this.vertices.get(curValue);

            // loop through its outgoing neighbors
            for (Edge edge : curVertex.getOutgoingEdges()) {
                // check if to's value is in graph vertices
                if (egoGraph.getVertices().get(edge.getTo().getValue()) != null) {
                    egoGraph.addEdge(edge.getFrom().getValue(), edge.getTo().getValue());
                }
            }
        }

        return egoGraph;
    }

    /* (non-Javadoc)
     * @see graph.Graph#getSCCs()
     */
    @Override
    public List<Graph> getSCCs() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see graph.Graph#exportGraph()
     */
    @Override
    public HashMap<Integer, HashSet<Integer>> exportGraph() {
        // a map that maps each vertex to a set of outgoing edges from it
        HashMap<Integer, HashSet<Integer>> exportedGraph = new HashMap<>();

        Set<Integer> vertexValues = this.getVertices().keySet();
        // loop through each vertex
        for (Integer curValue : vertexValues) {
			// loop through outgoing edges
            Vertex curVertex = this.vertices.get(curValue);

            // a set containing outgoing edges from current vertex
            HashSet<Integer> outgoingEdges = new HashSet<>();
            for (Edge edge : curVertex.getOutgoingEdges()) {
                outgoingEdges.add(edge.getTo().getValue());
            }

            exportedGraph.put(curValue, outgoingEdges);
        }

        return exportedGraph;
    }

    /**
     * Get the HashMap of vertices
     * @return HashMap
     */
    private HashMap<Integer, Vertex> getVertices() {
        return vertices;
    }
}