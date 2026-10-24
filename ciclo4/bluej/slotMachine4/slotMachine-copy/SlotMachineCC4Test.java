import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Casos propuestos para compartir en el ciclo cuatro.
 *
 * @author rodriguezr-vargasc
 */
public class SlotMachineCC4Test
{
    /** Crea una maquina vacia con dos colores registrados. */
    private SlotMachine createMachine()
    {
        SlotMachine machine = new SlotMachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        return machine;
    }

    @Test
    public void accordingRodriguezRVargasCShouldRejectRebelLock()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("rebel", 1);
        machine.placeSymbol(1, "red");

        machine.lock(1);

        assertFalse(machine.ok());

        // Debe seguir libre y poder girar.
        machine.spin(1, 1);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"blue"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldRejectRebelDeletion()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);
        machine.spin(new String[] {"red", "blue"});

        machine.delWheel(2);

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldRejectRebelSwapBothWays()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);
        machine.spin(new String[] {"red", "blue"});

        machine.swap(1, 2);

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );

        machine.swap(2, 1);

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldCopyLeftOnRandomSpin()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.spin(2);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"red", "red"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldCopyLeftOnNegativeSpin()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.spin(2, -2);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"red", "red"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldCopyLeftyChain()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.addWheel("lefty", 3);

        machine.spin(new String[] {"blue", "red", "red"});

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"blue", "blue", "blue"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldSpinLeftyWithoutNeighbor()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("lefty", 1);
        machine.placeSymbol(1, "red");

        machine.spin(1, 1);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"blue"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldKeepLockedLeftyInConfiguration()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.addWheel("lefty", 3);
        machine.placeSymbol(2, "blue");
        machine.lock(2);

        machine.spin(new String[] {"red", "blue", "red"});

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue", "blue"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldPreserveGeneralSpinFailure()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(2, "red");
        machine.lock(1);

        // La primera rueda esta vacia y bloqueada.
        // La segunda no tiene un simbolo valido para copiar.
        machine.spin();

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {null, "red"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldKeepLeftyWithZeroSteps()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.spin(2, 0);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldRejectInvalidConfigurations()
    {
        SlotMachine machine = createMachine();
        machine.addWheel(1);
        machine.addWheel(2);
        machine.spin(new String[] {"red", "blue"});

        String[] expected = {"red", "blue"};

        machine.spin((String[]) null);

        assertFalse(machine.ok());
        assertArrayEquals(expected, machine.configuration());

        machine.spin(new String[] {"blue"});

        assertFalse(machine.ok());
        assertArrayEquals(expected, machine.configuration());

        machine.spin(new String[] {"blue", "unknown"});

        assertFalse(machine.ok());
        assertArrayEquals(expected, machine.configuration());
    }

    @Test
    public void accordingRodriguezRVargasCShouldRejectChangingLockedConfiguration()
    {
        SlotMachine machine = createMachine();
        machine.addWheel(1);
        machine.addWheel(2);
        machine.spin(new String[] {"red", "blue"});
        machine.lock(2);

        machine.spin(new String[] {"blue", "red"});

        assertFalse(machine.ok());

        // Tampoco debe cambiar la primera rueda.
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldRegisterEverySymbolType()
    {
        SlotMachine machine = new SlotMachine();

        machine.addSymbol("normal", 1, "red");
        assertTrue(machine.ok());

        machine.addSymbol("ephemeral", 2, "blue");
        assertTrue(machine.ok());

        machine.addSymbol("shy", 3, "green");
        assertTrue(machine.ok());

        machine.addSymbol("growing", 4, "orange");
        assertTrue(machine.ok());

        assertArrayEquals(
            new String[] {"red", "blue", "green", "orange"},
            machine.symbols()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldRejectInvalidSymbolRegistrations()
    {
        SlotMachine machine = createMachine();
        String[] expected = {"red", "blue"};

        machine.addSymbol("unknown", 3, "green");

        assertFalse(machine.ok());
        assertArrayEquals(expected, machine.symbols());

        machine.addSymbol("shy", 3, "red");

        assertFalse(machine.ok());
        assertArrayEquals(expected, machine.symbols());

        machine.addSymbol("normal", 3, "symbolabc");

        assertFalse(machine.ok());
        assertArrayEquals(expected, machine.symbols());

        machine.addSymbol("normal", 3, null);

        assertFalse(machine.ok());
        assertArrayEquals(expected, machine.symbols());
    }

    @Test
    public void accordingRodriguezRVargasCShouldProtectUsedSymbols()
    {
        SlotMachine machine = createMachine();
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        machine.delSymbol("red");

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.symbols()
        );

        machine.delSymbol("blue");

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"red"},
            machine.symbols()
        );
        assertArrayEquals(
            new String[] {"red"},
            machine.configuration()
        );
    }

    @Test
    public void accordingRodriguezRVargasCShouldKeepNormalSymbolUnchanged()
    {
        Symbol symbol = new Symbol("red");

        symbol.onSelect();
        symbol.onSpin();

        assertEquals("red", symbol.getColor());
        assertEquals(30, symbol.getSize());
        assertTrue(symbol.isVisible());
    }

    @Test
    public void accordingRodriguezRVargasCShouldShrinkWithoutDisappearing()
    {
        Symbol symbol = new EphemeralSymbol("red");

        symbol.onSpin();
        assertEquals(25, symbol.getSize());

        for (int i = 0; i < 20; i++) {
            symbol.onSpin();
        }

        assertEquals(1, symbol.getSize());
        assertTrue(symbol.isVisible());
    }

    @Test
    public void accordingRodriguezRVargasCShouldLimitGrowingSize()
    {
        Symbol symbol = new GrowingSymbol("green");

        assertEquals(10, symbol.getSize());

        symbol.onSpin();
        assertEquals(15, symbol.getSize());

        for (int i = 0; i < 20; i++) {
            symbol.onSpin();
        }

        assertEquals(40, symbol.getSize());
    }

    @Test
    public void accordingRodriguezRVargasCShouldToggleShyOnlyOnSelection()
    {
        Symbol symbol = new ShySymbol("blue");

        assertTrue(symbol.isVisible());

        symbol.onSelect();
        assertFalse(symbol.isVisible());

        symbol.onSpin();
        assertFalse(symbol.isVisible());

        symbol.onSelect();
        assertTrue(symbol.isVisible());

        assertEquals("blue", symbol.getColor());
    }

    @Test
    public void accordingRodriguezRVargasCShouldKeepCopiesIndependent()
    {
        Symbol original = new GrowingSymbol("green");
        Symbol first = original.createCopy();
        Symbol second = original.createCopy();

        first.onSpin();

        assertNotSame(original, first);
        assertNotSame(first, second);

        assertEquals(10, original.getSize());
        assertEquals(15, first.getSize());
        assertEquals(10, second.getSize());

        Symbol shy = new ShySymbol("blue");
        Symbol copy = shy.createCopy();

        copy.onSelect();

        assertTrue(shy.isVisible());
        assertFalse(copy.isVisible());
    }
}