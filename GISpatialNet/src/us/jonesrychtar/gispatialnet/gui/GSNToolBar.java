/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui;

import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * @author sam
 *
 */
public class GSNToolBar extends JToolBar {
	JButton exitBtn,openBtn, saveBtn;
	
	public GSNToolBar(){
		exitBtn=new JButton("Exit");
		exitBtn.setIcon(util.getGeneralIcon("Delete"));
		this.add(exitBtn);
	}
}
