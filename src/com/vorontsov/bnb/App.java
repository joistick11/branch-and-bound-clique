package com.vorontsov.bnb;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.vorontsov.bnb.Graph.Node;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class App {
    public static void main(String[] args) throws InterruptedException {
        String file = "P:\\github\\branch-and-bound-clique\\graphs\\brock200_4.clq.txt";

        CliqueFindingJob cliqueFindingJob = new CliqueFindingJob(file);
        SimpleTimeLimiter simpleTimeLimiter = SimpleTimeLimiter.create(Executors.newSingleThreadExecutor());

        int timeLimit = 3600;

        try {
            long start = System.currentTimeMillis();
            simpleTimeLimiter.runWithTimeout(cliqueFindingJob, timeLimit, TimeUnit.SECONDS);
            System.out.println((System.currentTimeMillis() - start) / 1000.0);
        } catch (TimeoutException e) {
            System.out.println("0 " + cliqueFindingJob.algorithm.getMaxClique());
        }

        System.exit(0);
    }

    private static class CliqueFindingJob implements Runnable {
        public BrandAndBoundWithColorsHeuristics algorithm;
        private String filePath;

        CliqueFindingJob(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            Graph graph = Parser.graphFromFile(filePath);
            algorithm = new BrandAndBoundWithColorsHeuristics(graph);
            List<Node> clique = algorithm.findMaxClique();

            System.out.println(clique.size() + " " + clique);
        }
    }
}
