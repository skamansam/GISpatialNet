/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.GSNPanel;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.GISpatialNet;
import us.jonesrychtar.gispatialnet.cli;
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

	private class DSMTreeNode extends DefaultMutableTreeNode{
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

	public GISpatialNet getGSN() {
		return gsn;
	}

	public void setGSN(GISpatialNet gsn) {
		this.gsn = gsn;
	}
	public void addCSV(String theFile){
		Reader reader = new Reader();
		CSVOptionsFrame f = new CSVOptionsFrame();
        try {
            Vector<DataSet> vds = Reader.loadTxt(theFile, f.getDataSetTypeAsInt(), f.getMatrixTypeAsInt(), f.getRowsAsInt(), f.getColumnsAsInt(), f.getSeparator());

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
	
	private void reloadData(){
		System.out.println(""+gsn.getDataSets().size()+" datasets are loaded!");
		//TreeModel tm = this.getModel();
		
		//clear tree
		while(topNode.getChildCount()!=0){
			tm.removeNodeFromParent((MutableTreeNode) topNode.getChildAt(0));
		}
		
		for(DataSet ds : gsn.getDataSets()){
			DSMTreeNode next = new DSMTreeNode(ds.GetLoadedFiles().elementAt(0),ds);
			//System.out.println("Inserting "+ds.GetLoadedFiles().elementAt(0));
			next.add(new DSMTreeNode("X",ds.getX()) );
			next.add(new DSMTreeNode("Y",ds.getY()) );
			next.add(new DSMTreeNode("Adjacency",ds.getAdj()) );
			next.add( new DSMTreeNode("Attributes",ds.getAttb()) );
			topNode.add(next);
			
		}

		tm.setRoot(topNode);
		
	}
	
	public void addExcel(String theFile) {
		// TODO Auto-generated method stub
		
	}

	public void addUCINet(String theFile) {
		// TODO Auto-generated method stub
		
	}

	public void addPajek(String theFile) {
		// TODO Auto-generated method stub
		
	}

	public void addShapefile(String theFile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DSMTreeNode dt = (DSMTreeNode) this.getLastSelectedPathComponent();
		if(!dt.hasDataSet())
			System.out.print("Selected :\n"+dt.getMatrix());
	}
}
