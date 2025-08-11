package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class pelota extends JPanel {

    public int x, y;
    public int movX = 1, movY = 1;
    public int aceleracion = 15;
    private static final long serialVersionUID = 1L;

    // Referencias a las raquetas para colisión
    private JPanel raquetaIzq;
    private JPanel raquetaDer;
    private int centroPelotaY;
    private int centroRaquetaIzq;
    private int centroRaquetaDer;

    public pelota(int startX, int startY, JPanel raquetaIzq, JPanel raquetaDer) {
        this.x = startX;
        this.y = startY;
        this.raquetaIzq = raquetaIzq;
        this.raquetaDer = raquetaDer;
        setBounds(x, y, 24, 24);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.orange);
        g.fillOval(0, 0, 24, 24);
    }

    public void iniciarMovimiento() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                x += movX;
                y += movY;
                setBounds(x, y, 24, 24);
                
                centroPelotaY=y + getHeight() / 2;
                centroRaquetaIzq=raquetaIzq.getY() + raquetaIzq.getHeight() / 2;
                centroRaquetaDer=raquetaDer.getY() + raquetaDer.getHeight() / 2;

                // Rebote arriba y abajo
                if (y >= 509 || y <= 11) {
                    movY *= -1;
                    timer.cancel();
                    reiniciarTiempo();
                }

                // Colisión con raqueta izquierda
                if (getBounds().intersects(raquetaIzq.getBounds())) {

                    if (centroPelotaY <= centroRaquetaIzq) {
                        // Impacto en la mitad superior
                        movY = -Math.abs(movY);  // rebote hacia arriba
                    } else {
                        // Impacto en la mitad inferior
                        movY = Math.abs(movY);   // rebote hacia abajo 
                    }
                    movX = Math.abs(movX); // rebote hacia la derecha
                    aceleracion = actualizarAceleracion(aceleracion);
                    timer.cancel();
                    reiniciarTiempo();
                }

                // Colisión con raqueta derecha
                if (getBounds().intersects(raquetaDer.getBounds())) {
                	if (centroPelotaY <= centroRaquetaDer) {
                        movY = -Math.abs(movY); 
                    } else {
                        movY = Math.abs(movY);  
                    }
                    movX = -Math.abs(movX);  // Asegura que vaya a la izquierda
                    aceleracion = actualizarAceleracion(aceleracion);
                    timer.cancel();
                    reiniciarTiempo();
                }

                // Sale por los bordes (x)
                if (x >= 751 || x <= 30) {
                    resetPelota();
                }

                repaint();
            }
        };
        timer.scheduleAtFixedRate(task, aceleracion, aceleracion);
    }

    public void reiniciarTiempo() {
        iniciarMovimiento();
    }

    public int actualizarAceleracion(int aceleracion) {
        if (aceleracion > 2) {
            aceleracion -= 1;
        } else if (aceleracion == 2) {
            aceleracion = 2;
        }
        return aceleracion;
    }

    private void resetPelota() {
        x = 393;
        y = 260;
        movX = movX > 0 ? -1 : 1;  // Cambia dirección
        aceleracion = 18;
        setBounds(x, y, 24, 24);
    }
}

