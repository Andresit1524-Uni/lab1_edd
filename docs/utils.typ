#import "@preview/lilaq:0.6.0" as lq

#let style(body) = {
  set document(
    title: [Implementación y análisis de complejidad de listas, pilas y colas en Java],
  )

  // Colores
  show link: set text(fill: blue)
  show ref: set text(fill: blue)

  // Fuentes
  set text(lang: "es", font: "Ancizar Sans")
  show raw: set text(font: "Google Sans Code NF")
  show math.equation: set text(font: "Erewhon Math")

  body
}

/// Par tabla y gráfica para benchmarks
#let benchmark-table-plot(source) = {
  let data = csv(source)
  set text(size: 6pt)
  set table.cell(inset: 0.7em)

  grid(columns: (1fr, 1.5fr))[
    #table(
      columns: 6,
      stroke: 0.7pt,
      ..data.at(0).map(it => [*#it*]),
      ..data
        .slice(1)
        .map(
          it => (raw(it.at(0)), ..it.slice(1)),
        )
        .flatten(),
    )
  ][
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
          let data = it
            .slice(1)
            .map(
              i => calc.max(1, int(i)),
            )

          return lq.plot(
            lq.logspace(1, 5, num: 5),
            data,
            label: label,
          )
        }),
    )
  ]
}
