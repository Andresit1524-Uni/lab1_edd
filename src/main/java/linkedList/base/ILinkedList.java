package linkedList.base;

/** Interfaz para listas enlazadas */
public interface ILinkedList<T> {
    /**
     * Inserta un elemento al principio
     *
     * @param value valor a insertar
     */
    void pushFront(T value);

    /**
     * Inserta un elemento al final
     *
     * @param value valor a insertar
     */
    void pushBack(T value);

    /**
     * Elimina el primer elemento
     *
     * @return valor eliminado
     */
    T popFront();

    /**
     * Elimina el último elemento
     *
     * @return valor eliminado
     */
    T popBack();

    /**
     * Busca un elemento
     *
     * @param target valor a buscar
     * @return el nodo que contiene el valor encontrado
     */
    Node<T> find(T target);

    /**
     * Elimina un valor
     *
     * @param target nodo con el valor a eliminar
     * @return true si el elemento fue eliminado
     */
    boolean erase(Node<T> target);

    /**
     * Añade un valor antes de otro
     *
     * @param target nodo para insertar antes
     * @param value  valor a insertar
     */
    void addBefore(Node<T> target, T value);

    /**
     * Añade un valor después de otro
     *
     * @param target nodo para insertar después
     * @param value  valor a insertar
     */
    void addAfter(Node<T> target, T value);

    /**
     * Verifica si la lista está vacía
     *
     * @return true si al lista está vacía
     */
    boolean isEmpty();

    /**
     * @return el tamaño de la lista
     */
    int size();
}
