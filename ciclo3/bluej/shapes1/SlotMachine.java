import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * representa una maquina tragamonedas con ruedas y simbolos
 *
 * @author gaby-mathias
 * @version 2026-2
 */
public class SlotMachine {
    private ArrayList<Wheel> wheels;
    private ArrayList<String> colors;
    private ArrayList<Rectangle> rectangles;
    private ArrayList<Circle> circles;
    private boolean visible;
    private boolean finished;
    private boolean lastOperation;

    /**
     * crea una maquina sin ruedas ni simbolos en modo invisible
     */
    public SlotMachine() {
        wheels = new ArrayList<Wheel>();
        colors = new ArrayList<String>();
        rectangles = new ArrayList<Rectangle>();
        circles = new ArrayList<Circle>();
        visible = false;
        finished = false;
        lastOperation = true;
    }

    /**
     * crea una maquina con n ruedas y n simbolos diferentes
     * las ruedas comparten el mismo orden de simbolos
     *
     * @param n cantidad de ruedas y simbolos, entre 3 y 50
     */
    public SlotMachine(int n) {
        this();
        if (n < 3 || n > 50) {
            throw new IllegalArgumentException("n debe estar entre 3 y 50");
        }
        String[] basic = {"red", "blue", "green", "yellow", "magenta", "black"};
        for (int i = 0; i < n; i++) {
            colors.add(i < basic.length ? basic[i] : "symbol" + (i + 1));
        }
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            Wheel wheel = new Wheel(colors);
            int shift = random.nextInt(n);
            for (int j = 0; j < shift; j++) wheel.spin();
            wheels.add(wheel);
        }
        if (isJackpot()) wheels.get(n - 1).spin();
    }

    /**
     * agrega una rueda en la posicion indicada
     * las posiciones comienzan en 1 y se ajustan a los limites
     *
     * @param pos posicion donde se agrega la rueda
     */
    public void addWheel(int pos) {
        if (!active()) return;
        pos = adjust(pos, wheels.size() + 1);
        wheels.add(pos - 1, new Wheel(colors));
        lastOperation = true;
        updateView();
    }

    /**
     * elimina una rueda de la maquina
     * si no hay ruedas, la operacion falla
     *
     * @param pos posicion de la rueda que se elimina
     */
    public void delWheel(int pos) {
        if (!active()) return;
        if (wheels.isEmpty()) {
            error("No hay ruedas para eliminar");
            return;
        }
        pos = adjust(pos, wheels.size());
        wheels.remove(pos - 1);
        lastOperation = true;
        updateView();
    }

    /**
     * agrega un color a todas las ruedas en la misma posicion
     *
     * @param pos posicion donde se agrega el simbolo
     * @param color nombre del color del simbolo
     */
    public void addSymbol(int pos, String color) {
        if (!active()) return;
        if (!validColor(color) || colors.contains(color)) {
            error("El color no es valido o ya existe");
            return;
        }
        pos = adjust(pos, colors.size() + 1);
        colors.add(pos - 1, color);
        for (Wheel wheel : wheels) wheel.addSymbol(pos, color);
        lastOperation = true;
        updateView();
    }

    /**
     * elimina un simbolo de todas las ruedas
     *
     * @param symbol color del simbolo que se elimina
     */
    public void delSymbol(String symbol) {
        if (!active()) return;
        if (!colors.contains(symbol)) {
            error("El simbolo no existe");
            return;
        }
        colors.remove(symbol);
        for (Wheel wheel : wheels) wheel.delSymbol(symbol);
        lastOperation = true;
        updateView();
    }

    /**
     * selecciona un simbolo en la rueda indicada
     *
     * @param wheel posicion de la rueda
     * @param symbol color del simbolo que se selecciona
     */
    public void placeSymbol(int wheel, String symbol) {
        if (!active()) return;
        if (wheels.isEmpty()) {
            error("No hay ruedas");
            return;
        }
        wheel = adjust(wheel, wheels.size());
        if (!wheels.get(wheel - 1).placeSymbol(symbol)) {
            error("El simbolo no existe");
            return;
        }
        lastOperation = true;
        updateView();
    }

    /**
     * avanza una posicion el simbolo seleccionado de una rueda
     *
     * @param wheel posicion de la rueda que gira
     */
    public void spin(int wheel) {
        if (!active()) return;
        if (wheels.isEmpty()) {
            error("No hay ruedas");
            return;
        }
        wheel = adjust(wheel, wheels.size());
        if (wheels.get(wheel - 1).isLocked()) {
            error("La rueda esta bloqueada");
            return;
        }
        if (!wheels.get(wheel - 1).spin()) {
            error("La rueda no tiene simbolos");
            return;
        }
        lastOperation = true;
        updateView();
    }

    /**
     * avanza una posicion todas las ruedas de la maquina
     */
    public void spin() {
        if (!active()) return;
        if (wheels.isEmpty() || colors.isEmpty()) {
            error("No hay ruedas o simbolos para girar");
            return;
        }
        boolean moved = false;
        for (Wheel wheel : wheels) {
            if (!wheel.isLocked()) moved = wheel.spin() || moved;
        }
        if (!moved) {
            error("No hay ruedas desbloqueadas para girar");
            return;
        }
        lastOperation = true;
        updateView();
    }

    /**
     * intercambia dos ruedas si ambas estan desbloqueadas
     *
     * @param first posicion de la primera rueda
     * @param second posicion de la segunda rueda
     */
    public void swap(int first, int second) {
        if (!active()) return;
        if (first < 1 || first > wheels.size() ||
            second < 1 || second > wheels.size()) {
            error("La posicion de una rueda no es valida");
            return;
        }
        Wheel one = wheels.get(first - 1);
        Wheel two = wheels.get(second - 1);
        if (one.isLocked() || two.isLocked()) {
            error("No se puede intercambiar una rueda bloqueada");
            return;
        }
        wheels.set(first - 1, two);
        wheels.set(second - 1, one);
        lastOperation = true;
        updateView();
    }

    /**
     * bloquea la rueda indicada
     *
     * @param wheel posicion de la rueda
     */
    public void lock(int wheel) {
        if (!active()) return;
        if (wheel < 1 || wheel > wheels.size()) {
            error("La rueda no existe");
            return;
        }
        wheels.get(wheel - 1).lock();
        lastOperation = true;
        updateView();
    }

    /**
     * desbloquea la rueda indicada
     *
     * @param wheel posicion de la rueda
     */
    public void unlock(int wheel) {
        if (!active()) return;
        if (wheel < 1 || wheel > wheels.size()) {
            error("La rueda no existe");
            return;
        }
        wheels.get(wheel - 1).unlock();
        lastOperation = true;
        updateView();
    }

    /**
     * gira una rueda el numero indicado de pasos
     * si esta visible muestra cada paso
     *
     * @param wheel posicion de la rueda
     * @param steps cantidad de pasos no negativa
     */
    public void spin(int wheel, int steps) {
        if (!active()) return;
        if (wheel < 1 || wheel > wheels.size() || steps < 0) {
            error("Rueda o cantidad de pasos no valida");
            return;
        }
        Wheel selected = wheels.get(wheel - 1);
        if (selected.isLocked() || selected.size() == 0) {
            error("La rueda esta bloqueada o sin simbolos");
            return;
        }
        for (int i = 0; i < steps; i++) {
            selected.spin();
            updateView();
            pauseView();
        }
        lastOperation = true;
    }

    /**
     * coloca los simbolos indicados en todas las ruedas
     * no cambia nada si algun simbolo no existe
     *
     * @param configuration colores deseados por rueda
     */
    public void setConfiguration(String[] configuration) {
        if (!active()) return;
        if (configuration == null || configuration.length != wheels.size()) {
            error("La configuracion no coincide con las ruedas");
            return;
        }
        for (int i = 0; i < configuration.length; i++) {
            Wheel wheel = wheels.get(i);
            if (wheel.isLocked() && !equalsColor(wheel.selectedSymbol(), configuration[i])) {
                error("No se puede cambiar una rueda bloqueada");
                return;
            }
            if (!wheel.hasSymbol(configuration[i])) {
                error("La configuracion contiene un simbolo inexistente");
                return;
            }
        }
        for (int i = 0; i < configuration.length; i++) {
            wheels.get(i).placeSymbol(configuration[i]);
        }
        lastOperation = true;
        updateView();
    }

    /**
     * compara dos colores, incluso cuando alguno es null
     */
    private boolean equalsColor(String first, String second) {
        if (first == null) return second == null;
        return first.equals(second);
    }

    /**
     * permite observar el giro paso a paso en modo visible
     */
    private void pauseView() {
        if (!visible) return;
        try {
            Thread.sleep(wheels.size() > 10 ? 35 : 150);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * consulta los colores disponibles en la maquina
     *
     * @return arreglo de colores en el orden registrado
     */
    public String[] symbols() {
        return colors.toArray(new String[0]);
    }

    /**
     * consulta la cantidad de colores diferentes
     *
     * @return numero de colores registrados
     */
    public int distinctSymbols() {
        HashSet<String> shown = new HashSet<String>();
        for (Wheel wheel : wheels) {
            String symbol = wheel.selectedSymbol();
            if (symbol != null) shown.add(symbol);
        }
        return shown.size();
    }

    /**
     * consulta los simbolos seleccionados de izquierda a derecha
     *
     * @return arreglo con el color seleccionado en cada rueda
     */
    public String[] configuration() {
        String[] result = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            result[i] = wheels.get(i).selectedSymbol();
        }
        return result;
    }

    /**
     * comprueba si todas las ruedas muestran el mismo color
     *
     * @return true si existe al menos una rueda con simbolos
     *         y todas muestran el mismo color
     */
    public boolean isJackpot() {
        if (wheels.isEmpty() || colors.isEmpty()) return false;
        String first = wheels.get(0).selectedSymbol();
        if (first == null) return false;
        for (Wheel wheel : wheels) {
            if (!first.equals(wheel.selectedSymbol())) return false;
        }
        return true;
    }

    /**
     * muestra y actualiza la representacion grafica
     */
    public void makeVisible() {
        if (!active()) return;
        visible = true;
        lastOperation = true;
        updateView();
    }

    /**
     * oculta la representacion grafica sin borrar el estado
     */
    public void makeInvisible() {
        if (!active()) return;
        visible = false;
        clearView();
        Canvas.getCanvas().setVisible(false);
        lastOperation = true;
    }

    /**
     * termina el simulador y cierra su representacion grafica
     */
    public void exit() {
        if (!active()) return;
        if (visible) makeInvisible();
        finished = true;
        lastOperation = true;
    }

    /**
     * consulta si la ultima operacion se realizo correctamente
     *
     * @return true si la ultima operacion fue exitosa
     */
    public boolean ok() {
        return lastOperation;
    }

    /**
     * ajusta una posicion para que este entre 1 y el maximo
     *
     * @param pos posicion solicitada
     * @param maximum ultima posicion permitida
     * @return posicion ajustada
     */
    private int adjust(int pos, int maximum) {
        if (pos < 1) return 1;
        if (pos > maximum) return maximum;
        return pos;
    }

    /**
     * comprueba si el color pertenece a los permitidos por shapes
     *
     * @param color nombre del color
     * @return true si el color es valido
     */
    private boolean validColor(String color) {
        return "red".equals(color) || "blue".equals(color) ||
               "green".equals(color) || "yellow".equals(color) ||
               "magenta".equals(color) || "black".equals(color);
    }

    /**
     * comprueba si la maquina todavia puede recibir operaciones
     *
     * @return true si el simulador no ha terminado
     */
    private boolean active() {
        if (finished) {
            lastOperation = false;
            return false;
        }
        return true;
    }

    /**
     * registra una operacion fallida y muestra un mensaje si esta visible
     *
     * @param message mensaje que explica el error
     */
    private void error(String message) {
        lastOperation = false;
        if (visible) JOptionPane.showMessageDialog(null, message);
    }

    /**
     * oculta y elimina las figuras de la vista anterior
     */
    private void clearView() {
        for (Circle circle : circles) circle.makeInvisible();
        for (Rectangle rectangle : rectangles) rectangle.makeInvisible();
        circles.clear();
        rectangles.clear();
    }

    /**
     * dibuja un rectangulo utilizando la clase de shapes
     *
     * @param x coordenada horizontal
     * @param y coordenada vertical
     * @param width ancho del rectangulo
     * @param height alto del rectangulo
     * @param color color del rectangulo
     */
    private void drawRectangle(int x, int y, int width, int height, String color) {
        Rectangle rectangle = new Rectangle();
        rectangle.changeSize(height, width);
        rectangle.changeColor(color);
        rectangle.moveHorizontal(x - 70);
        rectangle.moveVertical(y - 15);
        rectangle.makeVisible();
        rectangles.add(rectangle);
    }

    /**
     * dibuja un circulo utilizando la clase de shapes
     *
     * @param x coordenada horizontal
     * @param y coordenada vertical
     * @param diameter diametro del circulo
     * @param color color del circulo
     */
    private void drawCircle(int x, int y, int diameter, String color) {
        Circle circle = new Circle();
        circle.changeSize(diameter);
        String paint = color;
        if (color != null && color.startsWith("symbol")) {
            String[] palette = {"red", "blue", "green", "yellow", "magenta", "black"};
            int index = colors.indexOf(color);
            if (index >= 0) paint = palette[index % palette.length];
        }
        circle.changeColor(paint);
        circle.moveHorizontal(x - 20);
        circle.moveVertical(y - 15);
        circle.makeVisible();
        circles.add(circle);
    }

    /**
     * vuelve a dibujar la maquina cuando esta visible
     * el fondo es verde si hay jackpot y negro si no lo hay
     */
    private void updateView() {
        if (!visible) return;
        clearView();
        drawRectangle(10, 15, 280, 270, isJackpot() ? "green" : "black");
        boolean compact = wheels.size() > 12;
        int columns = compact ? 10 : 6;
        for (int i = 0; i < wheels.size(); i++) {
            int column = i % columns;
            int row = i / columns;
            int x = compact ? 14 + column * 28 : 17 + column * 46;
            int y = compact ? 35 + row * 47 : 50 + row * 100;
            int width = compact ? 24 : 38;
            int height = compact ? 37 : 75;
            int diameter = compact ? 13 : 24;
            drawRectangle(x, y, width, height, "white");
            String selected = wheels.get(i).selectedSymbol();
            if (selected != null) {
                drawCircle(x + (compact ? 5 : 7), y + (compact ? 11 : 20), diameter, selected);
            }
        }
    }
}
