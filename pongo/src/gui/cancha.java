package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class cancha extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage fondoImage;
	
	 private int width;
	 private int height;

	
	public cancha(int width, int height) {
	    this.width = width;
        this.height = height;
		try {
            fondoImage = ImageIO.read(new File("media/Cancha1.png"));	// cargar imagen para la cancha
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(null);
	}
    @Override
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondoImage != null) {
            g.drawImage(fondoImage, 0, 0, width, height, this);		// para colocar la imagen de manera adecuada a la ventana
        }
    }

}
