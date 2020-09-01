import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
/**
 *  A weighted graph.
 *  @author
 */
public class Graph {

    /** Adjacency lists by vertex number. */
    private LinkedList<Edge>[] adjLists;
    /** Number of vertices in me. */
    private int vertexCount;

    /** A graph with NUMVERTICES vertices and no edges. */
    @SuppressWarnings("unchecked")
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /** Add to the graph a directed edge from vertex V1 to vertex V2,
     *  with weight EDGEWEIGHT. If the edge already exists, replaces
     *  the weight of the current edge EDGEWEIGHT. */
    public void addEdge(int v1, int v2, int edgeWeight) {
        if (!isAdjacent(v1, v2)) {
            LinkedList<Edge> v1Neighbors = adjLists[v1];
            v1Neighbors.add(new Edge(v1, v2, edgeWeight));
        } else {
            LinkedList<Edge> v1Neighbors = adjLists[v1];
            for (Edge e : v1Neighbors) {
                if (e.to() == v2) {
                    e.edgeWeight = edgeWeight;
                }
            }
        }
    }

    /** Add to the graph an undirected edge from vertex V1 to vertex V2,
     *  with weight EDGEWEIGHT. If the edge already exists, replaces
     *  the weight of the current edge EDGEWEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int edgeWeight) {
        addEdge(v1, v2, edgeWeight);
        addEdge(v2, v1, edgeWeight);
    }

    /** Returns true iff there is an edge from vertex FROM to vertex TO. */
    public boolean isAdjacent(int from, int to) {
        for (Edge e : adjLists[from]) {
            if (e.to() == to) {
                return true;
            }
        }
        return false;
    }

    /** Returns a list of all the neighboring vertices u
     *  such that the edge (VERTEX, u) exists in this graph. */
    public List<Integer> neighbors(int vertex) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (Edge e : adjLists[vertex]) {
            neighbors.add(e.to());
        }
        return neighbors;
    }

    /** Runs Dijkstra's algorithm starting from vertex STARTVERTEX and returns
     *  an integer array consisting of the shortest distances
     *  from STARTVERTEX to all other vertices. */
    public int[] dijkstras(int startVertex) {
        boolean[] m = new boolean[vertexCount];
        int[] r = new int[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            r[i] = Integer.MAX_VALUE;
        }
        Heap h = new Heap();
        h.set(startVertex, 0);
        while (h.size() != 0) {
            Heap.Node c = h.pop();
            int cv = c.value;
            int cp = c.weight;

            boolean f = false;
            for (int i = 0; i < m.length; i++) {
                if (!m[i]) {
                    break;
                } else if (i == m.length - 1) {
                    f = true;
                }
            }
            if (f) {
                break;
            }

            if (!m[cv]) {
                m[cv] = true;
                r[cv] = cp;
                for (int successor : neighbors(cv)) {
                    h.set(successor, getEdge(cv, successor).info() + cp);
                }
            }
        }
        return r;
    }

    /** Returns the edge (V1, V2). (ou may find this helpful to implement!) */
    private Edge getEdge(int v1, int v2) {
        return null;
    }

    /** Represents an edge in this graph. */
    private class Edge {

        /** End points of this edge. */
        private int from, to;
        /** Weight label of this edge. */
        private int edgeWeight;

        /** The edge (V0, V1) with weight WEIGHT. */
        Edge(int v0, int v1, int weight) {
            this.from = v0;
            this.to = v1;
            this.edgeWeight = weight;
        }

        /** Return neighbor vertex along this edge. */
        public int to() {
            return to;
        }

        /** Return weight of this edge. */
        public int info() {
            return edgeWeight;
        }

        @Override
        public String toString() {
            return "(" + from + "," + to + ",dist=" + edgeWeight + ")";
        }

    }

    /** Tests of Graph. */
    public static void main(String[] unused) {
        // Put some tests here!

        Graph g1 = new Graph(5);
        g1.addEdge(0, 1, 1);
        g1.addEdge(0, 2, 1);
        g1.addEdge(0, 4, 1);
        g1.addEdge(1, 2, 1);
        g1.addEdge(2, 0, 1);
        g1.addEdge(2, 3, 1);
        g1.addEdge(4, 3, 1);

        Graph g2 = new Graph(5);
        g2.addEdge(0, 1, 1);
        g2.addEdge(0, 2, 1);
        g2.addEdge(0, 4, 1);
        g2.addEdge(1, 2, 1);
        g2.addEdge(2, 3, 1);
        g2.addEdge(4, 3, 1);
    }
    private class Heap {
        private ArrayList<Node> list = new ArrayList<Node>();

        public Heap() {
            list.add(new Node(-1, -1));
        }

        private void bubbleup(int index) {
            assert(index > 0);
            if (index == 1) {
                return;
            }
            if (list.get(index).weight < list.get(index / 2).weight) {
                Node temp = list.get(index);
                list.set(index, list.get(index / 2));
                list.set(index / 2, temp);
                bubbleup(index / 2);
            }
        }

        private void bubbledown(int index) {
            assert(index > 0);
            int target = index * 2;
            if (target >= list.size() || list.get(target) == null) {
                return;
            } else if (target + 1 == list.size() || list.get(target + 1) == null) {
                if (list.get(index).weight > list.get(target).weight) {
                    Node temp = list.get(index);
                    list.set(index, list.get(index * 2));
                    list.set(index * 2, temp);
                }
            } else {
                if (list.get(target).weight < list.get(target + 1).weight
                        && list.get(target).weight < list.get(index).weight) {
                    Node temp = list.get(index);
                    list.set(index, list.get(target));
                    list.set(target, temp);
                    bubbledown(target);
                } else if (list.get(target).weight > list.get(target + 1).weight
                        && list.get(target + 1).weight < list.get(index).weight) {
                    Node temp = list.get(index);
                    list.set(index, list.get(target + 1));
                    list.set(target + 1, temp);
                    bubbledown(target + 1);
                }
            }
        }

        public void add(int v, int w) {
            list.add(new Node(v, w));
            bubbleup(list.size() - 1);
        }

        public boolean set(int v, int w) {
            for (Node n : list) {
                if (n.value == v && n.weight > w) {
                    n.weight = w;
                    return true;
                } else if (n.value == v) {
                    return false;
                }
            }
            add(v, w);
            return false;
        }

        public Node peek() {
            return list.get(1);
        }

        public Node pop() {
            Node rtn = peek();
            list.set(1, list.get(list.size() - 1));
            list.remove(list.size() - 1);
            bubbledown(1);
            return rtn;
        }

        public int size() {
            return list.size() - 1;
        }

        public class Node {
            int value;
            int weight;
            Node(int v, int w) {
                value = v;
                weight = w;
            }
        }
    }

}
