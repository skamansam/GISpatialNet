package us.jonesrychtar.gispatialnet.gui.helpers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */

public class FieldSelectionDialog extends javax.swing.JDialog {
	private JList theList;
	private JPanel bottomPanel;
	private JButton okButton;
	private JPanel topPanel;
	private JButton cancelButton;
	/**
	 * Auto-generated main method to display this JDialog
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				FieldSelectionDialog inst = new FieldSelectionDialog(frame);
				inst.setVisible(true);
			}
		});
	}

	public FieldSelectionDialog(JFrame frame,ArrayList<Object> theData) {
		super(frame);
		initGUI();
	}

	private void initGUI() {
		try {
			BoxLayout thisLayout = new BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS);
			getContentPane().setLayout(thisLayout);
			{
				topPanel = new JPanel();
				getContentPane().add(topPanel);
				{
					ListModel theListModel = 
						new DefaultComboBoxModel(
								new String[] { "Item One", "Item Two" });
					theList = new JList();
					BoxLayout theListLayout = new BoxLayout(theList, javax.swing.BoxLayout.X_AXIS);
					theList.setLayout(theListLayout);
					topPanel.add(getTheList());
					theList.setModel(theListModel);
					theList.setPreferredSize(new java.awt.Dimension(316, 311));
				}
			}
			{
				bottomPanel = new JPanel();
				BoxLayout jPanel1Layout = new BoxLayout(bottomPanel, javax.swing.BoxLayout.X_AXIS);
				bottomPanel.setLayout(jPanel1Layout);
				getContentPane().add(bottomPanel);
				{
					cancelButton = new JButton();
					bottomPanel.add(cancelButton);
					cancelButton.setText("Cancel");
				}
				{
					okButton = new JButton();
					bottomPanel.add(okButton);
					okButton.setText("OK");
				}
			}
			this.setSize(358, 384);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JList getTheList() {
		return theList;
	}

}
