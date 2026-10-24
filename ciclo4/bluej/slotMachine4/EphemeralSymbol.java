/**
 * Representa un simbolo que disminuye de tamaño al girar.
 */
public class EphemeralSymbol extends Symbol
{
    /**
     * Crea un simbolo efimero.
     *
     * @param color color del simbolo
     */
    public EphemeralSymbol(String color)
    {
        super(color);
    }

    /** Reduce cinco unidades sin bajar de uno. */
    @Override
    public void onSpin()
    {
        setSize(getSize() - 5);
    }

    /** Crea una instancia inicial para otra rueda. */
    @Override
    public Symbol createCopy()
    {
        return new EphemeralSymbol(getColor());
    }
}