package my_base;

import java.awt.Color;

import base.Game;
import base.GameCanvas;
import base.GameContent;
import base.GameDashboard;
import my_ui_elements.EndButton;
import my_ui_elements.StartButton;

public class MyGame extends Game {
	// github commit
	private MyContent content;

	@Override
	protected void initCanvas() {
		GameCanvas canvas = gameUI.canvas();

		// רקע למסך פתיחה
		canvas.setBackgroundImage("resources/Snake_reka.jpg");

		// כפתורים על הקנבס (ולא בדאשבורד!)
		StartButton start = new StartButton("btnStart", "START GAME", 200, 60, 650, 200);
		EndButton end = new EndButton("btnEND", "CLOSE", 200, 60, 650, 300);

		canvas.addUIElement(start);
		canvas.addUIElement(end);

	}

	@Override
	protected void initDashboard() {
		super.initDashboard();
		GameDashboard dashboard = gameUI.dashboard();
		dashboard.setBackground(Color.WHITE); // לא משנה כבר
		content.setGameUI(gameUI);

	}

	@Override
	public void setGameContent(GameContent content) {
		super.setGameContent(content);
		this.content = (MyContent) content;
	}

	public MyContent getContent() {
		return this.content;
	}

	public static void main(String[] args) {
		MyGame game = new MyGame();
		game.setGameContent(new MyContent());

		MyPeriodicLoop periodicLoop = new MyPeriodicLoop();
		periodicLoop.setContent(game.getContent());
		game.setPeriodicLoop(periodicLoop);

		game.setKeyboardListener(new MyKeyboardListener());

		game.initGame();

		// מסתירים את ה-dashboard
		game.gameUI.dashboard().setVisible(false);
		game.gameUI.dashboard().getParent().revalidate();
		game.gameUI.dashboard().getParent().repaint();

		// לוקחים את ה-JSplitPane שמחזיק canvas+dashboard
		java.awt.Container p = game.gameUI.canvas().getParent();
		if (p instanceof javax.swing.JSplitPane) {
			javax.swing.JSplitPane split = (javax.swing.JSplitPane) p;

			// כל הגובה לקנבס
			split.setResizeWeight(1.0);

			// שם את המחיצה הכי למטה
			split.setDividerLocation(1.0);

			// מעלים את הקו / פס הגרירה
			split.setDividerSize(0);
			split.setEnabled(false);
		}

		// מאזין לשינוי גודל הקנבס – מצייר רק את הרשת מחדש
		game.gameUI.canvas().addComponentListener(
				new java.awt.event.ComponentAdapter() {
					@Override
					public void componentResized(java.awt.event.ComponentEvent e) {
						// במקום gameBoard().draw(...)
						game.getContent().gameManager().redrawAfterResize();
					}
				});

	}

}
// this comment i added to demonstrate git use