/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.GSNPanel;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
//import javax.swing.tree.TreeModel;
//import javax.swing.tree.TreeNode;
//import javax.swing.tree.TreePath;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.GISpatialNet;
import us.jonesrychtar.gispatialnet.cli;
import us.jonesrychtar.gispatialnet.util;
import us.jonesrychtar.gispatialnet.Enums.MatrixInputType;
import us.jonesrychtar.gispatialnet.Enums.MatrixType;
import us.jonesrychtar.gispatialnet.Reader.DLreader;
import us.jonesrychtar.gispatialnet.Reader.Reader;
import us.jonesrychtar.gispatialnet.gui.helpers.CSVOptionsFrame;

/**
 * @author sam
 *
 */
public class DataLister extends JTree implements TreeSelectionListener {

	private static final long serialVersionUID = -337982300838439449L;

	GISpatialNet gsn = new GISpatialNet();
	static DefaultMutableTreeNode topNode = new DefaultMutableTreeNode("DataSets Loaded:");
	static DefaultTreeModel tm = new DefaultTreeModel(topNode);
	DataDisplayPanel display;
	Vector<Matrix> selectedMatrices = new Vector<Matrix>();
	
	private class DSMTreeNode extends DefaultMutableTreeNode{
		private static final long serialVersionUID = 3813976506231344444L;
		private int dsIdx = -1;
		private String theTitle = "[No Label]";
		private DataSet ds=null;
		private Matrix m = MatrixFactory.emptyMatrix();
		private boolean hasDS = false;
		public DSMTreeNode(String t,Matrix mat){this.setMatrix(mat);this.setTitle(t);}
		public DSMTreeNode(String t,DataSet d){this.setDataSet(d);this.setTitle(t);hasDS=true;}
		public void setIndex(int dsIdx) {this.dsIdx = dsIdx;}
		public int getIndex() {return dsIdx;}
		public void setTitle(String theTitle) {this.theTitle = theTitle;}
		public String getTitle() {return theTitle;}
		public DataSet getDataSet() {return ds;}
		public void setDataSet(DataSet ds) {this.ds = ds;}
		public Matrix getMatrix() {return m;}
		public void setMatrix(Matrix m) {this.m = m;}
		public String toString(){return theTitle;}
		public boolean hasDataSet(){return hasDS;}
	}
	
	public DataLister(){
		super(tm);
		this.setRootVisible(false);
		this.addTreeSelectionListener(this);
	}
	public void setDisplayPanel(DataDisplayPanel gsp){this.display=gsp;}
	public DataDisplayPanel getDisplayPanel(){return this.display;}

	public GISpatialNet getGSN() {
		return gsn;
	}
	
	public Vector<Matrix> getSelectedMatrices(){
		return this.selectedMatrices;
	}
	
	public void setGSN(GISpatialNet gsn) {
		this.gsn = gsn;
	}
	private void reloadData(){
		System.out.println(""+gsn.getDataSets().size()+" datasets are loaded!");
		//TreeModel tm = this.getModel();
		
		//clear tree
		while(topNode.getChildCount()!=0){
			tm.removeNodeFromParent((MutableTreeNode) topNode.getChildAt(0));
		}
		
		if (gsn.getDataSets().size()<1) return;
		
		for(DataSet ds : gsn.getDataSets()){
			DSMTreeNode next = new DSMTreeNode(ds.GetLoadedFiles().elementAt(0),ds);
			//System.out.println("Inserting "+ds.GetLoadedFiles().elementAt(0));
			if(!ds.getX().isEmpty() || !ds.getY().isEmpty())
				next.add(new DSMTreeNode("Coordinates",util.combine(ds.getX(),ds.getY())) );
			if(!ds.getAdj().isEmpty())
			next.add(new DSMTreeNode("Adjacency",ds.getAdj()) );
			if(!ds.getAttb().isEmpty())
			next.add( new DSMTreeNode("Attributes",ds.getAttb()) );
			topNode.add(next);
			
		}

		tm.setRoot(topNode);
		
	}
	
	public void addCSV(String theFile){
		Reader reader = new Reader();
		CSVOptionsFrame f = new CSVOptionsFrame(theFile);
		if(f.userCancelled()) return;
        try {
            Vector<DataSet> vds = Reader.loadTxt(theFile, f.getDataSetTypeAsInt(), f.getMatrixTypeAsInt(), f.getRowsAsInt(), f.getColumnsAsInt(), f.getSortByColumn(), f.getHasHeader(),f.getSeparator());

            for (int i = 0; i < vds.size(); i++) {
                if (f.getIsPolar()) vds.elementAt(i).PolarToXY();
                gsn.getDataSets().add(vds.elementAt(i));
            }
            reloadData();
        } catch (Exception ex) {
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
        }
        reloadData();

	}
	
	public void addExcel(String theFile) {
		Reader reader = new Reader();
		CSVOptionsFrame f = new CSVOptionsFrame(theFile);
        try {
            Vector<DataSet> vds = Reader.loadExcel(theFile, MatrixType.fromInt(f.getDataSetTypeAsInt()), MatrixInputType.fromInt(f.getMatrixTypeAsInt()), f.getColumnsAsInt(), f.getSeparator());
            for (int i = 0; i < vds.size(); i++) {
                if (f.getIsPolar()) vds.elementAt(i).PolarToXY();
                gsn.getDataSets().add(vds.elementAt(i));
            }
            reloadData();
        } catch (Exception ex) {
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	public void addUCINet(String theFile) {
		CSVOptionsFrame f = new CSVOptionsFrame(theFile);
        Vector<DataSet> vds=null;
        try {
            vds = Reader.loadDL(theFile, f.getMatrixTypeAsInt(), f.getRowsAsInt(), f.getColumnsAsInt());
            for (int i = 0; i < vds.size(); i++) {
                if (f.getIsPolar()) vds.elementAt(i).PolarToXY();
                gsn.getDataSets().add(vds.elementAt(i));
            }
            reloadData();
        } catch (Exception ex) {
        	JOptionPane.showMessageDialog(this, ex);
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.err.println("Imported "+vds.size()+" Data Sets.");

	}

	public void addPajek(String theFile) {
		CSVOptionsFrame f = new CSVOptionsFrame(theFile);
        Vector<DataSet> vds=null;
        try {
            vds = Reader.loadPajek(theFile, f.getMatrixTypeAsInt(), f.getRowsAsInt(), f.getColumnsAsInt());
            for (int i = 0; i < vds.size(); i++) {
                if (f.getIsPolar()) vds.elementAt(i).PolarToXY();
                gsn.getDataSets().add(vds.elementAt(i));
            }
            reloadData();
        } catch (Exception ex) {
        	JOptionPane.showMessageDialog(this, ex);
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.err.println("Imported "+vds.size()+" Data Sets.");
		
	}

	public void addShapefile(String theFile) {
		JFileChooser dlg=new JFileChooser();
		dlg.setCurrentDirectory(new File("."));
		if(dlg.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
		String edgeFile = dlg.getSelectedFile().getAbsolutePath();
		System.out.println("Opening "+theFile);

		try {
            DataSet ds = Reader.loadShapefile(theFile, edgeFile);
            gsn.getDataSets().add(ds);
        } catch (MalformedURLException ex) {
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("File not found.");
            ex.getMessage();
        }
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if(this.getLastSelectedPathComponent()==null) return;
		this.selectedMatrices.clear();
		TreePath[] tp = this.getSelectionPaths();
		for (TreePath t : tp){
			DSMTreeNode dt = (DSMTreeNode) t.getPath()[t.getPath().length-1];			
			Matrix m = dt.getMatrix();
			if(dt.hasDataSet()){
				m = util.combine(dt.getDataSet().getX(), util.combine(dt.getDataSet().getY(), util.combine(dt.getDataSet().getAdj(),dt.getDataSet().getAttb())));
			}
			this.selectedMatrices.add(m);
		}
		Matrix c=MatrixFactory.emptyMatrix();
		for(Matrix x : this.selectedMatrices){
			c=util.combine(c, x);
		}
		displayData(display,this.selectedMatrices.elementAt(this.selectedMatrices.size()-1));
		//displayData(display,c);
		System.out.println("Selection has "+this.selectedMatrices.size()+" matrices.");
	}
	private void displayData(DataDisplayPanel dsp,Matrix m){
		if(dsp == null){System.err.println("Display Panel is null!");return;}
		dsp.displayMatrix(m);
	}

    private int getChoice(String title, String info, String[] items) {
        //return and err out if length of items is less than one.
        if (items.length < 1) {
            System.err.println("Incorrect length for menu items.");
            return 0;
        }
        
        return JOptionPane.showOptionDialog(this, info, "Options", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, items, items[0]);
        
    }
}
