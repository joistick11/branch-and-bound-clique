package com.vorontsov.bnb;

import com.vorontsov.bnb.algorithm.BasicBranchAndBound;
import com.vorontsov.bnb.model.Graph;
import com.vorontsov.bnb.model.Node;
import com.vorontsov.bnb.model.Parser;

import java.util.List;

public class App {
    public static void main(String[] args) {
        Graph graph = Parser.graphFromFile("P:\\github\\branch-and-bound-clique\\C125.9.clq.txt");

        BasicBranchAndBound branchAndBound = new BasicBranchAndBound(graph);
        List<Node> clique = branchAndBound.findMaxQlique();

        System.out.print(clique);
    }
}
