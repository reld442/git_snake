package my_game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import base.Game;
import base.GameCanvas;
import base.KeyboardListener.Direction;
import shapes.Rectangle;

public class Snake {

    // מיקום לוגי של החוליות (בעולם הגריד: col,row)
    private final List<Point> bodyCells = new ArrayList<>();
    // הצורות הפיזיות על הקנבס
    private final List<Rectangle> segments = new ArrayList<>();

    private Direction direction = Direction.RIGHT;

    // צבע אחיד לכל החוליות
    private final Color bodyColor = Color.GRAY;

    /* ===== מחלקה פנימית לציור חוליות הנחש ===== */
    private static class SnakeSegment extends Rectangle {

        private final Color fillColor;
        private final Color borderColor;

        public SnakeSegment(String id, int x, int y, int size, Color fillColor, Color borderColor) {
            super(id, x, y, size, size);
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            // לא משתמשים במנגנון הצבעים של Rectangle
            setIsFilled(false); // נתעלם מזה ב-draw
        }

        @Override
        public void draw(Graphics2D g) {
            // מילוי
            g.setColor(fillColor);
            g.fillRect(getPosX(), getPosY(), getWidth(), getHeight());

            // מסגרת דקה
            g.setColor(borderColor);
            g.setStroke(new BasicStroke(1));
            g.drawRect(getPosX(), getPosY(), getWidth(), getHeight());
        }
    }
    /* ===== סוף מחלקה פנימית ===== */

    public Snake(GameBoard board, int headCol, int headRow, int length) {
        for (int i = 0; i < length; i++) {
            int col = headCol - i; // הולכים שמאלה מהראש
            int row = headRow;

            Point cell = new Point(col, row);
            bodyCells.add(cell);

            int x = board.gridX(col);
            int y = board.gridY(row);

            SnakeSegment seg = new SnakeSegment(
                    "snake_" + i,
                    x, y,
                    GameBoard.CELL_SIZE,
                    bodyColor, // מילוי אפור
                    Color.DARK_GRAY // מסגרת אפורה כהה
            );
            seg.setzOrder(15);
            segments.add(seg);
        }
    }

    // הוספת כל החוליות לקנבס
    public void addToCanvas() {
        GameCanvas canvas = Game.UI().canvas();
        for (Rectangle seg : segments) {
            canvas.addShape(seg);
        }
    }

    public void setDirection(Direction dir) {
        if (dir == null) {
            return;
        }

        // אם זה הכיוון ההפוך לגמרי – מתעלמים
        if ((this.direction == Direction.UP && dir == Direction.DOWN) ||
                (this.direction == Direction.DOWN && dir == Direction.UP) ||
                (this.direction == Direction.LEFT && dir == Direction.RIGHT) ||
                (this.direction == Direction.RIGHT && dir == Direction.LEFT)) {
            return;
        }

        this.direction = dir;
    }

    // צעד אחד קדימה
    public boolean moveOneStep(GameBoard board) {
        if (bodyCells.isEmpty())
            return false;

        // ראש נוכחי
        Point head = bodyCells.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case RIGHT:
                newHead.x += 1;
                break;
            case LEFT:
                newHead.x -= 1;
                break;
            case UP:
                newHead.y -= 1;
                break;
            case DOWN:
                newHead.y += 1;
                break;
        }

        // התנגשות בקיר – חוזרים false
        if (!board.inBounds(newHead.y, newHead.x)) {
            return false;
        }

        // מזיזים את הגוף מהזנב לראש
        for (int i = bodyCells.size() - 1; i > 0; i--) {
            bodyCells.get(i).x = bodyCells.get(i - 1).x;
            bodyCells.get(i).y = bodyCells.get(i - 1).y;
        }
        bodyCells.get(0).x = newHead.x;
        bodyCells.get(0).y = newHead.y;

        // מעדכנים מיקום גרפי
        updateSegmentsPosition(board);

        return true;
    }

    // נקרא מתוך GameManager.redrawAfterResize()
    public void redraw(GameBoard board) {
        segments.clear();

        for (int i = 0; i < bodyCells.size(); i++) {
            Point cell = bodyCells.get(i);
            int x = board.gridX(cell.x);
            int y = board.gridY(cell.y);

            SnakeSegment seg = new SnakeSegment(
                    "snake_" + i,
                    x, y,
                    GameBoard.CELL_SIZE,
                    bodyColor,
                    Color.DARK_GRAY);
            seg.setzOrder(15);
            segments.add(seg);
        }

        addToCanvas();
    }

    // עדכון מיקום הצורות אחרי כל צעד
    private void updateSegmentsPosition(GameBoard board) {
        for (int i = 0; i < bodyCells.size(); i++) {
            Point cell = bodyCells.get(i);
            Rectangle seg = segments.get(i);

            int x = board.gridX(cell.x);
            int y = board.gridY(cell.y);

            seg.moveToLocation(x, y);
            seg.setWidth(GameBoard.CELL_SIZE);
            seg.setHeight(GameBoard.CELL_SIZE);
        }
    }
}
