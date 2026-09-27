package dynamicArrays;

import dynamicArrays.base.DynamicArray;

/**
 * Cola basada en arreglo dinámico. Implementa alias sobre un arreglo dinámico
 */
public class Queue<T> extends DynamicArray<T> {
    /**
     * Inserta un elemento en la cola
     *
     * @param value elemento a añadir
     */
    public void push(T value) {
        pushBack(value);
    }

    /**
     * Elimina el elemento de la cola
     *
     * @return elemento eliminado
     */
    public T pop() {
        return popFront();
    }

    /**
     * Accede el siguiente elemento a eliminar sin eliminarlo
     *
     * @return siguiente elemento a eliminar
     */
    public T peek() {
        return front();
    }

    /**
     * Elimina la primer aparición de un elemento
     *
     * @return true si el elemento fue eliminado
     */
    public boolean delete(T target) {
        return super.delete(target);
    }

    /**
     * @return true si la cola está vacía
     */
    public boolean isEmpty() {
        return super.isEmpty();
    }

    /**
     * @return tamaño de la cola
     */
    public int size() {
        return super.size();
    }
}
