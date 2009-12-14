/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import us.jonesrychtar.gispatialnet.gui.GSNPanel.GSNPanel;

/**
 * @author sam
 * 
 */
public class GSNFrame extends JFrame implements ActionListener, WindowListener {
	private GSNMenuBar theMenu;
	private GSNToolBar theToolBar;
	private GSNPanel thePanel;
	private GSNStatusBar theStatus;

	/**
	 * @param title
	 */
	public GSNFrame() {

		this.setTitle("GISpatialNet");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		this.setBounds(100, 100, 500, 500);
		JPanel innerFrame=new JPanel();
		innerFrame.setLayout(new BorderLayout());
		//innerFrame.setLayout(new BoxLayout(innerFrame,BoxLayout.Y_AXIS));
		this.add(innerFrame);
		
		theMenu = new GSNMenuBar();
		theToolBar = new GSNToolBar();
		thePanel = new GSNPanel();
		theStatus = new GSNStatusBar();

		//tell the menu and toolbar they will be operating on the GSNPanel
		theMenu.setGSNPanel(thePanel);
		theToolBar.setGSNPanel(thePanel);
		
		//tell the other components they will be working with the status bar
		theMenu.setStatusBar(theStatus);
		theToolBar.setStatusBar(theStatus);
		thePanel.setStatusBar(theStatus);

		
		// add menu
		this.setJMenuBar(theMenu);
		// add toolbar
		innerFrame.add(theToolBar,BorderLayout.PAGE_START);
		// add main frame
		innerFrame.add(thePanel,BorderLayout.CENTER);
		// add statusbar
		innerFrame.add(theStatus,BorderLayout.PAGE_END);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean hasSetUI = false;
		try {
			UIManager.setLookAndFeel("org.fife.plaf.Office2003.Office2003LookAndFeel");
			hasSetUI=true;
		} catch (UnsupportedLookAndFeelException e) {
			System.err.println("Office 2003 LnF is Unsupported on your platform");
		} catch (ClassNotFoundException e) {
			System.err.println("Office 2003 LnF cannot be found");
		} catch (InstantiationException e) {
			System.err.println("Office 2003 LnF cannot be instatntiated");
		} catch (IllegalAccessException e) {
			System.err.println("Office 2003 LnF Error: Illegal Access");
		}
		
		if(!hasSetUI){
			try {
				System.err.println("Setting LnF to "+UIManager.getSystemLookAndFeelClassName());
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				hasSetUI=true;
			} catch (UnsupportedLookAndFeelException e) {
				System.err.println("System LnF is Unsupported on your platform");
			} catch (ClassNotFoundException e) {
				System.err.println("System LnF cannot be found");
			} catch (InstantiationException e) {
				System.err.println("System LnF cannot be instatntiated");
			} catch (IllegalAccessException e) {
				System.err.println("System LnF Error: Illegal Access");
			}
		}

		GSNFrame t = new GSNFrame();
		t.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.thePanel.onClose();
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}