package roadgraph;

import geography.GeographicPoint;

public class Road {
    private GeographicPoint from;
    private GeographicPoint to;
    private String name;
    private String type;
    private double length;

    public Road(GeographicPoint from, GeographicPoint to, String name, String type, double length) {
        this.from = from;
        this.to = to;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    public GeographicPoint getFrom() {
        return from;
    }

    public GeographicPoint getTo() {
        return to;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getLength() {
        return length;
    }
}