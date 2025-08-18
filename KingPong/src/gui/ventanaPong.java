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

    public static int width;
    public static int height;
    private int puntosIzq = 0;
    private int puntosDer = 0;
    private boolean visible = true;
    private final int velRaqueta = 10;
    
    private Timer timer;
    
    private pelota bocha;
    
    private JPanel pantallaInicio;
    
    public JLabel jugador1;
    public JLabel jugador2;
    private JLabel marcadorIzq;
    private JLabel marcadorDer;

    private ImageIcon spriteJugador;
    private ImageIcon[] imagenesMarcador;
    

    public ventanaPong() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1386,750);
        setLocationRelativeTo(null);
        setResizable(false);

        // Pantalla de inicio
        pantallaInicio = new JPanel(null) {		// pantalla de inicio del jego
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage img = ImageIO.read(new File("media/kingpong.jpg"));	// imagen para la pantalla de inicio
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Texto sobre la pantalla de inicio
        JLabel textoInicio = new JLabel("Presiona cualquier tecla para empezar");
        textoInicio.setFont(new Font("Arial", Font.BOLD, 50));
        textoInicio.setForeground(Color.WHITE);
        textoInicio.setBounds(235, 560, 1200, 50); 
        pantallaInicio.add(textoInicio);
        
        JLabel creditos = new JLabel("Desarrollado por PZRZ");
        creditos.setFont(new Font("Papyrus", Font.BOLD | Font.ITALIC, 26));
        creditos.setForeground(Color.WHITE);
        creditos.setBounds(590, 650, 300, 30); 
        pantallaInicio.add(creditos);

        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {			// timer para darle mas tematizacion arcade al inicio del juego (textoInicio se vuelve intermitente)
        	@Override
            public void run() {
               visible=!visible;
               textoInicio.setVisible(visible);
            }
        }, 0, 1000);
        
        
        getContentPane().add(pantallaInicio);

        // Escucha cualquier tecla para iniciar el juego
        pantallaInicio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                remove(pantallaInicio);  // quitamos la pantalla de inicio
                iniciarJuego();             // iniciamos el juego
                // para actualizar la ventana al haber hecho cambios
                revalidate();				
                repaint();
            }
        });
        pantallaInicio.setFocusable(true);
        pantallaInicio.requestFocusInWindow();	// para recibir entrada del teclado

        setVisible(true);
    }

    private void iniciarJuego() {
    	
    	
    	// para añadir el JPanel cancha a la pantalla
    	cancha contentPane = new cancha(1370, 710);
    	setContentPane(contentPane); 
    	
        // Cargar imágenes de marcador
        cargarImagenesMarcador();

        // Marcadores iniciales
        marcadorIzq = new JLabel(imagenesMarcador[0]);
        marcadorIzq.setBounds(36, 400, 80, 110);
        contentPane.add(marcadorIzq);

        marcadorDer = new JLabel(imagenesMarcador[0]);
        marcadorDer.setBounds(1255, 400, 80, 110);
        contentPane.add(marcadorDer);

        // sprite para la raqueta
        spriteJugador = new ImageIcon("media/jugador1.png");

        // para cargar imagenes y localizarlas en la pantalla adecuadamente
        Image img = spriteJugador.getImage();
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
        
        // Raquetas
        jugador1 = new JLabel(spriteJugador);
        jugador1.setBounds(158, 350, spriteJugador.getIconWidth(), spriteJugador.getIconHeight());
        contentPane.add(jugador1);

        jugador2 = new JLabel(new ImageIcon(invertida));
        jugador2.setBounds(1170, 350, spriteJugador.getIconWidth(), spriteJugador.getIconHeight());
        contentPane.add(jugador2);

        // Pelota
        bocha = new pelota(678, 435, jugador1, jugador2, this);
        bocha.setBackground(Color.BLACK);
        contentPane.add(bocha);
        
        // Timer para iniciar el movimiento de la pelota
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                bocha.movimiento();
                bocha.repaint();
            }
        };
        timer.schedule(task, 2000, 15);

        // Control de teclas
        Set<Integer> teclasPresionadas = new HashSet<>();	// permite recibir como entrada teclas en simultaneo
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

        // Movimiento de jugadores
        javax.swing.Timer movimientoTimer = new javax.swing.Timer(26, e -> {
            int limiteSuperior = 235;
            int limiteInferior = contentPane.getHeight() - jugador1.getHeight();

            if (teclasPresionadas.contains(KeyEvent.VK_W)) {
                int nuevaY = jugador1.getY() - velRaqueta;
                if (nuevaY < limiteSuperior) nuevaY = limiteSuperior;	// para evitar que el jugador sobrepase la ventana en el eje Y
                jugador1.setLocation(jugador1.getX(), nuevaY);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_S)) {
                int nuevaY = jugador1.getY() + velRaqueta;
                if (nuevaY > limiteInferior) nuevaY = limiteInferior;
                jugador1.setLocation(jugador1.getX(), nuevaY);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_UP)) {
                int nuevaY = jugador2.getY() - velRaqueta;
                if (nuevaY < limiteSuperior) nuevaY = limiteSuperior;
                jugador2.setLocation(jugador2.getX(), nuevaY);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_DOWN)) {
                int nuevaY = jugador2.getY() + velRaqueta;
                if (nuevaY > limiteInferior) nuevaY = limiteInferior;
                jugador2.setLocation(jugador2.getX(), nuevaY);
            }
        });
        movimientoTimer.start();	// timer para hacer el movimiento de los jugadores mas fluido

        // Animación inicial de marcadores
        animacionInicialMarcadores();

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
      
    }
    // funcion para cargar las imagenes del marcador
    private void cargarImagenesMarcador() {
        imagenesMarcador = new ImageIcon[13];
        for (int i = 0; i <= 12; i++) {
            imagenesMarcador[i] = new ImageIcon("media/num" + i + ".png");
        }
    }
    
    // funcion para brindarle una animacion de inicio al marcador (un delay de 1 segundo)
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
    
    // funcion para sumar puntos al jugador 1 
    public void sumarPuntoIzq() {
        puntosIzq++;
        if (puntosIzq > 7) puntosIzq = 7;
        if (puntosIzq == 7)
        {
        	timer.cancel();
        	Timer Timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    resetearJuego();
                }
            };
            Timer.schedule(task, 2000);
        	
        }
        marcadorIzq.setIcon(imagenesMarcador[puntosIzq]);
    }
    
    // funcion para sumar puntos al jugador 2
    public void sumarPuntoDer() {
        puntosDer++;
        if (puntosDer > 7) puntosDer = 7;
        if (puntosDer == 7)
        {
        	timer.cancel();
        	Timer Timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    resetearJuego();
                }
            };
            Timer.schedule(task, 2000);
        	
        }
        marcadorDer.setIcon(imagenesMarcador[puntosDer]);
    }
    
    public void resetearJuego() {
        getContentPane().removeAll();   // Quita todo
        revalidate();
        repaint();
        
        iniciarJuego();  // Vuelve a iniciar
    }

}

	