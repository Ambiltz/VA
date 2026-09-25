package org.upiiz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CargarImagen {
    public static Image cargarDesdeResources(String ruta) {
        try {
            // El "/" inicial indica que la ruta es relativa al classpath
            java.io.InputStream is = Main.class.getResourceAsStream(ruta);
            if (is == null) {
                System.err.println("No se encontró la imagen: " + ruta);
                return null;
            }
            BufferedImage bi = ImageIO.read(is);
            return bi.getScaledInstance(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
