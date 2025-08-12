package gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class pelota extends JPanel {

    private static final long serialVersionUID = 1L;

    private int velX = 3;
    private int velY = 3;

    private JLabel raquetaIzq;
    private JLabel raquetaDer;

    public pelota(int x, int y, JLabel raquetaIzq, JLabel raquetaDer) {
        setBounds(x, y, 20, 20);
        setBackground(new Color(0, 0, 0, 0)); // transparente
        this.raquetaIzq = raquetaIzq;
        this.raquetaDer = raquetaDer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillOval(0, 0, getWidth(), getHeight());
    }

    public void iniciarMovimiento() {
        setLocation(getX() + velX, getY() + velY);

        if (getY() <= 0 || getY() >= getParent().getHeight() - getHeight()) {
            velY = -velY;
        }

        if (getBounds().intersects(raquetaIzq.getBounds())) {
            velX = Math.abs(velX);
        }

        if (getBounds().intersects(raquetaDer.getBounds())) {
            velX = -Math.abs(velX);
        }

        if (getX() <= 0) {
            resetPelota();
            velX = Math.abs(velX);
        }

        if (getX() >= getParent().getWidth() - getWidth()) {
            resetPelota();
            velX = -Math.abs(velX);
        }
    }

    private void resetPelota() {
        int centroX = getParent().getWidth() / 2 - getWidth() / 2;
        int centroY = getParent().getHeight() / 2 - getHeight() / 2;
        setLocation(centroX, centroY);
    }
}
