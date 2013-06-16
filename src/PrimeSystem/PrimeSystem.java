package PrimeSystem;

import java.awt.Font;

import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;

import sleepygarden.*;

public class PrimeSystem extends BasicGame implements ComponentListener {

	TrueTypeFont font;
	TrueTypeFont mini;

	PPoint[] points;
	PDrawer pDrawer;
	Image background;

	float x;
	float y;

	int red;
	int green;
	int blue;

	TextField patternIn;
	TextField thetaIn;
	TextField distIn;

	int drawInterval = 25;
	long lastDraw;

	boolean paused;
	int pauseInterval = 100;
	long lastPause;

	List<Integer> primes;

	String vitality = "f-f-f-f+f-fff-fff-f+f-ffff-f-f-fff++f-f-f+f+f+f+";

	public PrimeSystem() {
		super("Prime-System Renderer");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		Font f = new Font("Verdana", Font.BOLD, 20);
		font = new TrueTypeFont(f, true);
		Font fmin = new Font("Verdana", Font.BOLD, 14);
		mini = new TrueTypeFont(fmin, true);

		patternIn = new TextField(gc, mini, 80, 45, 500, 25, this);
		thetaIn = new TextField(gc, mini, 745, 295, 50, 20, this);
		distIn = new TextField(gc, mini, 745, 347, 50, 20, this);
		thetaIn.setText("60");
		distIn.setText("25");

		patternIn.setText("f-");

		background = new Image("imgs/LSystemSplash.gif");

		paused=false;
		this.primes = PrimeList.getPrimes();

	}

	@Override
	public void componentActivated(AbstractComponent arg0) {

	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();

		if (!patternIn.hasFocus() && !thetaIn.hasFocus() && !distIn.hasFocus()) {

			if (pDrawer != null && input.isKeyDown(Input.KEY_R)) {
				pDrawer.clear();
				pDrawer = null;
				points = null;

			}

			if (pDrawer != null && !paused) {

				if (System.currentTimeMillis() - lastDraw > drawInterval) {
					lastDraw = System.currentTimeMillis();
					drawNextIteration();
				}

				points = pDrawer.getPoints();

				pDrawer.setTheta(Integer.parseInt(thetaIn.getText()));
				pDrawer.setDistance(Double.parseDouble(distIn.getText()));

				if (input.isKeyDown(Input.KEY_DOWN)) {
					pDrawer.move(0, (.2 * delta));
				}
				if (input.isKeyDown(Input.KEY_UP)) {
					pDrawer.move(0, (-.2 * delta));
				}
				if (input.isKeyDown(Input.KEY_LEFT)) {
					pDrawer.move((-.2 * delta), 0);
				}
				if (input.isKeyDown(Input.KEY_RIGHT)) {
					pDrawer.move((.2 * delta), 0);
				}
				if (input.isKeyDown(Input.KEY_W)) {
					pDrawer.scale(.0005 * delta);
				}
				if (input.isKeyDown(Input.KEY_S)) {
					pDrawer.scale(-.0005 * delta);
				}
				if (input.isKeyDown(Input.KEY_Q)) {
					pDrawer.rotate(-.0005 * delta);
				}
				if (input.isKeyDown(Input.KEY_E)) {
					pDrawer.rotate(.0005 * delta);
				}

			} else if (input.isKeyDown(Input.KEY_SPACE)) {
				System.out.println("init pDrawer!");
				paused=false;
				PPoint origin = new PPoint(300, 300);
				pDrawer = new PDrawer(new PPoint[] { origin });
				pDrawer.resetHeading(); // set initial bearing to UP

			}
			
			if (input.isKeyDown(Input.KEY_P)
					&& System.currentTimeMillis() - lastPause > pauseInterval) {
				lastPause = System.currentTimeMillis();
				paused = !paused;
				System.out.println("paused? " + paused);
			}
		}
	}

	public void drawNextIteration() {

		if (patternIn.getText().equals("vitality"))
			func(vitality);
		else
			func(patternIn.getText().trim());

	}

	public void func(String pattern) {

		for (char c : pattern.toCharArray()) {
			switch (c) {
			case ('F'):
				pDrawer.drawForward();
				break;
			case ('f'):
				pDrawer.drawForward();
				break;
			case ('+'):
				pDrawer.turnLeft();
				break;
			case ('-'):
				pDrawer.turnRight();
				break;
			}
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		background.draw(0, 0);

		if (points != null) {
			for (int index = 0; index < points.length; index++) {

				PPoint p = points[index];
				if (p != null) {
					if (primePoint(index)) {
						g.setColor(Color.red);
						g.draw(p);
					} else {
						g.setColor(Color.white);
						g.draw(p);
					}
				}
			}
		}
		g.setColor(Color.white);

		patternIn.render(gc, g);
		thetaIn.render(gc, g);
		distIn.render(gc, g);

	}

	public boolean primePoint(int index) {
		return (primes.contains(new Integer(index)));
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new PrimeSystem());
		app.setDisplayMode(800, 600, false);
		app.setShowFPS(false);
		app.setTargetFrameRate(60);
		app.start();
	}

}
