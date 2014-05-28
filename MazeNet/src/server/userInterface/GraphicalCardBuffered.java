package server.userInterface;

import generated.TreasureType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import server.Card;
import server.Card.CardShape;
import server.Card.Orientation;
import tools.Debug;
import tools.DebugLevel;

public class GraphicalCardBuffered extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7583185643671311612L;
	private Image shape;
	private Image treasure;
	private List<Integer> pin;

	private TexturePaint paintBuffer = null;

	private CardShape cardShape;
	private Orientation cardOrientation;
	private TreasureType cardTreasure;
	
	@Override
	public void setSize(Dimension d) {
		if (shape != null) {
			Image temp = shape;
			shape = new BufferedImage(d.width, d.height,
					BufferedImage.TYPE_INT_ARGB);
			shape = temp.getScaledInstance(d.width, d.height,
					Image.SCALE_DEFAULT);
		}
		updatePaint();
	}

	@Override
	public void setSize(int width, int height) {
		setSize(new Dimension(width, height));
	}

	public GraphicalCardBuffered() {
		super();
		loadShape(CardShape.T, Orientation.D0);
		treasure = null;
		pin = null;
		//setSize(new Dimension(100, 100));
	}

	public void setCard(Card c) {
		loadShape(c.getShape(), c.getOrientation());
		loadTreasure(c.getTreasure());
		loadPins(c.getPin().getPlayerID());
	}

	public void loadShape(CardShape cs, Orientation co) {
		if (cs == this.cardShape && co == this.cardOrientation) {
			return;
		}
		this.cardShape = cs;
		this.cardOrientation = co;
		try {

			URL url = GraphicalCardBuffered.class
					.getResource("/server/userInterface/resources/"
							+ cs.toString() + co.value() + ".png");
			Debug.print("Load: " + url.toString(), DebugLevel.DEBUG);
			shape = ImageIO.read(url);
			updatePaint();

		} catch (IOException e) {
		}
	}

	public void loadTreasure(TreasureType t) {
		if (t == this.cardTreasure) {
			return;
		}
		this.cardTreasure = t;
		try {
			if (t != null) {
				URL url = GraphicalCardBuffered.class
						.getResource("/server/userInterface/resources/"
								+ t.value() + ".png");
				Debug.print("Load: " + url.toString(), DebugLevel.DEBUG);
				treasure = ImageIO.read(url);
			} else {
				treasure = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		updatePaint();

	}

	public void loadPins(List<Integer> list) {
		if (list.size() != 0)
			pin = list;
		else
			pin = null;
		updatePaint();
	}

	private void updatePaint() {
		if (shape == null) {
			paintBuffer = null;
			return;
		}

		 int w = shape.getWidth(null);
		 int h = shape.getHeight(null);
//		int w = this.getWidth();
//		int h = this.getHeight();

		if (w <= 0 || h <= 0) {
			paintBuffer = null;
			return;
		}

		BufferedImage buff = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB_PRE);

		Graphics2D g2 = buff.createGraphics();
		if (shape != null) {
			g2.drawImage(shape, 0, 0, null);
		}
		if (treasure != null) {
			int zentrum = h / 2 - treasure.getHeight(null) / 2;
			g2.drawImage(treasure, zentrum, zentrum, null);
		}
		if (pin != null) {
			g2.setColor(Color.BLACK);
			int height = 15;
			int width = 20;
			int zentrum = h / 2 - height / 2;
			g2.fillOval(zentrum, zentrum, height, width);
			char[] number = { (pin.get(0) + "").charAt(0) };
			g2.drawChars(number, 0, 1, zentrum, zentrum);
		}
		paintBuffer = new TexturePaint(buff, new Rectangle(0, 0, w, h));
		g2.dispose();
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (paintBuffer != null) {
			int w = shape.getWidth(null);
			int h = shape.getHeight(null);
			Insets in = getInsets();

			int x = in.left;
			int y = in.top;
			w = w - in.left - in.right;
			h = h - in.top - in.bottom;

			if (w >= 0 && h >= 0) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setPaint(paintBuffer);
				g2.fillRect(x, y, w, h);
			}
		}
	}
	
	public void blinkCard(long millis, int n){
		Image save=shape;
		for (int i = 0; i < n; i++) {			
			shape=null;
			updatePaint();
			try {
				Thread.sleep(millis/n);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			shape=save;
			updatePaint();
		}
	}

	public void blinkPlayer(long millis, int n){
		List<Integer> save=pin;
		for (int i = 0; i < n; i++) {			
			pin=null;
			updatePaint();
			try {
				Thread.sleep(millis/n);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pin=save;
			updatePaint();
		}
	}
	
}
