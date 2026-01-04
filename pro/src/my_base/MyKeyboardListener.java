package my_base;

import java.awt.event.KeyEvent;

import base.KeyboardListener;
import base.KeyboardListener.Direction;

/**
 * מאזין מקלדת ספציפי למשחק Snake.
 * משתמש ב-enum Direction שמוגדר בתוך KeyboardListener.
 */
public class MyKeyboardListener extends KeyboardListener {

	private MyContent myContent;

	public MyKeyboardListener() {
		super();
		// Game.Content() מוזן לתוך this.content ע"י המחלקה Game
		this.myContent = (MyContent) this.content;
	}

	@Override
	public void directionalKeyPressed(Direction direction) {
		System.out.println("direction key pressed: " + direction);

		if (myContent == null || myContent.gameManager() == null) {
			return;
		}

		// מעדכן את כיוון הנחש דרך GameManager
		myContent.gameManager().setDirection(direction);
	}

	@Override
	public void characterTyped(char c) {
		System.out.println("key character = '" + c + "'" + " pressed.");
	}

	@Override
	public void enterKeyPressed() {
		System.out.println("enter key pressed.");
	}

	@Override
	public void backSpaceKeyPressed() {
		System.out.println("backSpace key pressed.");
	}

	@Override
	public void spaceKeyPressed() {
		System.out.println("space key pressed.");
	}

	@Override
	public void otherKeyPressed(KeyEvent e) {
		System.out.println("other key pressed. type:" + e);
	}
}
