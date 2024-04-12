package ch.correvon.clicker.window.mainWindow;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import de.ksquared.system.keyboard.GlobalKeyListener;
import de.ksquared.system.keyboard.KeyEvent;
import de.ksquared.system.keyboard.KeyListener;

public class MainWindow extends JFrame implements KeyListener
{
	public MainWindow()
	{
		super("Title");
		
		Dimension windowSize = new Dimension(375, 100);
		super.setSize(windowSize.width, windowSize.height);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension mainScreenSize = tk.getScreenSize();
		super.setLocation((mainScreenSize.width - windowSize.width)/2, (mainScreenSize.height - windowSize.height)/2);
		
		if(tk.isAlwaysOnTopSupported())
			super.setAlwaysOnTop(true);

		try
		{
			this.robot = new Robot();
		}
		catch(AWTException e)
		{
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		this.thread = null;

		JPanel panel = new JPanel();
		this.buttonClick = new JButton("Start (F1)");
		this.buttonClick.addActionListener(new ActionListener()
			{
				@Override public void actionPerformed(ActionEvent e)
				{
					start();
				}
			});

		JButton buttonStop = new JButton("Stop (F2)");
		buttonStop.addActionListener(new ActionListener()
			{
				@Override public void actionPerformed(ActionEvent e)
				{
					stop();
				}
			});

		JButton buttonExit = new JButton("Exit (F3/Esc)");
		buttonExit.addActionListener(new ActionListener()
			{
				@Override public void actionPerformed(ActionEvent e)
				{
					exit();
				}
			});
		
		this.spinNbClick = new JSpinner();
		this.spinNbClick.setModel(new SpinnerNumberModel(-1, -1, 9999, 1));

		this.spinWaitingTime = new JSpinner();
		this.spinWaitingTime.setModel(new SpinnerNumberModel(1, 1, 9999, 1));

		panel.add(this.buttonClick);
		panel.add(buttonStop);
		panel.add(buttonExit);

		panel.add(new JLabel("Nb click max"));
		panel.add(this.spinNbClick);

		panel.add(new JLabel("Temps entre click"));
		panel.add(this.spinWaitingTime);
		panel.add(new JLabel("ms"));

		new GlobalKeyListener().addKeyListener(this);

		super.setContentPane(panel);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setVisible(false);
	}

	public void run()
	{
		super.setVisible(true);
	}

	private void stop()
	{
		System.out.println("stop");
		if(this.thread != null)
			this.thread.exit();
		this.buttonClick.setEnabled(true);
	}

	private void start()
	{
		System.out.println("start");
		if(this.thread != null)
			this.thread.exit();
		this.buttonClick.setEnabled(false);
		this.thread = new ClickThread(this.robot, 100, (Integer)(this.spinNbClick.getValue()), (Integer)(this.spinWaitingTime.getValue()));
		this.thread.start();
	}

	private void exit()
	{
		stop();
		System.out.println("exit");
		MainWindow.this.dispose();
	}

	@Override public void keyReleased(KeyEvent e){}
	@Override public void keyPressed(KeyEvent e)
	{
//		boolean consume = true;
		switch(e.getVirtualKeyCode())
		{
			case KeyEvent.VK_F1 : this.start(); break;
			case KeyEvent.VK_F2 : this.stop(); break;
			case KeyEvent.VK_F3 : 
			case KeyEvent.VK_ESCAPE : this.exit(); break;
//			default : consume = false; break;
		}

//		if(consume)
//		{
//			On ne peut pas consummer un KeyEvent de chez ksquared O_o
//		}
	}

	private ClickThread thread;
	private JButton buttonClick;
	private Robot robot;
	private JSpinner spinNbClick;
	private JSpinner spinWaitingTime;
}