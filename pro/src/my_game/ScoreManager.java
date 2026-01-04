package my_game;

import base.Game;
import shapes.TextLabel;

/**
 * Minimal score manager for the StartGame story.
 * Only the fields/methods needed for StartGame are implemented.
 */
public class ScoreManager {

    private static final String SCORE_LABEL_ID = "scoreLabel";

    private int score = 0;
    private TextLabel scoreLabel;

    /** Reset score to 0 and update the UI. */
    public void reset() {
        score = 0;
        updateLabel();
    }

    public int getScore() {
        return score;
    }

    /** Update or create the score label on the canvas. */
    private void updateLabel() {
        if (Game.UI() == null) {
            return;
        }

        if (scoreLabel == null) {
            scoreLabel = new TextLabel(SCORE_LABEL_ID, buildText(), 20, 20);
            scoreLabel.setFontSize(18);
            scoreLabel.setzOrder(999); // keep on top
            Game.UI().canvas().addShape(scoreLabel);
        } else {
            // TextLabel stores text inside its JLabel
            scoreLabel.getLabel().setText(buildText());
            Game.UI().canvas().repaint();
        }
    }

    private String buildText() {
        return "Score: " + score;
    }

    public void redrawOnCanvas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'redrawOnCanvas'");
    }
}
