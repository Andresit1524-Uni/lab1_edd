package linkedList;

import java.util.Objects;

import linkedList.base.Node;

/** Lista enlazada simple con puntero de cola */
public class LinkedList<T> implements linkedList.base.LinkedList<T> {
    /** Cabeza de la lista */
    private Node<T> head;

    /** Cola de la lista */
    private Node<T> tail;

    /** Tamaño de la lista */
    private int size = 0;

    @Override
    public void pushFront(T value) {
        Node<T> newNode = new Node<>(value);

        // Vacío
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            size = 1;
            return;
        }

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
            tail = newNode;
            size = 1;
            return;
        }

        // Añade al final usando el puntero a cola
        tail.next = newNode;
        tail = newNode;
        size++;
    }

    @Override
    public T popFront() {
        // Vacío
        if (isEmpty())
            return null;

        Node<T> deleted = head;

        // Único elemento
        if (size == 1) {
            head = null;
            tail = null;
            size = 0;
            return deleted.value;
        }

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
            tail = null;
            size = 0;
            return deleted.value;
        }

        // Busca el penúltimo elemento por avance
        // Como tenemos la cola, no requerimos doble avance
        Node<T> prev = head;
        while (prev.next != tail) {
            prev = prev.next;
        }

        // Elimina y traslada la cola
        deleted = tail;
        tail = prev;
        tail.next = null;
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

        // Si el nodo a borrar es la cola, actualiza el puntero
        if (target == tail) {
            tail = prev;
        }

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

        // Añadir al final si es la cola
        if (target == tail) {
            pushBack(value);
            return;
        }

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
