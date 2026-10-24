import java.util.ArrayList;

/**
 * representa una rueda y controla el simbolo seleccionado
 *
 * @author gaby-mathias
 * @version 2026-2
 */
public class Wheel {
    private ArrayList<String> symbols;
    private int position;
    private boolean locked;

    /**
     * crea una rueda con una copia de los colores de la maquina
     *
     * @param colors colores iniciales de la rueda
     */
    public Wheel(ArrayList<String> colors) {
        symbols = new ArrayList<String>(colors);
        position = 0;
        locked = false;
    }

    /**
     * agrega un simbolo a la rueda y conserva la seleccion
     *
     * @param pos posicion en la que se agrega
     * @param color color del simbolo
     */
    public void addSymbol(int pos, String color) {
        symbols.add(pos - 1, color);
        if (symbols.size() > 1 && pos - 1 <= position) {
            position++;
        }
    }

    /**
     * elimina un simbolo y ajusta la posicion seleccionada
     *
     * @param color color del simbolo que se elimina
     */
    public void delSymbol(String color) {
        int index = symbols.indexOf(color);
        if (index == -1) return;
        symbols.remove(index);
        if (index < position) position--;
        if (position >= symbols.size()) position = 0;
    }

    /**
     * avanza al siguiente simbolo y vuelve al primero al terminar
     *
     * @return true si la rueda tiene simbolos para girar
     */
    public boolean spin() {
        if (symbols.isEmpty()) return false;
        position = (position + 1) % symbols.size();
        return true;
    }

    /**
     * selecciona un simbolo por su color
     *
     * @param color color que se desea seleccionar
     * @return true si el simbolo existe
     */
    public boolean placeSymbol(String color) {
        int index = symbols.indexOf(color);
        if (index == -1) return false;
        position = index;
        return true;
    }

    /**
     * bloquea la rueda
     */
    public void lock() {
        locked = true;
    }

    /**
     * desbloquea la rueda
     */
    public void unlock() {
        locked = false;
    }

    /**
     * informa si la rueda esta bloqueada
     *
     * @return true si esta bloqueada
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * comprueba si un simbolo pertenece a la rueda
     *
     * @param color nombre del simbolo
     * @return true si el simbolo existe
     */
    public boolean hasSymbol(String color) {
        return symbols.contains(color);
    }

    /**
     * consulta el color seleccionado
     *
     * @return color seleccionado o null si la rueda esta vacia
     */
    public String selectedSymbol() {
        if (symbols.isEmpty()) return null;
        return symbols.get(position);
    }

    /**
     * consulta cuantos simbolos contiene la rueda
     *
     * @return cantidad de simbolos
     */
    public int size() {
        return symbols.size();
    }

    /**
     * consulta los simbolos de la rueda
     *
     * @return colores en el orden de la rueda
     */
    public String[] symbols() {
        return symbols.toArray(new String[0]);
    }
}
