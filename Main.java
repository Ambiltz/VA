package org.upiiz;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import static org.upiiz.CargarImagen.cargarDesdeResources;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

        public class JFrameImagen extends JFrame{
            private JLabel etiqueta;

            public JFrameImagen(Image aux, String titulo) {
                etiqueta = new JLabel();
                ImageIcon ii = new ImageIcon(aux);
                etiqueta.setIcon(ii);
                add(etiqueta);
                // Opcional: configurar tamaño y cierre
                setTitle(titulo);
                setSize(400, 400);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setVisible(true);
            }
            public JFrameImagen(Image aux) {
                this(aux, "Imagen");
            }
        }

    public static Image abrirImagen (){

        try {
            // definir los filtros para lectura
            FileNameExtensionFilter filtro =
                    new FileNameExtensionFilter("Imagenes","jpg","jpeg","png","bmp");
            // crear un selector de archivos
            JFileChooser selector = new JFileChooser();
            // agregar el filtro al selector
            selector.addChoosableFileFilter(filtro);
            // especificar que solo se puedan abrir archivos
            selector.setFileSelectionMode(JFileChooser.FILES_ONLY);

            //ejecutar el selector de imagenes

            int res = selector.showOpenDialog(null);

            if (res == 1 ){

                return null;

            }

            File archivo = selector.getSelectedFile();

            BufferedImage bi = ImageIO.read(archivo);

            return toImage(bi);
        } catch (IOException ex) {
            Logger.getLogger(JFrameImagen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static Image toImage (BufferedImage bi){
        return bi.getScaledInstance(bi.getWidth(),bi.getHeight(), BufferedImage.TYPE_INT_RGB);
    }
    public static BufferedImage toBufferedImage (Image imagen){
        // imagen es un objeto de tipo BufferedImage
//        if (imagen instanceof BufferedImage){
//          return (BufferedImage)imagen;
//        }
        BufferedImage bi =
                new BufferedImage(imagen.getWidth(null), imagen.getHeight(null), BufferedImage.TYPE_INT_RGB);

        Graphics2D nueva = bi.createGraphics();
        nueva.drawImage(imagen, 0, 0,null);
        nueva.dispose();


        return bi;

    }
    public static Image copiarImagen(Image i){
        BufferedImage bi = toBufferedImage(i);
        return bi.getScaledInstance(bi.getWidth(),bi.getHeight(), BufferedImage.TYPE_INT_RGB);
    }
    //abrir la imagen
    public static void main(String[] args) {
        Image img = cargarDesdeResources("/imagen.jpg");
        Image img1 = cargarDesdeResources("/Mucho_contraste.png");
        Image img2 = cargarDesdeResources("/Poco_contraste.png");
        Image img3 = cargarDesdeResources("/imagen.jpg");
        BufferedImage img4 = toBufferedImage(cargarDesdeResources("/img.png"));
        BufferedImage bis = toBufferedImage(img);  //modificacion
        int alto = bis.getHeight();
        int ancho = bis.getWidth();
        int verde = 0xFF00FF00;
        for(int y=0;y<20;y++){
            for(int x=0;x<20;x++){

                    bis.setRGB(x,y,verde);

            }
        }

          run(bis);

        if (img != null) {
            Main instancia = new Main();
            Main.JFrameImagen ventana1 = instancia.new JFrameImagen(img, "Imagen RGB");
            Main.JFrameImagen ventana = instancia.new JFrameImagen(bis, "Imagen con un pixel pintado");
            Main.JFrameImagen ventana2 = instancia.new JFrameImagen(img4, "Imagen RGB");
            BufferedImage traslacion = trasladar(img4, 50, 30);
            instancia.new JFrameImagen(traslacion, "Traslación (dx=50, dy=30)");
            BufferedImage escalada =  escalar(img4, 1.5, 1.5);
            instancia.new JFrameImagen(escalada, "Escalada");
            BufferedImage rotada45 = rotar(img4, 45);
            instancia.new JFrameImagen(rotada45, "Rotada 45°");

            BufferedImage gris1 = convertirUna(img1);
            BufferedImage gris2 = convertirUna(img2);
            BufferedImage gris3 = convertirUna(img3);

            // Mostrar las versiones grises
            instancia.new JFrameImagen(gris1, "Mucho Contraste");
            instancia.new JFrameImagen(gris2, "Poco Contraste");
            instancia.new JFrameImagen(gris3, "Imagen Escala de grises");
            mostrarHistogramas(bis, "Histograma de Imagen RGB");
            mostrarHistogramas(gris1, "Mucho Contraste Histograma");
            mostrarHistogramas(gris2, "Poco Contraste Histograma");
            mostrarHistogramas(gris3, "Imagen Escala de grises Histograma");

            // Umbral y binarización
            int UMBRAL = 150;
            BufferedImage bin2 = operacionOr(gris1, gris2);
            BufferedImage bin3 = binarizar(gris3, UMBRAL);
            instancia.new JFrameImagen(bin3, "Imagen (umbral=" + UMBRAL + ")");
            mostrarHistogramas(bin3, "Imagen (umbral=" + UMBRAL + ")");
            instancia.new JFrameImagen(bin2, "Mucho Contraste");
        } else {
            System.out.println("No se pudo cargar la imagen.");
        }


    }
    public static void mostrarHistogramas(Image img, String nombreImagen) {
        BufferedImage bis = toBufferedImage(img);
        int w = bis.getWidth();
        int h = bis.getHeight();

        double[] r = new double[w * h];
        double[] g = new double[w * h];
        double[] b = new double[w * h];

        int i = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p = bis.getRGB(x, y);
                r[i] = (p >> 16) & 0xFF;
                g[i] = (p >> 8) & 0xFF;
                b[i] = p & 0xFF;
                i++;
            }
        }

        mostrarCanal("Rojo",  r, Color.RED, nombreImagen);
        mostrarCanal("Verde", g, Color.GREEN, nombreImagen);
        mostrarCanal("Azul",  b, Color.BLUE, nombreImagen);
    }
    public static BufferedImage binarizar(BufferedImage gris, int umbral) {
        int w = gris.getWidth();
        int h = gris.getHeight();

        BufferedImage bin = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p = gris.getRGB(x, y);
                int lum = p & 0xFF;

                // 1 | 0  ->  255 | 0
                int valor = (lum > umbral) ? 255 : 0;

                int rgb = (valor << 16) | (valor << 8) | valor;
                bin.setRGB(x, y, rgb);
            }
        }
        return bin;
    }
    private static void mostrarCanal(String nombre, double[] datos, Color color, String nombreImagen) {
        HistogramDataset ds = new HistogramDataset();
        ds.addSeries(nombre, datos, 256, 0, 256);

        JFreeChart chart = ChartFactory.createHistogram(
                "Canal " + nombre + "-"+nombreImagen, "Intensidad", "Frecuencia", ds);

        XYBarRenderer rend = (XYBarRenderer) chart.getXYPlot().getRenderer();
        rend.setSeriesPaint(0, color);
        rend.setShadowVisible(false);

        JFrame f = new JFrame("Histograma " + nombre);
        f.setContentPane(new ChartPanel(chart));
        f.setSize(700, 400);
        f.setLocationByPlatform(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }
    public static void run(BufferedImage ip) {
        int w = ip.getWidth();
        int h = ip.getHeight();

        for (int v = 0; v < h; v++) {
            for (int u = 0; u < w; u++) {
                int a = ip.getRGB(u, v);
                int b=(int) (a * 1.5 + 0.5);
                if (b > 255)
                    b = 255; // clamp to the maximum value (amax)
                ip.setRGB(u, v, b);
            }
        }
    }
    public static BufferedImage[] ImagenEscalaGrises(Image img1, Image img2, Image img3){
        return new BufferedImage[] {
                convertirUna(img1),
                convertirUna(img2),
                convertirUna(img3)
        };
    }
    private static BufferedImage convertirUna(Image img) {
        BufferedImage src = toBufferedImage(img);
        int w = src.getWidth();
        int h = src.getHeight();

        BufferedImage gris = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p = src.getRGB(x, y);

                int r = (p >> 16) & 0xFF;
                int g = (p >> 8)  & 0xFF;
                int b = p & 0xFF;

                int luminancia = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                int grisRGB = (luminancia << 16) | (luminancia << 8) | luminancia;

                gris.setRGB(x, y, grisRGB);
            }
        }

        return gris;
    }
    //Operaciones logicas
    private static BufferedImage operacionOr(BufferedImage img1, BufferedImage img2) {
        int ancho = Math.min(img1.getWidth(),  img2.getWidth());
        int alto  = Math.min(img1.getHeight(), img2.getHeight());

        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);

                int r = ((rgb1 >> 16) & 0xFF) | ((rgb2 >> 16) & 0xFF);
                int g = ((rgb1 >> 8)  & 0xFF) | ((rgb2 >> 8)  & 0xFF);
                int b = ( rgb1        & 0xFF) | ( rgb2        & 0xFF);

                resultado.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        return resultado;
    }
    //Traslacion en eje horizntal y eje vertical, origen: la eje superior izquierdo
    public static BufferedImage trasladar(BufferedImage img, int dx, int dy) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // Rellenar de negro (o blanco, según se prefiera) las zonas vacías
        Graphics2D g = resultado.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);
        g.dispose();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int nx = x + dx;
                int ny = y + dy;

                // Solo copiamos si el nuevo píxel cae dentro de la imagen
                if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                    resultado.setRGB(nx, ny, img.getRGB(x, y));
                }
            }
        }
        return resultado;
    }
    // escalamiento y rotacion
    public static BufferedImage escalar(Image img, double sx, double sy) {
        BufferedImage src = toBufferedImage(img);
        int w = src.getWidth();
        int h = src.getHeight();

        int nuevoW = (int) Math.round(w * sx);
        int nuevoH = (int) Math.round(h * sy);

        BufferedImage resultado = new BufferedImage(nuevoW, nuevoH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = resultado.createGraphics();
        // Interpolación bilineal para que no se vea pixelado al agrandar
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, nuevoW, nuevoH, null);
        g.dispose();

        return resultado;
    }
    // Rotación alrededor del centro de la imagen, ángulo en grados
    public static BufferedImage rotar(Image img, double grados) {
        BufferedImage src = toBufferedImage(img);
        int w = src.getWidth();
        int h = src.getHeight();

        double rad = Math.toRadians(grados);

        // Tamaño del lienzo resultante para que la imagen rotada quepa completa
        double cos = Math.abs(Math.cos(rad));
        double sin = Math.abs(Math.sin(rad));
        int nuevoW = (int) Math.round(w * cos + h * sin);
        int nuevoH = (int) Math.round(w * sin + h * cos);

        BufferedImage resultado = new BufferedImage(nuevoW, nuevoH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = resultado.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Mover el origen al centro del nuevo lienzo, rotar, y dibujar centrado
        g.translate(nuevoW / 2.0, nuevoH / 2.0);
        g.rotate(rad);
        g.drawImage(src, -w / 2, -h / 2, null);
        g.dispose();

        return resultado;
    }
}

