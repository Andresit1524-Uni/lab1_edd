package linkedList.base;

/** Nodo base para las listas enlazadas */
public class Node<T> {
    /** Dato que contiene el nodo */
    T data;

    /** Siguiente nodo */
    Node<T> next;
    /** Nodo anterior (para listas enlazadas dobles) */
    Node<T> prev;
}
