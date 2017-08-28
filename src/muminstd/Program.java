package muminstd;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author carls
 */
public class Program { 

    private JFrame mainFrame;
    private JPanel gamePanel;
    private JPanel controlPanel;
    private JPanel towerPanel;
    private JPanel startPanel;



    private Canvas canvas;
    private BufferStrategy bs;

    private Image hatifnatt_Icon;
    private Image morran_Icon;
    private Image cursor_Icon;
    private Image stinky_Icon;
    private Image cleaning_icon;
    private Image info_icon;

    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;

    private JLabel infoLabel;

    private JLabel timeLabel;

    private Cursor cursor;

    private boolean running;

    private Graphics2D g2d;

    private Stages oldStage;

    private boolean bool1;
    private boolean bool2;
    private boolean bool3;//Bool4 does not exist, button 4 does not need this
    private boolean bool5;
    private boolean bool6;
    private boolean bool7;
    private boolean bool8;
    private boolean click;

    private MouseEvent mousePressed;

    private Toolkit toolkit;

    private GameManager gameManager;

    private long lastDrawTick;
    private long lastUpdateTick;
    private int FPS;
    private int TPS;

    public void init() {
        try {
            hatifnatt_Icon = ImageIO.read(new File("src\\Images\\Hattifnatt.png"));
            morran_Icon = ImageIO.read(new File("src\\Images\\Morran.png"));
            cursor_Icon = ImageIO.read(new File("src\\Images\\cursor.png"));
            stinky_Icon = ImageIO.read(new File("src\\Images\\Stinky.png"));
            cleaning_icon = ImageIO.read(new File("src\\Images\\Clean.png"));
            info_icon = ImageIO.read(new File("src\\Images\\info.png"));

        } catch (IOException e) {
            System.out.println("error");
        }

        running = true;

        bool1 = false;
        bool2 = false;
        bool3 = false;

        bool5 = false;
        bool6 = false;
        click = false;
        bool7 = false;
        bool8 = false;

        toolkit = Toolkit.getDefaultToolkit();
        gameManager = new GameManager();

        FPS = 60;   //Frames per second
        TPS = 100;
        lastDrawTick = System.currentTimeMillis(); // Tick(updates) per second
        lastUpdateTick = System.currentTimeMillis();

        prepareGUI();
    }

    public void prepareGUI() {
        mainFrame = new JFrame("Mumin's-STD");

        mainFrame.pack();
        mainFrame.setSize(700, 650);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new Canvas();

        mainFrame.add(canvas);

        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();

        //Mouse to handle tower presses
        canvas.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                click = true;
                // gameManager.mousePressed(e);
                mousePressed = e;

                //Creates custom cursor
                cursor = toolkit.createCustomCursor(cursor_Icon, new Point(10, 10), "img");
                mainFrame.setCursor(cursor);

            }
        });

        gamePanel = new JPanel();
        controlPanel = new JPanel();
        towerPanel = new JPanel();
        startPanel = new JPanel();

        controlPanel.setBackground(Color.black);
        controlPanel.setLayout(new GridLayout(1, 4));

        gamePanel.setSize(100, 100);

        towerPanel.setBounds(0, 0, 200, 200);
        towerPanel.setLayout(new GridLayout(5, 0));
        towerPanel.setBackground(Color.green);

        mainFrame.setBackground(Color.yellow);

        button1 = new JButton("Button");

        button1.setVisible(true);
        button1.setIcon(new ImageIcon(hatifnatt_Icon));
        button1.setText("Hatifnatt : 200 Gold");

        button2 = new JButton("Button");

        button2.setVisible(true);
        button2.setIcon(new ImageIcon(morran_Icon));
        button2.setText("Morran : 400 Gold");

        button3 = new JButton("Button");

        button3.setVisible(true);

        button3.setText("Start");

        button4 = new JButton("Button");

        button4.setVisible(true);

        button4.setText("Upgrade");

        button5 = new JButton("Button");

        button5.setVisible(true);

        button5.setText("Scrubb Blood"); //This now cleans blood itead of clearing the wave
        button5.setIcon(new ImageIcon(cleaning_icon));

        button6 = new JButton("Button");

        button6.setVisible(true);

        button6.setText("Upgrade");

        button7 = new JButton("Button");
        button7.setVisible(true);
        button7.setIcon(new ImageIcon(stinky_Icon));
        button7.setText("Stinky : 300 Gold");

        button8 = new JButton("Button");

        button8.setVisible(true);
        button8.setIcon(new ImageIcon(morran_Icon));
        button8.setText("Start");

        infoLabel = new JLabel();
        infoLabel.setVisible(true);
        infoLabel.setIcon(new ImageIcon(info_icon));

        timeLabel = new JLabel();
        timeLabel.setText("Time:");

        mainFrame.add(startPanel, BorderLayout.NORTH);
        mainFrame.add(gamePanel, BorderLayout.CENTER);
        mainFrame.add(controlPanel, BorderLayout.SOUTH);
        mainFrame.add(towerPanel, BorderLayout.EAST);

        startPanel.add(button8);
        startPanel.add(infoLabel);

        towerPanel.add(button1);
        towerPanel.add(button2);
        towerPanel.add(button7);
        towerPanel.add(button6);
        towerPanel.add(button4);
        controlPanel.add(button3);

        controlPanel.add(button5);

        controlPanel.add(timeLabel);

        startPanel.setVisible(true);

        mainLoop();
    }

    //Prepares the start Gui to be shown
    public void prepareStartGUI() {

        System.out.println("Prepare start gui");
        startPanel.setEnabled(true);
        towerPanel.setEnabled(false);
        controlPanel.setEnabled(false);
        gamePanel.setEnabled(false);

        canvas.setVisible(false);

        startPanel.setVisible(true);
        towerPanel.setVisible(false);
        controlPanel.setVisible(false);
        gamePanel.setVisible(false);

        gameManager.init();

    }

    //Prepares the go(game) Gui to be shown
    public void prepareGoGUI() {
        System.out.println("Prepare go gui");
        startPanel.setEnabled(false);
        towerPanel.setEnabled(true);
        controlPanel.setEnabled(true);
        gamePanel.setEnabled(false);

        canvas.setSize(new Dimension(450, 560));
        canvas.setVisible(true);

        startPanel.setVisible(false);
        towerPanel.setVisible(true);
        controlPanel.setVisible(true);
        gamePanel.setVisible(false);

    }
//End gui doesn't exist. It's only here for nostalgia

    public void draw() {
        if (System.currentTimeMillis() > (lastDrawTick + (1000 / FPS))) {

            //try {
            g2d = (Graphics2D) bs.getDrawGraphics();

            gameManager.draw(g2d);

            if (!bs.contentsLost()) {
                bs.show();
            }

            g2d.clearRect(0, 0, 1000, 1000);
            lastDrawTick = System.currentTimeMillis();
        }

    }
// This part is the main loop of the program. It handles the different loops in different stages

    public void mainLoop() {

        changeStage(stage.START);
        while (running) {
            switch (stage) {

                case START:
                    startLoop();
                    break;

                case GO:

                    draw();
                    goLoop();
                    break;

                default:
                    System.exit(1);
            }

        }
    }
//Stage enum
    static Stages stage;

    public enum Stages {

        START, GO;
    }

    public void startLoop() {
        //checks if button is pressed
        button8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bool8 = true;

            }
        });

        if (bool8) {
            changeStage(Stages.GO);

            bool8 = false;

        }

    }

    public void goLoop() {

        if (Map.lost) {
            changeStage(Stages.START);

        }

        button4.setText("Gold: " + gameManager.getGold());

        if (gameManager.isWaveActive()) {
            button3.setText("Pause");
        } else {
            button3.setText("Start");
        }

        if (click) {
            System.out.println("click");
            gameManager.mousePressed(mousePressed);
            click = false;
        }

        //checks if button is pressed
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bool1 = true;

            }
        });
        if (bool1) {

            gameManager.setTowerType(GameManager.TowerType.HATIFNATT);

            cursor = toolkit.createCustomCursor(hatifnatt_Icon, new Point(10, 10), "img");
            mainFrame.setCursor(cursor);
            bool1 = false;
        }
        //checks if button is pressed
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bool2 = true;

            }
        });
        if (bool2) {

            cursor = toolkit.createCustomCursor(morran_Icon, new Point(10, 10), "img");
            mainFrame.setCursor(cursor);
            gameManager.setTowerType(GameManager.TowerType.MORRAN);

            bool2 = false;
        }
        //checks if button is pressed
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (bool3 != true) {
                    bool3 = true;
                }

            }
        });
        if (bool3) {

            gameManager.startButton();

            bool3 = false;
        }
        //checks if button is pressed
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bool5 = true;

            }
        });
        if (bool5) {

            gameManager.clearButton();

            bool5 = false;
        }
        //checks if button is pressed
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bool6 = true;

            }
        });
        if (bool6) {

            gameManager.upgradeButton();

            bool6 = false;
        }
        //checks if button is pressed
        button7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                bool7 = true;
            }
        });
        if (bool7) {
            gameManager.setTowerType(GameManager.TowerType.STINKY);

            cursor = toolkit.createCustomCursor(stinky_Icon, new Point(10, 10), "img");
            mainFrame.setCursor(cursor);

            bool7 = false;
        }

        //Kepps the updates within the ticks per second
        if (System.currentTimeMillis() > (lastUpdateTick + (1000 / TPS))) {

            gameManager.update();

            //saves the last update time for refference
            lastUpdateTick = System.currentTimeMillis();

        }

    }

//handles the different stage changes with the gui
    public void changeStage(Stages stage) {

        this.stage = stage;
        if (oldStage != stage) {

            switch (stage) {
                case START:
                    prepareStartGUI();

                    break;

                case GO:

                    prepareGoGUI();

                    break;

                default:
                    System.exit(1);
            }

        }
        oldStage = stage;
    }
}
