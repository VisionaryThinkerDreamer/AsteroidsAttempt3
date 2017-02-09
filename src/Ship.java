import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.Timer;

public class Ship implements ActionListener
{
	private int[] leftSideShipXPoints =
	{ 0, -16, -32, -16, -10, 0 };
	private int[] leftSideShipYPoints =
	{ -31, 9, 21, 18, 14, 14 };
	private int[] rightSideShipXPoints =
	{ 0, 10, 16, 32, 16, 0 };
	private int[] rightSideShipYPoints =
	{ 14, 14, 18, 21, 9, -31 };
	private int[] canopyXPoints =
	{ 7, 0, -7 };
	private int[] canopyYPoints =
	{ 6, -9, 6 };
	public int shipXPos;
	public int shipYPos;
	private int deltaX;
	private int deltaY;
	public int directionOfHeadOfShip = 90; // degrees
	public int colorChangeController;
	public int colorChangerRightSide;
	public int colorChangerLeftSide;
	private int speedOfShip = 0;
	private int speedLimitOfShip = 15;
	public Polygon shipLeftSide;
	public Polygon shipRightSide;
	public Polygon canopy;
	private int screenWidth;
	private int screenHeight;
	private int shipWreckHeight = 100;
	private int shipWreckWidth = 100;
	public Ellipse2D.Double shipWreck = new Ellipse2D.Double(
			shipXPos - shipWreckWidth / 2, shipYPos - shipWreckHeight / 2,
			shipWreckWidth, shipWreckHeight);
	private boolean shipDestroyed = false;
	private Timer ticker = new Timer(100, this);
	private Graphics2D g2;

	public Ship(int shipXPos, int shipYPos, int screenWidth, int screenHeight) // ship
	// constructor
	{
		this.shipXPos = shipXPos;
		this.setShipYPos(shipYPos);
		this.shipLeftSide = new Polygon(leftSideShipXPoints,
				leftSideShipYPoints, leftSideShipXPoints.length);
		this.shipRightSide = new Polygon(rightSideShipXPoints,
				rightSideShipYPoints, rightSideShipXPoints.length);
		this.canopy = new Polygon(canopyXPoints, canopyYPoints,
				canopyXPoints.length);
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		ticker.start();
	}

	public void paintShip(Graphics2D g2)
	{
		this.g2 = g2;
		// TODO: Figure out how to change sun position to top right
		colorChangerRightSide = Math.abs((int) directionOfHeadOfShip - 180);
		if (colorChangerRightSide > 180)
		{
			colorChangerRightSide = 360 - directionOfHeadOfShip;
		}
		colorChangerLeftSide = Math.abs((int) directionOfHeadOfShip);
		if (colorChangerLeftSide > 180)
		{
			colorChangerLeftSide = 360 - directionOfHeadOfShip;
		}
		Utilities.convertCourseSpeedToDxDy(directionOfHeadOfShip, speedOfShip);
		deltaX = Utilities.getDeltaX();
		deltaY = Utilities.getDeltaY();
		shipXPos = (int) (shipXPos + deltaX);
		setShipYPos((int) (getShipYPos() + deltaY));
		g2.translate(shipXPos, getShipYPos());
		g2.rotate(Math.toRadians(-directionOfHeadOfShip + 90));
		g2.setColor(new Color(Math.abs(colorChangerRightSide % 255),
				Math.abs(colorChangerRightSide % 255),
				Math.abs(colorChangerRightSide % 255)));
		g2.fill(shipLeftSide);
		g2.setColor(new Color(Math.abs(colorChangerLeftSide % 255),
				Math.abs(colorChangerLeftSide % 255),
				Math.abs(colorChangerLeftSide % 255)));
		g2.fill(shipRightSide);
		g2.setColor(Color.BLUE);
		g2.fill(canopy);
		g2.setColor(Color.black);
		if (getShipYPos() > screenHeight)
		{
			setShipYPos(0);
		}
		if (getShipYPos() < -20)
		{
			setShipYPos(screenHeight);
		}
		if (shipXPos > screenWidth + 20)
		{
			shipXPos = -20;
		}
		if (shipXPos < -20)
		{
			shipXPos = screenWidth + 20;
		}

	}

	public void setScreenWidth(int screenWidth)
	{
		this.screenWidth = screenWidth;
	}

	public void setScreenHeight(int screenHeight)
	{
		this.screenHeight = screenHeight;
	}

	public void setSpeedOfShip(int speedOfShip)// How fast the ship is going
	{
		this.speedOfShip = speedOfShip;
	}

	public void setDirectionOfHeadOfShip(double direction)
	{
		directionOfHeadOfShip = (int) direction;
	}

	public int getSpeedOfShip()
	{
		return speedOfShip;
	}

	public int getSpeedLimitOfShip()
	{
		return speedLimitOfShip;
	}

	public int getShipXPos()
	{
		return shipXPos;
	}

	public int getShipYPos()
	{
		return shipYPos;
	}

	public void setShipYPos(int shipYPos)
	{
		this.shipYPos = shipYPos;
	}

	public void setShipXPos(int shipXPos)
	{
		this.shipXPos = shipXPos;
	}

	public void setShipDestroyed(boolean shipDestroyed)
	{
		this.shipDestroyed = shipDestroyed;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (shipDestroyed == true)
		{
			g2.setColor(Color.red);
			g2.fillRect(500, 500, 200, 200);
			for (int i = 0; i < 200; i++)
			{
				g2.fill(shipWreck);
				shipWreck.width = 1000;
				shipWreck.height = 1000;
				shipWreck.x = shipXPos;
				shipWreck.y = shipYPos;
				speedOfShip = 0;
			}
		}
	}
}