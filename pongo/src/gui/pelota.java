package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class pelota extends JPanel {

    private static final long serialVersionUID = 1L;

    private int velX;
    private int velY;
    private int XInicial;
    private int YInicial;

    public int VelInicial = 3;
    public int VelMaxima = 12;

    private JLabel jugador1;
    private JLabel jugador2;
    private ventanaPong ventana;

    private Image imagenPelota;


    public pelota(int x, int y, JLabel jugador1, JLabel jugador2, ventanaPong ventana) {
    	this.XInicial=x;
    	this.YInicial=y;
        setBounds(XInicial, YInicial, 20, 20);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.ventana = ventana;

        velX = VelInicial;
        velY = VelInicial;

        try {
            imagenPelota = ImageIO.read(new File("media/PELOTA.png"));		// imagen de la pelota
        } catch (IOException e) {
            System.out.println("Error: No se pudo cargar la imagen PELOTA.png.");
        }

    }

    // funcion para cargar la imagen de la pelota
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenPelota != null) g.drawImage(imagenPelota, 0, 0, getWidth(), getHeight(), this);
        else {
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, getWidth(), getHeight());
        }
    }
    
    // funcion para aumentar la velocidad respecto a las coordenadas X e Y
    private void aumentarVelocidad() {
        if (Math.abs(velX) < VelMaxima) velX += (velX > 0) ? 1 : -1;
        if (Math.abs(velY) < VelMaxima) velY += (velY > 0) ? 1 : -1;
    }
    
    // funcion para el movimiento de la pelota
    public void movimiento() {
        setLocation(getX() + velX, getY() + velY);	// para poner las coordenadas de la pelota

        if (getY() <= 236 || getY() >= 715) velY = -velY;	// colisiones del eje Y de la pelota 

        if (getBounds().intersects(jugador1.getBounds())) 
        { 
        	velX = Math.abs(velX); 		// colision con el jugador 1
        	velY = Math.abs(velY) * (Math.random() < 0.5 ? 1 : -1); 
        	aumentarVelocidad();
       	}	
        if (getBounds().intersects(jugador2.getBounds())) 
        {	
        	velX = -Math.abs(velX);		// colision con el jugador 2
        	velY = Math.abs(velY) * (Math.random() < 0.5 ? 1 : -1); 
        	aumentarVelocidad();
        }
        // para que la pelota no se vaya del lado izquierdo de la ventana
        if (getX() <= 0) {
            ventana.sumarPuntoDer();
            resetPelota();
            velX = -VelInicial;
            velY = VelInicial;
        }
        
        // para que la pelota no se vaya del lado derecho de la ventana
        if (getX() >= getParent().getWidth() - getWidth()) {
        	System.out.println(getX());
            ventana.sumarPuntoIzq();
            resetPelota();
            velX = VelInicial;
            velY = VelInicial;
        }
    }
    
    // para resetear la pelota al lugar de origen
    private void resetPelota() {
        int centroX = XInicial;
        int centroY = YInicial;
        setLocation(centroX, centroY);
    }
}
