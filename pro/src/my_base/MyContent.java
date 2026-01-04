package my_base;

import base.Game;
import base.GameCanvas;
import base.GameContent;
import base.GameUI;
import my_game.GameBoard;
import my_game.GameManager;
import my_game.ScoreManager;

/**
 * Game-specific content for the StartGame story.
 */
public class MyContent extends GameContent {

	private ScoreManager scoreManager;
	private GameManager gameManager;
	private GameBoard gameBoard;
	private GameUI gameUI;

	@Override
	public void initContent() {
		scoreManager = new ScoreManager();
		gameBoard = new GameBoard();
		gameManager = new GameManager(this, scoreManager, gameBoard);
	}

	// IMPORTANT: framework base canvas() isn't implemented, so use
	// Game.UI().canvas()
	public GameCanvas canvas() {
		return Game.UI().canvas();
	}

	public ScoreManager scoreManager() {
		return scoreManager;
	}

	public GameManager gameManager() {
		return gameManager;
	}

	public GameBoard gameBoard() {
		return gameBoard;
	}

	public void setGameUI(GameUI gameUI) {
		this.gameUI = gameUI;
	}

	public GameUI gameUI() {
		return gameUI;
	}
}
