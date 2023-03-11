package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IGraph;
import edu.caltech.cs2.interfaces.ISet;




public class Graph<V, E> implements IGraph<V, E> {
    private IDictionary<V, IDictionary<V, E>> adjacencyMap;

    public Graph() {
        // Create a new ChainingHashDictionary that uses MoveToFrontDictionary as the underlying dictionary implementation.
        this.adjacencyMap = new ChainingHashDictionary<>(MoveToFrontDictionary::new);

    }

    @Override
    public boolean addVertex(V vertex) {

        if (this.adjacencyMap.containsKey(vertex)) {
            // If the vertex already exists, return false.
            return false;
        }
        // Otherwise, add a new dictionary to the adjacency map for the new vertex.
        this.adjacencyMap.put(vertex, new MoveToFrontDictionary<>());
        return true;
        //return false;
    }

    @Override
    public boolean addEdge(V src, V dest, E e) {

        if (!this.adjacencyMap.containsKey(src) || !this.adjacencyMap.containsKey(dest)) {
            // If either the source or destination vertex doesn't exist, return false.
            throw new IllegalArgumentException("Destination is required");
        }
        if(this.adjacencyMap.get(src).containsKey(dest)){
            this.adjacencyMap.get(src).put(dest, e);
            return false;
        }
        // Add the new edge to the source vertex's dictionary of neighbors.
        this.adjacencyMap.get(src).put(dest, e);
        return true;

    }

    @Override
    public boolean addUndirectedEdge(V n1, V n2, E e) {

        if (!this.adjacencyMap.containsKey(n1) || !this.adjacencyMap.containsKey(n2)) {
            // If either of the vertices doesn't exist, return false.
            throw new IllegalArgumentException("Destination is required");
            //return false;
        }
        if (!this.adjacencyMap.get(n1).containsKey(n2) && !this.adjacencyMap.get(n2).containsKey(n1)) {

            // Add the new edge to both vertices' dictionaries of neighbors.
            this.adjacencyMap.get(n1).put(n2, e);
            this.adjacencyMap.get(n2).put(n1, e);
            return true;
        }

        // If either of the vertices doesn't exist, return false.
        this.adjacencyMap.get(n1).put(n2, e);
        this.adjacencyMap.get(n2).put(n1, e);
        return false;

    }

    @Override
    public boolean removeEdge(V src, V dest) {

        if (!this.adjacencyMap.containsKey(src) || !this.adjacencyMap.containsKey(dest)) {
            // If either the source or destination vertex doesn't exist, return false.
            throw new IllegalArgumentException("Destination is required");
        }
        if(!this.adjacencyMap.get(src).containsKey(dest)){
            //throw new IllegalArgumentException("Edge does not exist");
            return false;
        }
        //remove the edge from the source vertex's dictionary of neighbors.

        this.adjacencyMap.get(src).remove(dest);
        return true;
    }

    @Override
    public ISet<V> vertices() {
        return this.adjacencyMap.keySet();
    }

    @Override
    public E adjacent(V i, V j) {
        if (!this.adjacencyMap.containsKey(i) || !this.adjacencyMap.containsKey(j)) {
            // If either the source or destination vertex doesn't exist, return null.
            return null;
        }
        // Return the edge corresponding to the neighbor j of vertex i.
        return this.adjacencyMap.get(i).get(j);
    }

    @Override
    public ISet<V> neighbors(V vertex) {
        if (!this.adjacencyMap.containsKey(vertex)) {
            // If the vertex doesn't exist, return null.
            return null;
        }
        // Return an ISet of all keys in the inner dictionary corresponding to the vertex's neighbors.
        return this.adjacencyMap.get(vertex).keySet();
    }
    }
