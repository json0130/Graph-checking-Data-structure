package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.yaml.snakeyaml.nodes.Node;

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
    return roots;
    // throw new UnsupportedOperationException();
  }

  public boolean checkDesitination(T vertex) {
    for (Edge<T> edge : edges) {
      if (edge.getDestination() == vertex) {
        return false;
      }
    }
    return true;
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
    // throw new UnsupportedOperationException();
  }

  public boolean containElement(T source, T destination) {
    for (Edge<T> edge : edges) {
      if ((edge.getSource() == source) && (edge.getDestination() == destination)) {
        return true;
      }
    }
    return false;
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
    // throw new UnsupportedOperationException();
  }

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
    // throw new UnsupportedOperationException();
  }

  public List<T> iterativeBreadthFirstSearch() {
    // iterate through the graph using breadth first search
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
    // throw new UnsupportedOperationException();
  }

  public Set<T> getVerticies(T vertex) {
    Set<T> verticies = new TreeSet<>();
    for (Edge<T> edge : edges) {
      if (edge.getSource().equals(vertex)) {
        verticies.add(edge.getDestination());
      }
    }
    return verticies;
  }

  public List<T> iterativeDepthFirstSearch() {
    // iterate through the graph using depth first search
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
              }
            }
          }
        }
      }
    }
    return visited;
    // throw new UnsupportedOperationException();
  }

  private class CustomQueue<T> {
    private Node<T> head;
    private Node<T> tail;

    boolean isEmpty() {
      return head == null;
    }

    void enqueue(T data) {
      Node<T> newNode = new Node<>(data);
      if (isEmpty()) {
        head = newNode;
        tail = newNode;
      } else {
        tail.next = newNode;
        tail = newNode;
      }
    }

    T dequeue() {
      if (isEmpty()) {
        // throw new NoSuchElementException("Queue is empty");
      }
      T data = head.data;
      head = head.next;
      if (head == null) {
        tail = null;
      }
      return data;
    }

    T peek() {
      if (isEmpty()) {
        // throw new NoSuchElementException("Queue is empty");
      }
      return head.data;
    }

    private class Node<T> {
      T data;
      Node<T> next;

      Node(T data) {
        this.data = data;
      }
    }
  }

  class CustomStack<T> {
    private CustomLinkedList<T> list;

    public CustomStack() {
      list = new CustomLinkedList<>();
    }

    public void push(T data) {
      list.addFirst(data);
    }

    public T pop() {
      if (isEmpty()) {
        // throw new NoSuchElementException("Stack is empty");
      }

      T data = list.getFirst();
      list.removeFirst();
      return data;
    }

    public boolean isEmpty() {
      return list.isEmpty();
    }
  }

  class CustomLinkedList<T> {
    private Node<T> head;

    public void addFirst(T data) {
      Node<T> newNode = new Node<>(data);
      newNode.next = head;
      head = newNode;
    }

    public T getFirst() {
      if (isEmpty()) {
        // throw new NoSuchElementException("List is empty");
      }

      return head.data;
    }

    public void removeFirst() {
      if (isEmpty()) {
        // throw new NoSuchElementException("List is empty");
      }

      head = head.next;
    }

    public boolean isEmpty() {
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

  public List<T> recursiveBreadthFirstSearch() {
    Set<T> roots = this.getRoots();
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();
    CustomQueue<T> queue = new CustomQueue<>();

    if (verticies.isEmpty()) {
      return visited;
    }

    for (T root : roots) {
      recursiveBFS(root, visited, visitedSet, queue);
    }

    return visited;
    // throw new UnsupportedOperationException();
  }

  private void recursiveBFS(T vertex, List<T> visited, Set<T> visitedSet, CustomQueue<T> queue) {
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
      recursiveBFS(nextVertex, visited, visitedSet, queue);
    }
  }

  public List<T> recursiveDepthFirstSearch() {
    // iterate through the graph using depth first search
    Set<T> roots = this.getRoots();
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();

    for (T root : roots) {
      recursiveDFS(root, visited, visitedSet);
    }

    return visited;
    // throw new UnsupportedOperationException();
  }

  private void recursiveDFS(T vertex, List<T> visited, Set<T> visitedSet) {
    if (visitedSet.contains(vertex)) {
      return;
    }

    visited.add(vertex);
    visitedSet.add(vertex);

    Set<T> vert = getVerticies(vertex);
    for (T v : vert) {
      recursiveDFS(v, visited, visitedSet);
    }
  }
}
