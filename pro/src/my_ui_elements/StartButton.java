package my_ui_elements;

import base.Game;
import my_base.MyContent;
import ui_elements.GameButton;

public class StartButton extends GameButton {

    public StartButton(String id, String name, int width, int height, int posX, int posY) {
        super(id, name, width, height, posX, posY);
    }

    @Override
    public void action() {
        super.action();

        // מוחק ספציפית את הכפתורים מה-Canvas (במקום deleteAllUIElements הבאגי)
        Game.UI().canvas().deleteUIElement("btnStart");
        Game.UI().canvas().deleteUIElement("btnEND");

        // מתחיל משחק
        MyContent content = (MyContent) Game.Content();
        content.gameManager().startGame();

        Game.UI().canvas().repaint();
    }
}
