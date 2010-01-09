/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author sam
 * 
 */
public class CSVOptionsFrame extends JDialog implements ActionListener,
		ChangeListener, KeyListener {
	int dsType = 0, matrixType = 0, rows = 24, cols = 4;
	char separator = ',';
	boolean isPolar = false;
	boolean hasCancelled = false;
	JComboBox dst = new JComboBox(new String[] {
			"Coordinate data with attributes", "Adjacency data",
			"Coordinate data only", "Attribute data" });
	JComboBox mt = new JComboBox(new String[] { "Full", "Upper", "Lower" });
	JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(25, 0, 999999, 1));
	JSpinner colSpinner = new JSpinner(new SpinnerNumberModel(4, 0, 999999, 1));
	JComboBox coord = new JComboBox(new String[] { "X,Y",
			"Polar (decimal and degrees)" });
	JComboBox sep = new JComboBox(new String[] { "Comma",
	"Tab","Space" });
	String FName = "[not specified]";
	
	public CSVOptionsFrame(String filename) {
		this.FName=filename;
		this.setupGUI();
	}
	
	public CSVOptionsFrame() {
		this.setupGUI();
	}
	
	private void setupGUI(){
		this.setTitle("CSV Import Options");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setBounds(100, 100, 500, 350);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		this.add(p);

		p.add(new JLabel("File: "+FName));
		
		dst.addActionListener(this);
		JLabel dstl = new JLabel("Data to read: ");
		JPanel r1 = new JPanel();
		r1.add(dstl);
		r1.add(dst);
		p.add(r1);
		mt.addActionListener(this);
		JLabel mtl = new JLabel("Type of Matrix: ");
		JPanel r2 = new JPanel();
		r2.add(mtl);
		r2.add(mt);
		p.add(r2);

		rowSpinner.addChangeListener(this);
		JLabel rl = new JLabel("Rows to read: ");
		JPanel r3 = new JPanel();
		r3.add(rl);
		r3.add(rowSpinner);
		p.add(r3);

		colSpinner.addChangeListener(this);
		JLabel cl = new JLabel("Columns to read (split every X rows): ");
		JPanel r4 = new JPanel();
		r4.add(cl);
		r4.add(colSpinner);
		p.add(r4);

		coord.addActionListener(this);
		JLabel coordl = new JLabel("Coordinate System: ");
		JPanel r6 = new JPanel();
		r6.add(coordl);
		r6.add(coord);
		p.add(r6);

		sep.addActionListener(this);
		sep.addKeyListener(this);
		sep.setEditable(true);
		JLabel sepl = new JLabel("Field Delimiter: ");
		JPanel r7 = new JPanel();
		r7.add(sepl);
		r7.add(sep);
		p.add(r7);
		
		p.add(new JSeparator());

		// TODO: add cancel/ok buttons
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setActionCommand("cancel");
		JButton ok = new JButton("Import");
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		JPanel r5 = new JPanel();
		r5.add(cancel);
		r5.add(ok);
		p.add(r5);

		//this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CSVOptionsFrame f = new CSVOptionsFrame();
		System.out.println("\n=========\n");
		f.printInfo();
		f.dispose();
	}
	public void printInfo(){
		System.out.println("DSType: " + getDataSetType());
		System.out.println("MatrixType: " + getMatrixType());
		System.out.println("Rows: " + getRows());
		System.out.println("Columns: " + getColumns());
		System.out.println("Separator: '" + getSeparator()+"'");		
	}
	// ["Node data (nodes with attributes)", "Adjacency data", "Node data only",
	// "Attribute data"]
	public String getDataSetType() {
		switch (dsType) {
		case 0:
			return "node_attb";
		case 1:
			return "adj";
		case 2:
			return "node";
		case 3:
			return "attb";
		default:
			return "node_attb";
		}
	}

	public int getDataSetTypeAsInt() {
		return dsType;
	}

	// {Full, Upper, Lower}
	public String getMatrixType() {
		switch (matrixType) {
		case 0:
			return "full";
		case 1:
			return "upper";
		case 2:
			return "lower";
		default:
			return "full";
		}
	}

	public int getMatrixTypeAsInt() {
		return matrixType;
	}

	public String getRows() {
		return new Integer(rows).toString();
	}

	public int getRowsAsInt() {
		return rows;
	}

	public String getColumns() {
		return new Integer(cols).toString();
	}

	public int getColumnsAsInt() {
		return cols;
	}

	public char getSeparator() {
		return separator;
	}

	public String getSeparatorAsString() {
		return new String("" + separator);
	}

	public int getIsPolarAsInt() {
		return this.isPolar ? 1 : 0;
	}

	public boolean getIsPolar() {
		return this.isPolar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			this.hasCancelled = true;
			this.setVisible(false);
		}
		if (e.getActionCommand().equals("ok")) {
			this.hasCancelled = false;
			this.setVisible(false);
		}

		if(e.getSource().equals(dst))
			this.dsType = dst.getSelectedIndex();
		if(e.getSource().equals(mt))
			this.matrixType = mt.getSelectedIndex();
		if(e.getSource().equals(coord))
			this.isPolar = coord.getSelectedIndex()==0?false:true;
		if(e.getSource().equals(sep)){
			String sepVal = (String)(sep.getEditor().getItem());
			if(sepVal.equals("Tab"))
				this.separator = '\u0009';
			else if(sepVal.equals("Space"))
				this.separator = ' ';
			else if(sepVal.equals("Comma"))
				this.separator = ',';
			else
				this.separator = sepVal.charAt(0);
		}
		//printInfo();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(rowSpinner))
			this.rows = new Integer(rowSpinner.getValue().toString());
		if(e.getSource().equals(colSpinner))
			this.cols = new Integer(colSpinner.getValue().toString());
		//printInfo();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getSource().equals(sep)){
			String sepVal = (String)(sep.getEditor().getItem());
			if(sepVal.equals("Tab"))
				this.separator = '\u0009';
			else if(sepVal.equals("Space"))
				this.separator = ' ';
			else if(sepVal.equals("Comma"))
				this.separator = ',';
			else
				this.separator = sepVal.charAt(0);
		}
		//printInfo();
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
