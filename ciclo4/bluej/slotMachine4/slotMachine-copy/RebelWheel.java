/**
 * Representa una rueda que no permite bloquearla,
 * intercambiarla ni eliminarla.
 */
public class RebelWheel extends Wheel
{
    /** Crea una rueda rebelde con cuerpo rojo. */
    public RebelWheel()
    {
        super();
        setBodyColor("red");
    }
    

    /**
     * Indica que la rueda no permite el bloqueo.
     *
     * @return false
     */
    @Override
    public boolean canLock()
    {
        return false;
    }

    /**
     * Indica que la rueda no permite el intercambio.
     *
     * @return false
     */
    @Override
    public boolean canSwap()
    {
        return false;
    }

    /**
     * Indica que la rueda no permite la eliminacion.
     *
     * @return false
     */
    @Override
    public boolean canDelete()
    {
        return false;
    }

    /** Conserva la rueda desbloqueada. */
    @Override
    public void lock()
    {
        // Una rueda rebelde no permite ser bloqueada.
    }
}