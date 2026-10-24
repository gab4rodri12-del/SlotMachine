import java.util.ArrayList;

/**
 * Representa una rueda normal de la maquina.
 * Conserva un estado independiente para sus simbolos.
 */
public class Wheel
{
    private Rectangle body;
    private Circle symbolShape;
    private Symbol symbol;
    private int position;
    private boolean visible;
    private boolean locked;

    private ArrayList<Symbol> registeredSymbols;
    private ArrayList<Symbol> localSymbols;

    /** Crea una rueda vacia y desbloqueada. */
    public Wheel()
    {
        body = new Rectangle();
        body.changeSize(100, 50);
        body.moveVertical(85);
        body.changeColor("yellow");

        symbolShape = new Circle();
        symbolShape.changeSize(30);
        symbolShape.moveHorizontal(60);
        symbolShape.moveVertical(120);

        symbol = null;
        position = 1;
        visible = false;
        locked = false;

        registeredSymbols = new ArrayList<Symbol>();
        localSymbols = new ArrayList<Symbol>();
    }

    /** Bloquea la rueda. */
    public void lock()
    {
        locked = true;
    }

    /** Desbloquea la rueda. */
    public void unlock()
    {
        locked = false;
    }

    /** Indica si esta bloqueada. */
    public boolean isLocked()
    {
        return locked;
    }

    /** Indica si permite ser bloqueada. */
    public boolean canLock()
    {
        return true;
    }

    /** Indica si permite ser intercambiada. */
    public boolean canSwap()
    {
        return true;
    }

    /** Indica si permite ser eliminada. */
    public boolean canDelete()
    {
        return true;
    }

    /** Cambia el color del cuerpo de la rueda. */
    protected void setBodyColor(String color)
    {
        body.changeColor(color);
    }

    /** Hace visible la rueda y actualiza su simbolo. */
    public void makeVisible()
    {
        visible = true;
        body.makeVisible();
        refreshSymbol();
    }

    /** Oculta la rueda y su simbolo. */
    public void makeInvisible()
    {
        visible = false;
        body.makeInvisible();
        symbolShape.makeInvisible();
    }

    /** Cambia la posicion visual de la rueda. */
    public void setPosition(int newPosition)
    {
        int distance = (newPosition - position) * 60;

        body.moveHorizontal(distance);
        symbolShape.moveHorizontal(distance);

        position = newPosition;
    }

    /** Conserva la operacion anterior que recibe un color. */
    public void setSymbol(String color)
    {
        if (color == null) {
            setSymbol((Symbol) null);
        } else {
            setSymbol(new Symbol(color));
        }
    }

    /** Coloca un simbolo y registra su seleccion. */
    public void setSymbol(Symbol newSymbol)
    {
        symbol = newSymbol;

        if (symbol != null) {
            symbol.onSelect();
        }

        refreshSymbol();
    }

    /** Retorna el color actual, o null si esta vacia. */
    public String getSymbol()
    {
        if (symbol == null) {
            return null;
        }

        return symbol.getColor();
    }

    /**
     * Selecciona la copia local de un simbolo registrado.
     * Conserva su estado entre selecciones.
     *
     * @param registered simbolo del catalogo de la maquina
     */
    public void useRegisteredSymbol(Symbol registered)
    {
        int index = registeredSymbols.indexOf(registered);

        if (index < 0) {
            registeredSymbols.add(registered);
            localSymbols.add(registered.createCopy());
            index = localSymbols.size() - 1;
        }

        setSymbol(localSymbols.get(index));
    }

    /**
     * Olvida un simbolo eliminado del catalogo.
     * La maquina solo llama este metodo si ya no se muestra.
     */
    public void forgetRegisteredSymbol(Symbol registered)
    {
        int index = registeredSymbols.indexOf(registered);

        if (index >= 0) {
            registeredSymbols.remove(index);
            localSymbols.remove(index);
        }
    }

    /**
     * Selecciona el color que mostrara al girar.
     * Las ruedas normales aceptan el color propuesto.
     */
    public String selectSpinSymbol(
        String proposedSymbol, Wheel leftWheel)
    {
        return proposedSymbol;
    }

    /** Aplica el comportamiento de giro al simbolo actual. */
    public void onSpin()
    {
        if (symbol != null) {
            symbol.onSpin();
            refreshSymbol();
        }
    }

    /**
     * Actualiza el dibujo.
     * Dibujar no cuenta como seleccionar ni girar.
     */
    private void refreshSymbol()
    {
        symbolShape.makeInvisible();

        if (symbol != null) {
            symbolShape.changeColor(symbol.getColor());
            symbolShape.changeSize(symbol.getSize());

            if (visible && symbol.isVisible()) {
                symbolShape.makeVisible();
            }
        }
    }
}