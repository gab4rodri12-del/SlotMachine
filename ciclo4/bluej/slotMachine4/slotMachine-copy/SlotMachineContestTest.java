import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del ciclo 3 en modo invisible.
 * @author rodriguezr-vargasc
 */
public class SlotMachineContestTest
{
    @Test
    public void shouldCreateMinimumMachine()
    {
        checkNewMachine(3);
    }

    @Test
    public void shouldCreateMaximumMachine()
    {
        checkNewMachine(50);
    }

    @Test
    public void shouldRejectZero()
    {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] result = contest.solve(0);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void shouldRejectNegativeSize()
    {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] result = contest.solve(-3);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void shouldRejectBelowMinimum()
    {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] result = contest.solve(2);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void shouldRejectAboveMaximum()
    {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] result = contest.solve(51);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void shouldRejectMinimumInteger()
    {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] result = contest.solve(-2147483648);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void shouldRejectMaximumInteger()
    {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] result = contest.solve(2147483647);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void shouldSolveThreeDifferentSymbols()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {0, 1, 2});
    }

    @Test
    public void shouldSolveOneDifferentWheel()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {0, 0, 0, 1});
    }

    @Test
    public void shouldSolveRepeatedGroups()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {0, 0, 1, 1, 2});
    }

    @Test
    public void shouldSolveReverseOrder()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {4, 3, 2, 1, 0});
    }

    @Test
    public void shouldSolveAlternatingSymbols()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {0, 1, 0, 1, 0, 1});
    }

    @Test
    public void shouldSolveSymbolCycleBoundary()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {0, 4, 0, 4, 0});
    }

    @Test
    public void shouldSolveAlreadyWinningMachine()
    {
        checkSolution(new SlotMachineContest(),
            new int[] {2, 2, 2});
    }

    @Test
    public void shouldSolveAll27ConfigurationsOfThreeWheels()
    {
        SlotMachineContest contest = new SlotMachineContest();
        int checked = 0;
        for (int first = 0; first < 3; first++) {
            for (int second = 0; second < 3; second++) {
                for (int third = 0; third < 3; third++) {
                    checkSolution(contest, new int[] {first, second, third});
                    checked++;
                }
            }
        }
        assertEquals(27, checked);
    }

    @Test
    public void shouldSolveAll256ConfigurationsOfFourWheels()
    {
        SlotMachineContest contest = new SlotMachineContest();
        int checked = 0;
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                for (int c = 0; c < 4; c++) {
                    for (int d = 0; d < 4; d++) {
                        checkSolution(contest, new int[] {a, b, c, d});
                        checked++;
                    }
                }
            }
        }
        assertEquals(256, checked);
    }

    @Test
    public void shouldSolveEveryAllowedSize()
    {
        SlotMachineContest contest = new SlotMachineContest();
        for (int n = 3; n <= 50; n++) {
            int[] initial = new int[n];
            for (int i = 0; i < n; i++) {
                initial[i] = i % 3;
            }
            checkSolution(contest, initial);
        }
    }

    @Test
    public void shouldSolveFiftyDifferentSymbols()
    {
        int[] initial = new int[50];
        for (int i = 0; i < 50; i++) {
            initial[i] = i;
        }
        checkSolution(new SlotMachineContest(), initial);
    }

    @Test
    public void shouldSolveFiftyWheelsWithOneDifferent()
    {
        int[] initial = new int[50];
        initial[49] = 49;
        checkSolution(new SlotMachineContest(), initial);
    }

    @Test
    public void shouldReuseContestWithDifferentConfigurations()
    {
        SlotMachineContest contest = new SlotMachineContest();
        checkSolution(contest, new int[] {0, 1, 2});
        checkSolution(contest, new int[] {3, 3, 0, 1});
        checkSolution(contest, new int[] {1, 1, 1});
        checkSolution(contest, new int[] {2, 0, 2});
    }

    @Test
    public void shouldSolveAfterInvalidPublicCall()
    {
        SlotMachineContest contest = new SlotMachineContest();
        assertEquals(0, contest.solve(2).length);
        int[][] solution = contest.solve(3);
        checkActions(solution, 3);
        assertTrue(solution.length > 0);
    }

    @Test
    public void shouldReturnValidActionsFromPublicSolve()
    {
        SlotMachineContest contest = new SlotMachineContest();
        int[] sizes = {3, 5, 10, 25, 50};
        for (int i = 0; i < sizes.length; i++) {
            int[][] solution = contest.solve(sizes[i]);
            checkActions(solution, sizes[i]);
            assertTrue(solution.length > 0);
        }
    }


    /** Comprueba el constructor requerido por el ciclo 3. */
    private void checkNewMachine(int n)
    {
        SlotMachine machine = new SlotMachine(n);
        String[] symbols = machine.symbols();
        String[] configuration = machine.configuration();
        assertTrue(machine.ok());
        assertEquals(n, symbols.length);
        assertEquals(n, configuration.length);
        assertFalse(machine.isJackpot());
        for (int i = 0; i < n; i++) {
            assertNotNull(symbols[i]);
            for (int j = i + 1; j < n; j++) {
                assertNotEquals(symbols[i], symbols[j]);
            }
            boolean found = false;
            for (int j = 0; j < n; j++) {
                if (symbols[j].equals(configuration[i])) {
                    found = true;
                }
            }
            assertTrue(found, "Una rueda tiene un simbolo fuera del catalogo");
        }
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
