package com.vorontsov.bnb;

import com.vorontsov.bnb.Graph.Node;
import java.util.*;

/**
 * Branch and bound with heuristics of nodes colors: it skips branches which a priori cannot expand the max clique.
 */
public class BrandAndBoundWithColorsHeuristics {
    private Graph graph;
    private List<Graph.Node> maxClique;

    public BrandAndBoundWithColorsHeuristics(Graph graph) {
        this.graph = graph;
        this.maxClique = new LinkedList<>();
    }

    public List<Node> findMaxClique() {
        List<Node> nodes = new LinkedList<>(graph.getNodes().values());
        Map<Integer, Integer> colors = findChromaticNumber(nodes);
        // Sorting by color number - color number for node shows number of adjacent with each other vertices
        // and we are interested only in nodes with a lot of such neighbours.
        sortByColor(nodes, colors);

        branchAndBound(nodes, new LinkedList<>(), colors);
        return getMaxClique();
    }

    public void branchAndBound(List<Node> candidates, List<Node> clique, Map<Integer, Integer> colors) {
        for (Node c : candidates) {
            // we can predict if node cannot expand current max clique
            if (clique.size() + colors.get(c.getIndex()) <= getMaxClique().size()) { // |Q|+|R| > |Qmax|
                // and as nodes are sorted by color number, we don't need to verify further nodes
                // so just exit this branch
                return;
            }

            List<Node> currentClique = new LinkedList<>(clique);
            currentClique.add(c);

            List<Node> currentCandidates = new LinkedList<>(candidates);
            // Skip also candidates with smaller index when current one
            // this way we avoid situations like checking both 1-2-3 and 2-3-1 cliques
            currentCandidates = currentCandidates.subList(currentCandidates.indexOf(c), currentCandidates.size());
            currentCandidates.retainAll(c.getNeighbours());

            if (currentCandidates.size() > 0) {
                // More candidates found, so continuing
                Map<Integer, Integer> candidateColors = findChromaticNumber(currentCandidates);
                sortByColor(currentCandidates, candidateColors);
                branchAndBound(currentCandidates, currentClique, candidateColors);
            } else if (currentClique.size() > getMaxClique().size()) {
                maxClique = currentClique;
            }
        }
    }

    private void sortByColor(List<Node> collection, Map<Integer, Integer> colors) {
        collection.sort(Comparator.<Node>comparingInt(left -> colors.get(left.getIndex())).reversed());
    }

    /**
     * Implementation based on Tomita and Yamada (1978), Fujii and Tomita (1982), and Tomita et al. (1988)
     * http://www.dcs.gla.ac.uk/~pat/jchoco/clique/indSetMachrahanish/papers/tomita2006.pdf
     * Returns chromatic number for given nodes
     *
     * @param nodes collection of nodes to find chromatic number for
     * @return map contains node_index -> color_number
     */
    private static Map<Integer, Integer> findChromaticNumber(Collection<Node> nodes) {
        int maxColor = 0;
        // contains sets with vertexes of the same color. Key - color number, value - set of nodes of this color
        Map<Integer, Set<Node>> colorsSets = new HashMap<>();
        Map<Integer, Integer> colors = new HashMap<>();

        for (Node node : nodes) {
            int k = 1;

            while (true) {
                // Get all nodes of current K color
                Set<Node> nodesOfCurrentColor = colorsSets.get(k) != null ?
                        new HashSet<>(colorsSets.get(k)) : new HashSet<>();

                // And try to find neighbours with this color
                nodesOfCurrentColor.retainAll(node.getNeighbours());

                // if none - great, current K is suitable for coloring current node
                if (nodesOfCurrentColor.isEmpty()) {
                    break;
                }
                // Otherwise  - continue cycle
                k++;
            }

            if (k > maxColor) {
                maxColor = k;
                // New color, so create a new set for nodes
                colorsSets.put(k, new HashSet<>());
            }
            colorsSets.get(k).add(node);
            colors.put(node.getIndex(), k);
        }

        return colors;
    }

    public List<Node> getMaxClique() {
        return maxClique;
    }
}
