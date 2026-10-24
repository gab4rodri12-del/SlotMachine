/**
 * Representa un simbolo que alterna su visibilidad
 * cada vez que es seleccionado.
 */
public class ShySymbol extends Symbol
{
    private boolean visible;

    /**
     * Crea un simbolo inicialmente visible.
     *
     * @param color color del simbolo
     */
    public ShySymbol(String color)
    {
        super(color);
        visible = true;
    }

    /** Alterna la visibilidad. */
    @Override
    public void onSelect()
    {
        visible = !visible;
    }

    /** Retorna la visibilidad propia del simbolo. */
    @Override
    public boolean isVisible()
    {
        return visible;
    }

    /** Crea una instancia inicial para otra rueda. */
    @Override
    public Symbol createCopy()
    {
        return new ShySymbol(getColor());
    }
}