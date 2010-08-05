package us.jonesrychtar.gispatialnet.gui.helpers;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.freedesktop.tango.icons.IconFactory;

public class ArrayCopyDialog extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Collection<String> fromArray, toArray;
	private JList fromList=new JList();
	private JList toList=new JList();
	private JButton moveToBtn=new JButton("",IconFactory.getActionIcon("go-next"));
	private JButton moveFromBtn=new JButton("",IconFactory.getActionIcon("go-previous"));
	private JButton moveUpBtn=new JButton("",IconFactory.getActionIcon("go-up"));
	private JButton moveDownBtn=new JButton("",IconFactory.getActionIcon("go-down"));
	private JButton moveTopBtn=new JButton("",IconFactory.getActionIcon("go-top"));
	private JButton moveBottomBtn=new JButton("",IconFactory.getActionIcon("go-bottom"));
	private JButton OKBtn=new JButton("OK",IconFactory.getActionIcon("edit-redo"));
	private JButton CancelBtn=new JButton("Cancel",IconFactory.getActionIcon("process-stop"));
	private JPanel topRow=new JPanel();
	private JPanel buttonBox=new JPanel();
	private JPanel bottomRow=new JPanel();
	private JPanel fromPane=new JPanel();
	private JPanel toPane=new JPanel();
	private JPanel buttonPane=new JPanel();

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		ArrayList<String> f=new ArrayList<String>(0),
						  t=new ArrayList<String>(0);
		for(int i =0;i<100;i++)
			f.add(new Integer(i).toString());
/*		f.add("2");
		f.add("Three");
		f.add("4");
		f.add("Five");
		f.add("Six");*/
		
		ArrayCopyDialog acd=new ArrayCopyDialog(f,t);
		//acd.setVisible(true);
	}
	
	public ArrayCopyDialog() {
		initGUI();
	}
	public ArrayCopyDialog(Collection<String> fromArray) {
		this.fromArray=fromArray;
		this.fromList=new JList(fromArray.toArray());
		initGUI();
	}
	public ArrayCopyDialog(Collection<String> fromArray,Collection<String> toArray) {
		this.fromArray=fromArray;
		this.toArray=toArray;
		this.fromList=new JList(fromArray.toArray());
		this.toList=new JList(toArray.toArray());
		initGUI();
	}
	
	private void initGUI() {
		this.setTitle("Choose which items you want:");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setBounds(100, 100, 620, 400);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		this.add(p);

		//main window
		this.topRow.setBounds(0,0,720,500-60);
		p.add(this.topRow);
		p.add(this.bottomRow);
		
		//top pane
		this.fromPane.setBounds(0,0,300,150);
		this.toPane.setBounds(0,0,300,150);
		this.buttonPane.setBounds(0,0,620,60);
		this.topRow.add(this.fromPane);
		this.topRow.add(this.buttonPane);
		this.topRow.add(this.toPane);
		
		//bottom pane
		this.bottomRow.add(this.CancelBtn);
		this.bottomRow.add(this.OKBtn);
		
		//from pane
		this.fromList.setBounds(0,0,620,60);
		fromPane.add(new JScrollPane(fromList));

		//button pane
		this.buttonPane.setLayout(new BoxLayout(buttonPane,BoxLayout.Y_AXIS));
		this.moveBottomBtn.setContentAreaFilled(false);
		this.moveBottomBtn.setBorderPainted(false);
		this.moveDownBtn.setContentAreaFilled(false);
		this.moveDownBtn.setBorderPainted(false);
		this.moveFromBtn.setContentAreaFilled(false);
		this.moveFromBtn.setBorderPainted(false);
		this.moveToBtn.setContentAreaFilled(false);
		this.moveToBtn.setBorderPainted(false);
		this.moveTopBtn.setContentAreaFilled(false);
		this.moveTopBtn.setBorderPainted(false);
		this.moveUpBtn.setContentAreaFilled(false);
		this.moveUpBtn.setBorderPainted(false);
		this.buttonPane.add(this.moveTopBtn);
		this.buttonPane.add(this.moveUpBtn);
		this.buttonPane.add(this.moveToBtn);
		this.buttonPane.add(this.moveFromBtn);
		this.buttonPane.add(this.moveDownBtn);
		this.buttonPane.add(this.moveBottomBtn);
		//to pane
		toPane.add(new JScrollPane(toList));

		
		this.setModal(true);
		this.setVisible(true);
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
