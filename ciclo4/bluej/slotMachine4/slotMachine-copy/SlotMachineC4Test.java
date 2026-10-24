import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del ciclo cuatro.
 *
 * @author rodriguezr-vargasc
 */
public class SlotMachineC4Test
{
    /** Crea una maquina con dos colores registrados. */
    private SlotMachine createMachine()
    {
        SlotMachine machine = new SlotMachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        return machine;
    }

    @Test
    public void shouldCreateNormalWheel()
    {
        SlotMachine machine = createMachine();

        machine.addWheel(1);

        assertTrue(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    @Test
    public void shouldRejectUnknownWheelType()
    {
        SlotMachine machine = createMachine();

        machine.addWheel("unknown", 1);

        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    @Test
    public void shouldSpinNormalWheel()
    {
        SlotMachine machine = createMachine();
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        machine.spin(1, 1);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"blue"},
            machine.configuration()
        );
    }

    @Test
    public void shouldKeepSymbolWithZeroSteps()
    {
        SlotMachine machine = createMachine();
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        machine.spin(1, 0);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"red"},
            machine.configuration()
        );
    }

    @Test
    public void shouldSpinWithNegativeSteps()
    {
        SlotMachine machine = createMachine();
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        machine.spin(1, -1);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"blue"},
            machine.configuration()
        );
    }

    @Test
    public void shouldCopyLeftSymbol()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.spin(2, 2);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"red", "red"},
            machine.configuration()
        );
    }

    @Test
    public void shouldSpinLeftyWithoutLeftNeighbor()
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
    public void shouldCopyThroughLeftyChain()
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
    public void shouldKeepLockedLefty()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.lock(2);

        machine.spin(2, 1);

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
    }

    @Test
    public void shouldRejectLockingRebel()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("rebel", 1);

        machine.lock(1);

        assertFalse(machine.ok());
    }

    @Test
    public void shouldRejectDeletingRebel()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("rebel", 1);

        machine.delWheel(1);

        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    @Test
    public void shouldRejectSwappingRebel()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.swap(1, 2);

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
    }

    @Test
    public void shouldAllowRebelToSpin()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("rebel", 1);
        machine.placeSymbol(1, "red");

        machine.lock(1);
        assertFalse(machine.ok());

        machine.spin(1, 1);

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"blue"},
            machine.configuration()
        );
    }

    @Test
    public void shouldPreserveFailureDuringGeneralSpin()
    {
        SlotMachine machine = createMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(2, "red");
        machine.lock(1);

        machine.spin();

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {null, "red"},
            machine.configuration()
        );
    }

    @Test
    public void shouldRegisterSpecialSymbols()
    {
        SlotMachine machine = new SlotMachine();

        machine.addSymbol("ephemeral", 1, "red");
        assertTrue(machine.ok());

        machine.addSymbol("shy", 2, "blue");
        assertTrue(machine.ok());

        machine.addSymbol("growing", 3, "green");
        assertTrue(machine.ok());

        assertArrayEquals(
            new String[] {"red", "blue", "green"},
            machine.symbols()
        );
    }

    @Test
    public void shouldRejectUnknownSymbolType()
    {
        SlotMachine machine = new SlotMachine();

        machine.addSymbol("unknown", 1, "red");

        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    @Test
    public void shouldRejectRepeatedColor()
    {
        SlotMachine machine = new SlotMachine();
        machine.addSymbol("ephemeral", 1, "red");

        machine.addSymbol("shy", 2, "red");

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red"},
            machine.symbols()
        );
    }

    @Test
    public void shouldRejectInvalidColor()
    {
        SlotMachine machine = new SlotMachine();

        machine.addSymbol(1, "symbolabc");

        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    @Test
    public void shouldAcceptOrange()
    {
        SlotMachine machine = new SlotMachine();

        machine.addSymbol(1, "orange");

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"orange"},
            machine.symbols()
        );
    }

    @Test
    public void shouldRejectInvalidConfigurationWithoutChanges()
    {
        SlotMachine machine = createMachine();
        machine.addWheel(1);
        machine.addWheel(2);
        machine.spin(new String[] {"red", "blue"});

        machine.spin(new String[] {"blue", "unknown"});

        assertFalse(machine.ok());
        assertArrayEquals(
            new String[] {"red", "blue"},
            machine.configuration()
        );
    }

    @Test
    public void shouldRejectDeletingUsedSymbol()
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
    }

    @Test
    public void shouldDeleteUnusedSymbol()
    {
        SlotMachine machine = createMachine();
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        machine.delSymbol("blue");

        assertTrue(machine.ok());
        assertArrayEquals(
            new String[] {"red"},
            machine.symbols()
        );
    }

    @Test
    public void shouldKeepNormalSymbolSize()
    {
        Symbol symbol = new Symbol("red");

        symbol.onSpin();
        symbol.onSelect();

        assertEquals(30, symbol.getSize());
        assertTrue(symbol.isVisible());
    }

    @Test
    public void shouldShrinkEphemeralSymbol()
    {
        EphemeralSymbol symbol = new EphemeralSymbol("red");

        symbol.onSpin();

        assertEquals(25, symbol.getSize());
    }

    @Test
    public void shouldKeepEphemeralMinimumSize()
    {
        EphemeralSymbol symbol = new EphemeralSymbol("red");

        for (int i = 0; i < 20; i++) {
            symbol.onSpin();
        }

        assertEquals(1, symbol.getSize());
    }

    @Test
    public void shouldAlternateShyVisibility()
    {
        ShySymbol symbol = new ShySymbol("blue");

        assertTrue(symbol.isVisible());

        symbol.onSelect();
        assertFalse(symbol.isVisible());

        symbol.onSelect();
        assertTrue(symbol.isVisible());
    }

    @Test
    public void shouldNotToggleShyFromSpinAlone()
    {
        ShySymbol symbol = new ShySymbol("blue");

        symbol.onSpin();

        assertTrue(symbol.isVisible());
    }

    @Test
    public void shouldGrowGrowingSymbol()
    {
        GrowingSymbol symbol = new GrowingSymbol("green");

        assertEquals(10, symbol.getSize());

        symbol.onSpin();

        assertEquals(15, symbol.getSize());
    }

    @Test
    public void shouldKeepGrowingMaximumSize()
    {
        GrowingSymbol symbol = new GrowingSymbol("green");

        for (int i = 0; i < 20; i++) {
            symbol.onSpin();
        }

        assertEquals(40, symbol.getSize());
    }

    @Test
    public void shouldCreateIndependentSymbolCopies()
    {
        Symbol original = new GrowingSymbol("green");
        Symbol first = original.createCopy();
        Symbol second = original.createCopy();

        first.onSpin();

        assertNotSame(first, second);
        assertEquals(15, first.getSize());
        assertEquals(10, second.getSize());
        assertEquals(10, original.getSize());
    }
}