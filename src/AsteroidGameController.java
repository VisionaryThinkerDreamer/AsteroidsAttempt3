import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AsteroidGameController extends JComponent
		implements ActionListener, KeyListener
{
	public JFrame space = new JFrame();
	int screenWidth = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize().width;
	int screenHeight = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize().height;
	private Image spaceImage = new ImageIcon(getClass().getResource("spacePicture.jpg")).getImage();// Image spaceImage;
	public Timer ticker = new Timer(30, this);
	public int[] asteroid1XPoints =
	{ 21, 16, 20, 15, 0, -19, -17, -21, -15 };
	public int[] asteroid1YPoints =
	{ 24, 19, 18, 16, 17, 24, 20, 17, 18 };
	private int speedOfShip = 0;
	private int speedLimitOfShip = 10;
	private int middleScreenXPos = screenWidth / 2;
	private int middleScreenYPos = screenHeight / 2;
	private int directionOfHeadOfShip = 90; // degrees
	public int colorChangeController;
	public int colorChanger = (int) directionOfHeadOfShip - colorChangeController;
	private boolean moveFaster;
	private boolean turnLeft;
	private boolean turnRight;
	private boolean slowDown;
	public Ship arwing;
	public AsteroidDestroyingProjectile shot;
	public ArrayList<Asteroid> asteroidList = new ArrayList<>();
	public ArrayList<AsteroidDestroyingProjectile> projectileList = new ArrayList<>();
	public AffineTransform identity = new AffineTransform(); // identity transform
	public Random r = new Random();
	public int asteroidSpawnQuadrantPicker;
	public Utilities util = new Utilities();
	
	public static void main(String[] args)
	{
		new AsteroidGameController().getGoing();
	}

	void getGoing()
	{
		
		/*********************************************************
		 * spawn asteroids
		 *********************************************************/
		for (int j = 0; j < 14; j++)
		{
			asteroidSpawner();
		}
		/*********************************************************
		 * spawn projectiles
		 *********************************************************/
		for (int i = 0; i < projectileList.size(); i++)
		{
			AsteroidDestroyingProjectile shot = projectileList.get(i);
		}
		
		arwing = new Ship(middleScreenXPos, middleScreenYPos);
		arwing.setScreenHeight(screenHeight);
		arwing.setScreenWidth(screenWidth);
		ticker.start();
		space.setSize(screenWidth, screenHeight);
		space.setVisible(true);
		space.setDefaultCloseOperation(space.EXIT_ON_CLOSE);
		space.add(this);
		space.setBackground(Color.BLACK);
		space.setTitle("HEY! GUESS WHAT? I'M A TITLE!");
		space.addKeyListener(this);
		arwing.setRotationDegree(0);
	}

	public void asteroidSpawner()
	{
		asteroidSpawnQuadrantPicker = r.nextInt(4);
		if (asteroidSpawnQuadrantPicker == 0)// west
		{
			asteroidList.add(new Asteroid(-50, r.nextInt(screenHeight),
					r.nextInt(90) - 45, 3, Math.random() * 0.1, Math.random()));// xpos, ypos, course, speed, scale factor, rotation speed
		}
		if (asteroidSpawnQuadrantPicker == 1) // north
		{
			asteroidList.add(new Asteroid(r.nextInt(screenWidth), -50,
					r.nextInt(90) - 135, 3, Math.random() * 0.1, Math.random()));
		}
		if (asteroidSpawnQuadrantPicker == 2) // east
		{
			asteroidList.add(new Asteroid(screenWidth + 50,
					r.nextInt(screenHeight), r.nextInt(90) - 225, 3, Math.random() * 0.1, Math.random()));
		}
		if (asteroidSpawnQuadrantPicker == 3) // south
		{
			asteroidList.add(new Asteroid(r.nextInt(screenWidth),
					screenHeight + 50, r.nextInt(90) + 45, 3, Math.random() * 0.1, Math.random()));
		}
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		shipMovementRegulator();
		arwing.setDirectionOfHeadOfShip(directionOfHeadOfShip);
		arwing.setSpeedOfShip(speedOfShip);
		repaint();
	}

	public void shipMovementRegulator()
	{
		double rotationDegree = Math.toRadians(directionOfHeadOfShip);
		util.shipMovementRegulator(rotationDegree, rotationDegree, moveFaster, turnRight, turnLeft, 
				slowDown, speedOfShip, speedLimitOfShip, rotationDegree, arwing);
		arwing.setMoveFaster(moveFaster);
	}
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setTransform(identity);
		g2.scale(1.25, 1);
		g2.drawImage(spaceImage, 0, 0, null);
		g2.setTransform(identity);
		arwing.paintShip(g2);
		for (int i = 0; i < asteroidList.size(); i++)
		{
			g2.setTransform(identity); // cleans up screen
			asteroidList.get(i).paintAsteroid(g2); 
			Asteroid asteroid = asteroidList.get(i);
			Area asteroidArea = new Area(asteroid.asteroidShape);
			AffineTransform asteroidAT = new AffineTransform();
			asteroidAT.setToTranslation(asteroid.asteroidXPos, asteroid.asteroidYPos);
			asteroidArea.transform(asteroidAT);
			if (util.isOffScreen(asteroid.asteroidXPos, asteroid.asteroidYPos, screenWidth, screenHeight))
				{
					asteroidList.remove(i);
					asteroidSpawner();
				}
			for (int j = 0; j < projectileList.size(); j++) //checking all bullets
			{
				AsteroidDestroyingProjectile shot = projectileList.get(j);
				Area shotArea = new Area(shot.shotShape);
				AffineTransform shotAT = new AffineTransform();
				shotAT.setToTranslation(shot.projectileXPos, shot.projectileYPos);
				shotArea.transform(shotAT);
				shotArea.intersect(asteroidArea);
				if (util.isOffScreen(shot.projectileXPos, shot.projectileYPos, screenWidth, screenHeight))
				{
					projectileList.remove(j);
				}
				if (!shotArea.isEmpty())
				{
					asteroidList.remove(i);
					projectileList.remove(j);
				}
					g2.setTransform(identity);
					shot.paintProjectile(g2);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			turnLeft = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			moveFaster = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			slowDown = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			turnRight = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			projectileList.add(new AsteroidDestroyingProjectile(arwing.shipXPos, arwing.shipYPos, directionOfHeadOfShip, speedOfShip));
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			turnLeft = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			moveFaster = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			slowDown = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			turnRight = false;
		}
	}
}
