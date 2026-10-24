import java.util.ArrayList;

/**
 * Resuelve y simula el problema de la maraton
 * usando SlotMachine como herramienta.
 *
 * @author rodriguezr-vargasc
 * @version 1.2
 */
public class SlotMachineContest
{
    private SlotMachine lastSimulation;

    /**
     * Busca una secuencia de acciones que lleva
     * la maquina al jackpot.
     *
     * @param n cantidad de ruedas y simbolos
     * @return acciones con rueda y pasos; matriz vacia
     *         si n es invalido o no se consigue una solucion
     */
    public int[][] solve(int n)
    {
        if (n < 3 || n > 50) {
            return new int[0][0];
        }

        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();

        return solveMachine(machine, n);
    }

    /**
     * Muestra la solucion de una nueva maquina
     * y oculta la simulacion anterior.
     *
     * @param n cantidad de ruedas y simbolos
     */
    public void simulate(int n)
    {
        if (n < 3 || n > 50) {
            return;
        }

        if (lastSimulation != null) {
            lastSimulation.makeInvisible();
        }

        SlotMachine machine = new SlotMachine(n);
        lastSimulation = machine;

        machine.makeVisible();
        solveMachine(machine, n);
    }

    /**
     * Ejecuta la estrategia de solucion
     * sobre una maquina.
     */
    int[][] solveMachine(SlotMachine machine, int n)
    {
        ArrayList<int[]> actions = new ArrayList<int[]>();

        if (n < 3 || n > 50) {
            return new int[0][0];
        }

        makeAllDifferent(machine, n, actions);

        if (machine.distinctSymbols() == 1) {
            return toMatrix(actions);
        }

        int[] next = findOrder(machine, n, actions);

        if (machine.distinctSymbols() != 1) {
            makeJackpot(machine, n, next, actions);
        }

        if (machine.distinctSymbols() != 1) {
            return new int[0][0];
        }

        return toMatrix(actions);
    }

    /**
     * Deja las ruedas mostrando simbolos diferentes.
     * Si encuentra jackpot durante la busqueda, termina.
     */
    private void makeAllDifferent(
        SlotMachine machine,
        int n,
        ArrayList<int[]> actions)
    {
        for (int wheel = 1; wheel <= n; wheel++) {

            int best = machine.distinctSymbols();
            int bestStep = 0;

            for (int step = 1; step <= n; step++) {

                move(machine, wheel, 1, actions);

                int current = machine.distinctSymbols();

                if (current == 1) {
                    return;
                }

                if (current > best) {
                    best = current;
                    bestStep = step % n;
                }
            }

            if (bestStep != 0) {
                move(machine, wheel, bestStep, actions);

                if (machine.distinctSymbols() == 1) {
                    return;
                }
            }
        }
    }

    /**
     * Encuentra cual rueda esta un simbolo
     * despues de cada rueda.
     */
    private int[] findOrder(
        SlotMachine machine,
        int n,
        ArrayList<int[]> actions)
    {
        int[] next = new int[n + 1];

        // Indica si una rueda ya fue elegida como sucesora.
        boolean[] assigned = new boolean[n + 1];

        for (int first = 1; first <= n; first++) {

            for (int second = 1; second <= n; second++) {

                if (first != second && next[first] == 0 &&
                    !assigned[second]) {

                    if (isNext(
                        machine,
                        first,
                        second,
                        n,
                        actions)) {

                        next[first] = second;
                        assigned[second] = true;
                    }

                    if (machine.distinctSymbols() == 1) {
                        return next;
                    }
                }
            }
        }

        return next;
    }

    /**
     * Comprueba si la segunda rueda esta
     * un simbolo despues de la primera.
     */
    private boolean isNext(
        SlotMachine machine,
        int first,
        int second,
        int n,
        ArrayList<int[]> actions)
    {
        move(machine, first, 1, actions);
        move(machine, second, -1, actions);

        boolean result =
            machine.distinctSymbols() == n;

        move(machine, second, 1, actions);
        move(machine, first, -1, actions);

        return result;
    }

    /**
     * Gira las ruedas hasta mostrar
     * el mismo simbolo que la rueda uno.
     */
    private void makeJackpot(
        SlotMachine machine,
        int n,
        int[] next,
        ArrayList<int[]> actions)
    {
        int wheel = 1;

        for (int distance = 1; distance < n; distance++) {

            wheel = next[wheel];

            if (wheel == 0) {
                return;
            }

            move(machine, wheel, -distance, actions);

            if (machine.distinctSymbols() == 1) {
                return;
            }
        }
    }

    /**
     * Realiza una accion y la guarda.
     * No mueve la maquina si ya gano
     * o si alcanzo el limite de acciones.
     */
    private void move(
        SlotMachine machine,
        int wheel,
        int steps,
        ArrayList<int[]> actions)
    {
        if (machine.distinctSymbols() == 1 ||
            actions.size() >= 10000) {

            return;
        }

        machine.spin(wheel, steps);
        actions.add(new int[] {wheel, steps});
    }

    /**
     * Convierte la lista de acciones
     * en la matriz pedida por solve.
     */
    private int[][] toMatrix(ArrayList<int[]> actions)
    {
        int[][] result = new int[actions.size()][2];

        for (int i = 0; i < actions.size(); i++) {
            result[i][0] = actions.get(i)[0];
            result[i][1] = actions.get(i)[1];
        }

        return result;
    }
}