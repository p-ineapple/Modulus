package com.example.modulus.Utils;

import java.util.*;
public class ModuleGraph<T> {
    // Use Hashmap to store the edges in the graph
    private Map<T, List<T> > map = new HashMap<>();
    private Map<T, String > costs = new HashMap<>();
    public void addVertex(T s, String cost) {
        map.put(s, new LinkedList<T>());
        costs.put(s, cost);
    }

    public void addEdge(T source, T destination, String cost) {
        if (!map.containsKey(source)){
            addVertex(source, cost);
        }else if(!costs.get(source).equals(cost)){
            costs.put(source, cost);
        }
        if (!map.containsKey(destination)){
            addVertex(destination, "NIL");
        }
        map.get(source).add(destination);
    }

    public int getVertexCount() {
        return map.keySet().size();
    }

    public int getEdgesCount() {
        int count = 0;
        for (T v : map.keySet()) {
            count += map.get(v).size();
        }
        return count;
    }

    public boolean hasVertex(T s) {
        if (map.containsKey(s)) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean hasEdge(T s, T d) {
        if (map.get(s).contains(d)) {
            return true;
        }
        else {
            return false;
        }
    }

    public List<T> neighbours(T s) {
        if(!map.containsKey(s))
            return null;
        return map.get(s);
    }

    public String getCost(T s) {
        if(!costs.containsKey(s))
            return null;
        return costs.get(s);
    }

    // Prints the adjancency list of each vertex.
    @Override public String toString() {
        StringBuilder builder = new StringBuilder();

        for (T v : map.keySet()) {
            builder.append(v.toString() + ": ");
            for (T w : map.get(v)) {
                builder.append(w.toString() + " ");
            }
            builder.append("\n");
        }
        return (builder.toString());
    }
}

// Driver Code
//public class Main {
//
//    public static void main(String args[])
//    {
//
//        // Object of graph is created.
//        Graph<Integer> g = new Graph<Integer>();
//
//        // edges are added.
//        // Since the graph is bidirectional,
//        // so boolean bidirectional is passed as true.
//        g.addEdge(0, 1, true);
//        g.addEdge(0, 4, true);
//        g.addEdge(1, 2, true);
//        g.addEdge(1, 3, true);
//        g.addEdge(1, 4, true);
//        g.addEdge(2, 3, true);
//        g.addEdge(3, 4, true);
//
//        // Printing the graph
//        System.out.println("Graph:\n" + g.toString());
//
//        // Gives the no of vertices in the graph.
//        g.getVertexCount();
//
//        // Gives the no of edges in the graph.
//        g.getEdgesCount(true);
//
//        // Tells whether the edge is present or not.
//        g.hasEdge(3, 4);
//
//        // Tells whether vertex is present or not
//        g.hasVertex(5);
//        g.neighbours(1);
//
//    }
//}
