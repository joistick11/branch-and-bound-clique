package com.vorontsov.bnb.model;

import java.util.HashMap;
import java.util.Map;

public class Graph {
    private Map<Integer, Node> nodes;

    Graph() {
        this.nodes = new HashMap<>();
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    void createEdge(int index1, int index2) {
        Node node1 = getOrCreateNode(index1);
        Node node2 = getOrCreateNode(index2);

        node1.addNeighbour(node2);
        node2.addNeighbour(node1);
    }

    private Node getOrCreateNode(int index) {
        if (nodes.get(index) == null) {
            nodes.put(index, new Node(index));
        }

        return nodes.get(index);
    }
}
