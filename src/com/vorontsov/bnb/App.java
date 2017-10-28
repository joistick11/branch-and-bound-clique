package com.vorontsov.bnb;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.vorontsov.bnb.Graph.Node;

import java.util.List;
import java.util.concurrent.*;

import static java.lang.Integer.parseInt;

public class App {
    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            throw new RuntimeException("No file provided!");
        }

        String file = args[0];

        CliqueFindingJob cliqueFindingJob = new CliqueFindingJob(file);
        SimpleTimeLimiter simpleTimeLimiter = SimpleTimeLimiter.create(Executors.newSingleThreadExecutor());

        int timeLimit = args.length > 1 ? parseInt(args[1]) : 3600;

        try {
            long start = System.currentTimeMillis();
            List<Node> clique = simpleTimeLimiter.callWithTimeout(cliqueFindingJob, timeLimit, TimeUnit.SECONDS);
            System.out.println((System.currentTimeMillis() - start) / 1000.0 + " " + clique.size() + " "
                + clique);
        } catch (TimeoutException e) {
            System.out.println(cliqueFindingJob.algorithm.getMaxClique().size() + " "
                    + cliqueFindingJob.algorithm.getMaxClique() + " timeout!");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public static class CliqueFindingJob implements Callable<List<Node>> {
        public BrandAndBoundWithColorsHeuristics algorithm;
        private String filePath;

        CliqueFindingJob(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public List<Node> call() throws Exception {
            Graph graph = Parser.graphFromFile(filePath);
            algorithm = new BrandAndBoundWithColorsHeuristics(graph);
            return algorithm.findMaxClique();
        }
    }
}
