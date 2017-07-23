import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.css.Rect;

public class PowerUp
{
	private int powerUpXPos;
	private int powerUpYPos; // make the power up move to the right, and have
							// collision detection with the ship
	private double powerUpCourse;
	private double powerUpRotation;
	Rectangle2D.Double powerUpShape;
	AffineTransform powerUpTransform = new AffineTransform(); // Unity Transform
																// (scale = 1,
																// rotation = 0,
																// x = 0, y = 0)

	public PowerUp(int powerUpXPos, int powerUpYPos, double course, double speed, double rotation, boolean isTouchingShip, boolean isTouchingLaser)
	{
		this.powerUpXPos = powerUpXPos;
		this.powerUpYPos = powerUpYPos;
		this.powerUpCourse = course;
		this.powerUpRotation = rotation;
		powerUpShape = new Rectangle2D.Double(powerUpXPos, powerUpYPos, 40, 40);
	}
	public void paintPowerUp(Graphics2D g2)
	{
		g2.setTransform(powerUpTransform);
		g2.setColor(Color.white);
//		g2.translate(powerUpXPos, powerUpYPos);
		g2.fill(powerUpShape);
		g2.translate(powerUpXPos, powerUpYPos);

	}
}
