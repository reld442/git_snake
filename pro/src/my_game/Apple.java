package my_game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import base.Game;
import base.GameCanvas;
import shapes.Rectangle;

public class Apple extends Rectangle {

    private final Color fillColor = Color.RED; // צבע התפוח
    private final Color borderColor = Color.RED; // מסגרת התפוח

    public Apple(int x, int y) {
        super("apple", x, y,
                GameBoard.CELL_SIZE,
                GameBoard.CELL_SIZE);

        // לא משתמשים יותר ב-setColor / setCustomSideColors
        // ולא משנים isFilled כאן – נטפל בזה ב-draw
        setzOrder(20); // מעל הגריד
    }

    @Override
    public void draw(Graphics2D g) {
        // ציור מילוי
        g.setColor(fillColor);
        g.fillRect(getPosX(), getPosY(), getWidth(), getHeight());

        // ציור מסגרת
        g.setColor(borderColor);
        g.setStroke(new BasicStroke(0));
        g.drawRect(getPosX(), getPosY(), getWidth(), getHeight());
    }

    public void addToCanvas() {
        GameCanvas canvas = Game.UI().canvas();
        canvas.addShape(this);
    }
}
