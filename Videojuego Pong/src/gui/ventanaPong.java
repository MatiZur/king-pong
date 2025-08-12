package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ventanaPong extends JFrame {

    private static final long serialVersionUID = 1L;

    private pelota bocha;

    private JPanel contentPane;
    private int WIDTH=794;
    private int HEIGHT=545;

    public JPanel raquetaIzq;
    public JPanel raquetaDer;
    
    
    
    private final int velRaqueta = 10;

    public ventanaPong() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 810, 584);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        raquetaIzq = new JPanel();
        raquetaIzq.setBackground(Color.WHITE);
        raquetaIzq.setBounds(30, 222, 15, 100);
        contentPane.add(raquetaIzq);

        raquetaDer = new JPanel();
        raquetaDer.setBackground(Color.WHITE);
        raquetaDer.setBounds(749, 222, 15, 100);
        contentPane.add(raquetaDer);
        
        int xPelota=(WIDTH / 2 - pelota.WIDTH /2);
        int yPelota=(HEIGHT / 2 - pelota.HEIGHT / 2);
        
        //para detectar las colisiones
        bocha = new pelota(xPelota, yPelota, raquetaIzq, raquetaDer);
        bocha.setBackground(new Color(0, 0, 0));
        contentPane.add(bocha);

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                bocha.iniciarMovimiento();
            }
        };

        timer.schedule(task, 5000);
        
        
        Set<Integer> teclasPresionadas = new HashSet<>(); // para registrar mas de una tecla presionada en simultaneo
        javax.swing.Timer movimientoTimer;
        
        addKeyListener(new KeyAdapter() {	// para detectar con mayor precision las teclas presionadas
            @Override
            public void keyPressed(KeyEvent e) {
                teclasPresionadas.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                teclasPresionadas.remove(e.getKeyCode());
            }
        });
    
        movimientoTimer = new javax.swing.Timer(15, (ActionListener) new ActionListener() {	// para mover las raquetas con mayor fluidez
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        movimientoTimer.start();	// empezar timer para que sea mas fluido

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


