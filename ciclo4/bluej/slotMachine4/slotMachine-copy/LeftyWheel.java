/**
 * Representa una rueda que al girar copia
 * el simbolo de su vecina izquierda.
 */
public class LeftyWheel extends Wheel
{
    /** Crea una rueda izquierda con cuerpo azul. */
    public LeftyWheel()
    {
        super();
        setBodyColor("blue");
    }

    /**
     * Selecciona el simbolo de la vecina izquierda.
     * Si no existe vecina, acepta el simbolo propuesto.
     *
     * @param proposedSymbol simbolo propuesto por la maquina
     * @param leftWheel rueda izquierda, o null si no existe
     * @return simbolo seleccionado
     */
    @Override
    public String selectSpinSymbol(
        String proposedSymbol, Wheel leftWheel)
    {
        if (leftWheel != null) {
            return leftWheel.getSymbol();
        }

        return proposedSymbol;
    }
}