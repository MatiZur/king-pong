package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ventanaPong extends JFrame {

    private static final long serialVersionUID = 1L;

    private pelota bocha;
    private JPanel contentPane;

    public JLabel raquetaIzq;
    public JLabel raquetaDer;

    private final int velRaqueta = 10;
    private BufferedImage fondoImage;
    private ImageIcon spriteRaqueta;

    public ventanaPong() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            fondoImage = ImageIO.read(new File("media/Cancha1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        spriteRaqueta = new ImageIcon("media/jugador1.png");

        contentPane = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fondoImage != null) {
                    g.drawImage(fondoImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        setContentPane(contentPane);

        // Raqueta izquierda
        raquetaIzq = new JLabel(spriteRaqueta);
        raquetaIzq.setBounds(250, 250, spriteRaqueta.getIconWidth(), spriteRaqueta.getIconHeight());
        contentPane.add(raquetaIzq);
        
        //invertir el sprite para la raqueta derecha
        Image img = spriteRaqueta.getImage();
        BufferedImage invertida = new BufferedImage(
                img.getWidth(null),
                img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = invertida.createGraphics();

        g2d.drawImage(
                img,
                img.getWidth(null), img.getHeight(null),  
                -img.getWidth(null), -img.getHeight(null), 
                null
        );
        g2d.dispose();
        // Raqueta derecha 
        raquetaDer = new JLabel(new ImageIcon(invertida));
        raquetaDer.setBounds(1000, 250, spriteRaqueta.getIconWidth(), spriteRaqueta.getIconHeight());
        contentPane.add(raquetaDer);

        // Pelota
        bocha = new pelota(393, 260, raquetaIzq, raquetaDer);
        bocha.setBackground(Color.BLACK);
        bocha.setBounds(393, 260, 20, 20);
        contentPane.add(bocha);

        // Movimiento de la pelota con Timer
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                bocha.iniciarMovimiento();
                bocha.repaint();
            }
        };
        timer.schedule(task, 0, 15);

        // Control de teclas
        Set<Integer> teclasPresionadas = new HashSet<>();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                teclasPresionadas.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                teclasPresionadas.remove(e.getKeyCode());
            }
        });

        // Movimiento de raquetas
        javax.swing.Timer movimientoTimer = new javax.swing.Timer(15, e -> {
            int limiteSuperior = 10;
            int limiteInferior = contentPane.getHeight() - raquetaIzq.getHeight() - 10;

            if (teclasPresionadas.contains(KeyEvent.VK_W)) {
                int nuevaY = raquetaIzq.getY() - velRaqueta;
                if (nuevaY < limiteSuperior) nuevaY = limiteSuperior;
                raquetaIzq.setLocation(raquetaIzq.getX(), nuevaY);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_S)) {
                int nuevaY = raquetaIzq.getY() + velRaqueta;
                if (nuevaY > limiteInferior) nuevaY = limiteInferior;
                raquetaIzq.setLocation(raquetaIzq.getX(), nuevaY);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_UP)) {
                int nuevaY = raquetaDer.getY() - velRaqueta;
                if (nuevaY < limiteSuperior) nuevaY = limiteSuperior;
                raquetaDer.setLocation(raquetaDer.getX(), nuevaY);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_DOWN)) {
                int nuevaY = raquetaDer.getY() + velRaqueta;
                if (nuevaY > limiteInferior) nuevaY = limiteInferior;
                raquetaDer.setLocation(raquetaDer.getX(), nuevaY);
            }
        });
        movimientoTimer.start();

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setVisible(true);
        requestFocusInWindow();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ventanaPong frame = new ventanaPong();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}


