package nz.ac.auckland.se281.datastructures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A graph that is composed of a set of verticies and edges.
 *
 * <p>You must NOT change the signature of the existing methods or constructor of this class.
 *
 * @param <T> The type of each vertex, that have a total ordering.
 */
public class Graph<T extends Comparable<T>> {

  private Set<T> verticies;
  private Set<Edge<T>> edges;

  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
  }

  public Set<T> getRoots() {
    // Check if the source and destination of edges are equal to each other.
    // If they are, then the vertex is a root.
    throw new UnsupportedOperationException();
  }

  public boolean isReflexive() {
    // Check if the graph is reflexive or not.
    int count = 0;
    for (T vertex : verticies) {
      for (Edge<T> edge : edges) {
        if ((edge.getSource() == vertex) && (edge.getDestination() == vertex)) {
          count++;
        }
      }
    }
    if (count == verticies.size()) {
      return true;
    }
    return false;
    // throw new UnsupportedOperationException();
  }

  public boolean isSymmetric() {
    // Check is the graph is symmetric or not.
    int count = 0;
    int numberOfEdges = 0;
    for (T vertex : verticies) {
      for (Edge<T> edge : edges) {

        if (edge.getSource() == vertex) {
          numberOfEdges++;
          for (Edge<T> edge2 : edges) {
            if ((edge2.getDestination() == vertex)
                && (edge2.getSource() == edge.getDestination())) {
              count++;
            }
          }
        }
      }
    }
    if (count == numberOfEdges) {
      return true;
    }
    return false;
    // throw new UnsupportedOperationException();
  }

  public boolean isTransitive() {
    // Check if the graph is transitive or not.
    int count = 0;
    for (Edge<T> edge : edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      for (Edge<T> edge2 : edges) {
        if (edge2.getSource() == destination) {
          for (Edge<T> edge3 : edges) {
            if ((edge3.getSource() == source)
                && (edge3.getDestination() == edge2.getDestination())) {
              count++;
            }
          }
        }
      }
    }
    if (count == edges.size()) {
      return true;
    }
    return false;
    // throw new UnsupportedOperationException();
  }

  public boolean isAntiSymmetric() {
    // Check if the graph is anti-symmetric or not.
    int count = 0;
    int checkCount = 0;
    for (Edge<T> edge : edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      for (Edge<T> edge2 : edges) {
        if ((edge2.getSource() == destination) && (edge2.getDestination() == source)) {
          count++;
          if (source == destination) {
            checkCount++;
          }
        }
      }
    }
    if (count == checkCount) {
      return true;
    }
    return false;
    // throw new UnsupportedOperationException();
  }

  public boolean isEquivalence() {
    // Check if the graph is an equivalence relation or not.
    if (isReflexive() && isSymmetric() && isTransitive()) {
      return true;
    }
    return false;
    //throw new UnsupportedOperationException();
  }

  public Set<T> getEquivalenceClass(T vertex) {
    // Get the equivalence class of a vertex.
    Set<T> equivalenceClass = new HashSet<T>();
    for (Edge<T> edge : edges) {
      if (edge.getSource() == vertex) {
        for (Edge<T> edge2 : edges) {
          if ((edge.getDestination() == edge2.getSource()) && (edge2.getDestination() == vertex)) {
            equivalenceClass.add(edge.getDestination());
          }
        }
      }
    }
    return equivalenceClass;
    //throw new UnsupportedOperationException();
  }

  public List<T> iterativeBreadthFirstSearch() {
    // TODO: Task 2.
    throw new UnsupportedOperationException();
  }

  public List<T> iterativeDepthFirstSearch() {
    // TODO: Task 2.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveBreadthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveDepthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }
}
