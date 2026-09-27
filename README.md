# Implementación y análisis de complejidad de listas, pilas y colas en Java

> De Hayran Andrés López

## Objetivo
Implementar las estructuras de datos `List` (listas enlazadas), `Stack` (pila) y `Queue` (cola) en Java, abarcando tanto arreglos dinámicos como listas enlazadas, realizando un análisis de complejidad de los métodos asociados.

## Dependencias
- Java 21 (OpenJDK 21.0.12.1 2026-08-18 LTS)
- XChart 3.8.8

## Estructuras de datos a implementar
> [!Warning]
> 
> _Hemos simplificado los nombres de las estructuras de datos respecto a los usados en la guía. El resto de las especificaciones se conservan._

1. Listas enlazadas en todas sus variantes:
   - `LinkedListNoTail`: Lista enlazada
   - `LinkedList`: Lista enlazada con puntero a cola
   - `DoublyLinkedListNoTail`: Lista enlazada doble
   - `DoublyLinkedList`: Lista enlazada doble con puntero a cola

2. Pilas y colas genéricas (`Stack<T>` y `Queue<T>`) basadas en arreglos dinámicos:
   - `Stack<T>`: Pila mediante arreglo dinámico
   - `Queue<T>`: Cola mediante arreglo dinámico

    Para los arreglos dinámicos elegiremos duplicación de capacidad en cada reasignación.

## API de cada estructura

### Listas enlazadas (todas las variantes)
```Java
public interface LinkedList<T> {
    // Inserta al inicio
    void pushFront(T value); 
    // Inserta al final
    void pushBack(T value); 
    
    // Elimina al inicio (retorna el dato)
    T popFront();
    // Elimina al final (retorna el dato)
    T popBack();

    // Busca un elemento por valor. Retorna el nodo que lo contiene
    Node<T> find(T target);
    // Elimina el nodo dado. Retorna su éxito o fracaso
    boolean erase(Node<T> target);

    // Añade antes del elemento dado
    void addBefore(Node<T> target, T value);
    // Añade después del elemento dado 
    void addAfter(Node<T> target, T value);
    
    // Verifica si la lista está vacía
    boolean isEmpty();
}
```

### Pilas
```Java
class Stack<T> {
    // Inserta un elemento en la cima
    void push(T value);
    // Elimina y retorna el elemento en la cima
    T pop();
    // Retorna el elemento en la cima sin eliminarlo
    T peek();

    // Elimina la primera aparición del valor. Retorna si lo logró o no
    boolean delete(T target);

    // Verifica si la pila está vacía
    boolean isEmpty();
    // Retorna el número de elementos en la pila
    int size();
}
```

### Colas
```Java
class Queue<T> {
    // Inserta un elemento al final
    void enqueue(T value);
    // Elimina y retorna el primer elemento
    T dequeue();
    // Retorna el primer elemento sin eliminarlo
    T front();

    // Elimina la primera aparición del valor. Retorna si lo logró o no
    boolean delete(T target);

    // Verifica si la cola está vacía
    boolean isEmpty();
    // Retorna el número de elementos en la cola
    int size();
}
```

## Análisis de complejidad y visualización
1. Hipótesis sobre el rendimiento de cada estructura de datos (incluyendo reasignación de arreglos dinámicos)
2. Medidas de tamaño exponencial (10, 100, 1000, ...) para abarcar todas las escalas en todas las estructuras y todos sus métodos
3. Entradas aleatorias para todos los casos
4. Gráficas en formato logarítmico con XChart (dependencia en Java)
5. Comparativa de métodos equivalentes (un método de cualquier lista vs pila o cola, el más óptimo)

## Conclusiones e informe
- Detallar cuando se usa mejor cada estructura y cada implementación
- Identificar usos reales de pilas y colas
- Analizar ventajas y desventajas de arreglos dinámicos vs. listas enlazadas en Java

## Suite de Benchmarks y Exportación a CSV
La suite de benchmarks está implementada de forma estructurada e imperativa en [Main.java](src/main/java/Main.java). Mide el tiempo promedio de ejecución (en nanosegundos) para todas las operaciones de cada estructura de datos.

### Funcionamiento de la Suite:
1. **JVM Warmup:** Ejecuta pasadas iniciales descartadas para permitir que la JVM realice optimizaciones JIT.
2. **Escala Exponencial ($N$):** Evalúa cada método con tamaños $N \in \{10, 100, 1000, 10000, 100000\}$.
3. **Población con Datos Aleatorios:** Se llenan las estructuras previamente usando `Random(42)` para métodos de eliminación o consulta.
4. **Archivos CSV Matriciales Separados:** Se genera un archivo `.csv` independiente por cada estructura de datos.

### Formato de Salida CSV:
Las filas corresponden a cada método de la estructura y las columnas representan el tiempo promedio obtenido para cada tamaño \(N\):

```csv
Método,10,100,1000,10000,100000
pushFront,150,180,210,250,290
pushBack,120,130,140,150,160
popFront,90,95,100,105,110
...
```
