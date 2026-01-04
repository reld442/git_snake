package my_base;

import base.PeriodicLoop;

public class MyPeriodicLoop extends PeriodicLoop {

	private MyContent content;

	public void setContent(MyContent content) {
		this.content = content;
	}

	@Override
	public void execute() {
		// שומר על מדידת הזמן של התשתית
		super.execute();

		// אם עוד אין תוכן או GameManager – לא עושים כלום
		if (content == null || content.gameManager() == null) {
			return;
		}

		// אם המשחק רץ – מזיזים את הנחש צעד אחד
		if (content.gameManager().isRunning()) {
			content.gameManager().moveSnakeOneStep();
		}
	}
}
