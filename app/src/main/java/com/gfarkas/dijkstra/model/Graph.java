package com.gfarkas.dijkstra.model;

import java.io.Serializable;
import java.util.HashSet;

public class Graph implements Serializable {

    private HashSet<Node> nodes = new HashSet<>();

    public Graph() {
    }

    public Graph(HashSet<Node> nodes) {
        this.nodes = nodes;
    }

    public HashSet<Node> getNodes() {
        return nodes;
    }

    public void setNodes(HashSet<Node> nodes) {
        this.nodes = nodes;
    }

}
