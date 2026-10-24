SLOTMACHINE - CICLO 3

Clases principales:
- SlotMachine.java: conserva las operaciones de los ciclos 1 y 2.
  Agrega SlotMachine(int n), 3 <= n <= 50; corrige distinctSymbols()
  para contar los simbolos visibles.
- SlotMachineContest.java: resuelve el problema sin consultar configuracion()
  ni isJackpot(). Usa solamente el constructor n, spin(wheel, steps),
  distinctSymbols(), makeVisible() cuando simula.
- SlotMachineContestTest.java: pruebas unitarias JUnit.
- SlotMachineContestCTest.java: dos pruebas compartidas JUnit.
- Canvas, Circle, Rectangle, Triangle: codigo original sin cambios.

En BlueJ:
1. Abrir la carpeta shapes1 como proyecto.
2. Compilar.
3. Crear un SlotMachineContest.
4. Ejecutar solve(3) o solve(7) para calcular movimientos invisiblemente.
5. Ejecutar simulate(3) para ver la solucion graficamente.
6. Ejecutar las pruebas JUnit.

La maquina del solve() es invisible; simulate() crea una nueva maquina
visible y resuelve esa misma maquina. El algoritmo de solve() devuelve
acciones {rueda, pasos} en el orden ejecutado.

Notas de entrega:
- Actualizar Astah y preparar dos pruebas de aceptacion.
- Agregar las iniciales de autores en los tests compartidos.
- Las ruedas con mas de seis simbolos usan colores graficos repetidos
  porque shapes admite una paleta limitada; los simbolos internos son
  distintos y el algoritmo trabaja con sus identidades.
