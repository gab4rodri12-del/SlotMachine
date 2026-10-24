import java.util.ArrayList;

/**
 * resuelve el problema i de la maraton usando solo consultas permitidas
 * no lee los simbolos ni la configuracion durante la solucion
 *
 * @author dopo
 * @version 2026-2
 */
public class SlotMachineContest {
    private SlotMachine machine;
    private ArrayList<int[]> moves;
    private int n;
    private int[] offset;

    /**
     * calcula los movimientos sin mostrar la maquina
     *
     * @param size cantidad de ruedas y simbolos, de 3 a 50
     * @return acciones, cada una contiene numero de rueda y pasos
     */
    public int[][] solve(int size) {
        prepare(size, false);
        search();
        return actions();
    }

    /**
     * ejecuta el algoritmo mostrando los giros en pantalla
     *
     * @param size cantidad de ruedas y simbolos, de 3 a 50
     */
    public void simulate(int size) {
        prepare(size, true);
        search();
    }

    /**
     * comprueba si la ultima ejecucion encontro jackpot
     */
    public boolean isSolved() {
        return machine != null && machine.distinctSymbols() == 1;
    }

    /**
     * prepara la maquina y las acciones
     */
    private void prepare(int size, boolean show) {
        if (size < 3 || size > 50) {
            throw new IllegalArgumentException("n debe estar entre 3 y 50");
        }
        n = size;
        machine = new SlotMachine(n);
        moves = new ArrayList<int[]>();
        offset = new int[n];
        if (show) machine.makeVisible();
    }

    /**
     * realiza un giro positivo y guarda la accion
     */
    private void rotate(int wheel, int steps) {
        int amount = ((steps % n) + n) % n;
        if (amount == 0) return;
        machine.spin(wheel, amount);
        if (!machine.ok()) throw new IllegalStateException("No se pudo girar la rueda");
        moves.add(new int[]{wheel, amount});
        offset[wheel - 1] = (offset[wheel - 1] + amount) % n;
    }

    /**
     * consulta la cantidad de simbolos visibles para cada posicion
     * restaura la posicion inicial de la rueda al finalizar
     */
    private int[] scan(int wheel) {
        int[] counts = new int[n];
        for (int i = 0; i < n; i++) {
            counts[i] = machine.distinctSymbols();
            rotate(wheel, 1);
        }
        return counts;
    }

    /**
     * obtiene el jackpot usando solo la cantidad de simbolos visibles
     */
    private void search() {
        if (machine.distinctSymbols() == 1) return;

        // busca en la primera rueda un simbolo ausente y uno presente en las otras
        int[] first = scan(1);
        int high = 0;
        int low = 0;
        for (int i = 1; i < n; i++) {
            if (first[i] > first[high]) high = i;
            if (first[i] < first[low]) low = i;
        }
        if (first[high] == first[low]) {
            throw new IllegalStateException("No se encontro un simbolo de referencia");
        }

        rotate(1, high);
        int[] target = new int[n];
        target[0] = offset[0];

        // cambia solo el simbolo de la rueda 1 entre dos referencias
        // la diferencia de consultas identifica el simbolo objetivo de cada rueda
        for (int wheel = 2; wheel <= n; wheel++) {
            int[] withAbsent = scan(wheel);
            rotate(1, low - high);
            int[] withPresent = scan(wheel);
            rotate(1, high - low);

            int best = 0;
            int bestDelta = Integer.MAX_VALUE;
            for (int k = 0; k < n; k++) {
                int delta = withAbsent[k] - withPresent[k];
                if (delta < bestDelta) {
                    bestDelta = delta;
                    best = k;
                }
            }
            target[wheel - 1] = best;
        }

        // alinea todas las ruedas con el simbolo ausente seleccionado
        for (int wheel = 2; wheel <= n; wheel++) {
            rotate(wheel, target[wheel - 1]);
        }
        if (machine.distinctSymbols() != 1) {
            throw new IllegalStateException("No se alcanzo jackpot");
        }
    }

    /**
     * devuelve una copia de las acciones realizadas
     */
    private int[][] actions() {
        int[][] result = new int[moves.size()][2];
        for (int i = 0; i < moves.size(); i++) {
            result[i][0] = moves.get(i)[0];
            result[i][1] = moves.get(i)[1];
        }
        return result;
    }
}