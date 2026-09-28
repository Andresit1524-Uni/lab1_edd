#import "@preview/lilaq:0.6.0" as lq
#import "@preview/sourcecraft:0.1.0": *

#show: setup-sourceuml

#set document(
  title: [Implementación y análisis de complejidad de listas, pilas y colas en Java],
)
#set text(lang: "es", font: "Ancizar Sans")
#show raw: set text(font: "Google Sans Code NF")
#show math.equation: set text(font: "Erewhon Math")

#align(center)[
  #title()
  *Hayran Andrés López González*

  #divider()
]

= Objetivo del taller
Implementar las estructuras de datos `List` (listas enlazadas), `Stack` (pila) y `Queue` (cola) en Java, abarcando tanto arreglos dinámicos como listas enlazadas, realizando un análisis de complejidad de los métodos asociados.

= Explicación de la implementación
Para la implementación de este ejercicio se ha seguido la siguiente estructura:

- Una interfaz `ILinkedList` con los métodos comunes para todas las listas enlazadas (insertar, eliminar, buscar, etc.).
- Una clase abstracta `DynamicArray` con los métodos de un arreglo dinámico ya implementados. Es abstracta porque no la usaremos directamente.

De aquí parten las clases que si se usan:

- Las 4 variantes de listas enlazadas, todas implementando la interfaz `ILinkedList`:
  - `LinkedList`
  - `LinkedListNoTail`
  - `DoublyLinkedList`
  - `DoublyLinkedListNoTail`
- `Stack` y `Queue` para las pilas y colas respectivamente

El diagrama UML de este proyecto es el siguiente:

#figure[
  #image("assets/uml.png")
]

#pagebreak()

= Análisis de complejidad

== Listas enlazadas

=== Simples
- Buscar siempre toma $O(n)$, y es el cuello de botella de los siguientes métodos
- Insertar y eliminar al frente es inmediato porque tenemos un puntero a la cabeza de la lista y el siguiente nodo. Complejidad $O(1)$
- Insertar y eliminar al final requiere buscar el último nodo y el penúltimo (al eliminar), por lo que la complejidad es $O(n)$
- Añadir antes de un nodo requiere buscar el anterior ($O(n)$) pero añadir después se puede hacer de inmediato porque tenemos acceso al siguiente nodo ($O(1)$)
- Eliminar un nodo requiere buscar el anterior, entonces su complejidad es $O(n)$
- Verificar que está vacío es tan sencillo como ver si hay cabeza. Complejidad $O(1)$

Ten en cuenta que los métodos `addBefore` y `addAfter` ofrecen un puntero directo al nodo, entonces por ello pueden ser más rápidos.

=== Simples (con cola)
El nuevo puntero con cola hace que añadir elementos al final sea más sencillo porque tenemos la cola directamente. Pero el resto de operaciones siguen igual en cuanto requieren búsquedas (especialmente para buscar el nodo anterior a uno dado).

=== Dobles
Las listas enlazadas dobles introducen un puntero anterior que nos permite trabajar hacia atrás también. Gracias a eso, ahora borrar nodos y añadir antes de uno puede hacerse en tiempo constante porque podemos ver hacia atrás y cablear directamente.

Pero como no tienen puntero a cola, aún debemos buscarla, degradando las operaciones al final de la lista.

=== Dobles (con cola)
Ahora con el puntero a cola, podemos ir directo al final de la lista y operar sobre ella, haciendo que la adición/eliminación de elementos se torne de tiempo constante.

Con estas consideraciones, la tabla de complejidades queda como se ve acontinuación. En azul las operaciones con rendimiento mejorado y en rojo las que quedan peor a comparación.

#figure[
  #let best(eq) = text(fill: blue.darken(20%), eq)
  #let worst(eq) = text(fill: red.darken(20%), eq)

  #table(
    columns: 5,
    [*Operación*], [*Simple*], [*Simple (con cola)*], [*Doble*], [*Doble (con cola)*],
    `pushFront`, $O(1)$, $O(1)$, $O(1)$, $O(1)$,
    `pushBack`, worst($O(n)$), best($O(1)$), worst($O(n)$), best($O(1)$),
    `popFront`, $O(1)$, $O(1)$, $O(1)$, $O(1)$,
    `popBack`, worst($O(n)$), worst($O(n)$), worst($O(n)$), best($O(1)$),
    `find`, $O(n)$, $O(n)$, $O(n)$, $O(n)$,
    `erase`, worst($O(n)$), worst($O(n)$), best($O(1)$), best($O(1)$),
    `addBefore`, worst($O(n)$), worst($O(n)$), best($O(1)$), best($O(1)$),
    `addAfter`, $O(1)$, $O(1)$, $O(1)$, $O(1)$,
    `isEmpty`, $O(1)$, $O(1)$, $O(1)$, $O(1)$,
  )
]

Comparando las tablas obtenemos que la mejor estructura en términos de rendimiento general es la *lista enlazada doble con puntero a cola*, al permitir un avance en dos direcciones y acceso a los dos extremos.

== Arreglos dinámicas, pilas y colas
Los arreglos dinámicos son arreglos suyo tamaño puede crecer de manera indefinida, permitiendo acceder a elementos de manera aleatoria (por índice) y reasignarse automáticamente cuando se llena.

Eliminar o añadir un elemento de una posición diferente a la última requiere trasladar el resto de elementos hacia adelante o hacia atrás, lo cual hace que su complejidad sea de $O(1)$. El resto de operaciones pueden hacerse de manera inmediata y por ende con $O(1)$.

La complejidad de sus operaciones es la siguiente:

#figure[
  #table(
    columns: 4,
    [*Operación*], [*Pila*], [*Cola*], [*Complejidad*],
    `pushFront`, `-`, `-`, $O(n)$,
    `pushBack`, `push`, `enqueue`, $O(1)$,
    `popFront`, `-`, `dequeue`, $O(n)$,
    `popBack`, `pop`, `-`, $O(1)$,
    `delete`, `delete`, `delete`, $O(n)$,
    `isEmpty`, `isEmpty`, `isEmpty`, $O(1)$,
    `size`, `size`, `size`, $O(1)$,
    `front`, `-`, `front`, $O(1)$,
    `back`, `peek`, `-`, $O(1)$,
  )
]

Ten en cuenta que se están usando arreglos dinámicos llenados de inicio a fin. Con arreglos circulares se puede lograr más rendimiento, y con arreglos circulares dinámicos (no implementados en este caso) también una capacidad máxima arbitraria.

= Gráficación y resultados empíricos
Para analizar el comportamiento real de estos métodos utilizaremos medidas basadas en `java.time.Instant` y `java.time.Duration`. El esquema de medición es el siguiente:

- Mediremos el rendimiento con 10, 100, 1000, ..., hasta $10^10$ operaciones
- Esta medición es por cada método de cada clase implementada
- Haremos la tabla de tiempos y gráficas para los mismos. Una tabla por cada estructura de datos
- Las gráficas serán en escala logarítmica para evidenciar todas las escalas adecuadamente. Una gráfica por cada tabla
- Se usarán *nanosegundos* para las mediciones

== Funcionamiento de la Suite:
1. *JVM Warmup:* Ejecuta pasadas iniciales descartadas para permitir que la JVM realice optimizaciones JIT.
2. *Escala Exponencial ($N$):* Evalúa cada método con tamaños $N in {10, 100, 1000, 10000, 100000}$.
3. *Población con Datos Aleatorios:* Se llenan las estructuras previamente usando valores aleatorios para métodos de eliminación o consulta.
4. *Archivos CSV Matriciales Separados:* Se genera un archivo `.csv` independiente por cada estructura de datos.

El código para registrar estos tiempos no interfiere con los algoritmos, y se ubica en la clase `Main`. Las gráficas se crean con *lilaq*, una librería de *Typst*, el mismo sistema con el que se ha compilado este PDF. los datos para lograrlo se registrarán en un CSV.

== Listas enlazadas

/// Tabla y gráfica para benchmarks
#let bench-table-plot(source) = {
  let data = csv(source)

  columns(2)[
    #set text(size: 6pt)
    #table(
      columns: 6,
      stroke: 0.7pt,
      ..data.at(0).map(it => [*#it*]),
      ..data.slice(1).map(it => (raw(it.at(0)), ..it.slice(1))).flatten(),
    )

    #colbreak()

    #figure[
      #lq.diagram(
        width: 8cm,
        height: 5cm,
        xscale: "log",
        yscale: "log",
        legend: (position: (100% + .5em, 0%)),
        xlabel: [Número de operaciones],
        ylabel: [Tiempo (ns)],
        ..data
          .slice(1)
          .map(it => {
            let label = it.at(0)
            let data = it.slice(1).map(i => int(i))

            return lq.plot(
              lq.logspace(1, 5, num: 5),
              data,
              label: label,
            )
          }),
      )
    ]
  ]
}

=== 1. Simples
#bench-table-plot("../data/LinkedListNoTail_results.csv")

=== 2. Simples (con cola)
#bench-table-plot("../data/LinkedList_results.csv")

#pagebreak()

=== 3. Dobles
#bench-table-plot("../data/DoublyLinkedListNoTail_results.csv")

=== 4. Dobles (sin cola)
#bench-table-plot("../data/DoublyLinkedList_results.csv")

#pagebreak()

== Pilas
#bench-table-plot("../data/Stack_results.csv")

== Colas
#bench-table-plot("../data/Queue_results.csv")

#pagebreak()

= Conclusiones
Hemos hecho un análisis teórico y empírico sobre el funcionamiento de las listas enlazadas en todas sus formas, las pilas y las colas y hecho supuestos en base a su funcionamiento y a análisis simples.

La experimentación demostró que la complejidad de las estructuras de datos coincidió con lo esperado y demostrado a lo largo de este informe, gracias a la implementación y medición hechos en Java.

Estos resultados son esenciales en el manejo real de estructuras de datos, las cuales se usan en todos los casos de uso posibles dentro del software real. Su comprensión nos permitirá elegir la mejor estructura para cada contexto.
