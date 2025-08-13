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
    private JPanel pantallaInicio;

    public JLabel raquetaIzq;
    public JLabel raquetaDer;

    private final int velRaqueta = 10;
    private BufferedImage fondoImage;
    private ImageIcon spriteRaqueta;

    // Marcadores
    private JLabel marcadorIzq;
    private JLabel marcadorDer;
    private ImageIcon[] imagenesMarcador;
    private int puntosIzq = 0;
    private int puntosDer = 0;

    public ventanaPong() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // Pantalla de inicio
        pantallaInicio = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage img = ImageIO.read(new File("media/kingpong.jpg"));
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        pantallaInicio.setBounds(0, 0, 1600, 800);

        // Texto sobre la pantalla de inicio
        JLabel textoInicio = new JLabel("Presiona cualquier tecla para empezar");
        textoInicio.setFont(new Font("Arial", Font.BOLD, 50));
        textoInicio.setForeground(Color.WHITE);
        textoInicio.setBounds(250, 700, 1200, 50); // posición del texto
        pantallaInicio.add(textoInicio);
        
        JLabel creditos = new JLabel("Desarrollado por PZRZ");
        creditos.setFont(new Font("Arial", Font.PLAIN, 25));
        creditos.setForeground(Color.WHITE);
        creditos.setBounds(1300, 25, 300, 30); // ajustá según resolución
        pantallaInicio.add(creditos);

        getContentPane().add(pantallaInicio);

        // Escucha de tecla para iniciar el juego
        pantallaInicio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                remove(pantallaInicio);  // quitamos la pantalla de inicio
                initJuego();             // iniciamos el juego
                revalidate();
                repaint();
            }
        });
        pantallaInicio.setFocusable(true);
        pantallaInicio.requestFocusInWindow();

        setVisible(true);
    }

    private void initJuego() {
        // Fondo inicial
        try {
            fondoImage = ImageIO.read(new File("media/Cancha1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        // Cargar imágenes de marcador
        cargarImagenesMarcador();

        // Marcadores iniciales
        marcadorIzq = new JLabel(imagenesMarcador[0]);
        marcadorIzq.setBounds(50, 400, 100, 100);
        contentPane.add(marcadorIzq);

        marcadorDer = new JLabel(imagenesMarcador[0]);
        marcadorDer.setBounds(1450, 400, 100, 100);
        contentPane.add(marcadorDer);

        // Raquetas
        spriteRaqueta = new ImageIcon("media/jugador1.png");

        raquetaIzq = new JLabel(spriteRaqueta);
        raquetaIzq.setBounds(158, 250, spriteRaqueta.getIconWidth(), spriteRaqueta.getIconHeight());
        contentPane.add(raquetaIzq);

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

        raquetaDer = new JLabel(new ImageIcon(invertida));
        raquetaDer.setBounds(1165, 250, spriteRaqueta.getIconWidth(), spriteRaqueta.getIconHeight());
        contentPane.add(raquetaDer);

        // Pelota
        bocha = new pelota(393, 260, raquetaIzq, raquetaDer, this);
        bocha.setBackground(Color.BLACK);
        bocha.setBounds(393, 260, 20, 20);
        contentPane.add(bocha);

        // Timer pelota
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

        // Animación inicial de marcadores
        animacionInicialMarcadores();

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
    }

    private void cargarImagenesMarcador() {
        imagenesMarcador = new ImageIcon[13];
        for (int i = 0; i <= 12; i++) {
            imagenesMarcador[i] = new ImageIcon("media/num" + i + ".png");
        }
    }

    private void animacionInicialMarcadores() {
        puntosIzq = 1;
        puntosDer = 1;
        marcadorIzq.setIcon(imagenesMarcador[puntosIzq]);
        marcadorDer.setIcon(imagenesMarcador[puntosDer]);

        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            puntosIzq = 2;
            puntosDer = 2;
            marcadorIzq.setIcon(imagenesMarcador[puntosIzq]);
            marcadorDer.setIcon(imagenesMarcador[puntosDer]);
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void sumarPuntoIzq() {
        puntosIzq++;
        if (puntosIzq > 12) puntosIzq = 12;
        marcadorIzq.setIcon(imagenesMarcador[puntosIzq]);
    }

    public void sumarPuntoDer() {
        puntosDer++;
        if (puntosDer > 12) puntosDer = 12;
        marcadorDer.setIcon(imagenesMarcador[puntosDer]);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new ventanaPong();
        });
    }
}
