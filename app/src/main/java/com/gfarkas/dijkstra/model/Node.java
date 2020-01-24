package com.gfarkas.dijkstra.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Node implements Serializable {

    private String name;
    private int cost = Integer.MAX_VALUE;
    private Node predecessor = null;
    private Map<Node, Integer> childNodes = new HashMap<>();

    public Node() {
    }

    public Node(String name) {
        this.name = name;
    }

    public Node(String name, int cost, Node predecessor, Map<Node, Integer> childNodes) {
        this.name = name;
        this.cost = cost;
        this.predecessor = predecessor;
        this.childNodes = childNodes;
    }

    public void addChildNode(Node childNode, int cost) {

        this.childNodes.put(childNode, cost);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public Map<Node, Integer> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(Map<Node, Integer> childNodes) {
        this.childNodes = childNodes;
    }

}
