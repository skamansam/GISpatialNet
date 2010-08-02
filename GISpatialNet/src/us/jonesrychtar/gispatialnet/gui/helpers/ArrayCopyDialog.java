package us.jonesrychtar.gispatialnet.gui.helpers;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ArrayCopyDialog extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Collection<String> fromArray, toArray;
	private JList fromList=new JList();
	private JList toList=new JList();
	private JButton OKBtn=new JButton("OK");
	private JButton CancelBtn=new JButton("Cancel");

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		ArrayList<String> f=new ArrayList<String>(0),t=new ArrayList<String>(0);
		f.add("One");f.add("2");f.add("Three");f.add("4");f.add("Five");f.add("Six");
		ArrayCopyDialog acd=new ArrayCopyDialog(f,t);
		acd.setVisible(true);
	}
	
	public ArrayCopyDialog() {
		initGUI();
	}
	public ArrayCopyDialog(Collection<String> fromArray) {
		this.fromArray=fromArray;
		initGUI();
	}
	public ArrayCopyDialog(Collection<String> fromArray,Collection<String> toArray) {
		this.fromArray=fromArray;
		this.toArray=toArray;
		initGUI();
	}
	
	private void initGUI() {
		
		this.setSize(400, 300);
		BorderLayout layout = new BorderLayout();
		//add all components
		this.getContentPane().setLayout(layout);
		
		this.add(this.fromList,BorderLayout.WEST);
		this.add(this.toList,BorderLayout.EAST);
		JPanel bottomRow=new JPanel();
		bottomRow.setLayout(new BoxLayout(bottomRow,BoxLayout.X_AXIS));
		this.add(bottomRow,BorderLayout.SOUTH);
		bottomRow.add(CancelBtn);
		bottomRow.add(OKBtn);
		
	
	}

	/**
	 * @param toArray the toArray to set
	 */
	public void setToArray(Collection<String> toArray) {
		this.toArray = toArray;
	}

	/**
	 * @return the toArray
	 */
	public Collection<String> getToArray() {
		return toArray;
	}

	/**
	 * @param fromArray the fromArray to set
	 */
	public void setFromArray(Collection<String> fromArray) {
		this.fromArray = fromArray;
	}

	/**
	 * @return the fromArray
	 */
	public Collection<String> getFromArray() {
		return fromArray;
	}
	
}
