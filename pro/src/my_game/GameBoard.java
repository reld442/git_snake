package my_game;

import java.awt.Color;
import java.awt.Point;

import base.GameCanvas;
import shapes.Rectangle;

/**
 * GameBoard – לוח לוגי של המשחק:
 * - מספר שורות / עמודות קבוע
 * - תאים תפוסים / פנויים
 * - המרה בין (row,col) לבין פיקסלים.
 */
public class GameBoard {

    // גריד לוגי קבוע
    public static final int LOGICAL_ROWS = 20;
    public static final int LOGICAL_COLS = 40;

    // גודל תא בפיקסלים (מחושב לפי גודל המסך)
    public static int CELL_SIZE = 40;

    // אפשר להזיז את הלוח (אם רוצים למרכז בעתיד)
    private int offsetX = 0;
    private int offsetY = 0;

    private boolean[][] occupied = new boolean[LOGICAL_ROWS][LOGICAL_COLS];

    /** מצייר את הלוח לפי גודל ה-canvas הנוכחי */
    public void draw(GameCanvas canvas) {

        int canvasW = canvas.getWidth();
        int canvasH = canvas.getHeight();

        // גודל תא לפי גודל המסך
        int cellSizeX = canvasW / LOGICAL_COLS;
        int cellSizeY = canvasH / LOGICAL_ROWS;
        CELL_SIZE = Math.max(1, Math.min(cellSizeX, cellSizeY));

        // אם תרצה למרכז את הלוח – אפשר להפעיל offset:
        int boardWidth = CELL_SIZE * LOGICAL_COLS;
        int boardHeight = CELL_SIZE * LOGICAL_ROWS;

        offsetX = (canvasW - boardWidth) / 2;
        offsetY = (canvasH - boardHeight) / 2;

        if (offsetX < 0)
            offsetX = 0;
        if (offsetY < 0)
            offsetY = 0;

        // איפוס תאים תפוסים
        occupied = new boolean[LOGICAL_ROWS][LOGICAL_COLS];

        Color bg = new Color(170, 215, 81);
        Color gridColor = new Color(120, 170, 60);

        canvas.setBackground(bg);

        for (int row = 0; row < LOGICAL_ROWS; row++) {
            for (int col = 0; col < LOGICAL_COLS; col++) {

                int x = offsetX + col * CELL_SIZE;
                int y = offsetY + row * CELL_SIZE;

                Rectangle cell = new Rectangle(
                        "cell_" + row + "_" + col,
                        x, y,
                        CELL_SIZE, CELL_SIZE);

                cell.setIsFilled(false);
                cell.setColor(gridColor);
                cell.setzOrder(0);

                canvas.addShape(cell);
            }
        }
    }

    /* ---------- חוקי מרחב ---------- */

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < LOGICAL_ROWS &&
                col >= 0 && col < LOGICAL_COLS;
    }

    public boolean isOccupied(int row, int col) {
        return occupied[row][col];
    }

    public void occupy(int row, int col) {
        occupied[row][col] = true;
    }

    public void release(int row, int col) {
        occupied[row][col] = false;
    }

    /** מחזיר תא פנוי רנדומלי (col=xIndex, row=yIndex) */
    public Point randomFreeCell() {
        int row, col;
        do {
            row = (int) (Math.random() * LOGICAL_ROWS);
            col = (int) (Math.random() * LOGICAL_COLS);
        } while (occupied[row][col]);
        return new Point(col, row);
    }

    /* ---------- המרה גריד ← פיקסלים ---------- */

    public int gridX(int col) {
        return offsetX + col * CELL_SIZE;
    }

    public int gridY(int row) {
        return offsetY + row * CELL_SIZE;
    }
}
