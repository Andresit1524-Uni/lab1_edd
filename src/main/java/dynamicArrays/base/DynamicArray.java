package dynamicArrays.base;

import java.util.Objects;

/** Arreglo dinámico, con duplicación de tamaño */
public abstract class DynamicArray<T> {
    /** Tamaño de la lista. Equivale al primer índice libre */
    private int size = 0;
    /** Capacidad de la lista */
    private int capacity = 1;

    /** Arreglo */
    private T[] arr = (T[]) new Object[1];

    /**
     * Inserta un elemento al principio
     *
     * @param value valor a insertar
     */
    protected void pushFront(T value) {
        // Reasignación
        if (size == capacity)
            realloc();

        shiftForward();
        arr[0] = value;
        size++;
    }

    /**
     * Inserta un elemento al final
     *
     * @param value valor a insertar
     */
    protected void pushBack(T value) {
        // Reasignación
        if (size == capacity)
            realloc();

        arr[size] = value;
        size++;
    }

    /**
     * Elimina el primer elemento
     *
     * @return elemento eliminado
     */
    protected T popFront() {
        // Vacío
        if (isEmpty())
            return null;

        T deleted = arr[0];
        shiftBackward();
        arr[--size] = null;
        return deleted;
    }

    /**
     * Elimina el último elemento
     *
     * @return elemento eliminado
     */
    protected T popBack() {
        // Vacío
        if (isEmpty())
            return null;

        T deleted = arr[--size];
        arr[size] = null;
        return deleted;
    }

    /**
     * Elimina la primer aparición del un valor
     *
     * @param target valor a buscar y eliminar
     * @return true si el valor fue eliminado
     */
    protected boolean delete(T target) {
        int index = -1;

        // Búsqueda
        for (int i = 0; i < size; i++) {
            if (Objects.equals(arr[i], target)) {
                index = i;
                break;
            }
        }

        if (index == -1)
            return false;

        // Traslado
        for (int i = index; i < size - 1; i++) {
            arr[i] = arr[i + 1];
        }

        size--;
        arr[size] = null;
        return true;
    }

    /**
     * @return true si la lista está vacía
     */
    protected boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return tamaño de la lista
     */
    protected int size() {
        return size;
    }

    /**
     * @return el primer elemento del arreglo
     */
    protected T front() {
        return arr[0];
    }

    /**
     * @return el último elemento del arreglo
     */
    protected T back() {
        return arr[size - 1];
    }

    /**
     * Auxiliar: reasigna la lista a otra del doble de capacidad.
     * Esto se hace justo al llenarse (top == 2^n) y no cuando se le ingresa un
     * elemento a una lista llena
     */
    protected void realloc() {
        // Nuevo arreglo
        capacity *= 2;
        T[] newArr = (T[]) new Object[capacity];

        // Copia (solo es necesario hasta el tope)
        for (int i = 0; i < size; i++) {
            newArr[i] = arr[i];
        }

        arr = newArr;
    }

    /**
     * Auxiliar: traslada todos los elementos hacia adelante una posición
     */
    private void shiftForward() {
        for (int i = size; i > 0; i--) {
            arr[i] = arr[i - 1];
        }

        arr[0] = null;
    }

    /**
     * Auxiliar: traslada todos los elementos hacia atrás una posición.
     */
    private void shiftBackward() {
        for (int i = 0; i < size - 1; i++) {
            arr[i] = arr[i + 1];
        }
    }
}
