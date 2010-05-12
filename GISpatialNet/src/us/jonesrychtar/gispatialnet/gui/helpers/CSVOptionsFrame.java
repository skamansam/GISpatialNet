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
import java.io.File;
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
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.enums.FileFormat;
import org.ujmp.core.stringmatrix.impl.CSVMatrix;

import us.jonesrychtar.gispatialnet.Enums;

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
	//int dsType = 0, matrixType = 0, 
	int rows = 24, cols = 4;
	int xcol=0,ycol=1;
	Enums.MatrixFormat fmt = Enums.MatrixFormat.FULL;
	Enums.DataSetMatrixType dsType=Enums.DataSetMatrixType.COORD_ATT;
	char separator = ',';
	boolean isPolar = false;
	boolean hasCancelled = false;

	JPanel seprow = new JPanel();
	JLabel seplbl = new JLabel("Field Delimiter: ");
	JComboBox sep = new JComboBox(new String[] { "Comma","Tab","Space" });
	
	JPanel hasheaderrow = new JPanel();
	JCheckBox hasHeader = new JCheckBox("First row is a column header row.");

	JPanel haslabelcol = new JPanel();
	JCheckBox hasLabels = new JCheckBox("First column is an ID column. (put in with attribute data).");

	JPanel colselrow = new JPanel();
	JLabel colSelectLbl = new JLabel("Split Based On:");
	JComboBox columnList = new JComboBox(new String[] { "Please select delimiter first."});

	JPanel xcolselrow = new JPanel();
	JLabel xcolselLbl = new JLabel("X Coordinate Column: ");
	JComboBox xcolList = new JComboBox(new String[] { "Please select delimiter first."});

	JPanel ycolselrow = new JPanel();
	JLabel ycolselLbl = new JLabel("Y Coordinate Column: ");
	JComboBox ycolList = new JComboBox(new String[] { "Please select delimiter first."});

	JPanel useheuristicrow = new JPanel();
	JCheckBox useHeuristic = new JCheckBox("Import using heuristics.");

	JPanel dstrow = new JPanel();
	JLabel dstlbl = new JLabel("Data to read: ");
	JComboBox dst = new JComboBox(new String[] {
			"Coordinate data with attributes", "Adjacency data",
			"Coordinate data only", "Attribute data" });

	JPanel mtrow = new JPanel();
	JLabel mtlbl = new JLabel("Type of Matrix: ");
	JComboBox mt = new JComboBox(new String[] { "Full",  "Lower", "Upper" });

	JPanel numrowsrow = new JPanel();
	JLabel numrowslbl = new JLabel("Rows to read (split every x rows): ");
	JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(25, 0, 999999, 1));

	JPanel numcolsrow = new JPanel();
	JLabel numcolslbl = new JLabel("Columns to read: ");
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
		this.setBounds(100, 100, 620, 400);

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

		//has header? row
		hasLabels.addActionListener(this);
		hasLabels.setSelected(true);
		haslabelcol.add(hasLabels);
		p.add(haslabelcol);

		//column list
		columnList.addActionListener(this);
		columnList.setEditable(false);
		colselrow.add(colSelectLbl);
		colselrow.add(columnList);
		p.add(colselrow);

		//x column list
		xcolList.addActionListener(this);
		xcolList.setEditable(false);
		xcolselrow.add(xcolselLbl);
		xcolselrow.add(xcolList);
		p.add(xcolselrow);

		//y column list
		ycolList.addActionListener(this);
		ycolList.setEditable(false);
		ycolselrow.add(ycolselLbl);
		ycolselrow.add(ycolList);
		p.add(ycolselrow);

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
		if(this.FName.contains("lower") || this.FName.contains("bottom")){mt.setSelectedIndex(1);}
		if(this.FName.contains("upper") || this.FName.contains("top")){mt.setSelectedIndex(2);}

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
		if(this.FName.contains("dist") || this.FName.contains("pol")){coord.setSelectedIndex(1);}

		
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
		int h = this.getInsets().bottom+this.getInsets().top+
		seprow.getHeight()+
		hasheaderrow.getHeight()+
		colselrow.getHeight()+
		xcolselrow.getHeight()+
		ycolselrow.getHeight()+
		colselrow.getHeight()+
		useheuristicrow.getHeight()+
		dstrow.getHeight()+
		mtrow.getHeight()+
		numrowsrow.getHeight()+
		numcolsrow.getHeight()+
		coordtyperow.getHeight()+
		btnsep.getHeight()+
		btnrow.getHeight()+20;
		//System.err.println("window control height "+h);
		if(this.getInsets().bottom-this.getInsets().top<h)
			this.setBounds(this.getX(), this.getY(), this.getWidth(), h);

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
		System.out.println("MatrixType: " + getMatrixFormat());
		System.out.println("Rows: " + getRows());
		System.out.println("Columns: " + getColumns());
		System.out.println("Separator: '" + getSeparator()+"'");		
	}
	// ["Node data (nodes with attributes)", "Adjacency data", "Node data only",
	// "Attribute data"]
	public Enums.DataSetMatrixType getDataSetType() {
		return this.dsType;
	}

	// {Full, Upper, Lower}
	public Enums.MatrixFormat getMatrixFormat(){
		return this.fmt;
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
	public boolean getHasLabels() {
		return this.hasLabels.isSelected();
	}
	public int getSortByColumn() {
		return this.splitBy;
	}
	public int getXColumn() {
		return this.xcol;
	}
	public int getYColumn() {
		return this.ycol;
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
			this.dsType = Enums.DataSetMatrixType.fromInt(dst.getSelectedIndex());
		else if(e.getSource().equals(mt)){
			System.out.println("Seeting DataSet Type to ("+mt.getSelectedIndex()+") "+Enums.MatrixFormat.fromInt(mt.getSelectedIndex()));
			this.fmt = Enums.MatrixFormat.fromInt(mt.getSelectedIndex());
		}
		else if(e.getSource().equals(coord))
			this.isPolar = coord.getSelectedIndex()==0?false:true;
		else if(e.getSource().equals(columnList))
			this.splitBy=columnList.getSelectedIndex()>0?columnList.getSelectedIndex()-1:-1;
		else if(e.getSource().equals(xcolList))
			this.xcol=xcolList.getSelectedIndex()>0?xcolList.getSelectedIndex()-1:-1;
		else if(e.getSource().equals(ycolList))
			this.ycol=ycolList.getSelectedIndex()>0?ycolList.getSelectedIndex()-1:-1;
		else if(e.getSource().equals(hasHeader))
			populateColumnList(""+separator);
		else if(e.getSource().equals(hasLabels))
			populateColumnList(""+separator);
		else if(e.getSource().equals(sep)){
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
		else if(e.getSource().equals(useHeuristic)){
			setHeuristicControlVisibility(!useHeuristic.isSelected());
		}
		//printInfo();
	}


	private void populateColumnList(String seperator) {
		//TODO: get matrix with UJMP and parse headers
		//System.out.println("Parsing "+this.FName);
		try {
			Matrix m = MatrixFactory.linkToFile(FileFormat.CSV, new File(this.FName));
			//Matrix m = new CSVMatrix(new File(this.FName),new String(seperator));
			columnList.removeAllItems();
			columnList.addItem("[Use Row number]");
			rowSpinner.setValue((int) m.getRowCount());
			if(this.hasHeader.isSelected())	rowSpinner.setValue((int)m.getRowCount()-1);
			colSpinner.setValue((int)m.getColumnCount());
			//System.out.println("This stream has "+m.getColumnCount()+" columns with "+(hasHeader.isSelected()?"":"no ")+"header using "+seperator+" seperator.");
			for(int i=0;i<(int)m.getColumnCount();i++){
				String theLabel = m.getAsString(0,i);
				if(hasHeader.isSelected()){
					columnList.addItem(theLabel);
					xcolList.addItem(theLabel);
					ycolList.addItem(theLabel);
				}else{
					columnList.addItem(""+i+" ["+theLabel+"]");
					xcolList.addItem(""+i+" ["+theLabel+"]");
					ycolList.addItem(""+i+" ["+theLabel+"]");
				}
			}
			System.out.print("\n");
		} catch (Exception e) {
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
	public void windowGainedFocus(WindowEvent e) {fitWindow();}
	@Override
	public void windowLostFocus(WindowEvent e) {}
	
}
