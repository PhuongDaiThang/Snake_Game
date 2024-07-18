import java.awt.*;
import java.util.ArrayList;

public class Snake {
    private ArrayList<Point> body;
    private Direction direction;

    public Snake() {
        body = new ArrayList<>();
        body.add(new Point(10, 10)); // Đầu rắn ở vị trí ban đầu
        direction = Direction.RIGHT; // Hướng di chuyển ban đầu
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public Direction getDirection() {
        return direction;
    }

    public void changeDirection(Direction newDirection) {
        if (newDirection == Direction.UP && direction != Direction.DOWN) {
            direction = Direction.UP;
        } else if (newDirection == Direction.DOWN && direction != Direction.UP) {
            direction = Direction.DOWN;
        } else if (newDirection == Direction.LEFT && direction != Direction.RIGHT) {
            direction = Direction.LEFT;
        } else if (newDirection == Direction.RIGHT && direction != Direction.LEFT) {
            direction = Direction.RIGHT;
        }
    }

    public void move() {
        // Thêm phần thân của rắn phía trước
        Point newHead = new Point(getHead());
        switch (direction) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }
        body.add(0, newHead);

        // Xoá phần đuôi của rắn (nếu không ăn quả táo)
        body.remove(body.size() - 1);
    }

    public Point getHead() {
        return body.get(0);
    }
}
