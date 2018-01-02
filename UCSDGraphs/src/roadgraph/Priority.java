package roadgraph;

import geography.GeographicPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

abstract class Priority {
    private String name;

    Priority() {
        this.name = Constant.DIJKSTRA;
    }

    Priority(String name) {
        this.name = name;
    }

    public abstract void resetNodes(Set<GeographicPoint> vertices, HashMap<GeographicPoint, MapNode> pointNodeMap);

    public abstract void initializeStartNode(MapNode n);

    public abstract boolean updateNode(MapNode start, MapNode end, MapNode cur, MapEdge edge);

    public abstract int compare(MapNode source, MapNode target);

    public String getName() {
        return name;
    }
}