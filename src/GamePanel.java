import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int CELL_SIZE = 20; // Kích thước của mỗi ô vuông trong game
    private static final int WIDTH = 20; // Số lượng ô vuông theo chiều ngang
    private static final int HEIGHT = 20; // Số lượng ô vuông theo chiều dọc
    private static final Color GAME_AREA_COLOR = Color.BLACK; // Màu nền của vùng chơi game
    private static final Color BORDER_COLOR = Color.BLUE; // Màu của khung viền

    private Snake snake;
    private Apple apple;
    private Timer timer;
    private JButton startButton;
    private JLabel scoreLabel; // Label để hiển thị điểm số

    private int score = 0; // Điểm số ban đầu

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE));
        setLayout(new BorderLayout()); // Sử dụng BorderLayout để đặt các thành phần

        JPanel gameArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Vẽ rắn
                g.setColor(Color.GREEN);
                for (Point bodyPart : snake.getBody()) {
                    g.fillRect(bodyPart.x * CELL_SIZE, bodyPart.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }

                // Vẽ quả táo
                g.setColor(Color.RED);
                int appleX = apple.getPosition().x * CELL_SIZE;
                int appleY = apple.getPosition().y * CELL_SIZE;
                g.fillOval(appleX + 1, appleY + 1, CELL_SIZE - 2, CELL_SIZE - 2); // Đảm bảo quả táo nằm trong ô vuông
            }
        };
        gameArea.setPreferredSize(new Dimension(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE));
        gameArea.setBackground(GAME_AREA_COLOR);

        snake = new Snake();
        apple = new Apple(new Point(5, 5)); // Vị trí ban đầu của quả táo

        timer = new Timer(100, this); // Tạo timer với 100ms (10 lần/giây)

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setForeground(Color.WHITE); // Màu chữ của điểm số
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Phông chữ và kích thước chữ

        JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(GAME_AREA_COLOR); // Đặt nền cho panel
        controlsPanel.add(startButton);
        controlsPanel.add(scoreLabel); // Thêm label điểm số vào panel

        add(gameArea, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.SOUTH);

        addKeyListener(this);
    }

    private void startGame() {
        score = 0; // Đặt lại điểm số khi bắt đầu game mới
        scoreLabel.setText("Score: " + score); // Cập nhật label điểm số
        startButton.setVisible(false); // Ẩn nút Start khi game đã bắt đầu
        timer.start(); // Bắt đầu timer để game bắt đầu diễn ra
        requestFocus(); // Yêu cầu focus để bắt đầu nhận sự kiện từ bàn phím
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ khung chơi game
        g.setColor(BORDER_COLOR);
        g.drawRect(0, 0, WIDTH * CELL_SIZE - 1, HEIGHT * CELL_SIZE - 1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snake.move();
        checkCollision();
        checkAppleCollision();
        scoreLabel.setText("Score: " + score); // Cập nhật label điểm số sau mỗi lần di chuyển
        repaint(); // Vẽ lại game sau mỗi lần di chuyển
    }

    private void checkCollision() {
        // Kiểm tra va chạm với tường
        Point head = snake.getHead();
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            gameOver();
        }

        // Kiểm tra va chạm với chính rắn
        for (int i = 1; i < snake.getBody().size(); i++) {
            if (head.equals(snake.getBody().get(i))) {
                gameOver();
            }
        }
    }

    private void checkAppleCollision() {
        // Kiểm tra ăn quả táo
        Point head = snake.getHead();
        if (head.equals(apple.getPosition())) {
            snake.getBody().add(new Point(apple.getPosition())); // Thêm đốt mới vào rắn
            score++; // Tăng điểm số khi ăn được quả táo
            scoreLabel.setText("Score: " + score); // Cập nhật label điểm số
            generateNewApple();
        }
    }

    private void generateNewApple() {
        // Tạo quả táo mới ở vị trí ngẫu nhiên
        int x = (int) (Math.random() * WIDTH);
        int y = (int) (Math.random() * HEIGHT);
        apple.setPosition(new Point(x, y));
    }

    private void gameOver() {
        // Xử lý khi game kết thúc
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
        resetGame();
    }

    private void resetGame() {
        snake = new Snake();
        generateNewApple();
        startButton.setVisible(true); // Hiển thị lại nút Start để bắt đầu game mới
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        Direction newDirection = snake.getDirection();

        switch (key) {
            case KeyEvent.VK_UP:
                newDirection = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                newDirection = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                newDirection = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                newDirection = Direction.RIGHT;
                break;
        }

        snake.changeDirection(newDirection);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Không cần thiết cho phương thức này
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Không cần thiết cho phương thức này
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new GamePanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
