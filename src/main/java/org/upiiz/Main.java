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

            public JFrameImagen(Image aux) {
                etiqueta = new JLabel();
                ImageIcon ii = new ImageIcon(aux);
                etiqueta.setIcon(ii);
                add(etiqueta);
                // Modificar el
                // Opcional: configurar tamaño y cierre
                setSize(400, 300);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setVisible(true);
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
        Image img = cargarDesdeResources("/img.png");
       // BufferedImage buffered = toBufferedImage(img);



        BufferedImage bis = toBufferedImage(img);  //modificacion
        int alto = bis.getHeight();
        int ancho = bis.getWidth();
        int verde = 0xFF00FF00;
        for(int y=0;y<20;y++){
            for(int x=0;x<20;x++){

                    bis.setRGB(x,y,verde);

            }
        }



        if (img != null) {
            Main instancia = new Main();
            Main.JFrameImagen ventana1 = instancia.new JFrameImagen(img);
            Main.JFrameImagen ventana = instancia.new JFrameImagen(bis);

            mostrarHistogramas(bis);
        } else {
            System.out.println("No se pudo cargar la imagen.");
        }
    }
    public static void mostrarHistogramas(Image img) {
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

        mostrarCanal("Rojo",  r, Color.RED);
        mostrarCanal("Verde", g, Color.GREEN);
        mostrarCanal("Azul",  b, Color.BLUE);
    }

    private static void mostrarCanal(String nombre, double[] datos, Color color) {
        HistogramDataset ds = new HistogramDataset();
        ds.addSeries(nombre, datos, 256, 0, 256);

        JFreeChart chart = ChartFactory.createHistogram(
                "Canal " + nombre, "Intensidad", "Frecuencia", ds);

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
}