package base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ui_elements.ScreenPoint;
import ui_elements.UIElement;
import shapes.Circle;
import shapes.Image;
import shapes.Line;
import shapes.Polyline;
import shapes.Rectangle;
import shapes.Shape;
import shapes.Shape.STATUS;
import shapes.TextLabel;

/**
 * A 2D screen that displays graphical shapes and enables to set their location
 * at runtime, causing an animation effect.
 * 
 * <h1>Properties:</h1>
 * <ul>
 * <li>int width - the screen width, in pixels<br>
 * <li>int height - the screen hight, in pixels<br>
 * <li>borderColor - the color of the screen border, from a list of given colors
 * [red, black, yellow, gray, white]<br>
 * <li>borderWidth - the width of the screen border, in pixels (0 if no
 * border)<br>
 * <li>int positionX, positionY - the screen position, in pixels<br>
 * </ul>
 * 
 * <h1>Methods:</h1>
 * <ul>
 * <li>void addShape(shape) - adds a graphical shape, identified by id, to the
 * screen.<br>
 * <li>void moveShape(id, dx, dy) - moves a shape identified by id by dx and dy
 * pixels respectively.
 * For animation effect, one can execute the moveShape(id, dx, dy) in a loop
 * with a time-based wait condition between each moveObject call.
 * <br>
 * <li>void moveToLocation(id, cordX, cordY) - moves an shape identified by id
 * to coordinates cordX and cordY respectively.<br>
 * <li>void deleteShape(id) - permanently removes a shape identified by id from
 * the screen.<br>
 * <li>void flipStatus(id) - changes the status of a shape and shows or hide it
 * accordingly.<br>
 * <il>void show(id) - shows a shape identified by id.<br>
 * <il>void hide(id) - hides a shape identified by id.<br>
 * <il>void showAll() shows all shapes.<br>
 * <il>void hideAll() - hides all shapes.<br>
 * <il>void deleteAll() - deletes all shapes from the screen.<br>
 * </ul>
 * 
 */
public class GameCanvas extends JPanel {

	private static final long serialVersionUID = 1L;

	private final Map<String, Shape> shapes;
	private final Map<String, UIElement> uiElements;

	private Shape[] sortedShapes;

	private boolean resort = true;

	int borderWidth;

	int positionX;
	int positionY;

	private ImageIcon backgroundImage = null;
	private ScreenPoint backgroundImagePoint = null;
	private int[] backgroundImageSize = null;

	public GameCanvas() {
		super();
		this.setBackground(Color.WHITE);
		this.shapes = new HashMap<>();
		this.uiElements = new HashMap<>();
		// Not relevant -> will be assigned by the default values in the
		this.setLayout(null);

	}

	public void setBackgroundImage(String imageFile) {
		backgroundImage = new ImageIcon(imageFile);
	}

	public void addUIElement(UIElement uiElement) {
		uiElements.put(uiElement.getId(), uiElement);
		this.add(uiElement.getJComponent());
		this.updateUI();
	}

	public void addShape(Shape shape) {
		shapes.put(shape.getId(), shape);
		resort = true;
		this.repaint();
	}

	public Shape getShape(String id) {
		return shapes.get(id);
	}

	public UIElement getUIElement(String id) {
		return uiElements.get(id);
	}

	public void changeImage(String id, String src, int width, int height) {
		Shape shape = shapes.get(id);
		if (shape == null) {
			return;
		}
		if (!(shape instanceof Image)) {
			return;
		}
		Image image = (Image) shape;
		this.remove(image.getImg());
		image.setImage(src, width, height);
		this.add(image.getImg());
		this.repaint();
	}

	public void moveShape(String id, int dx, int dy) {
		Shape shape = shapes.get(id);
		if (shape != null) {
			shape.move(dx, dy);
			this.repaint();
			if (shape.getshapeListener() != null) {
				shape.getshapeListener().shapeMoved(id, dx, dy);
			}
		}
	}

	public void moveShapeToLocation(String id, int cordX, int cordY) {
		Shape shape = shapes.get(id);
		if (shape != null) {
			shape.moveToLocation(cordX, cordY);
			;
			this.repaint();
		}
	}

	public void deleteShape(String id) {
		Shape shape = shapes.get(id);
		if (shape != null) {
			hideShape(id);
			if (shape instanceof Image) {
				Image image = (Image) shape;
				this.remove(image.getImg());
			}
			shapes.remove(id);
		}
		resort = true;
		this.repaint();
	}

	public void deleteUIElement(String id) {
		UIElement uiElement = uiElements.get(id);
		if (uiElement != null) {
			// remove from Swing container
			this.remove(uiElement.getJComponent());
			// remove from map
			uiElements.remove(id);
			this.revalidate();
			this.repaint();
		}
	}

	public void hideAllShapes() {
		for (Shape shape : shapes.values()) {
			shape.setStatus(STATUS.HIDE);
		}
		resort = true;
		this.repaint();
	}

	public void hideAllUIElements() {
		for (UIElement uiElement : uiElements.values()) {
			uiElement.setStatus(STATUS.HIDE);
		}
		resort = true;
		this.repaint();
	}

	public void showAllShapes() {
		for (Shape shape : shapes.values()) {
			shape.setStatus(STATUS.SHOW);
		}
		resort = true;
		this.repaint();
	}

	public void showAllUIElements() {
		for (UIElement uiElement : uiElements.values()) {
			uiElement.setStatus(STATUS.SHOW);
		}
		resort = true;
		this.repaint();
	}

	public void deleteAllShapes() {
		Shape shape;
		for (String id : shapes.keySet()) {
			shape = shapes.get(id);
			if (shape != null) {
				hideShape(id);
			}
			if (shape instanceof Image) {
				Image image = (Image) shape;
				this.remove(image.getImg());
			}
		}
		shapes.clear();
		resort = true;
		this.repaint();
	}

	public void deleteAllUIElements() {
		for (UIElement uiElement : uiElements.values()) {
			this.remove(uiElement.getJComponent());
		}
		uiElements.clear();
		this.revalidate();
		this.repaint();
	}

	public void flipShapeStatus(String id) {
		Shape shape = shapes.get(id);
		if (shape != null) {
			if (shape.getStatus() == STATUS.HIDE) {
				shape.setStatus(STATUS.SHOW);
			} else if (shape.getStatus() == STATUS.SHOW) {
				shape.setStatus(STATUS.HIDE);
			}
		}
		resort = true;
		this.repaint();
	}

	public void flipUIElementStatus(String id) {
		UIElement uiElement = uiElements.get(id);
		if (uiElement != null) {
			if (uiElement.getStatus() == STATUS.HIDE) {
				uiElement.setStatus(STATUS.SHOW);
			} else if (uiElement.getStatus() == STATUS.SHOW) {
				uiElement.setStatus(STATUS.HIDE);
			}
		}
		resort = true;
		this.repaint();
	}

	public void showShape(String id) {
		Shape shape = shapes.get(id);
		if (shape != null) {
			shape.setStatus(STATUS.SHOW);
		}
		resort = true;
		this.repaint();
	}

	public void showUIElement(String id) {
		UIElement uiElement = uiElements.get(id);
		if (uiElement != null) {
			uiElement.setStatus(STATUS.SHOW);
		}
		resort = true;
		this.repaint();
	}

	public void hideShape(String id) {
		Shape shape = shapes.get(id);
		if (shape != null) {
			shape.setStatus(STATUS.HIDE);
		}
		resort = true;
		this.repaint();
	}

	public void hideUIElement(String id) {
		UIElement uiElement = uiElements.get(id);
		if (uiElement != null) {
			uiElement.setStatus(STATUS.HIDE);
		}
		resort = true;
		this.repaint();
	}

	private Shape[] getImagesSortedByZOrder() {
		if (resort) {
			sortedShapes = shapes.values().toArray(new Shape[0]);
			if (sortedShapes.length > 1) {
				Arrays.sort(sortedShapes, (s1, s2) -> {
					return (s1.getzOrder() - s2.getzOrder());
				});
			}
			resort = false;
		}
		return sortedShapes;
	}

	private Shape getShapeByXY(final int x, final int y) {
		Shape[] tempShapes = getImagesSortedByZOrder();
		Shape shape;

		// Run over the shapes in reverse order so that top shape is selected
		// before a bottom shape.
		for (int i = tempShapes.length - 1; i >= 0; i--) {
			shape = tempShapes[i];
			if (shape.getStatus() == STATUS.SHOW) {
				if (shape.isInArea(x, y)) {
					return shape;
				}
			}
		}
		return null;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (backgroundImage != null) {
			g.drawImage(backgroundImage.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		}

		// Draw the shapes according to their ZOrder.
		// To have a geometric shape in front of another it should be drawn later.
		// To have an image in front of another it should be drawn earlier.
		// Thus, we traverse the array twice, in order for the shapes and then in
		// reverse
		// order for the images.
		Shape[] tempShapes = getImagesSortedByZOrder();
		Shape shape;

		// Draw geometric shapes and texts in order
		for (int i = 0; i < tempShapes.length; i++) {
			shape = tempShapes[i];
			if (shape instanceof Image) {
				continue; // Do not handle Images in this pass
			}
			if (shape.getStatus() == STATUS.SHOW) {
				shape.draw((Graphics2D) g);
				if (shape instanceof TextLabel) {
					TextLabel lbl = (TextLabel) shape;
					this.add(lbl.getLabel());
				}

			}
			if (shape.getStatus() == STATUS.HIDE) {
				if (shape instanceof TextLabel) {
					TextLabel lbl = (TextLabel) shape;
					this.remove(lbl.getLabel());
				}

			}
		}

		// Draw images in reverse order
		for (int i = tempShapes.length - 1; i >= 0; i--) {
			shape = tempShapes[i];
			if (!(shape instanceof Image)) {
				continue; // Do not handle Non-Image shapes in this pass
			}
			if (shape.getStatus() == STATUS.SHOW) {
				shape.draw((Graphics2D) g);
				Image image = (Image) shape;
				this.add(image.getImg());

			}
			if (shape.getStatus() == STATUS.HIDE) {
				Image image = (Image) shape;
				this.remove(image.getImg());
			}
		}

	}

	public static void main(final String[] args) {

		// Create a frame window and set its name, size and behavior when clicking the X
		final JFrame frame = new JFrame("My Screen");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 1000);

	}

}
