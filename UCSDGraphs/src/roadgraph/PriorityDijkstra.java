package roadgraph;

import geography.GeographicPoint;

import java.util.HashMap;
import java.util.Set;

public class PriorityDijkstra extends Priority {
    PriorityDijkstra() {
        super(Constant.DIJKSTRA);
    }

    @Override
    public void resetNodes(Set<GeographicPoint> vertices, HashMap<GeographicPoint, MapNode> pointNodeMap) {
        for (GeographicPoint point : vertices) {
            MapNode mapNode = pointNodeMap.get(point);
            mapNode.setDistance(Double.POSITIVE_INFINITY);
        }
    }

    @Override
    public void initializeStartNode(MapNode n) {
        if (n != null) {
            n.setDistance(0.0);
            n.setPriority(this);
        }
    }

    @Override
    public boolean updateNode(MapNode start, MapNode end, MapNode curNode, MapEdge edge) {
        double distanceFromCurNode = curNode.getDistance() + edge.getLength();
        MapNode neighborNode = edge.getEndNode();

        // and path through curNode to neighborNode is shorter
        if (distanceFromCurNode < neighborNode.getDistance()) {
            // update distance, parentMap, and add to the queue
            neighborNode.setDistance(distanceFromCurNode);

            // save priority preference
            neighborNode.setPriority(this);

            return true;
        }
        return false;
    }

    @Override
    public int compare(MapNode source, MapNode target) {
        return (source.getDistance() >= target.getDistance()) ? 1 : -1;
    }
}
