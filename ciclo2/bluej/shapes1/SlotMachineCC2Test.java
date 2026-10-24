
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * pruebas compartidas del ciclo 2
 */
public class SlotMachineCC2Test {

    /**
     * comprueba que una rueda bloqueada
     * no puede intercambiarse
     */
    @Test
    public void shouldNotSwapWhenLocked() {
        SlotMachine maquina = new SlotMachine();

        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);

        maquina.placeSymbol(2, "blue");
        String[] antes = maquina.configuration();

        maquina.lock(1);
        maquina.swap(1, 2);

        assertFalse(maquina.ok());
        assertArrayEquals(
            antes, maquina.configuration()
        );
    }

    /**
     * comprueba que una rueda puede girar
     * despues de desbloquearse
     */
    @Test
    public void shouldSpinAfterUnlock() {
        SlotMachine maquina = new SlotMachine();

        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);

        maquina.lock(1);
        maquina.unlock(1);
        maquina.spin(1, 1);

        assertTrue(maquina.ok());
        assertEquals(
            "blue", maquina.configuration()[0]
        );
    }
}
