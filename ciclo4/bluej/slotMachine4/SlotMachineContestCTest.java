import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del ciclo 3 en modo invisible.
 * @author rodriguezr-vargasc
 */
public class SlotMachineContestCTest
{
    @Test
    public void accordingRodriguezRVargasCShouldKeepJackpot()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {1, 1, 1});
    }

    @Test
    public void accordingRodriguezRVargasCShouldSolveOneDifferent()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {0, 0, 0, 1});
    }

    @Test
    public void accordingRodriguezRVargasCShouldSolveReverseOrder()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {4, 3, 2, 1, 0});
    }

    @Test
    public void accordingRodriguezRVargasCShouldSolveAlternatingSymbols()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {0, 1, 0, 1, 0, 1});
    }

    @Test
    public void accordingRodriguezRVargasCShouldReuseContest()
    {
        SlotMachineContest contest = new SlotMachineContest();
        checkSolution(contest, new int[] {0, 2, 1});
        checkSolution(contest, new int[] {3, 0, 3, 0});
    }


    /** Resuelve y reproduce la secuencia desde la misma configuracion. */
    private void checkSolution(SlotMachineContest contest, int[] initial)
    {
        int n = initial.length;
        SlotMachine machine = new SlotMachine(n);
        String[] symbols = machine.symbols();
        String[] configuration = new String[n];
        for (int i = 0; i < n; i++) {
            configuration[i] = symbols[initial[i]];
        }
        machine.spin(configuration);
        assertTrue(machine.ok(), "No se pudo preparar la configuracion");
        boolean alreadyWon = machine.isJackpot();

        int[][] solution = contest.solveMachine(machine, n);
        checkActions(solution, n);
        assertTrue(machine.isJackpot(), "El solucionador no termino en jackpot");
        assertEquals(1, machine.distinctSymbols());
        if (alreadyWon) {
            assertEquals(0, solution.length, "Una maquina ganadora no debe moverse");
        } else {
            assertTrue(solution.length > 0, "Faltan las acciones de la solucion");
        }

        String[] finalConfiguration = machine.configuration();
        machine.spin(configuration);
        assertTrue(machine.ok());
        replay(machine, solution);
        assertTrue(machine.isJackpot(), "Las acciones devueltas no llevan al jackpot");
        assertArrayEquals(finalConfiguration, machine.configuration());
    }

    /** Comprueba formato, indices, pasos y limite de acciones. */
    private void checkActions(int[][] solution, int n)
    {
        assertNotNull(solution);
        assertTrue(solution.length <= 10000, "Se superaron las 10000 acciones");
        for (int i = 0; i < solution.length; i++) {
            assertNotNull(solution[i], "Accion nula en la posicion " + i);
            assertEquals(2, solution[i].length);
            assertTrue(solution[i][0] >= 1 && solution[i][0] <= n);
            assertTrue(solution[i][1] >= -1000000000);
            assertTrue(solution[i][1] <= 1000000000);
        }
    }

    /** Reproduce las acciones y rechaza movimientos posteriores al jackpot. */
    private void replay(SlotMachine machine, int[][] solution)
    {
        for (int i = 0; i < solution.length; i++) {
            assertFalse(machine.isJackpot(),
                "Hay un movimiento despues de ganar: accion " + i);
            machine.spin(solution[i][0], solution[i][1]);
            assertTrue(machine.ok(), "Fallo la accion " + i);
        }
    }
}
