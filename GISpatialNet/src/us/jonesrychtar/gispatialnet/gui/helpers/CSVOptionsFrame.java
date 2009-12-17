/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.helpers;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * @author sam
 *
 */
public class CSVOptionsFrame extends JFrame {
	int dsType=0,matrixType=0,rows=0,cols=0;
	char separator=',';
	boolean hasCancelled = false;
	
	public CSVOptionsFrame(){
		this.setTitle("CSV Import Options");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setBounds(100, 100, 400, 200);
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		this.add(p);		
		JLabel dstl = new JLabel("Data to read: ");
		JComboBox dst = new JComboBox(new String[]{"Node data (nodes with attributes)", "Adjacency data", "Node data only", "Attribute data"});
		//dst.setListData(new String[]{"Node data (nodes with attributes)", "Adjacency data", "Node data only", "Attribute data"});
		JPanel r1 = new JPanel();
		r1.add(dstl);r1.add(dst);
		p.add(r1);
		JLabel mtl = new JLabel("Type of Matrix: ");
		JComboBox mt = new JComboBox(new String[]{"Full","Upper","Lower"});
		JPanel r2 = new JPanel();
		r2.add(mtl);r2.add(mt);
		p.add(r2);

		JLabel rl = new JLabel("Rows to read: ");
		JSpinner rowSpinner=new JSpinner(new SpinnerNumberModel(25,0,999999,1));
		JPanel r3 = new JPanel();
		r3.add(rl);r3.add(rowSpinner);
		p.add(r3);

		JLabel cl = new JLabel("Columns to read: ");
		JSpinner colSpinner=new JSpinner(new SpinnerNumberModel(4,0,999999,1));
		JPanel r4 = new JPanel();
		r4.add(cl);r4.add(colSpinner);
		p.add(r4);
		
		//TODO: add cancel/ok buttons
		
		this.setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CSVOptionsFrame f = new CSVOptionsFrame();
		System.out.println("DSType: "+f.getDataSetType());
		System.out.println("MatrixType: "+f.getMatrixType());
		System.out.println("Rows: "+f.getRows());
		System.out.println("Columns: "+f.getColumns());
		System.out.println("Separator: "+f.getSeperator());
	}
	
	//["Node data (nodes with attributes)", "Adjacency data", "Node data only", "Attribute data"]
	public String getDataSetType(){
		switch(dsType){
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
	public int getDataSetTypeAsInt(){return dsType;}

	//{Full, Upper, Lower}
	public String getMatrixType(){
		switch(matrixType){
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
	public int getMatrixTypeAsInt(){return matrixType;}

	public String getRows(){return new Integer(rows).toString();}
	public int getRowsAsInt(){return rows;}

	public String getColumns(){return new Integer(cols).toString();}
	public int getColumnsAsInt(){return cols;}

	public char getSeperator(){return separator;}
	public String getColumnsAsString(){return new String(""+separator);}
}
