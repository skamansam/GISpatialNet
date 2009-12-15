/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.GSNPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import us.jonesrychtar.gispatialnet.gui.GSNStatusBarInterface;

/**
 * @author sam
 *
 */
public class GSNPanel extends JPanel implements ActionListener{
	private boolean isTainted = false;
	private DataLister theList=new DataLister();
	private DataDisplayPanel theDisplay = new DataDisplayPanel();
	GSNStatusBarInterface theStatus;

	public GSNPanel(){
		JScrollPane leftPane = new JScrollPane();
		JScrollPane rightPane = new JScrollPane();
		leftPane.setMinimumSize(new Dimension(100,200));
		leftPane.add(theList);
		rightPane.setMinimumSize(new Dimension(100,200));
		rightPane.add(theDisplay);
		JSplitPane thePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane,rightPane);
		this.add(thePane);
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action on GSNPanel: "+e.getActionCommand());
		//exit has been issued
		if (e.getActionCommand() == "exit"){
				onClose();
		}
		
	}

	public void saveAllData(){
		JOptionPane.showMessageDialog(this, "Save All has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
		this.isTainted=false;
	}

	public void onClose() {
		if(isTainted && JOptionPane.showConfirmDialog(this,"Do you wish to save first?","Data has been edited, but not saved.",JOptionPane.YES_NO_OPTION)==0){
			this.saveAllData();
		}else{
			//this.getRootPane().getParent().
			System.exit(0);
		}
		
	}
	public void setStatusBar(GSNStatusBarInterface sb){this.theStatus=sb;}

}
