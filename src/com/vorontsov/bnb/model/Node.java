package com.vorontsov.bnb.model;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Node implements Comparable<Node> {
    private int index;
    private SortedSet<Node> neighbours;

    public Node(int index) {
        this.index = index;
        this.neighbours = new TreeSet<>();
    }

    public void addNeighbour(Node neighbour) {
        neighbours.add(neighbour);
    }

    public int getIndex() {
        return index;
    }

    public List<Node> getNeighbours() {
        return new LinkedList<>(neighbours.tailSet(this));
    }

    @Override
    public int compareTo(Node another) {
        return this.index - another.index;
    }

    @Override
    public String toString() {
        return String.valueOf(index);
    }
}
