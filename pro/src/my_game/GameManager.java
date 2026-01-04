package my_game;

import java.awt.Point;

import base.GameCanvas;
import my_base.MyContent;
import base.KeyboardListener.Direction;
import shapes.TextLabel;
import java.awt.Color;
import base.GameCanvas;

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

    public GameManager(MyContent content, ScoreManager scoreManager, GameBoard gameBoard) {
        this.content = content;
        this.scoreManager = scoreManager;
        this.gameBoard = gameBoard;
    }

    public void startGame() {
        running = true;
        gameOver = false;
        GameCanvas canvas = content.canvas();

        canvas.deleteAllShapes();
        canvas.setBackgroundImage(null);
        gameBoard.draw(canvas);

        Point headCell = gameBoard.randomFreeCell();

        // נבנה את הנחש קודם
        snake = new Snake(gameBoard, headCell.x, headCell.y, 3);
        snake.setDirection(Direction.RIGHT);
        snake.addToCanvas();

        // נסמן את כל תאי הנחש כתפוסים
        // (פשוט נשתמש בפונקציה קטנה שנוסיף לסנייק אם תרצה, או נסמן ידנית)

        // ואז נגריל תפוח מתא פנוי
        appleCell = gameBoard.randomFreeCell();
        // כאן כבר randomFreeCell לא יחזיר תא שנכבש ע"י הנחש

        apple = new Apple(
                gameBoard.gridX(appleCell.x),
                gameBoard.gridY(appleCell.y));
        apple.addToCanvas();

        scoreManager.reset();
        canvas.repaint();
    }

    private void handleGameOver(String reason) {
        if (gameOver) {
            return; // כבר טופל
        }

        running = false;
        gameOver = true;

        // מצייר מחדש את הלוח + הנחש + התפוח + הטקסט
        redrawAfterResize();

        System.out.println("GAME OVER: " + reason);
    }

    /** נקרא כשמשנים את גודל החלון – מצייר לוח, נחש ותפוח מחדש */
    public void redrawAfterResize() {
        GameCanvas canvas = content.canvas();

        // אם עוד לא התחלנו משחק ולא היה GameOver – אל תצייר כלום
        if (!running && !gameOver) {
            return;
        }

        // מוחק את כל הצורות (כולל GAME OVER ישן אם יש)
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
            // ליתר ביטחון מוחקים טקסט ישן עם אותו id (אם קיים)
            canvas.deleteShape("gameOverLabel");

            int cx = canvas.getWidth() / 2 - 100;
            int cy = canvas.getHeight() / 2;

            TextLabel gameOverLabel = new TextLabel(
                    "gameOverLabel",
                    "GAME OVER",
                    cx,
                    cy);
            gameOverLabel.setFontSize(32);
            // אם יש לך setColor – מעולה, אם לא, אפשר להוריד:
            // gameOverLabel.setColor(Color.RED);
            gameOverLabel.setzOrder(1000);

            canvas.addShape(gameOverLabel);
        } else {
            // במצב משחק רגיל – נעדכן את הניקוד (בינתיים תמיד 0)
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
