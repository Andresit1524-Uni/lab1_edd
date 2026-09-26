package linkedList.base;

/** Nodo base para las listas enlazadas */
public class Node<T> {
    /** Dato que contiene el nodo */
    public T value;

    /** Siguiente nodo */
    public Node<T> next;
    /** Nodo anterior (para listas enlazadas dobles) */
    public Node<T> prev;

    public Node(T value) {
        this.value = value;
    }
}
