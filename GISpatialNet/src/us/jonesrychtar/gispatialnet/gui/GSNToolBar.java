/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import us.jonesrychtar.gispatialnet.gui.GSNPanel.GSNPanel;

/**
 * @author sam
 *
 */
public class GSNToolBar extends JToolBar implements MouseListener {
	JButton exitBtn,openBtn, saveBtn;
	GSNPanel thePanel;
	GSNStatusBarInterface theStatus;
	public GSNToolBar(){
		//add toolbar buttons
		exitBtn = util.addToolBarButton(this,"Exit","Exit program","Stop","exit");
		saveBtn = util.addToolBarButton(this,"Save","Save data","Save","save");
		openBtn = util.addToolBarButton(this,"Open","Open a file","Open","open");
	}
	/**Since this toolbar works with a GSNPanel, we need a reference to it. 
	 * The GSNPanel is the Panel that handles the ActionEvents for 
	 * the buttons. 
	 * @param gsp the GSNPanel which we wish to control 
	 */
	public void setActionPanel(GSNPanel gsp){
		this.thePanel=gsp;
		registerPanel();
	}
	private void registerPanel(){
		exitBtn.addActionListener(thePanel);
		exitBtn.addMouseListener(this);
		openBtn.addActionListener(thePanel);
		saveBtn.addActionListener(thePanel);
		
	}

	public void setStatusBar(GSNStatusBarInterface theStatus) {
		this.theStatus=theStatus;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		JButton theItem = (JButton)e.getSource();
		theStatus.setStatus(theItem.getToolTipText());
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {
		//theStatus.setStatus("");
		}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

}
