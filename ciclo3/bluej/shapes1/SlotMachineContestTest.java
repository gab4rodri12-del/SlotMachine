import static org.junit.Assert.*;
import org.junit.Test;

/** pruebas del algoritmo del ciclo 3 */
public class SlotMachineContestTest {
    @Test
    public void shouldSolveThreeWheels() {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] movements = contest.solve(3);
        assertTrue(contest.isSolved());
        assertTrue(movements.length > 0);
    }

    @Test
    public void shouldSolveSevenWheels() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(7);
        assertTrue(contest.isSolved());
    }

    @Test
    public void shouldSolveFiftyWheels() {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] movements = contest.solve(50);
        assertTrue(contest.isSolved());
        assertTrue(movements.length <= 10000);
    }

    @Test
    public void shouldReturnValidMovements() {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] movements = contest.solve(8);
        for (int[] movement : movements) {
            assertEquals(2, movement.length);
            assertTrue(movement[0] >= 1 && movement[0] <= 8);
            assertTrue(movement[1] >= -1000000000 && movement[1] <= 1000000000);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectTwoWheels() {
        new SlotMachineContest().solve(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectFiftyOneWheels() {
        new SlotMachineContest().solve(51);
    }

    @Test
    public void shouldHaveAsManySymbolsAsWheels() {
        SlotMachine machine = new SlotMachine(10);
        assertEquals(10, machine.symbols().length);
        assertEquals(10, machine.configuration().length);
    }

    @Test
    public void shouldCountOnlyShownDistinctSymbols() {
        SlotMachine machine = new SlotMachine(3);
        machine.setConfiguration(new String[]{"red", "red", "blue"});
        assertEquals(2, machine.distinctSymbols());
        machine.setConfiguration(new String[]{"red", "red", "red"});
        assertEquals(1, machine.distinctSymbols());
    }
}
