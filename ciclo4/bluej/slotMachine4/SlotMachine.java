import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Simula una maquina tragamonedas con diferentes
 * tipos de ruedas y simbolos.
 *
 * @author rodriguezr-vargasc
 * @version 4.1
 */
public class SlotMachine
{
    private Rectangle body;
    private boolean visible;
    private boolean lastOperationOk;
    private ArrayList<Wheel> wheels;
    private ArrayList<Symbol> symbols;

    /** Crea una maquina vacia e invisible. */
    public SlotMachine()
    {
        body = new Rectangle();
        body.changeSize(220, 250);
        body.moveHorizontal(-40);
        body.moveVertical(30);
        body.changeColor("black");

        wheels = new ArrayList<Wheel>();
        symbols = new ArrayList<Symbol>();
        visible = false;
        lastOperationOk = true;
    }

    /**
     * Crea n ruedas normales y n simbolos normales.
     *
     * @param n cantidad de ruedas y simbolos, entre 3 y 50
     */
    public SlotMachine(int n)
    {
        this();

        if (n < 3 || n > 50) {
            operationFailed("n debe estar entre 3 y 50");
            return;
        }

        for (int i = 0; i < n; i++) {
            symbols.add(new Symbol("symbol" + i));
            addWheel(i + 1);
        }

        for (Wheel wheel : wheels) {
            int index = (int) (Math.random() * symbols.size());
            wheel.useRegisteredSymbol(symbols.get(index));
        }

        if (isJackpot()) {
            int index = findSymbolIndex(
                wheels.get(1).getSymbol()
            );

            wheels.get(1).useRegisteredSymbol(
                symbols.get((index + 1) % n)
            );
        }

        updateJackpotAppearence();
    }

    /** Agrega una rueda normal. */
    public void addWheel(int pos)
    {
        addWheel("normal", pos);
    }

    /**
     * Agrega una rueda del tipo indicado.
     *
     * @param type normal, lefty o rebel
     * @param pos posicion solicitada
     */
    public void addWheel(String type, int pos)
    {
        Wheel wheel;

        if ("normal".equals(type)) {
            wheel = new Wheel();
        } else if ("lefty".equals(type)) {
            wheel = new LeftyWheel();
        } else if ("rebel".equals(type)) {
            wheel = new RebelWheel();
        } else {
            operationFailed("tipo de rueda no valido");
            return;
        }

        int position = Math.max(
            1, Math.min(pos, wheels.size() + 1)
        );

        wheels.add(position - 1, wheel);
        updateMachineAppearance();
        operationSucceeded();
    }

    /** Elimina una rueda si su tipo lo permite. */
    public void delWheel(int pos)
    {
        if (wheels.isEmpty()) {
            operationFailed("no hay ruedas para eliminar");
            return;
        }

        int index = wheelIndex(pos);
        Wheel wheel = wheels.get(index);

        if (!wheel.canDelete()) {
            operationFailed(
                "esta rueda no permite ser eliminada"
            );
            return;
        }

        wheels.remove(index);
        wheel.makeInvisible();
        updateMachineAppearance();
        operationSucceeded();
    }

    /** Bloquea una rueda si su tipo lo permite. */
    public void lock(int pos)
    {
        if (wheels.isEmpty()) {
            operationFailed("no hay ruedas para fijar");
            return;
        }

        Wheel wheel = wheels.get(wheelIndex(pos));

        if (!wheel.canLock()) {
            operationFailed(
                "esta rueda no permite ser bloqueada"
            );
            return;
        }

        wheel.lock();
        operationSucceeded();
    }

    /** Desbloquea una rueda. */
    public void unlock(int pos)
    {
        if (wheels.isEmpty()) {
            operationFailed("no hay ruedas para liberar");
            return;
        }

        wheels.get(wheelIndex(pos)).unlock();
        operationSucceeded();
    }

    /** Intercambia dos ruedas si ambas lo permiten. */
    public void changePlaceWheel(int p1, int p2)
    {
        if (wheels.isEmpty()) {
            operationFailed("no hay ruedas para intercambiar");
            return;
        }

        int first = wheelIndex(p1);
        int second = wheelIndex(p2);

        Wheel a = wheels.get(first);
        Wheel b = wheels.get(second);

        if (!a.canSwap() || !b.canSwap()) {
            operationFailed(
                "una rueda no permite el intercambio"
            );
            return;
        }

        wheels.set(first, b);
        wheels.set(second, a);

        updateMachineAppearance();
        operationSucceeded();
    }

    /** Intercambia dos ruedas. */
    public void swap(int wheel1, int wheel2)
    {
        changePlaceWheel(wheel1, wheel2);
    }

    /** Agrega un simbolo normal. */
    public void addSymbol(int pos, String color)
    {
        addSymbol("normal", pos, color);
    }

    /**
     * Agrega un simbolo del tipo indicado.
     *
     * @param type normal, ephemeral, shy o growing
     * @param pos posicion solicitada
     * @param color color unico del simbolo
     */
    public void addSymbol(String type, int pos, String color)
    {
        if (!Canvas.isValidColor(color)) {
            operationFailed("color no valido");
            return;
        }

        if (findSymbolIndex(color) >= 0) {
            operationFailed("el simbolo ya existe");
            return;
        }

        Symbol symbol = createSymbol(type, color);

        if (symbol == null) {
            operationFailed("tipo de simbolo no valido");
            return;
        }

        int position = Math.max(
            1, Math.min(pos, symbols.size() + 1)
        );

        symbols.add(position - 1, symbol);
        operationSucceeded();
    }

    /** Elimina un simbolo si ninguna rueda lo muestra. */
    public void delSymbol(String color)
    {
        int index = findSymbolIndex(color);

        if (index < 0) {
            operationFailed("el simbolo no existe");
            return;
        }

        for (Wheel wheel : wheels) {
            if (color.equals(wheel.getSymbol())) {
                operationFailed(
                    "el simbolo esta siendo usado"
                );
                return;
            }
        }

        Symbol removed = symbols.remove(index);

        for (Wheel wheel : wheels) {
            wheel.forgetRegisteredSymbol(removed);
        }

        operationSucceeded();
    }

    /** Coloca un simbolo sin ejecutar un giro. */
    public void placeSymbol(int wheel, String color)
    {
        int index = findSymbolIndex(color);

        if (wheels.isEmpty() || index < 0) {
            operationFailed("operacion no valida");
            return;
        }

        Wheel selected = wheels.get(wheelIndex(wheel));

        if (selected.isLocked()) {
            if (!color.equals(selected.getSymbol())) {
                operationFailed(
                    "una rueda fijada no puede cambiar"
                );
                return;
            }
        } else {
            selected.useRegisteredSymbol(symbols.get(index));
        }

        updateJackpotAppearence();
        redrawWheels();
        operationSucceeded();
    }

    /** Gira una rueda con una propuesta aleatoria. */
    public void spin(int wheel)
    {
        if (!canSpin(wheel)) {
            return;
        }

        int index = wheelIndex(wheel);
        int random = (int) (Math.random() * symbols.size());

        if (!applySpin(index, symbols.get(random).getColor())) {
            return;
        }

        operationSucceeded();
    }

    /**
     * Gira las ruedas libres de izquierda a derecha.
     * Conserva el fallo si alguna rueda no puede girar.
     */
    public void spin()
    {
        if (wheels.isEmpty() || symbols.isEmpty()) {
            operationFailed("no hay ruedas o simbolos");
            return;
        }

        for (int i = 0; i < wheels.size(); i++) {
            if (!wheels.get(i).isLocked()) {
                spin(i + 1);

                if (!ok()) {
                    return;
                }
            }
        }

        operationSucceeded();
    }

    /**
     * Gira una rueda una cantidad de pasos.
     * Cada paso selecciona un simbolo y aplica su comportamiento.
     *
     * @param wheel posicion solicitada
     * @param steps pasos; negativos para girar hacia atras
     */
    public void spin(int wheel, int steps)
    {
        if (!canSpin(wheel)) {
            return;
        }

        int index = wheelIndex(wheel);

        int current = findSymbolIndex(
            wheels.get(index).getSymbol()
        );

        if (current < 0) {
            operationFailed("simbolo no existente");
            return;
        }

        int direction = Integer.compare(steps, 0);
        long total = Math.abs((long) steps);

        for (long step = 0; step < total; step++) {
            current = Math.floorMod(
                current + direction, symbols.size()
            );

            if (!applySpin(
                index, symbols.get(current).getColor())) {

                return;
            }

            if (visible) {
                Canvas.getCanvas().wait(250);
            }
        }

        operationSucceeded();
    }

    /** Conserva el nombre anterior del giro por pasos. */
    public void spin_pasos(int pos, int pasos)
    {
        spin(pos, pasos);
    }

    /**
     * Valida la configuracion antes de modificar las ruedas.
     * Gira de izquierda a derecha respetando cada tipo.
     *
     * @param setSymbols colores propuestos para las ruedas
     */
    public void spin(String[] setSymbols)
    {
        if (setSymbols == null ||
            setSymbols.length != wheels.size()) {

            operationFailed("la configuracion no es valida");
            return;
        }

        for (int i = 0; i < setSymbols.length; i++) {
            if (findSymbolIndex(setSymbols[i]) < 0) {
                operationFailed(
                    "la configuracion contiene un simbolo no valido"
                );
                return;
            }

            Wheel wheel = wheels.get(i);

            if (wheel.isLocked() &&
                !setSymbols[i].equals(wheel.getSymbol())) {

                operationFailed(
                    "una rueda fijada no puede cambiar"
                );
                return;
            }
        }

        for (int i = 0; i < wheels.size(); i++) {
            if (!wheels.get(i).isLocked()) {
                if (!applySpin(i, setSymbols[i])) {
                    return;
                }
            }
        }

        operationSucceeded();
    }

    /**
     * Aplica un giro a una rueda.
     *
     * @param index indice de la rueda, empezando desde cero
     * @param proposed color propuesto
     * @return true si se pudo realizar el giro
     */
    private boolean applySpin(int index, String proposed)
    {
        Wheel wheel = wheels.get(index);
        Wheel left = null;

        if (index > 0) {
            left = wheels.get(index - 1);
        }

        String color = wheel.selectSpinSymbol(proposed, left);
        int symbolIndex = findSymbolIndex(color);

        if (symbolIndex < 0) {
            operationFailed(
                "la rueda no tiene un simbolo valido para copiar"
            );
            return false;
        }

        wheel.useRegisteredSymbol(symbols.get(symbolIndex));
        wheel.onSpin();

        updateJackpotAppearence();
        redrawWheels();

        return true;
    }

    /** Valida que haya elementos y que la rueda este libre. */
    private boolean canSpin(int pos)
    {
        if (wheels.isEmpty() || symbols.isEmpty()) {
            operationFailed("no hay ruedas o simbolos");
            return false;
        }

        if (wheels.get(wheelIndex(pos)).isLocked()) {
            operationFailed("la rueda esta fijada");
            return false;
        }

        return true;
    }

    /** Retorna los colores registrados, en orden. */
    public String[] symbols()
    {
        String[] result = new String[symbols.size()];

        for (int i = 0; i < symbols.size(); i++) {
            result[i] = symbols.get(i).getColor();
        }

        return result;
    }

    /** Retorna los colores de las ruedas de izquierda a derecha. */
    public String[] configuration()
    {
        String[] result = new String[wheels.size()];

        for (int i = 0; i < wheels.size(); i++) {
            result[i] = wheels.get(i).getSymbol();
        }

        return result;
    }

    /** Cuenta los colores distintos sin contar ruedas vacias. */
    public int distinctSymbols()
    {
        ArrayList<String> distinct = new ArrayList<String>();

        for (Wheel wheel : wheels) {
            String color = wheel.getSymbol();

            if (color != null && !distinct.contains(color)) {
                distinct.add(color);
            }
        }

        return distinct.size();
    }

    /** Indica si todas las ruedas tienen el mismo simbolo. */
    public boolean isJackpot()
    {
        if (wheels.isEmpty()) {
            return false;
        }

        String first = wheels.get(0).getSymbol();

        if (first == null) {
            return false;
        }

        for (Wheel wheel : wheels) {
            if (!first.equals(wheel.getSymbol())) {
                return false;
            }
        }

        return true;
    }

    /** Muestra la maquina. */
    public void makeVisible()
    {
        visible = true;
        body.makeVisible();
        redrawWheels();
    }

    /** Oculta la maquina. */
    public void makeInvisible()
    {
        visible = false;
        body.makeInvisible();

        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
    }

    /** Oculta el simulador. */
    public void exit()
    {
        makeInvisible();
        operationSucceeded();
    }

    /** Retorna el resultado de la ultima operacion. */
    public boolean ok()
    {
        return lastOperationOk;
    }

    /**
     * Ajusta una posicion a un indice valido.
     * Solo se utiliza cuando hay ruedas.
     */
    private int wheelIndex(int pos)
    {
        return Math.max(
            1, Math.min(pos, wheels.size())
        ) - 1;
    }

    /** Busca un color; retorna -1 si no existe. */
    private int findSymbolIndex(String color)
    {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getColor().equals(color)) {
                return i;
            }
        }

        return -1;
    }

    /** Crea un simbolo del tipo solicitado. */
    private Symbol createSymbol(String type, String color)
    {
        if ("normal".equals(type)) {
            return new Symbol(color);
        }

        if ("ephemeral".equals(type)) {
            return new EphemeralSymbol(color);
        }

        if ("shy".equals(type)) {
            return new ShySymbol(color);
        }

        if ("growing".equals(type)) {
            return new GrowingSymbol(color);
        }

        return null;
    }

    /** Actualiza tamaño, posiciones y colores. */
    private void updateMachineAppearance()
    {
        body.changeSize(
            220, Math.max(250, 100 + 60 * wheels.size())
        );

        for (int i = 0; i < wheels.size(); i++) {
            wheels.get(i).setPosition(i + 1);
        }

        updateJackpotAppearence();
        redrawWheels();
    }

    /** Dibuja las ruedas si la maquina esta visible. */
    private void redrawWheels()
    {
        if (visible) {
            for (Wheel wheel : wheels) {
                wheel.makeVisible();
            }
        }
    }

    /** Actualiza el cuerpo segun el estado de jackpot. */
    private void updateJackpotAppearence()
    {
        if (isJackpot()) {
            body.changeColor("green");
        } else {
            body.changeColor("black");
        }
    }

    /** Registra un fallo y muestra el aviso si esta visible. */
    private void operationFailed(String message)
    {
        lastOperationOk = false;

        if (visible) {
            JOptionPane.showMessageDialog(null, message);
        }
    }

    /** Registra una operacion correcta. */
    private void operationSucceeded()
    {
        lastOperationOk = true;
    }
}