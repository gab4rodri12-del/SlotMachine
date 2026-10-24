
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * pruebas unitarias del ciclo 2
 * todas las pruebas se hacen en modo invisible
 */
public class SlotMachineC2Test {

    /**
     * crea una maquina con dos ruedas
     * y dos simbolos diferentes
     */
    private SlotMachine crearMaquina() {
        SlotMachine maquina = new SlotMachine();

        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");

        maquina.addWheel(1);
        maquina.addWheel(2);

        return maquina;
    }

    /**
     * verifica que dos ruedas se intercambian
     */
    @Test
    public void shouldSwapWheels() {
        SlotMachine maquina = crearMaquina();

        maquina.placeSymbol(2, "blue");
        maquina.swap(1, 2);

        assertTrue(maquina.ok());
        assertArrayEquals(
            new String[]{"blue", "red"},
            maquina.configuration()
        );
    }

    /**
     * verifica que una rueda bloqueada no gira
     */
    @Test
    public void shouldNotSpinLockedWheel() {
        SlotMachine maquina = crearMaquina();

        maquina.lock(1);
        String[] antes = maquina.configuration();

        maquina.spin(1, 1);

        assertFalse(maquina.ok());
        assertArrayEquals(
            antes, maquina.configuration()
        );
    }

    /**
     * verifica que una rueda puede girar
     * despues de desbloquearla
     */
    @Test
    public void shouldSpinUnlockedWheel() {
        SlotMachine maquina = crearMaquina();

        maquina.lock(1);
        maquina.unlock(1);
        maquina.spin(1, 1);

        assertTrue(maquina.ok());
        assertEquals(
            "blue", maquina.configuration()[0]
        );
    }

    /**
     * verifica que una rueda gira varios pasos
     */
    @Test
    public void shouldSpinMultipleSteps() {
        SlotMachine maquina = crearMaquina();

        maquina.spin(1, 3);

        assertTrue(maquina.ok());
        assertEquals(
            "blue", maquina.configuration()[0]
        );
    }

    /**
     * verifica que se puede establecer
     * una configuracion determinada
     */
    @Test
    public void shouldSetConfiguration() {
        SlotMachine maquina = crearMaquina();

        String[] esperada = {"blue", "red"};
        maquina.setConfiguration(esperada);

        assertTrue(maquina.ok());
        assertArrayEquals(
            esperada, maquina.configuration()
        );
    }

    /**
     * verifica que una configuracion invalida
     * no modifica la maquina
     */
    @Test
    public void shouldRejectInvalidConfiguration() {
        SlotMachine maquina = crearMaquina();

        String[] antes = maquina.configuration();

        maquina.setConfiguration(
            new String[]{"green", "red"}
        );

        assertFalse(maquina.ok());
        assertArrayEquals(
            antes, maquina.configuration()
        );
    }

    /**
     * verifica que no se intercambia
     * una rueda bloqueada
     */
    @Test
    public void shouldNotSwapLockedWheel() {
        SlotMachine maquina = crearMaquina();

        maquina.placeSymbol(2, "blue");
        String[] antes = maquina.configuration();

        maquina.lock(1);
        maquina.swap(1, 2);

        assertFalse(maquina.ok());
        assertArrayEquals(
            antes, maquina.configuration()
        );
    }
}
