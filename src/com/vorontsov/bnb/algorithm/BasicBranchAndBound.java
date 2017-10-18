package com.vorontsov.bnb.algorithm;

import com.vorontsov.bnb.model.Graph;
import com.vorontsov.bnb.model.Node;

import java.util.LinkedList;
import java.util.List;

public class BasicBranchAndBound {
    private Graph graph;
    private List<Node> maxClique;

    public BasicBranchAndBound(Graph graph) {
        this.graph = graph;
        this.maxClique = new LinkedList<>();
    }

    public List<Node> findMaxQlique() {
        branchAndBound(new LinkedList<>(graph.getNodes().values()), new LinkedList<>());
        return maxClique;
    }

    public void branchAndBound(List<Node> candidates, List<Node> clique) {
        for (Node c : candidates) {
            List<Node> currentClique = new LinkedList<>(clique);
            currentClique.add(c);

            List<Node> currentCandidates = new LinkedList<>(candidates);
            currentCandidates.retainAll(c.getNeighbours());

            if (currentCandidates.size() > 0) {
                branchAndBound(currentCandidates, currentClique);
            } else if (currentClique.size() > maxClique.size()) {
                maxClique = currentClique;
            }
        }
    }
}
