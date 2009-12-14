/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui;

import javax.swing.JButton;
import javax.swing.JToolBar;

import us.jonesrychtar.gispatialnet.gui.GSNPanel.GSNPanel;

/**
 * @author sam
 *
 */
public class GSNToolBar extends JToolBar {
	JButton exitBtn,openBtn, saveBtn;
	GSNPanel thePanel;
	GSNStatusBarInterface theStatus;
	public GSNToolBar(){
		exitBtn=createButton("Exit","Delete");
		exitBtn.setActionCommand("exit");
		this.add(exitBtn);
		saveBtn=createButton("Save","Save");
		saveBtn.setActionCommand("save");
		this.add(saveBtn);
		openBtn=createButton("Open","Folder");
		openBtn.setActionCommand("open");
		this.add(openBtn);
	}
	private JButton createButton(String label, String icon){
		JButton tmp = new JButton(label);
		tmp.setIcon(util.getGeneralIcon(icon));
		tmp.setVerticalTextPosition(JButton.BOTTOM);
		tmp.setHorizontalTextPosition(JButton.CENTER);
		return tmp;
	}
	public void setGSNPanel(GSNPanel gsp){
		this.thePanel=gsp;
		registerPanel();
	}
	private void registerPanel(){
		exitBtn.addActionListener(thePanel);
		openBtn.addActionListener(thePanel);
		saveBtn.addActionListener(thePanel);
		
	}

	public void setStatusBar(GSNStatusBarInterface theStatus) {
		this.theStatus=theStatus;
	}
}
