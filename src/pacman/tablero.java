package pacman;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class tablero extends JPanel implements ActionListener {

    private Dimension dimension;
    // Fuente para las palabras de inicio para empezar el juego y el puntaje
    private final Font fuentePequenia = new Font("Helvetica", Font.BOLD, 14);

    private Image ii;
    private final Color colorPastillas = new Color(192, 192, 0);
    private final Color colorSuperPastilla = new Color(192, 61, 0);
    private Color colorBloques;

    private Clip clipBucle;

    private boolean enJuego = false;
    private boolean muriendo = false;

    private final int tamanioBloque = 24;// Celda de la matriz
    private final int cantidadBloques = 25;
    private final int tamanioPantalla = cantidadBloques * tamanioBloque;
    private final int retrasoAnimacionPacman = 2;
    private final int contadorAnimacionPacman = 4;
    private final int máximoFantasmas = 12;
    private final int velocidadPacman = 6;

    private int contadorAnimacionPacmanActual = retrasoAnimacionPacman;
    private int direcciónAnimaciónPacman = 1;
    private int posiciónAnimaciónPacman = 0;
    private int cantidadFantasmas = 6;
    private int pacmansRestantes, puntaje;
    private int[] dx, dy;
    private ArrayList<Integer> posiciónFantasmasX = new ArrayList<Integer>();
    private ArrayList<Integer> posiciónFantasmasY = new ArrayList<Integer>();
    private ArrayList<Integer> direcciónFantasmasX = new ArrayList<Integer>();
    private ArrayList<Integer> direcciónFantasmasY = new ArrayList<Integer>();
    private ArrayList<Integer> velocidadFantasmas = new ArrayList<Integer>();
    private ArrayList<Image> imagenesFantasmas = new ArrayList<Image>();
    private ArrayList<Boolean> fantasmasMuriendo = new ArrayList<Boolean>();
    private ArrayList<Boolean> fantasmasComidos = new ArrayList<Boolean>();


    private Image ojosFantasma, pacman1, cherry, pacman2Arriba, pacman2Izquierda, pacman2Derecha, pacman2Abajo, ghostDie, superPill;
    private Image pacman3Arriba, pacman3Abajo, pacman3Izquierda, pacman3Derecha;
    private Image pacman4Arriba, pacman4Abajo, pacman4Izquierda, pacman4Derecha;
    private Image pantallaInicio;

    private int pacmanx, pacmany, pacmandx, pacmandy;
    private int reqdx, reqdy, viewdx, viewdy;

    private final short datosNivel[] = {
            19, 26, 26, 26, 26, 18, 26, 26, 26, 26, 26, 22, 0, 0, 0, 19, 26, 26, 26, 26, 26, 18, 26, 26, 22,
            21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 21,
            21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 21,
            21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 21,
            17, 26, 26, 26, 26, 32, 26, 26, 18, 26, 26, 24, 26, 26, 26, 24, 26, 26, 18, 26, 26, 32, 26, 26, 20,
            21, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 21,
            21, 0, 0, 0, 0, 21, 0, 0, 25, 26, 18, 18, 22, 0, 19, 18, 18, 26, 28, 0, 0, 21, 0, 0, 21,
            25, 26, 26, 26, 26, 20, 0, 0, 0, 0, 17, 16, 20, 0, 17, 16, 20, 0, 0, 0, 0, 21, 0, 0, 21,
            0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 17, 16, 20, 0, 17, 16, 20, 0, 0, 0, 0, 17, 26, 26, 28,
            0, 0, 0, 0, 0, 21, 0, 0, 19, 18, 24, 24, 24, 26, 24, 24, 24, 18, 22, 0, 0, 21, 0, 0, 0,
            0, 0, 0, 0, 0, 21, 0, 0, 17, 20, 3, 2, 2, 0, 2, 2, 6, 17, 20, 0, 0, 21, 0, 0, 0,
            0, 0, 0, 0, 0, 21, 0, 0, 17, 20, 1, 0, 0, 0, 0, 0, 4, 17, 20, 0, 0, 21, 0, 0, 0,
            26, 26, 26, 26, 26, 16, 26, 26, 16, 20, 1, 0, 0, 0, 0, 0, 4, 17, 16, 26, 26, 24, 26, 26, 26,
            0, 0, 0, 0, 0, 21, 0, 0, 17, 20, 1, 0, 0, 0, 0, 0, 4, 17, 20, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 21, 0, 0, 17, 20, 9, 8, 8, 8, 8, 8, 12, 17, 20, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 21, 0, 0, 17, 24, 26, 26, 26, 26, 26, 26, 26, 24, 24, 18, 26, 26, 26, 26, 30,
            35, 26, 26, 26, 26, 16, 26, 18, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0,
            21, 0, 0, 0, 0, 21, 0, 17, 16, 18, 18, 18, 22, 0, 19, 18, 18, 18, 18, 16, 18, 18, 18, 18, 22,
            25, 26, 22, 0, 0, 21, 0, 17, 24, 24, 24, 24, 24, 74, 24, 24, 24, 24, 24, 16, 24, 24, 16, 24, 28,
            0, 0, 21, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0,
            19, 26, 24, 18, 18, 20, 0, 17, 18, 18, 18, 18, 22, 0, 19, 26, 26, 26, 18, 20, 0, 0, 17, 18, 22,
            21, 0, 0, 17, 16, 20, 0, 17, 16, 16, 16, 16, 20, 0, 21, 0, 0, 0, 17, 20, 0, 0, 17, 32, 20,
            21, 0, 0, 25, 24, 28, 0, 25, 24, 24, 16, 16, 20, 0, 21, 0, 0, 0, 25, 24, 26, 26, 24, 24, 20,
            21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 32, 20, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21,
            25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 24, 24, 24, 26, 24, 26, 26, 26, 26, 26, 26, 26, 26, 26, 28
    };


    private final int velocidadesValidas[] = {1, 2, 3, 4, 6, 8};
    private final int velocidadMaxima = 6;

    private int currentspeed = 3;
    private short[] datosPantalla;
    private Timer temporizador;

    private boolean superPillActive = false;

    private Timer tempGhostDie;

    private final int superPillDuration = 10000;

    public tablero() {
        loadImages();
        inicializarVariables();

        addKeyListener(new TAdapter());

        setFocusable(true);

        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    private void mostrarPantallaIntro(Graphics2D g2d) {
        if (!enJuego) {
            int anchoImagen = 620;
            int altoImagen = 680;
            g2d.drawImage(pantallaInicio, 0, 0, anchoImagen, altoImagen, this);
        }
        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(50, tamanioPantalla / 2 - 30, tamanioPantalla - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, tamanioPantalla / 2 - 30, tamanioPantalla - 100, 50);
        String s = "Presiona p para empezar.";
        Font fuente = new Font("Helvetica", Font.BOLD, 15);
        FontMetrics metr = this.getFontMetrics(fuente);
        g2d.drawString(s, (tamanioPantalla - metr.stringWidth(s)) / 2, tamanioPantalla / 2);
        g2d.setColor(Color.white);
        g2d.setFont(fuente);
    }

    private void inicializarVariables() {

        datosPantalla = new short[cantidadBloques * cantidadBloques];
        colorBloques = new Color(0, 212, 245);
        dimension = new Dimension(400, 400);
        posiciónFantasmasX = new ArrayList<>();
        direcciónFantasmasX = new ArrayList<>();
        posiciónFantasmasY = new ArrayList<>();
        direcciónFantasmasY = new ArrayList<>();
        velocidadFantasmas = new ArrayList<>();
        dx = new int[4];
        dy = new int[4];

        temporizador = new Timer(40, this);
        temporizador.start();
    }

    public void playMusic(String musicPath,Boolean loop) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(musicPath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clipBucle = clip;
            }else {
                clip.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public long MusicSecuencial(String musicPath, boolean loop) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(musicPath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }

            return clip.getMicrosecondLength() / 1000; // Devuelve la duración en milisegundos
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public void addNotify() {
        super.addNotify();

        initGame();
    }

    private void doAnim() {

        contadorAnimacionPacmanActual--;

        if (contadorAnimacionPacmanActual <= 0) {
            contadorAnimacionPacmanActual = retrasoAnimacionPacman;
            posiciónAnimaciónPacman = posiciónAnimaciónPacman + direcciónAnimaciónPacman;

            if (posiciónAnimaciónPacman == (contadorAnimacionPacman - 1) || posiciónAnimaciónPacman == 0) {
                direcciónAnimaciónPacman = -direcciónAnimaciónPacman;
            }
        }
    }

    private void playGame(Graphics2D g2d) {

        if (muriendo) {

            death();

        } else {

            movePacman(g2d);
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }


    private void drawScore(Graphics2D g) {

        int i;
        String s;

        g.setFont(fuentePequenia);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + puntaje;
        g.drawString(s, tamanioPantalla / 2 + 96, tamanioPantalla + 16);

        for (i = 0; i < pacmansRestantes; i++) {
            g.drawImage(pacman3Izquierda, i * 28 + 8, tamanioPantalla + 1, this);
        }
    }

    private void checkMaze() {

        short i = 0;
        boolean finished = true;

        while (i < cantidadBloques * cantidadBloques && finished) {

            if ((datosPantalla[i] & 48) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            puntaje += 50;

            if (cantidadFantasmas < máximoFantasmas) {
                cantidadFantasmas++;
            }

            if (currentspeed < velocidadMaxima) {
                currentspeed++;
            }

            initLevel();
        }
    }

    private void death() {//murio

        pacmansRestantes--;

        if (pacmansRestantes == 0) {
            enJuego = false;
        }

        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {//mover fantasmas

        short i;
        int pos;
        int count;

        for (i = 0; i < cantidadFantasmas; i++) {
            if (posiciónFantasmasX.get(i) % tamanioBloque == 0 && posiciónFantasmasY.get(i) % tamanioBloque == 0) {
                pos = posiciónFantasmasX.get(i) / tamanioBloque + cantidadBloques * (posiciónFantasmasY.get(i) / tamanioBloque);

                count = 0;

                if ((datosPantalla[pos] & 1) == 0 && direcciónFantasmasX.get(i) != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((datosPantalla[pos] & 2) == 0 && direcciónFantasmasY.get(i) != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((datosPantalla[pos] & 4) == 0 && direcciónFantasmasX.get(i) != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((datosPantalla[pos] & 8) == 0 && direcciónFantasmasY.get(i) != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((datosPantalla[pos] & 15) == 15) {
                        direcciónFantasmasX.set(i, 0);
                        direcciónFantasmasY.set(i, 0);
                    } else {
                        direcciónFantasmasX.set(i, -direcciónFantasmasX.get(i));
                        direcciónFantasmasY.set(i, -direcciónFantasmasY.get(i));
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    direcciónFantasmasX.set(i, dx[count]);
                    direcciónFantasmasY.set(i, dy[count]);

                    if (pacmanx > (posiciónFantasmasX.get(i) - 12) && pacmanx < (posiciónFantasmasX.get(i) + 12)
                            && pacmany > (posiciónFantasmasY.get(i) - 12) && pacmany < (posiciónFantasmasY.get(i) + 12)
                            && enJuego && fantasmasMuriendo.get(i) && superPillActive) {
                        fantasmasComidos.set(i, true);
                        puntaje += 300;
                        posiciónFantasmasX.set(i, 14 * tamanioBloque);
                        posiciónFantasmasY.set(i, 14 * tamanioBloque);
                    } else if (pacmanx > (posiciónFantasmasX.get(i) - 12) && pacmanx < (posiciónFantasmasX.get(i) + 12)
                            && pacmany > (posiciónFantasmasY.get(i) - 12) && pacmany < (posiciónFantasmasY.get(i) + 12)
                            && enJuego) {
                        muriendo = true;
                    }
                }

            }
            posiciónFantasmasX.set(i, posiciónFantasmasX.get(i) + (direcciónFantasmasX.get(i) * velocidadFantasmas.get(i)));
            posiciónFantasmasY.set(i, posiciónFantasmasY.get(i) + (direcciónFantasmasY.get(i) * velocidadFantasmas.get(i)));
            drawGhost(g2d, posiciónFantasmasX.get(i) + 1, posiciónFantasmasY.get(i) + 1, i);

            if (pacmanx > (posiciónFantasmasX.get(i) - 12) && pacmanx < (posiciónFantasmasX.get(i) + 12)
                    && pacmany > (posiciónFantasmasY.get(i) - 12) && pacmany < (posiciónFantasmasY.get(i) + 12)
                    && enJuego && fantasmasMuriendo.get(i) && superPillActive) {
                fantasmasComidos.set(i, true);
                puntaje += 300;
                posiciónFantasmasX.set(i, 14 * tamanioBloque);
                posiciónFantasmasY.set(i, 14 * tamanioBloque);
            } else if (pacmanx > (posiciónFantasmasX.get(i) - 12) && pacmanx < (posiciónFantasmasX.get(i) + 12)
                    && pacmany > (posiciónFantasmasY.get(i) - 12) && pacmany < (posiciónFantasmasY.get(i) + 12)
                    && enJuego) {
                muriendo = true;
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y, int index) {
        if (fantasmasComidos.get(index)) {
            g2d.drawImage(ojosFantasma, x, y, this);
        } else if (fantasmasMuriendo.get(index) && superPillActive) {
            g2d.drawImage(ghostDie, x, y, this);
        } else {
            g2d.drawImage(imagenesFantasmas.get(index), x, y, this);
        }
    }


    private void movePacman(Graphics2D g2d) {

        int pos;
        short ch;

        if (reqdx == -pacmandx && reqdy == -pacmandy) {
            pacmandx = reqdx;
            pacmandy = reqdy;
            viewdx = pacmandx;
            viewdy = pacmandy;
        }

        if (pacmanx % tamanioBloque == 0 && pacmany % tamanioBloque == 0) {
            pos = pacmanx / tamanioBloque + cantidadBloques * (int) (pacmany / tamanioBloque);
            ch = datosPantalla[pos];

            if ((ch & 16) != 0) {
                datosPantalla[pos] = (short) (ch & 15);
                puntaje++;
            }

            if ((ch & 32) != 0) {
                datosPantalla[pos] = (short) (ch & 15);
                long duration = MusicSecuencial(getClass().getResource("../music/pacmanDeadth.wav").getPath(), false);
                new Thread(() -> {
                    try {
                        Thread.sleep(duration);
                        playMusic(getClass().getResource("../music/ghost-turn-to-blue.wav").getPath(), true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                superPillActive = true;
                for (int i = 0; i < cantidadFantasmas; i++) {
                    fantasmasMuriendo.set(i, true);
                }
                tempGhostDie.restart();
                puntaje += 50;
            }
            if ((ch & 64) != 0) {
                datosPantalla[pos] = (short) (ch & 15);
                puntaje += 100;
                playMusic(getClass().getResource("../music/eatFruit.wav").getPath(),false);
            }

            if (reqdx != 0 || reqdy != 0) {
                if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0)
                        || (reqdx == 1 && reqdy == 0 && (ch & 4) != 0)
                        || (reqdx == 0 && reqdy == -1 && (ch & 2) != 0)
                        || (reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
                    pacmandx = reqdx;
                    pacmandy = reqdy;
                    viewdx = pacmandx;
                    viewdy = pacmandy;
                }
            }

            // Check for standstill
            if ((pacmandx == -1 && pacmandy == 0 && (ch & 1) != 0)
                    || (pacmandx == 1 && pacmandy == 0 && (ch & 4) != 0)
                    || (pacmandx == 0 && pacmandy == -1 && (ch & 2) != 0)
                    || (pacmandx == 0 && pacmandy == 1 && (ch & 8) != 0)) {
                pacmandx = 0;
                pacmandy = 0;
            }
        }
        pacmanx = pacmanx + velocidadPacman * pacmandx;
        pacmany = pacmany + velocidadPacman * pacmandy;
    }

    private void drawPacman(Graphics2D g2d) {

        if (viewdx == -1) {
            drawPacnanLeft(g2d);
        } else if (viewdx == 1) {
            drawPacmanRight(g2d);
        } else if (viewdy == -1) {
            drawPacmanUp(g2d);
        } else {
            drawPacmanDown(g2d);
        }
    }

    private void drawPacmanUp(Graphics2D g2d) {

        switch (posiciónAnimaciónPacman) {
            case 1:
                g2d.drawImage(pacman2Arriba, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3Arriba, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4Arriba, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void drawPacmanDown(Graphics2D g2d) {

        switch (posiciónAnimaciónPacman) {
            case 1:
                g2d.drawImage(pacman2Abajo, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3Abajo, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4Abajo, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void drawPacnanLeft(Graphics2D g2d) {
        switch (posiciónAnimaciónPacman) {
            case 1:
                g2d.drawImage(pacman2Izquierda, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3Izquierda, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4Izquierda, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void drawPacmanRight(Graphics2D g2d) {
        switch (posiciónAnimaciónPacman) {
            case 1:
                g2d.drawImage(pacman2Derecha, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3Derecha, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4Derecha, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void drawMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;
        for (y = 0; y < tamanioPantalla; y += tamanioBloque) {
            for (x = 0; x < tamanioPantalla; x += tamanioBloque) {

                g2d.setColor(colorBloques);
                g2d.setStroke(new BasicStroke(2));

                if ((datosPantalla[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + tamanioBloque - 1);
                }

                if ((datosPantalla[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + tamanioBloque - 1, y);
                }

                if ((datosPantalla[i] & 4) != 0) {
                    g2d.drawLine(x + tamanioBloque - 1, y, x + tamanioBloque - 1,
                            y + tamanioBloque - 1);
                }

                if ((datosPantalla[i] & 8) != 0) {
                    g2d.drawLine(x, y + tamanioBloque - 1, x + tamanioBloque - 1,
                            y + tamanioBloque - 1);
                }
                if ((datosPantalla[i] & 16) != 0) {
                    g2d.setColor(colorPastillas);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }
                if ((datosPantalla[i] & 32) != 0) {
                    g2d.drawImage(superPill, x + 3, y + 3, this);
                }
                if ((datosPantalla[i] & 64) != 0) {
                    g2d.drawImage(cherry, x + 3, y + 3, this);
                }
                i++;
            }
        }
    }

    private void initGame() {

        pacmansRestantes = 4;
        puntaje = -0;
        initLevel();
        cantidadFantasmas = 4;
        currentspeed = 3;
        tempGhostDie = new Timer(superPillDuration, e -> {
            superPillActive = false;
            if(clipBucle != null) {
                clipBucle.stop();
                clipBucle.close();
            }
            for (int i = 0; i < cantidadFantasmas; i++) {
                fantasmasMuriendo.set(i, false);
                if (fantasmasComidos.get(i)) {
                    fantasmasComidos.set(i, false);
                    posiciónFantasmasX.set(i, 14 * tamanioBloque); // Ajusta la posición según tu configuración del mapa
                    posiciónFantasmasY.set(i, 14 * tamanioBloque);
                }
            }
            tempGhostDie.stop();
        });
    }

    private void initLevel() {

        int i;
        for (i = 0; i < cantidadBloques * cantidadBloques; i++) {
            datosPantalla[i] = datosNivel[i];
        }
        Image nuevoFantasmaImg = new ImageIcon(getClass().getResource("../images/ghost_dead.png")).getImage();

        while (posiciónFantasmasX.size() < cantidadFantasmas) {
            posiciónFantasmasY.add(14 * tamanioBloque);
            posiciónFantasmasX.add(14 * tamanioBloque);
            direcciónFantasmasY.add(0);
            direcciónFantasmasX.add(1);
            velocidadFantasmas.add(currentspeed);
            fantasmasMuriendo.add(false);
            imagenesFantasmas.add(nuevoFantasmaImg);
        }

        continueLevel();
    }


    private void continueLevel() {
        short i;
        int dx = 1;
        int random;

        for (i = 0; i < cantidadFantasmas; i++) {
            fantasmasComidos.add(false);
            posiciónFantasmasY.set(i, 14 * tamanioBloque);
            posiciónFantasmasX.set(i, 14 * tamanioBloque);
            direcciónFantasmasY.set(i, 0);
            direcciónFantasmasX.set(i, dx);
            dx = -dx;
            random = (int) (Math.random() * (currentspeed + 1));
            if (random > currentspeed) {
                random = currentspeed;
            }
            velocidadFantasmas.set(i, velocidadesValidas[random]);
        }

        pacmanx = 13 * tamanioBloque;
        pacmany = 15 * tamanioBloque;
        pacmandx = 0;
        pacmandy = 0;
        reqdx = 0;
        reqdy = 0;
        viewdx = -1;
        viewdy = 0;
        muriendo = false;
    }

    private void loadImages() {
        pantallaInicio = new ImageIcon(getClass().getResource("../images/intro.png")).getImage();
        imagenesFantasmas.add(new ImageIcon(getClass().getResource("../images/ghost_teal.png")).getImage());
        imagenesFantasmas.add(new ImageIcon(getClass().getResource("../images/ghost_orange.png")).getImage());
        imagenesFantasmas.add(new ImageIcon(getClass().getResource("../images/ghost_pink.png")).getImage());
        imagenesFantasmas.add(new ImageIcon(getClass().getResource("../images/ghost_red.png")).getImage());
        pacman1 = new ImageIcon(getClass().getResource("../images/pacman.gif")).getImage();
        pacman2Arriba = new ImageIcon(getClass().getResource("../images/pacman_up.png")).getImage();
        pacman3Arriba = new ImageIcon(getClass().getResource("../images/pacman_up.png")).getImage();
        pacman4Arriba = new ImageIcon(getClass().getResource("../images/pacman_up.png")).getImage();
        pacman2Abajo = new ImageIcon(getClass().getResource("../images/pacman_down.png")).getImage();
        pacman3Abajo = new ImageIcon(getClass().getResource("../images/pacman_down.png")).getImage();
        pacman4Abajo = new ImageIcon(getClass().getResource("../images/pacman_down.png")).getImage();
        pacman2Izquierda = new ImageIcon(getClass().getResource("../images/pacman_left.png")).getImage();
        pacman3Izquierda = new ImageIcon(getClass().getResource("../images/pacman_left.png")).getImage();
        pacman4Izquierda = new ImageIcon(getClass().getResource("../images/pacman_left.png")).getImage();
        pacman2Derecha = new ImageIcon(getClass().getResource("../images/pacman_right.png")).getImage();
        pacman3Derecha = new ImageIcon(getClass().getResource("../images/pacman_right.png")).getImage();
        pacman4Derecha = new ImageIcon(getClass().getResource("../images/pacman_right.png")).getImage();
        ghostDie = new ImageIcon(getClass().getResource("../images/ghost_dead.png")).getImage();
        superPill = new ImageIcon(getClass().getResource("../images/super.png")).getImage();
        ojosFantasma = new ImageIcon(getClass().getResource("../images/ghost_eyes.png")).getImage();
        cherry = new ImageIcon(getClass().getResource("../images/cherry.png")).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, dimension.width, dimension.height);

        drawMaze(g2d);
        drawScore(g2d);
        doAnim();

        if (enJuego) {
            playGame(g2d);
        } else {
            mostrarPantallaIntro(g2d);
        }

        g2d.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private static class Continue {

        public Continue() {
        }
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (enJuego) {
                if (key == KeyEvent.VK_LEFT) {
                    reqdx = -1;
                    reqdy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    reqdx = 1;
                    reqdy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    reqdx = 0;
                    reqdy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    reqdx = 0;
                    reqdy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && temporizador.isRunning()) {
                    enJuego = false;
                } else if (key == KeyEvent.VK_PAUSE) {
                    if (temporizador.isRunning()) {
                        temporizador.stop();
                    } else {
                        temporizador.start();
                    }
                }
            } else {
                if (key == 'p' || key == 'P') {
                    enJuego = true;
                    initGame();
                    playMusic(getClass().getResource("../music/gameStart.wav").getPath(),false);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                reqdx = 0;
                reqdy = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();
    }
}