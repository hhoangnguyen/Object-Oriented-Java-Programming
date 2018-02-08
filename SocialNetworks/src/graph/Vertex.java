package graph;

import java.util.HashSet;

public class Vertex {
    private int value;
    private HashSet<Edge> incomingEdges;
    private HashSet<Edge> outgoingEdges;

    public Vertex(int value) {
        this.value = value;
        this.incomingEdges = new HashSet<>();
        this.outgoingEdges = new HashSet<>();
    }

    public int getValue() {
        return value;
    }

    public HashSet<Edge> getIncomingEdges() {
        return this.incomingEdges;
    }

    public HashSet<Edge> getOutgoingEdges() {
        return this.outgoingEdges;
    }

    public boolean addIncomingEdge(Edge edge) {
        return this.incomingEdges.add(edge);
    }

    public boolean addOutgoingEdge(Edge edge) {
        return this.outgoingEdges.add(edge);
    }
}
