package us.jonesrychtar.gispatialnet.gui.helpers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ArrayCopyDialog extends javax.swing.JDialog {

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				ArrayCopyDialog inst = new ArrayCopyDialog(frame);
				inst.setVisible(true);
			}
		});
	}
	
	public ArrayCopyDialog(JFrame frame) {
		super(frame);
		initGUI();
	}
	
	private void initGUI() {
		try {
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
