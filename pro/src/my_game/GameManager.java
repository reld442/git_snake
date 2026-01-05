package my_game;

import java.awt.Point;

import base.GameCanvas;
import my_base.MyContent;
import base.KeyboardListener.Direction;
import shapes.TextLabel;

public class GameManager {

    private final MyContent content;
    private final ScoreManager scoreManager;
    private final GameBoard gameBoard;

    private boolean running = false;
    private boolean gameOver = false;

    private Snake snake;
    private Apple apple;

    // התא הלוגי של התפוח (כדי לצייר מחדש אחרי שינוי גודל)
    private Point appleCell;

    // שדה לטקסט GAME OVER – נשתמש בו גם במשחק וגם בריסייז
    private TextLabel gameOverLabel;

    public GameManager(MyContent content, ScoreManager scoreManager, GameBoard gameBoard) {
        this.content = content;
        this.scoreManager = scoreManager;
        this.gameBoard = gameBoard;
    }

    public void startGame() {
        running = true;
        gameOver = false;
        gameOverLabel = null; // מתחילים משחק חדש – אין GAME OVER

        GameCanvas canvas = content.canvas();

        canvas.deleteAllShapes();
        canvas.setBackgroundImage(null);
        gameBoard.draw(canvas);

        // בוחרים ראש נחש עם מרחק מהקירות (margin)
        int margin = 3;
        Point headCell;
        do {
            headCell = gameBoard.randomFreeCell();
        } while (headCell.x < margin
                || headCell.x >= GameBoard.LOGICAL_COLS - margin
                || headCell.y < margin
                || headCell.y >= GameBoard.LOGICAL_ROWS - margin);

        snake = new Snake(gameBoard, headCell.x, headCell.y, 3);
        snake.setDirection(Direction.RIGHT);
        snake.addToCanvas();

        // מגרילים תפוח
        appleCell = gameBoard.randomFreeCell();
        while (appleCell.equals(headCell)) {
            appleCell = gameBoard.randomFreeCell();
        }

        apple = new Apple(
                gameBoard.gridX(appleCell.x),
                gameBoard.gridY(appleCell.y));
        apple.addToCanvas();

        scoreManager.reset();
        canvas.repaint();
    }

    /** טיפול במצב GAME OVER – לא קוראים כאן ל-redrawAfterResize */
    private void handleGameOver(String reason) {
        if (gameOver) {
            return; // כבר טופל
        }

        running = false;
        gameOver = true;

        GameCanvas canvas = content.canvas();

        // מוחקים GAME OVER ישן אם היה
        canvas.deleteShape("gameOverLabel");

        int cx = canvas.getWidth() / 2 - 100;
        int cy = canvas.getHeight() / 2;

        gameOverLabel = new TextLabel(
                "gameOverLabel",
                "GAME OVER",
                cx,
                cy);
        gameOverLabel.setFontSize(32);
        gameOverLabel.setzOrder(1000);

        canvas.addShape(gameOverLabel);
        canvas.repaint();

        System.out.println("GAME OVER: " + reason);
    }

    /**
     * נקרא כשמשנים את גודל החלון – מצייר לוח, נחש ותפוח מחדש, וגם GAME OVER אם צריך
     */
    public void redrawAfterResize() {
        GameCanvas canvas = content.canvas();

        // אם עוד לא התחלנו משחק ולא היה GameOver – אל תצייר כלום
        if (!running && !gameOver) {
            return;
        }

        // מוחק את כל הצורות
        canvas.deleteAllShapes();

        // מצייר לוח חדש לפי גודל המסך
        gameBoard.draw(canvas);

        // מצייר מחדש את הנחש לפי התא הלוגי של כל חוליה
        if (snake != null) {
            snake.redraw(gameBoard);
        }

        // מצייר מחדש את התפוח לפי התא הלוגי ששמרנו
        if (appleCell != null) {
            apple = new Apple(
                    gameBoard.gridX(appleCell.x),
                    gameBoard.gridY(appleCell.y));
            apple.addToCanvas();
        }

        // אם המשחק נגמר – מוסיפים טקסט GAME OVER אחד בלבד, באמצע
        if (gameOver) {
            int cx = canvas.getWidth() / 2 - 100;
            int cy = canvas.getHeight() / 2;

            gameOverLabel = new TextLabel(
                    "gameOverLabel",
                    "GAME OVER",
                    cx,
                    cy);
            gameOverLabel.setFontSize(32);
            gameOverLabel.setzOrder(1000);

            canvas.addShape(gameOverLabel);
        } else {
            // במצב משחק רגיל – רק מעדכנים ניקוד (בינתיים תמיד 0)
            scoreManager.reset();
        }

        canvas.repaint();
    }

    // ייקרא ע"י ה-KeyboardListener
    public void setDirection(Direction dir) {
        if (snake != null) {
            snake.setDirection(dir);
        }
    }

    // ייקרא ע"י ה-PeriodicLoop שלך בכל טיק
    public void moveSnakeOneStep() {
        if (!running || snake == null) {
            return;
        }

        boolean moved = snake.moveOneStep(gameBoard);

        // אם לא הצליח לזוז – נניח שהתנגש בקיר → Game Over
        if (!moved) {
            handleGameOver("Hit the wall");
            return;
        }

        content.canvas().repaint();
    }

    public boolean isRunning() {
        return running;
    }
}
