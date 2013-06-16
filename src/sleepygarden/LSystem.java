package sleepygarden;

import java.awt.Font;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;

public class LSystem extends BasicGame implements ComponentListener {

	TrueTypeFont font;
	TrueTypeFont mini;

	Line[] lines;
	LDrawer lDrawer;
	float x;
	float y;
	Image background;

	int red;
	int green;
	int blue;

	boolean redDesc = false;
	boolean greenDesc = false;
	boolean blueDesc = false;

	TextField patternIn;
	TextField thetaIn;
	TextField distIn;

	int timeInterval = 10;
	int colorShiftInterval = 30;
	int halfsecond = 500;

	long lastTime;
	long colorShiftTime;
	long rotationTime;
	long mutateTime;

	List<Integer> primes;

	String vitality = "f-f-f-f+f-fff-fff-f+f-ffff-f-f-fff++f-f-f+f+f+f+";
	String serpinski = "";

	public LSystem() {
		super("L-System Renderer");
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

		this.primes = PrimeList.getPrimes();

	}

	@Override
	public void componentActivated(AbstractComponent arg0) {

	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();

		if (!patternIn.hasFocus() && !thetaIn.hasFocus() && !distIn.hasFocus()) {

			if (lDrawer != null && input.isKeyDown(Input.KEY_R)) {
				lDrawer.clear();
				lDrawer = null;
				lines = null;

			}

			if (lDrawer != null) {
				if (input.isKeyDown(Input.KEY_M)) {
					if (System.currentTimeMillis() - mutateTime > halfsecond) {
						mutateTime = System.currentTimeMillis();
						// lDrawer.mutate();

					}
				}

				if (input.isKeyDown(Input.KEY_SPACE)) {

					if (System.currentTimeMillis() - lastTime > timeInterval) {
						lastTime = System.currentTimeMillis();
						drawNextIteration();
					}
				}

				lDrawer.setTheta(Integer.parseInt(thetaIn.getText()));
				lDrawer.setDistance(Double.parseDouble(distIn.getText()));
				lines = lDrawer.getLines();

				if (input.isKeyDown(Input.KEY_DOWN)) {
					lDrawer.move(0, (.2 * delta));
				}
				if (input.isKeyDown(Input.KEY_UP)) {
					lDrawer.move(0, (-.2 * delta));
				}
				if (input.isKeyDown(Input.KEY_LEFT)) {
					lDrawer.move((-.2 * delta), 0);
				}
				if (input.isKeyDown(Input.KEY_RIGHT)) {
					lDrawer.move((.2 * delta), 0);
				}
				if (input.isKeyDown(Input.KEY_W)) {
					lDrawer.scale(.0005 * delta);
				}
				if (input.isKeyDown(Input.KEY_S)) {
					lDrawer.scale(-.0005 * delta);
				}
				if (input.isKeyDown(Input.KEY_Q)) {
					lDrawer.rotate(-.0005 * delta);
				}
				if (input.isKeyDown(Input.KEY_E)) {
					lDrawer.rotate(.0005 * delta);
				}

			} else if (input.isKeyDown(Input.KEY_SPACE)) {

				System.out.println("init drawing!");
				LPoint origin = new LPoint(400, 300);
				lDrawer = new LDrawer(new LPoint[] { origin });
				lDrawer.resetHeading(); // set initial bearing to UP
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
				lDrawer.drawForward();
				break;
			case ('f'):
				lDrawer.drawForward();
				break;
			case ('+'):
				lDrawer.turnLeft();
				break;
			case ('-'):
				lDrawer.turnRight();
				break;
			}
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		background.draw(0, 0);

		// Color c = getColor(); //for color pulse
		// Color c2 = getColor();

		// Color.white;
		// Color.white
		if (lines != null) {
			for (int index = 0; index < lines.length; index++) {

				Line l = lines[index];
				if (l != null) {
					// if (primeHead(index)) {
					// g.draw(l,new GradientFill(x, y, Color.red, l.getX2(),
					// l.getY2(),Color.white));
					// } else if (primeTail(index)) {
					// g.draw(l,new GradientFill(x, y, Color.white, l.getX2(),
					// l.getY2(),Color.red));
					// } else {
					if (primeLine(index))
						g.draw(l, new GradientFill(x, y, Color.red, l.getX2(),
								l.getY2(), Color.red));
					else
						g.draw(l, new GradientFill(x, y, Color.white,
								l.getX2(), l.getY2(), Color.white));

					// g.draw(l, new GradientFill(x, y, c, l.getX2(), l.getY2(),
					// //for color pulse
					// c2));
					// }

				}

			}
		}
		patternIn.render(gc, g);
		thetaIn.render(gc, g);
		distIn.render(gc, g);

	}

	public boolean primeLine(int index) {

		return (primes.get(new Integer(index))) != null;

	}

	public Color getColor() {

		if (System.currentTimeMillis() - colorShiftTime > colorShiftInterval) {
			colorShiftTime = System.currentTimeMillis();
			Random random = new Random();

			if (red > 235) {
				redDesc = true;
			}
			if (green > 235) {
				greenDesc = true;
			}
			if (blue > 235) {
				blueDesc = true;
			}

			if (red < 120) {
				redDesc = false;
			}
			if (green < 120) {
				greenDesc = false;
			}
			if (blue < 120) {
				blueDesc = false;
			}

			int colorChange = random.nextInt(25); // 0-4
			if (redDesc)
				colorChange *= -1;
			red += colorChange;

			colorChange = random.nextInt(25);
			if (greenDesc)
				colorChange *= -1;
			green += colorChange;

			colorChange = random.nextInt(25);
			if (blueDesc)
				colorChange *= -1;
			blue += colorChange;
		}

		return new Color(red, green, blue);

	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new LSystem());
		app.setDisplayMode(800, 600, false);
		app.setShowFPS(false);
		app.setTargetFrameRate(60);
		app.start();
	}

}
