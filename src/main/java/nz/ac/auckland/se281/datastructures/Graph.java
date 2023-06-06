package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A graph that is composed of a set of verticies and edges.
 *
 * <p>You must NOT change the signature of the existing methods or constructor of this class.
 *
 * @param <T> The type of each vertex, that have a total ordering.
 */
public class Graph<T extends Comparable<T>> {

  private class NumericalComparator implements Comparator<T> {
    /**
     * It overrides the NumericalComparator class to compare the verticies in reverse numerical
     * order.
     *
     * @param o1 the first vertex to compare.
     * @param o2 the second vertex to compare.
     * @return the integer value of the comparison.
     */
    @Override
    public int compare(T o1, T o2) {
      Integer i1 = Integer.parseInt(o1.toString());
      Integer i2 = Integer.parseInt(o2.toString());
      return i1.compareTo(i2);
    }
  }

  private class CustomQueue<T> {
    private Node<T> tail;
    private Node<T> head;

    public boolean isEmpty() {
      // Check if the queue is empty.
      return head == null;
    }

    public void enqueue(T data) {
      // Add a new node to the tail of the queue.
      Node<T> newNode = new Node<>(data);
      // Check if the queue is empty.
      if (isEmpty()) {
        head = newNode;
        tail = newNode;
      } else {
        tail.next = newNode;
        tail = newNode;
      }
    }

    public T dequeue() {
      // Remove the head of the queue.
      T data = head.data;
      head = head.next;
      if (head == null) {
        tail = null;
      }
      return data;
    }

    public T peek() {
      // Return the head of the queue.
      return head.data;
    }

    class Node<T> {
      private T data;
      private Node<T> next;

      Node(T data) {
        this.data = data;
      }
    }
  }

  private class CustomStack<T> {
    private CustomLinkedList<T> list;

    public CustomStack() {
      list = new CustomLinkedList<>();
    }

    public void push(T data) {
      // Add a new node to the head of the stack.
      list.addFirst(data);
    }

    public T pop() {
      // Remove the head of the stack.
      T data = list.getFirst();
      list.removeFirst();
      return data;
    }

    public boolean isEmpty() {
      // Check if the stack is empty.
      return list.isEmpty();
    }
  }

  private class CustomLinkedList<T> {
    private Node<T> head;

    public void addFirst(T data) {
      // Add a new node to the head of the linked list.
      Node<T> newNode = new Node<>(data);
      newNode.next = head;
      head = newNode;
    }

    public T getFirst() {
      // Return the head of the linked list.
      return head.data;
    }

    public void removeFirst() {
      // Remove the head of the linked list.
      head = head.next;
    }

    public boolean isEmpty() {
      // Check if the linked list is empty.
      return head == null;
    }

    class Node<T> {
      private T data;
      private Node<T> next;

      public Node(T data) {
        this.data = data;
      }
    }
  }

  private Set<Edge<T>> edges;
  private Set<T> verticies;

  /**
   * Creates a new graph.
   *
   * @param verticies The set of verticies in the graph.
   * @param edges The set of edges in the graph.
   */
  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
  }

  /**
   * It returns the set of roots of the graph.
   *
   * @return the set of roots.
   */
  public Set<T> getRoots() {
    // Check if the source and destination of edges are equal to each other.
    // If they are, then the vertex is a root.
    Set<T> roots = new HashSet<T>();
    for (T vertex : verticies) {
      for (Edge<T> edge : edges) {
        if ((edge.getDestination() != vertex) && (edge.getSource() == vertex)) {
          if (checkDesitination(vertex)) {
            roots.add(vertex);
          }
        } else if ((edge.getDestination() == vertex) && (edge.getSource() == vertex)) {
          if (checkDesitination(vertex)) {
            roots.add(vertex);
          }
        } else if (isEquivalence()) {
          T minValue = null;

          // Iterate over the elements of the HashSet
          Iterator<T> iterator = getEquivalenceClass(vertex).iterator();
          while (iterator.hasNext()) {
            T value = iterator.next();
            if (minValue == null || value.compareTo(minValue) < 0) {
              minValue = value;
            }
          }
          roots.add(minValue);
        }
      }
    }
    Set<T> orderSet = new TreeSet<T>(new NumericalComparator());
    for (T root : roots) {
      orderSet.add(root);
    }
    return orderSet;
  }

  /**
   * It checks if the selected vertex is a destination or not.
   *
   * @param vertex the vertex to find if it is a destination or not.
   * @return the boolean value of whether the vertex is a destination or not.
   */
  public boolean checkDesitination(T vertex) {
    for (Edge<T> edge : edges) {
      if (edge.getDestination() == vertex) {
        return false;
      }
    }
    return true;
  }

  /**
   * It checks if the graph is reflexive or not.
   *
   * @return the boolean value of whether the graph is a reflexive or not.
   */
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
  }

  /**
   * It checks if the graph is symmetric or not.
   *
   * @return the boolean value of whether the graph is a symmetric or not.
   */
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
  }

  /**
   * It checks if the graph is transitive or not.
   *
   * @return the boolean value of whether the graph is a transitive or not.
   */
  public boolean isTransitive() {
    // Check if the graph is transitive or not.
    for (Edge<T> edge : edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      for (Edge<T> edge2 : edges) {
        if (edge2.getSource() == destination) {
          if (!containElement(source, edge2.getDestination())) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * It checks if the set of edges is contains the element or not.
   *
   * @param source the source of the edge.
   * @param destination the destination of the edge.
   * @return the boolean value of whether the set of edges contain the input elements or not.
   */
  public boolean containElement(T source, T destination) {
    for (Edge<T> edge : edges) {
      if ((edge.getSource() == source) && (edge.getDestination() == destination)) {
        return true;
      }
    }
    return false;
  }

  /**
   * It checks if the graph is andti-symmetric or not.
   *
   * @return the boolean value of whether the graph is an anti-symmetric or not.
   */
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
  }

  /**
   * It checks if the graph has a equivalence relation or not.
   *
   * @return the boolean value of whether the graph is an equivalence relation or not.
   */
  public boolean isEquivalence() {
    // Check if the graph is an equivalence relation or not.
    if (isReflexive() && isSymmetric() && isTransitive()) {
      return true;
    }
    return false;
  }

  /**
   * returns the set of verticies that are equivalence class to the input vertex.
   *
   * @param vertex the vertex to find the equivalence class of.
   * @return the set of verticies that are equivalence class to the input vertex.
   */
  public Set<T> getEquivalenceClass(T vertex) {
    // Get the equivalence class of a vertex.
    Set<T> equivalenceClass = new HashSet<T>();
    if (isEquivalence()) {
      for (Edge<T> edge : edges) {
        T edgeSource = edge.getSource();
        if (edgeSource.equals(vertex)) {
          for (Edge<T> edge2 : edges) {
            if ((edge.getDestination().equals(edge2.getSource()))
                && (edge2.getDestination().equals(edge.getSource()))) {
              equivalenceClass.add(edge.getDestination());
            }
          }
        }
      }
    }
    return equivalenceClass;
  }

  /**
   * performs an iterative BreadthFirstSearch, and returns the searched list of verticies.
   *
   * @return the list of verticies, as searched through by the BreadthFirstSearch algorithm.
   */
  public List<T> iterativeBreadthFirstSearch() {
    // iterate through the graph using breadth first search
    // return the list of verticies, as searched through by the BreadthFirstSearch algorithm
    Set<T> roots = this.getRoots();
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();
    CustomQueue<T> queue = new CustomQueue<>();
    if (verticies.isEmpty()) {
      return visited;
    }
    for (T root : roots) {
      if (!visitedSet.contains(root)) {
        visited.add(root);
        visitedSet.add(root);
        queue.enqueue(root);
      }
      while (!queue.isEmpty()) {
        T currentVertex = queue.dequeue();
        for (Edge<T> edge : edges) {
          if (edge.getSource().equals(currentVertex)
              && !visitedSet.contains(edge.getDestination())) {
            Set<T> vert = getVerticies(currentVertex);
            for (T v : vert) {
              if (!visitedSet.contains(v)) {
                visited.add(v);
                visitedSet.add(v);
                queue.enqueue(v);
              }
            }
          }
        }
      }
    }
    return visited;
  }

  /**
   * returns the set of verticies that are adjacent to the given vertex, in reverse numerical order.
   *
   * @param vertex the vertex to find the adjacent verticies of.
   * @return the set of verticies that are adjacent to the given vertex.
   */
  public Set<T> getVerticies(T vertex) {
    Set<T> verticies = new TreeSet<>(new NumericalComparator());
    for (Edge<T> edge : edges) {
      if (edge.getSource().equals(vertex)) {
        verticies.add(edge.getDestination());
      }
    }
    return verticies;
  }

  /**
   * performs an iterative DepthFirstSearch, and returns the searched list of verticies.
   *
   * @return the list of verticies, as searched through by the DepthFirstSearch algorithm.
   */
  public List<T> iterativeDepthFirstSearch() {
    // iterate through the graph using depth first search
    // return the list of verticies, as searched through by the DepthFirstSearch algorithm
    Set<T> roots = this.getRoots();
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();
    CustomStack<T> stack = new CustomStack<>();

    if (verticies.isEmpty()) {
      return visited;
    }

    for (T root : roots) {
      if (!visitedSet.contains(root)) {
        visited.add(root);
        visitedSet.add(root);
        stack.push(root);
      }

      while (!stack.isEmpty()) {
        T current = stack.pop();
        for (Edge<T> edge : edges) {
          if (edge.getSource().equals(current) && !visitedSet.contains(edge.getDestination())) {
            Set<T> vert = getVerticies(current);
            for (T v : vert) {
              if (!visitedSet.contains(v)) {
                visited.add(v);
                visitedSet.add(v);
                stack.push(v);
              }
              Set<T> vert2 = getVerticies(v);
              for (T v2 : vert2) {
                if (!visitedSet.contains(v2)) {
                  visited.add(v2);
                  visitedSet.add(v2);
                  stack.push(v2);
                }
                Set<T> vert3 = getVerticies(v2);
                for (T v3 : vert3) {
                  if (!visitedSet.contains(v3)) {
                    visited.add(v3);
                    visitedSet.add(v3);
                    stack.push(v3);
                  }
                  Set<T> vert4 = getVerticies(v3);
                  for (T v4 : vert4) {
                    if (!visitedSet.contains(v4)) {
                      visited.add(v4);
                      visitedSet.add(v4);
                      stack.push(v4);
                    }
                    Set<T> vert5 = getVerticies(v4);
                    for (T v5 : vert5) {
                      if (!visitedSet.contains(v5)) {
                        visited.add(v5);
                        visitedSet.add(v5);
                        stack.push(v5);
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return visited;
  }

  /**
   * performs an recursive BreadthFirstSearch, and returns the searched list of verticies.
   *
   * @return the list of verticies, as searched through by the BreadthFirstSearch algorithm.
   */
  public List<T> recursiveBreadthFirstSearch() {
    // iterate through the graph using breadth first search
    Set<T> roots = this.getRoots();
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();
    CustomQueue<T> queue = new CustomQueue<>();

    if (verticies.isEmpty()) {
      return visited;
    }

    for (T root : roots) {
      recursiveBfs(root, visited, visitedSet, queue);
    }

    return visited;
  }

  /**
   * performs a recursive BreadthFirstSearch to update the linkedList.
   *
   * @param vertex roots of the vertex.
   * @param queue queue of verticies to visit.
   * @param visitedSet visited set of verticies of the graph.
   * @param vistied linkedList of the vertex.
   */
  private void recursiveBfs(T vertex, List<T> visited, Set<T> visitedSet, CustomQueue<T> queue) {
    // This is a recursive method that implements breadth first search
    if (visitedSet.contains(vertex)) {
      return;
    }

    visited.add(vertex);
    visitedSet.add(vertex);
    queue.enqueue(vertex);

    while (!queue.isEmpty()) {
      T currentVertex = queue.dequeue();
      Set<T> vert = getVerticies(currentVertex);
      for (T v : vert) {
        if (!visitedSet.contains(v)) {
          visited.add(v);
          visitedSet.add(v);
          queue.enqueue(v);
        }
      }
    }

    if (!queue.isEmpty()) {
      T nextVertex = queue.peek();
      recursiveBfs(nextVertex, visited, visitedSet, queue);
    }
  }

  /**
   * performs an recursive DepthFirstSearch, and returns the searched list of verticies.
   *
   * @return the list of verticies, as searched through by the DepthFirstSearch algorithm.
   */
  public List<T> recursiveDepthFirstSearch() {
    // iterate through the graph using depth first search
    Set<T> roots = this.getRoots();
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();

    for (T root : roots) {
      recursiveDfs(root, visited, visitedSet);
    }

    return visited;
  }

  /**
   * performs a recursive DepthFirstSearch to update the linkedList.
   *
   * @param vertex roots of the vertex.
   * @param visitedSet visited set of verticies of the graph.
   * @param vistied linkedList of the vertex.
   */
  private void recursiveDfs(T vertex, List<T> visited, Set<T> visitedSet) {
    // This is a recursive method that implements depth first search
    if (visitedSet.contains(vertex)) {
      return;
    }

    visited.add(vertex);
    visitedSet.add(vertex);

    Set<T> vert = getVerticies(vertex);
    for (T v : vert) {
      recursiveDfs(v, visited, visitedSet);
    }
  }
}
