package ch.correvon.clicker.window.mainWindow;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class ClickThread extends Thread
{
	public ClickThread(Robot robot)
	{
		this(robot, 0, -1, 1);
	}

	public ClickThread(Robot robot, int initialWaiting, double nbClick, int waitingTime)
	{
		this.robot = robot;
		this.initialWaiting = initialWaiting;
		this.nbClick = nbClick;
		this.waitingTime = waitingTime;

		this.exit = false;
	}

	@Override public void run()
	{
		double currentNbClick = 0;
		try
		{
			System.out.println("initial waiting (" + this.initialWaiting + " ms)");
			super.sleep(this.initialWaiting);
			System.out.println("go !");
			System.out.print("\ncliking...");

			while(!this.exit)
			{
				if(this.nbClick > 0)
				{
					currentNbClick++;
					if(currentNbClick >= this.nbClick)
					{
						this.exit = true;
						System.out.println("Limite de click atteint : " + this.nbClick);
					}
				}

				this.robot.mousePress(InputEvent.BUTTON1_MASK);
				this.robot.delay(5);
				this.robot.mouseRelease(InputEvent.BUTTON1_MASK);
				System.out.print(".");
				super.sleep(this.waitingTime);
			}
			System.out.println("\nend clicking");
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void exit()
	{
		this.exit = true;
	}

	private Robot robot;
	private int initialWaiting;
	private boolean exit;
	private double nbClick;
	private int waitingTime;
}
