import static org.junit.Assert.*;
import org.junit.Test;

/** pruebas compartidas del ciclo 3; agregar iniciales de autores segun la guia */
public class SlotMachineContestCTest {
    @Test
    public void shouldReachJackpotWithFiveWheels() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(5);
        assertTrue(contest.isSolved());
    }

    @Test
    public void shouldFinishWithinActionLimit() {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] movements = contest.solve(50);
        assertTrue(contest.isSolved());
        assertTrue(movements.length <= 10000);
    }
}
