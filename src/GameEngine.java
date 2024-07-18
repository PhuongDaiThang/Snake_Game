import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameEngine implements Runnable, KeyListener {
    private static final int WIDTH = 20; // Chiều rộng của màn hình console
    private static final int HEIGHT = 20; // Chiều cao của màn hình console
    private static final int INITIAL_SPEED = 200; // Tốc độ ban đầu của rắn (milliseconds)

    private Snake snake;
    private Apple apple;
    private boolean running;
    private int score;
    private Random random;
    private int speed; // Tốc độ di chuyển của rắn (milliseconds)

    public GameEngine() {
        snake = new Snake();
        spawnApple();
        running = true;
        score = 0;
        random = new Random();
        speed = INITIAL_SPEED;
    }

    // Phương thức để bắt đầu game
    public void start() {
        running = true;
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    // Phương thức để dừng game
    public void stop() {
        running = false;
    }

    // Phương thức để cập nhật trạng thái của game
    private void update() {
        snake.move();
        checkCollision();
        checkAppleCollision();
    }

    // Phương thức để vẽ lại màn hình console
    private void draw() {
        // Xóa màn hình console
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Hiển thị điểm số
        System.out.println("Score: " + score);

        // Vẽ rắn
        ArrayList<Point> snakeBody = snake.getBody();
        char[][] board = new char[HEIGHT][WIDTH];
        for (Point segment : snakeBody) {
            board[segment.y][segment.x] = 'S';
        }

        // Vẽ quả táo
        board[apple.getPosition().y][apple.getPosition().x] = 'A';

        // In ra màn hình console
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == 'S') {
                    System.out.print('S');
                } else if (board[i][j] == 'A') {
                    System.out.print('A');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }

    // Phương thức để kiểm tra va chạm với tường hoặc chính rắn
    private void checkCollision() {
        ArrayList<Point> snakeBody = snake.getBody();
        Point head = snakeBody.get(0);

        // Kiểm tra va chạm với tường
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            stop();
        }

        // Kiểm tra va chạm với chính rắn
        for (int i = 1; i < snakeBody.size(); i++) {
            if (head.equals(snakeBody.get(i))) {
                stop();
            }
        }
    }

    // Phương thức để kiểm tra va chạm với quả táo
    private void checkAppleCollision() {
        ArrayList<Point> snakeBody = snake.getBody();
        Point head = snakeBody.get(0);

        // Kiểm tra nếu đầu rắn ăn được quả táo
        if (head.equals(apple.getPosition())) {
            // Tăng độ dài của rắn và tăng điểm số
            snakeBody.add(new Point(apple.getPosition()));
            score += 10;

            // Tăng tốc độ di chuyển của rắn
            speed -= 10;
            if (speed < 50) {
                speed = 50; // Giới hạn tốc độ tối thiểu
            }

            // Tạo quả táo mới
            spawnApple();
        }
    }

    // Phương thức để tạo quả táo mới
    private void spawnApple() {
        // Tạo một vị trí ngẫu nhiên cho quả táo
        int x = random.nextInt(WIDTH);
        int y = random.nextInt(HEIGHT);
        apple = new Apple(new Point(x, y));
    }

    // Phương thức chính của game để chạy trong vòng lặp
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            update();
            draw();

            // Ngủ một lát để giảm tốc độ của game
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // In thông báo kết thúc game
        System.out.println("Game Over! Score: " + score);
        scanner.close();
    }

    // Phương thức để xử lý sự kiện từ bàn phím
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

        // Thay đổi hướng di chuyển của rắn
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

    // Phương thức main để khởi động game
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        game.start();

        // Đăng ký bàn phím
        Scanner scanner = new Scanner(System.in);
        while (game.running) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("quit")) {
                game.stop();
            }
        }
        scanner.close();
    }
}
