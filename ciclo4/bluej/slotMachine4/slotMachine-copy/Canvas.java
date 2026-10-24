import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Lienzo utilizado por las figuras.
 * Basado en Canvas de BlueJ, de Bruce Quig y Michael Kolling.
 */
public class Canvas
{
    private static Canvas canvasSingleton;

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private BufferedImage canvasImage;
    private ArrayList<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;

    /** Obtiene y muestra el lienzo compartido. */
    public static Canvas getCanvas()
    {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas(
                "slot machine", 3200, 300, Color.white
            );
        }

        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    /** Crea el lienzo y su imagen de dibujo. */
    private Canvas(
        String title, int width, int height, Color background)
    {
        backgroundColour = background;
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object, ShapeDescription>();

        canvasImage = new BufferedImage(
            width, height, BufferedImage.TYPE_INT_RGB
        );

        graphic = canvasImage.createGraphics();
        graphic.setColor(backgroundColour);
        graphic.fillRect(0, 0, width, height);

        canvas = new CanvasPane();
        canvas.setPreferredSize(new Dimension(width, height));

        frame = new JFrame(title);
        frame.setContentPane(canvas);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
    }

    /** Cambia la visibilidad de la ventana. */
    public void setVisible(boolean visible)
    {
        frame.setVisible(visible);
    }

    /** Agrega o actualiza una figura. */
    public void draw(Object object, String color, Shape shape)
    {
        objects.remove(object);
        objects.add(object);

        shapes.put(object, new ShapeDescription(shape, color));
        redraw();
    }

    /** Elimina una figura del lienzo. */
    public void erase(Object object)
    {
        objects.remove(object);
        shapes.remove(object);
        redraw();
    }

    /** Indica si un nombre representa un color admitido. */
    public static boolean isValidColor(String name)
    {
        return resolveColor(name) != null;
    }

    /**
     * Obtiene el color.
     * Retorna null cuando el nombre no es valido.
     */
    private static Color resolveColor(String name)
    {
        if (name == null) {
            return null;
        }

        if (name.equals("red")) {
            return Color.red;
        }
        if (name.equals("black")) {
            return Color.black;
        }
        if (name.equals("blue")) {
            return Color.blue;
        }
        if (name.equals("yellow")) {
            return Color.yellow;
        }
        if (name.equals("green")) {
            return Color.green;
        }
        if (name.equals("magenta")) {
            return Color.magenta;
        }
        if (name.equals("white")) {
            return Color.white;
        }
        if (name.equals("orange")) {
            return Color.orange;
        }
        if (name.equals("cyan")) {
            return Color.cyan;
        }
        if (name.equals("pink")) {
            return Color.pink;
        }
        if (name.equals("gray")) {
            return Color.gray;
        }

        // Acepta symbol0, symbol1, symbol2, etc.
        if (name.matches("symbol(0|[1-9][0-9]*)")) {
            try {
                int number = Integer.parseInt(name.substring(6));

                float hue = (float) (
                    (number * 0.61803398875) % 1.0
                );

                return Color.getHSBColor(hue, 0.75f, 0.95f);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    /** Establece el color de dibujo. */
    public void setForegroundColor(String name)
    {
        Color color = resolveColor(name);

        if (color == null) {
            color = Color.black;
        }

        graphic.setColor(color);
    }

    /** Espera la cantidad indicada de milisegundos. */
    public void wait(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Redibuja todas las figuras registradas. */
    private void redraw()
    {
        graphic.setColor(backgroundColour);

        graphic.fillRect(
            0, 0, canvasImage.getWidth(), canvasImage.getHeight()
        );

        for (Object object : objects) {
            shapes.get(object).draw();
        }

        canvas.repaint();
    }

    /** Panel que presenta la imagen del lienzo. */
    private class CanvasPane extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    /** Guarda una figura y su color. */
    private class ShapeDescription
    {
        private Shape shape;
        private String color;

        public ShapeDescription(Shape shape, String color)
        {
            this.shape = shape;
            this.color = color;
        }

            /**
     * Dibuja la figura y agrega un borde negro a los circulos.
     */
    public void draw()
    {
        setForegroundColor(color);
        graphic.fill(shape);
    
        if (shape instanceof java.awt.geom.Ellipse2D) {
            graphic.setColor(Color.black);
        }

        graphic.draw(shape);
    }
    }
}