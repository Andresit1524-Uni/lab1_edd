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
    private static final int WARMUP_RUNS = 1;
    /** Número de repeticiones para calcular el promedio por medición */
    private static final int BENCHMARK_RUNS = 5;

    public static void main(String[] args) {
        System.out.println("Iniciando suite de benchmarks...");

        runWarmup();

        // Benchmarks de listas enlazadas
        benchmarkLinkedList("LinkedListNoTail", 0);
        benchmarkLinkedList("LinkedList", 1);
        benchmarkLinkedList("DoublyLinkedListNoTail", 2);
        benchmarkLinkedList("DoublyLinkedList", 3);

        // Benchmarks de estructuras dinámicas
        benchmarkStack();
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
            int targetVal = random.nextInt(Math.max(1, n * 2));

            // Los métodos que no sean push exigen listas ya ocupadas
            if (!method.equals("pushFront") && !method.equals("pushBack")) {
                populateLinkedList(list, n);
            }

            // Ejecuta el método y calcula el tiempo
            long start = System.nanoTime();
            executeLinkedListMethod(list, method, targetVal);
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

            // Cualquier método diferente a apilar exige una pila ya ocupada
            if (!method.equals("push")) {
                populateStack(stack, n);
            }

            // Ejecuta el método y calcula el tiempo
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

            // Cualquier método diferente a encolar requiere una lista ya ocupada
            if (!method.equals("enqueue")) {
                populateQueue(queue, n);
            }

            // Ejecuta el método y calcula el tiempo
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
     * Despacha la llamada al método correspondiente de la lista enlazada.
     */
    private static void executeLinkedListMethod(ILinkedList<Integer> list, String method, int targetVal) {
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
                Node<Integer> n1 = list.find(targetVal);
                if (n1 != null)
                    list.erase(n1);
                break;
            case "addBefore":
                Node<Integer> n2 = list.find(targetVal);
                if (n2 != null)
                    list.addBefore(n2, -1);
                break;
            case "addAfter":
                Node<Integer> n3 = list.find(targetVal);
                if (n3 != null)
                    list.addAfter(n3, -1);
                break;
            case "isEmpty":
                list.isEmpty();
                break;
            case "size":
                list.size();
                break;
        }
    }

    /**
     * Escribe la cabecera del archivo CSV con las columnas de los tamaños N.
     */
    private static void writeHeader(PrintWriter writer) {
        writer.print("Method");
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
            list.pushBack(random.nextInt(n * 2));
        }
    }

    /** Llenado inicial para pila */
    private static void populateStack(Stack<Integer> stack, int n) {
        for (int i = 0; i < n; i++) {
            stack.push(random.nextInt(n * 2));
        }
    }

    /** Llenado inicial para cola */
    private static void populateQueue(Queue<Integer> queue, int n) {
        for (int i = 0; i < n; i++) {
            queue.enqueue(random.nextInt(n * 2));
        }
    }
}
