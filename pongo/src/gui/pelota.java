package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class pelota extends JPanel {

    private static final long serialVersionUID = 1L;

    private int velX;
    private int velY;

    private final int VELOCIDAD_INICIAL = 3;
    private final int MAX_VELOCIDAD = 10;

    private JLabel raquetaIzq;
    private JLabel raquetaDer;

    private Image imagenPelota;

    private Timer aumentoVelocidadTimer;

    public pelota(int x, int y, JLabel raquetaIzq, JLabel raquetaDer) {
        setBounds(x, y, 20, 20);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        this.raquetaIzq = raquetaIzq;
        this.raquetaDer = raquetaDer;

        velX = VELOCIDAD_INICIAL;
        velY = VELOCIDAD_INICIAL;

        try {
            imagenPelota = ImageIO.read(new File("media/PELOTA.png"));
        } catch (IOException e) {
            System.out.println("Error: No se pudo cargar la imagen PELOTA.png. Revisa que el archivo exista en la carpeta media.");
        }

        aumentoVelocidadTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aumentarVelocidad();
            }
        });
        aumentoVelocidadTimer.start();
    }

    private void aumentarVelocidad() {
        if (Math.abs(velX) < MAX_VELOCIDAD) {
            velX += (velX > 0) ? 1 : -1;
        }
        if (Math.abs(velY) < MAX_VELOCIDAD) {
            velY += (velY > 0) ? 1 : -1;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagenPelota != null) {
            g.drawImage(imagenPelota, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, getWidth(), getHeight());
        }
    }

    public void iniciarMovimiento() {
        setLocation(getX() + velX, getY() + velY);

        if (getY() <= 232 || getY() >= 688) {
            velY = -velY;
        }

        if (getBounds().intersects(raquetaIzq.getBounds())) {
        	System.out.println(getX());
            velX = Math.abs(velX);
        }

        if (getBounds().intersects(raquetaDer.getBounds())) {
            velX = -Math.abs(velX);
            System.out.println(getX());
        }

        if (getX() <= 0) {
            resetPelota();
            velX = VELOCIDAD_INICIAL; 
            velY = VELOCIDAD_INICIAL; 
        }

        if (getX() >= getParent().getWidth() - getWidth()) {
            resetPelota();
            velX = -VELOCIDAD_INICIAL; 
            velY = VELOCIDAD_INICIAL; 
        }
    }

    private void resetPelota() {
        int centroX = getParent().getWidth() / 2 - getWidth() / 2;
        int centroY = getParent().getHeight() / 2 - getHeight() / 2;
        setLocation(centroX, centroY);
    }
}
