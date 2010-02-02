/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
//import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ujmp.core.Matrix;
import org.ujmp.core.stringmatrix.impl.CSVMatrix;

/**
 * @author sam
 * 
 */
public class CSVOptionsFrame extends JDialog implements ActionListener,
		ChangeListener, KeyListener, WindowFocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2749991861366815222L;
	int dsType = 0, matrixType = 0, rows = 24, cols = 4;
	char separator = ',';
	boolean isPolar = false;
	boolean hasCancelled = false;

	JPanel seprow = new JPanel();
	JLabel seplbl = new JLabel("Field Delimiter: ");
	JComboBox sep = new JComboBox(new String[] { "Comma","Tab","Space" });
	
	JPanel hasheaderrow = new JPanel();
	JCheckBox hasHeader = new JCheckBox("First row is a column header row.");

	JPanel colselrow = new JPanel();
	JLabel colSelectLbl = new JLabel("Split Based On:");
	JComboBox columnList = new JComboBox(new String[] { "Please select delimiter first."});

	JPanel useheuristicrow = new JPanel();
	JCheckBox useHeuristic = new JCheckBox("Import using heuristics.");

	private void setHeuristicControlVisibility(boolean b){
		dstlbl.setEnabled(b);
		dst.setEnabled(b);
		mtlbl.setEnabled(b);
		mt.setEnabled(b);
		numrowslbl.setEnabled(b);
		rowSpinner.setEnabled(b);
		numcolslbl.setEnabled(b);
		colSpinner.setEnabled(b);
		coordtypelbl.setEnabled(b);
		coord.setEnabled(b);
	}

	private void fitWindow(){
		int h = this.getInsets().bottom+this.getInsets().top;
		h+=seprow.getHeight();
		h+=hasheaderrow.getHeight();
		h+=colselrow.getHeight();
		h+=useheuristicrow.getHeight();
		h+=dstrow.getHeight();
		h+=mtrow.getHeight();
		h+=numrowsrow.getHeight();
		h+=numcolsrow.getHeight();
		h+=coordtyperow.getHeight();
		h+=btnsep.getHeight();
		h+=btnrow.getHeight()+20;
		//System.err.println("window control height "+h);
		if(this.getInsets().bottom-this.getInsets().top<h)
			this.setBounds(this.getX(), this.getY(), this.getWidth(), h);

	}
	JPanel dstrow = new JPanel();
	JLabel dstlbl = new JLabel("Data to read: ");
	JComboBox dst = new JComboBox(new String[] {
			"Coordinate data with attributes", "Adjacency data",
			"Coordinate data only", "Attribute data" });

	JPanel mtrow = new JPanel();
	JLabel mtlbl = new JLabel("Type of Matrix: ");
	JComboBox mt = new JComboBox(new String[] { "Full", "Upper", "Lower" });

	JPanel numrowsrow = new JPanel();
	JLabel numrowslbl = new JLabel("Rows to read: ");
	JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(25, 0, 999999, 1));

	JPanel numcolsrow = new JPanel();
	JLabel numcolslbl = new JLabel("Columns to read (split every X rows): ");
	JSpinner colSpinner = new JSpinner(new SpinnerNumberModel(4, 0, 999999, 1));

	JPanel coordtyperow = new JPanel();
	JLabel coordtypelbl = new JLabel("Coordinate System: ");
	JComboBox coord = new JComboBox(new String[] { "X,Y", "Polar (decimal and degrees)" });

	JSeparator btnsep = new JSeparator();
	JPanel btnrow = new JPanel();

	String FName = "[not specified]";
	int splitBy;
	ArrayList<Object> noHeuristicCtrlGroup=new ArrayList<Object>();
	
	public CSVOptionsFrame(String filename) {
		this.FName=filename;
		this.setupGUI();
	}
	
	public CSVOptionsFrame() {
		this.setupGUI();
	}

	public boolean userCancelled(){return this.hasCancelled;}
	
	private void setupGUI(){
		this.setTitle("CSV Import Options");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setBounds(100, 100, 500, 350);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		this.add(p);

		p.add(new JLabel("File: "+FName));
		
		//field delimiter row
		sep.addActionListener(this);
		sep.addKeyListener(this);
		sep.setEditable(true);
		seprow.add(seplbl);
		seprow.add(sep);
		p.add(seprow);

		//has header? row
		hasHeader.addActionListener(this);
		hasHeader.setSelected(true);
		hasheaderrow.add(hasHeader);
		p.add(hasheaderrow);

		//column list
		columnList.addActionListener(this);
		columnList.setEditable(false);
		colselrow.add(colSelectLbl);
		colselrow.add(columnList);
		p.add(colselrow);

		//use heuristic? row
		useHeuristic.addActionListener(this);
		useheuristicrow.add(useHeuristic);
		p.add(useheuristicrow);

		//DataSet type
		dst.addActionListener(this);
		dstrow.add(dstlbl);
		dstrow.add(dst);
		p.add(dstrow);
		
		//matrix type [full, half, etc.]
		mt.addActionListener(this);
		mtrow.add(mtlbl);
		mtrow.add(mt);
		p.add(mtrow);

		//number of rows to read
		rowSpinner.addChangeListener(this);
		numrowsrow.add(numrowslbl);
		numrowsrow.add(rowSpinner);
		p.add(numrowsrow);

		//number of columns to read
		colSpinner.addChangeListener(this);
		numcolsrow.add(numcolslbl);
		numcolsrow.add(colSpinner);
		p.add(numcolsrow);

		//coordinate type
		coord.addActionListener(this);
		coordtyperow.add(coordtypelbl);
		coordtyperow.add(coord);
		p.add(coordtyperow);

		
		p.add(btnsep);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setActionCommand("cancel");
		JButton ok = new JButton("Import");
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		btnrow.add(cancel);
		btnrow.add(ok);
		p.add(btnrow);

		populateColumnList(",");

		this.addWindowFocusListener(this);
		//this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setVisible(true);
		
	}

	/**
	 * @param args
	 */
/*	public static void main(String[] args) {
		CSVOptionsFrame f = new CSVOptionsFrame();
		System.out.println("\n=========\n");
		f.printInfo();
		f.dispose();
	}
*/
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
	public boolean getHasHeader() {
		return this.hasHeader.isSelected();
	}
	public int getSortByColumn() {
		return this.splitBy;
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
		if(e.getSource().equals(columnList))
			this.splitBy=columnList.getSelectedIndex()>0?columnList.getSelectedIndex()-1:-1;
		if(/*e.getSource().equals(columnList) || */e.getSource().equals(hasHeader)){
			populateColumnList(""+separator);
		}
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
		if(e.getSource().equals(useHeuristic)){
			setHeuristicControlVisibility(!useHeuristic.isSelected());
		}
		//printInfo();
	}


	private void populateColumnList(String seperator) {
		//TODO: get matrix with UJMP and parse headers
		try {
			Matrix m = new CSVMatrix(this.FName,new String(seperator));
			columnList.removeAllItems();
			columnList.addItem("[Use Row number]");
			//System.out.println("This stream has "+m.getColumnCount()+" columns with "+(hasHeader.isSelected()?"":"no ")+"header using "+seperator+" seperator.");
			for(int i=0;i<m.getColumnCount();i++){
				String theLabel = m.getAsString(0,i);
				if(hasHeader.isSelected())
					columnList.addItem(theLabel);
				else
					columnList.addItem(""+i+" ["+theLabel+"]");
				System.out.print(""+theLabel+" ");
			}
			System.out.print("\n");
		} catch (IOException e) {
			System.out.println("Error parsing "+this.FName+" with "+(hasHeader.isSelected()?"":"no ")+"header using "+seperator+" seperator.");
		}
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

	@Override
	public void windowGainedFocus(WindowEvent e) {
		fitWindow();
		
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
