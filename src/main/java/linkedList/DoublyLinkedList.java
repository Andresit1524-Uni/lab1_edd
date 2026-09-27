package linkedList;

import java.util.Objects;

import linkedList.base.Node;

/** Lista enlazada doble con puntero de cola */
public class DoublyLinkedList<T> implements linkedList.base.ILinkedList<T> {
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
        head.prev = newNode;
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
        newNode.prev = tail;
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
        head.prev = null;
        deleted.next = null;
        size--;
        return deleted.value;
    }

    @Override
    public T popBack() {
        // Vacío
        if (isEmpty())
            return null;

        Node<T> deleted = tail;

        // Único elemento
        if (size == 1) {
            head = null;
            tail = null;
            size = 0;
            return deleted.value;
        }

        // Traslada la cola y retorna
        tail = tail.prev;
        tail.next = null;
        deleted.prev = null;
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

        // Borrar a la cola
        if (target == tail) {
            popBack();
            return true;
        }

        // En lista doble, target.prev no puede ser null si está en medio
        if (target.prev == null)
            return false;

        // Elimina nodo intermedio
        target.prev.next = target.next;
        if (target.next != null) {
            target.next.prev = target.prev;
        }

        target.prev = null;
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

        // En lista doble, target.prev no puede ser null si está en la lista y no es
        // head
        if (target.prev == null)
            return;

        // Añade el nuevo valor
        Node<T> newNode = new Node<>(value);
        newNode.prev = target.prev;
        newNode.next = target;
        target.prev.next = newNode;
        target.prev = newNode;
        size++;
    }

    @Override
    public void addAfter(Node<T> target, T value) {
        // Vacío o inválido
        if (isEmpty() || target == null)
            return;

        // Añadir al final
        if (target == tail) {
            pushBack(value);
            return;
        }

        // Añade el nuevo valor
        Node<T> newNode = new Node<>(value);
        newNode.next = target.next;
        newNode.prev = target;

        if (target.next != null) {
            target.next.prev = newNode;
        }

        target.next = newNode;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0 && head == null;
    }

    @Override
    public int size() {
        return size;
    }
}
