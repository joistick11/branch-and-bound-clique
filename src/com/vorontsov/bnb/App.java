package com.vorontsov.bnb;

import com.vorontsov.bnb.Graph.Node;

import java.util.List;

public class App {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Graph graph = Parser.graphFromFile("P:\\github\\branch-and-bound-clique\\graphs\\brock200_2.clq.txt");

        BrandAndBoundWithColorsHeuristics brandAndBoundWithColorsHeuristics = new BrandAndBoundWithColorsHeuristics(graph);
        List<Node> clique = brandAndBoundWithColorsHeuristics.findMaxClique();
        System.out.println((System.currentTimeMillis() - start) / 1000.0);
        System.out.print(clique.size());
    }
}
