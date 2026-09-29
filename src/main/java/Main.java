import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import dynamicArrays.Queue;
import dynamicArrays.Stack;
import linkedList.DoublyLinkedList;
import linkedList.DoublyLinkedListNoTail;
import linkedList.LinkedList;
import linkedList.LinkedListNoTail;
import linkedList.base.ILinkedList;
import linkedList.base.Node;

/**
 * Suite de benchmarks para medir el rendimiento de listas enlazadas, pilas y
 * colas.
 * Genera reportes de tiempo promedio en formato CSV por cada estructura.
 */
public class Main {
    /** Generador de números aleatorios reproducible mediante semilla fija */
    private static final Random random = new Random();
    /** Tamaños exponenciales de prueba */
    private static final int[] SIZES = { 10, 100, 1000, 10000, 100000 };

    /** Número de ejecuciones preliminares para calentamiento de JVM (JIT) */
    private static final int WARMUP_RUNS = 100;
    /** Número de repeticiones para calcular el promedio por medición */
    private static final int BENCHMARK_RUNS = 1;

    public static void main(String[] args) {
        System.out.println("Iniciando suite de benchmarks...");

        System.out.println("Ejecutando calentamiento (Warmup)...");
        runWarmup();

        // Benchmarks de listas enlazadas
        System.out.println("-> Midiendo LinkedListNoTail...");
        benchmarkLinkedList("LinkedListNoTail", 0);

        System.out.println("-> Midiendo LinkedList (con cola)...");
        benchmarkLinkedList("LinkedList", 1);

        System.out.println("-> Midiendo DoublyLinkedListNoTail...");
        benchmarkLinkedList("DoublyLinkedListNoTail", 2);

        System.out.println("-> Midiendo DoublyLinkedList (con cola)...");
        benchmarkLinkedList("DoublyLinkedList", 3);

        // Benchmarks de estructuras dinámicas
        System.out.println("-> Midiendo Stack...");
        benchmarkStack();

        System.out.println("-> Midiendo Queue...");
        benchmarkQueue();

        System.out.println("Benchmarks completados con éxito. Archivos CSV generados.");
    }

    /**
     * Calentamiento inicial para permitir que el JIT de la JVM optimice el código.
     */
    private static void runWarmup() {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            LinkedList<Integer> list = new LinkedList<>();
            for (int j = 0; j < 1000; j++) {
                list.pushBack(j);
            }
        }
    }

    /**
     * Ejecuta las pruebas de rendimiento para una variante de lista enlazada.
     *
     * @param structName nombre de la estructura
     * @param type       identificador del tipo de lista
     */
    private static void benchmarkLinkedList(String structName, int type) {
        String fileName = "data/" + structName + "_results.csv";
        String[] methods = {
                "pushFront", "pushBack", "popFront", "popBack", "find", "erase",
                "addBefore", "addAfter", "isEmpty", "size"
        };

        runStructureBenchmark(fileName, methods, (method, n) -> {
            ILinkedList<Integer> list = createLinkedList(type);

            // Llenar la lista previa para evaluar métodos sobre tamaño N
            populateLinkedList(list, n);

            // Pre-buscar nodo fuera de la toma de tiempo para métodos que reciben Node<T>
            Node<Integer> targetNode = null;
            int targetVal = random.nextInt(Math.max(1, n));
            if (method.equals("erase") || method.equals("addBefore") || method.equals("addAfter")) {
                targetNode = list.find(targetVal);
            }

            // Mide el método en cuestión
            long start = System.nanoTime();
            switch (method) {
                case "pushFront":
                    list.pushFront(targetVal);
                    break;
                case "pushBack":
                    list.pushBack(targetVal);
                    break;
                case "popFront":
                    list.popFront();
                    break;
                case "popBack":
                    list.popBack();
                    break;
                case "find":
                    list.find(targetVal);
                    break;
                case "erase":
                    if (targetNode != null)
                        list.erase(targetNode);
                    break;
                case "addBefore":
                    if (targetNode != null)
                        list.addBefore(targetNode, -1);
                    break;
                case "addAfter":
                    if (targetNode != null)
                        list.addAfter(targetNode, -1);
                    break;
                case "isEmpty":
                    list.isEmpty();
                    break;
                case "size":
                    list.size();
                    break;
            }
            return System.nanoTime() - start;
        });
    }

    /**
     * Ejecuta las pruebas de rendimiento para la pila basada en arreglo dinámico.
     */
    private static void benchmarkStack() {
        String fileName = "data/Stack_results.csv";
        String[] methods = { "push", "pop", "peek", "delete", "isEmpty", "size" };

        runStructureBenchmark(fileName, methods, (method, n) -> {
            Stack<Integer> stack = new Stack<>();
            int targetVal = random.nextInt(Math.max(1, n * 2));

            populateStack(stack, n);

            long start = System.nanoTime();
            switch (method) {
                case "push":
                    stack.push(targetVal);
                    break;
                case "pop":
                    stack.pop();
                    break;
                case "peek":
                    stack.peek();
                    break;
                case "delete":
                    stack.delete(targetVal);
                    break;
                case "isEmpty":
                    stack.isEmpty();
                    break;
                case "size":
                    stack.size();
                    break;
            }
            return System.nanoTime() - start;
        });
    }

    /**
     * Ejecuta las pruebas de rendimiento para la cola basada en arreglo dinámico.
     */
    private static void benchmarkQueue() {
        String fileName = "data/Queue_results.csv";
        String[] methods = { "enqueue", "dequeue", "peek", "delete", "isEmpty", "size" };

        runStructureBenchmark(fileName, methods, (method, n) -> {
            Queue<Integer> queue = new Queue<>();
            int targetVal = random.nextInt(Math.max(1, n * 2));

            populateQueue(queue, n);

            long start = System.nanoTime();
            switch (method) {
                case "enqueue":
                    queue.enqueue(targetVal);
                    break;
                case "dequeue":
                    queue.dequeue();
                    break;
                case "peek":
                    queue.peek();
                    break;
                case "delete":
                    queue.delete(targetVal);
                    break;
                case "isEmpty":
                    queue.isEmpty();
                    break;
                case "size":
                    queue.size();
                    break;
            }
            return System.nanoTime() - start;
        });
    }

    /**
     * Interfaz para la medición individual de un método dado un tamaño N.
     */
    @FunctionalInterface
    private interface MethodRunner {
        long run(String method, int n);
    }

    /**
     * Orquestador genérico para la iteración sobre métodos, tamaños N y la
     * escritura en CSV.
     */
    private static void runStructureBenchmark(String fileName, String[] methods, MethodRunner runner) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writeHeader(writer);

            for (String method : methods) {
                writer.print(method);
                for (int n : SIZES) {
                    long totalTime = 0;

                    for (int run = 0; run < BENCHMARK_RUNS; run++) {
                        totalTime += runner.run(method, n);
                    }

                    long avgTime = totalTime / BENCHMARK_RUNS;
                    writer.print("," + avgTime);
                }
                writer.println();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Escribe la cabecera del archivo CSV con las columnas de los tamaños N.
     */
    private static void writeHeader(PrintWriter writer) {
        writer.print("Método");
        for (int n : SIZES) {
            writer.print("," + n);
        }
        writer.println();
    }

    /**
     * Fábrica para crear la instancia deseada de ILinkedList.
     */
    private static ILinkedList<Integer> createLinkedList(int type) {
        switch (type) {
            case 0:
                return new LinkedListNoTail<>();
            case 1:
                return new LinkedList<>();
            case 2:
                return new DoublyLinkedListNoTail<>();
            case 3:
                return new DoublyLinkedList<>();
            default:
                throw new IllegalArgumentException("Tipo de lista no válido: " + type);
        }
    }

    /** Llenado inicial para listas enlazadas */
    private static void populateLinkedList(ILinkedList<Integer> list, int n) {
        for (int i = 0; i < n; i++) {
            list.pushBack(i);
        }
    }

    /** Llenado inicial para pila */
    private static void populateStack(Stack<Integer> stack, int n) {
        for (int i = 0; i < n; i++) {
            stack.push(i);
        }
    }

    /** Llenado inicial para cola */
    private static void populateQueue(Queue<Integer> queue, int n) {
        for (int i = 0; i < n; i++) {
            queue.enqueue(i);
        }
    }
}
