package dynamicArrays;

import dynamicArrays.base.DynamicArray;

/**
 * Pila basada en arreglo dinámico. Implementa alias sobre un arreglo dinámico
 */
public class Stack<T> extends DynamicArray<T> {
    /**
     * Inserta un elemento en la pila
     *
     * @param value elemento a añadir
     */
    public void push(T value) {
        pushBack(value);
    }

    /**
     * Elimina el elemento de la pila
     *
     * @return elemento eliminado
     */
    public T pop() {
        return popBack();
    }

    /**
     * Accede el siguiente elemento a eliminar sin eliminarlo
     *
     * @return siguiente elemento a eliminar
     */
    public T peek() {
        return back();
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
     * @return true si la pila está vacía
     */
    public boolean isEmpty() {
        return super.isEmpty();
    }

    /**
     * @return tamaño de la pila
     */
    public int size() {
        return super.size();
    }
}
