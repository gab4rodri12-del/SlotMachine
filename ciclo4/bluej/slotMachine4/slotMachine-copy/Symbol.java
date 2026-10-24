/**
 * Representa un simbolo normal.
 */
public class Symbol
{
    private String color;
    private int size;

    /**
     * Crea un simbolo de tamaño treinta.
     *
     * @param color color del simbolo
     */
    public Symbol(String color)
    {
        this.color = color;
        size = 30;
    }

    /** Retorna el color del simbolo. */
    public String getColor()
    {
        return color;
    }

    /** Retorna el tamaño del simbolo. */
    public int getSize()
    {
        return size;
    }

    /** Cambia el tamaño conservando un minimo de uno. */
    protected void setSize(int newSize)
    {
        size = Math.max(1, newSize);
    }

    /** El simbolo normal no cambia al girar. */
    public void onSpin()
    {
    }

    /** El simbolo normal no cambia al seleccionarlo. */
    public void onSelect()
    {
    }

    /** El simbolo normal permite ser mostrado. */
    public boolean isVisible()
    {
        return true;
    }

    /**
     * Crea una instancia inicial independiente para otra rueda.
     *
     * @return nuevo simbolo del mismo tipo y color
     */
    public Symbol createCopy()
    {
        return new Symbol(color);
    }
}