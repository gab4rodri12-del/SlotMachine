/**
 * Representa un simbolo que crece con cada giro.
 * Es el tipo adicional propuesto para el ciclo cuatro.
 */
public class GrowingSymbol extends Symbol
{
    /**
     * Crea un simbolo de tamaño diez.
     *
     * @param color color del simbolo
     */
    public GrowingSymbol(String color)
    {
        super(color);
        setSize(10);
    }

    /** Aumenta cinco unidades sin superar cuarenta. */
    @Override
    public void onSpin()
    {
        setSize(Math.min(40, getSize() + 5));
    }

    /** Crea una instancia inicial para otra rueda. */
    @Override
    public Symbol createCopy()
    {
        return new GrowingSymbol(getColor());
    }
}