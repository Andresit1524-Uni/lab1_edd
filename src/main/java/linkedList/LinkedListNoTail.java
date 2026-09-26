package linkedList;

import java.util.Objects;

import linkedList.base.LinkedList;
import linkedList.base.Node;

/** Lista enlazada simple sin punteros de cola */
public class LinkedListNoTail<T> implements LinkedList<T> {
    /** Cabeza de la lista */
    private Node<T> head;

    /** Tamaño de la lista */
    private int size = 0;

    @Override
    public void pushFront(T value) {
        Node<T> newNode = new Node<>(value);

        // Añade y traslada la cabeza
        newNode.next = head;
        head = newNode;
        size++;
    }

    @Override
    public void pushBack(T value) {
        Node<T> newNode = new Node<>(value);

        // Vacío
        if (isEmpty()) {
            head = newNode;
            size = 1;
            return;
        }

        // Busca el último nodo
        Node<T> tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }

        // Añade
        tail.next = newNode;
        size++;
    }

    @Override
    public T popFront() {
        // Vacío
        if (isEmpty())
            return null;

        Node<T> deleted = head;

        // Traslada la cabeza y retorna
        head = head.next;
        deleted.next = null;
        size--;
        return deleted.value;
    }

    @Override
    public T popBack() {
        // Vacío
        if (isEmpty())
            return null;

        Node<T> deleted;

        // Único elemento
        if (size == 1) {
            deleted = head;
            head = null;
            size = 0;
            return deleted.value;
        }

        // Busca el penúltimo elemento por doble avance
        // Siempre habrán al menos 2 elementos aquí, así que funciona
        Node<T> prev = head;
        while (prev.next.next != null) {
            prev = prev.next;
        }

        // Elimina y retorna
        deleted = prev.next;
        prev.next = null;
        size--;
        return deleted.value;
    }

    @Override
    public Node<T> find(T target) {
        // Vacío
        if (isEmpty())
            return null;

        // Busca hasta el correcto o hasta el último
        // Usa Objects.equals por seguridad
        Node<T> current = head;
        while (current != null) {
            if (Objects.equals(current.value, target))
                return current;

            current = current.next;
        }

        return null;
    }

    @Override
    public boolean erase(Node<T> target) {
        // Vacío o inválido
        if (isEmpty() || target == null)
            return false;

        // Borrar a la cabeza
        if (target == head) {
            popFront();
            return true;
        }

        // Busca el nodo previo
        Node<T> prev = head;
        while (prev.next != null && prev.next != target) {
            prev = prev.next;
        }

        // No encontrado (caso límite)
        if (prev.next == null)
            return false;

        // Elimina
        prev.next = target.next;
        target.next = null;
        size--;
        return true;
    }

    @Override
    public void addBefore(Node<T> target, T value) {
        // Vacío o inválido
        if (isEmpty() || target == null)
            return;

        // Añadir al principio
        if (target == head) {
            pushFront(value);
            return;
        }

        // Busca el nodo previo
        Node<T> prev = head;
        while (prev.next != null && prev.next != target) {
            prev = prev.next;
        }

        // No encontrado (caso límite)
        if (prev.next == null)
            return;

        // Añade el nuevo valor
        Node<T> newNode = new Node<>(value);
        newNode.next = target;
        prev.next = newNode;
        size++;
    }

    @Override
    public void addAfter(Node<T> target, T value) {
        // Vacío o inválido
        if (isEmpty() || target == null)
            return;

        // Añade el nuevo valor
        Node<T> newNode = new Node<>(value);
        newNode.next = target.next;
        target.next = newNode;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0 && head == null;
    }
}
