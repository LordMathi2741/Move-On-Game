import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {

    public static class Game extends JPanel implements KeyListener {
        private final int WIDTH = 800;
        private  final int HEIGHT = 600;
        private  final int UNIT_SIZE = 10;
        private final int characterLength = 10;
        private int speed = 1000;
        private final int DELAY = speed/UNIT_SIZE;
        private int currentScore = 0;
        private char direction = 'R';
        private boolean running = false;
        private Timer timer;
        private int foodX;
        private int foodY;
        private int foodNegativeX;
        private int foodNegativeY;
        private int powerUpX;
        private int powerUpY;
        private boolean showTrollMessage = false;
        private int x[] = new int[WIDTH/UNIT_SIZE];
        private int y[] = new int[HEIGHT/UNIT_SIZE];
        public Game() {
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setFocusable(true);
            requestFocus();
            addKeyListener(this);
            startGame();
        }

        public void startGame() {
            running = true;
            timer = new Timer(DELAY, e -> {
                move();
                repaint();
                checkCollision();

            });
            timer.start();
            Timer foodTimer = new Timer(5000, e -> {
                newFood();
            });
            Timer negativeFoodTimer = new Timer(2000, e -> {
                newNegativeFood();
            });
            Timer powerUpTimer = new Timer(10000, e -> {
                newPowerUp();
            });
            foodTimer.start();
            negativeFoodTimer.start();
            powerUpTimer.start();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            showScore(g);
            if (running) {
                for (int i = 0; i < characterLength; i++) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                drawFoods(g);
                if(showTrollMessage){
                    showTrollMessageAlert(g);
                }
            }else {
                gameOver(g);
            }
        }

        public void move(){
            for(int i = characterLength; i > 0; i--){
                x[i] = x[i-1];
                y[i] = y[i-1];
            }
            switch (direction){
                case 'U':
                    y[0] -= UNIT_SIZE;
                    break;
                case 'D':
                    y[0] += UNIT_SIZE;
                    break;
                case 'L':
                    x[0] -= UNIT_SIZE;
                    break;
                case 'R':
                    x[0] += UNIT_SIZE;
                    break;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
            }
        }

        public void checkCollision(){
            if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
                running = false;
            }
            if(x[0] == foodX && y[0] == foodY){
                currentScore++;
                newFood();
            }
            if(x[0] == foodNegativeX && y[0] == foodNegativeY){
                currentScore--;
                newNegativeFood();
            }
            if(x[0] == powerUpX && y[0] == powerUpY){
                currentScore += 5;
                speed = 200;
                newPowerUp();
            }
            if(x[0] == powerUpX - 30 && y[0] == powerUpY -30){
                setRandomPosition();
                showTrollMessage = true;
            }
            if(currentScore < 0){
                running = false;
            }
        }

        public void drawFoods(Graphics g){
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.BLUE);
            g.fillRect(foodNegativeX, foodNegativeY, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.YELLOW);
            g.fillRect(powerUpX, powerUpY, UNIT_SIZE, UNIT_SIZE);
        }

        public void setRandomPosition(){
            x[0] = (int) (Math.random() * ((double) WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            y[0] = (int) (Math.random() * ((double) HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        }

        public void showScore(Graphics g){
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+currentScore, 0, g.getFont().getSize());

        }

        public void showTrollMessageAlert(Graphics g){
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("You have been trolled", 300, g.getFont().getSize());
            Timer timer = new Timer(2000, e -> {
                showTrollMessage = false;
            });
            timer.start();
        }

        public void newFood(){
            foodX = (int)(Math.random() * ((double) WIDTH /UNIT_SIZE)) * UNIT_SIZE;
            foodY = (int)(Math.random() * ((double) HEIGHT /UNIT_SIZE)) * UNIT_SIZE;
        }

        public void newNegativeFood(){
            foodNegativeX = (int)(Math.random() * ((double) WIDTH /UNIT_SIZE)) * UNIT_SIZE;
            foodNegativeY = (int)(Math.random() * ((double) HEIGHT /UNIT_SIZE)) * UNIT_SIZE;
        }

        public void newPowerUp(){
            powerUpX = (int)(Math.random() * ((double) WIDTH /UNIT_SIZE)) * UNIT_SIZE;
            powerUpY = (int)(Math.random() * ((double) HEIGHT /UNIT_SIZE)) * UNIT_SIZE;
        }

        public void gameOver(Graphics g){
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over"))/2, HEIGHT/2);
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Move-On Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Game());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}