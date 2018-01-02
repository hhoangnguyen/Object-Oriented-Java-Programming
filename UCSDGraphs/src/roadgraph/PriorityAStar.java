package roadgraph;

import geography.GeographicPoint;

import java.util.HashMap;
import java.util.Set;

public class PriorityAStar extends Priority {
    PriorityAStar() {
        super(Constant.ASTAR);
    }

    @Override
    public void resetNodes(Set<GeographicPoint> vertices, HashMap<GeographicPoint, MapNode> pointNodeMap) {
        for (GeographicPoint point : vertices) {
            MapNode mapNode = pointNodeMap.get(point);
            mapNode.setDistance(Double.POSITIVE_INFINITY);
            mapNode.setPredictedDistance(Double.POSITIVE_INFINITY);
        }
    }

    @Override
    public void initializeStartNode(MapNode n) {
        if (n != null) {
            n.setDistance(0.0);
            n.setPredictedDistance(0.0);
            n.setPriority(this);
        }
    }

    @Override
    public boolean updateNode(MapNode start, MapNode end, MapNode curNode, MapEdge edge) {
        GeographicPoint endNodeLocation = (end != null) ? end.getLocation() : new GeographicPoint(0, 0);
        MapNode neighborNode = edge.getEndNode();

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

            // save priority preference
            neighborNode.setPriority(this);

            return true;
        }
        return false;
    }

    @Override
    public int compare(MapNode source, MapNode target) {
        return (source.getPredictedDistance() >= target.getPredictedDistance()) ? 1 : -1;
    }
}
