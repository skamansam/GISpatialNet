/**
 * Note: some of this code extracted from http://www.java2s.com/Code/Java/Swing-JFC/TreeDragandDrop.htm
 */
package us.jonesrychtar.gispatialnet.gui.GSNPanel;

import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
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
import us.jonesrychtar.gispatialnet.Enums.MatrixFormat;
import us.jonesrychtar.gispatialnet.Enums.DataSetMatrixType;
import us.jonesrychtar.gispatialnet.Reader.CSVFileReader;
import us.jonesrychtar.gispatialnet.Reader.DLreader;
import us.jonesrychtar.gispatialnet.Reader.Reader;
import us.jonesrychtar.gispatialnet.gui.helpers.CSVOptionsFrame;

/**
 * @author sam
 *
 */
public class DataLister extends JTree implements TreeSelectionListener, ActionListener /*,Autoscroll*/ {

	private static final long serialVersionUID = -337982300838439449L;

	GISpatialNet gsn = new GISpatialNet();
	static DefaultMutableTreeNode topNode = new DefaultMutableTreeNode("DataSets Loaded:");
	static DefaultTreeModel tm = new DefaultTreeModel(topNode);
	DataDisplayPanel display;
	Vector<Matrix> selectedMatrices = new Vector<Matrix>();
	Vector<Integer> selectedDataSetIdxs = new Vector<Integer>();
	
	//TODO: add context menu for adding to fields.
	JPopupMenu popup = new JPopupMenu();
	MouseListener popupListener = new PopupListener();
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

	private class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			handleShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			handleShowPopup(e);
		}

		private void handleShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	///////////////////////begin DnD stuff /////////////////////////////////////////////////
/*	 private Insets insets;
		private int top = 0, bottom = 0, topRow = 0, bottomRow = 0; //for DnD capabilities
	  private class TreeDropTargetListener implements DropTargetListener {

		    public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
		      // Setup positioning info for auto-scrolling
		      top = Math.abs(getLocation().y);
		      bottom = top + getParent().getHeight();
		      topRow = getClosestRowForLocation(0, top);
		      bottomRow = getClosestRowForLocation(0, bottom);
		      insets = new Insets(top + 10, 0, bottom - 10, getWidth());
		    }

		    public void dragExit(DropTargetEvent dropTargetEvent) {
		    }

		    public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
		    }

		    public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
		    }

		    public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
		      // Only support dropping over nodes that aren't leafs

		      Point location = dropTargetDropEvent.getLocation();
		      TreePath path = getPathForLocation(location.x, location.y);
		      Object node = path.getLastPathComponent();
		      if ((node != null) && (node instanceof TreeNode)
		          && (!((TreeNode) node).isLeaf())) {
		        try {
		          Transferable tr = dropTargetDropEvent.getTransferable();
		          if (tr
		              .isDataFlavorSupported(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR)) {
		            dropTargetDropEvent
		                .acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		            Object userObject = tr
		                .getTransferData(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR);
		            addElement(path, userObject);
		            dropTargetDropEvent.dropComplete(true);
		          } else if (tr
		              .isDataFlavorSupported(DataFlavor.stringFlavor)) {
		            dropTargetDropEvent
		                .acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		            String string = (String) tr
		                .getTransferData(DataFlavor.stringFlavor);
		            addElement(path, string);
		            dropTargetDropEvent.dropComplete(true);
		          } else if (tr
		              .isDataFlavorSupported(DataFlavor.plainTextFlavor)) {
		            dropTargetDropEvent
		                .acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		            Object stream = tr
		                .getTransferData(DataFlavor.plainTextFlavor);
		            if (stream instanceof InputStream) {
		              InputStreamReader isr = new InputStreamReader(
		                  (InputStream) stream);
		              BufferedReader reader = new BufferedReader(isr);
		              String line;
		              while ((line = reader.readLine()) != null) {
		                addElement(path, line);
		              }
		              dropTargetDropEvent.dropComplete(true);
		            } else if (stream instanceof Reader) {
		              BufferedReader reader = new BufferedReader(
		                  (Reader) stream);
		              String line;
		              while ((line = reader.readLine()) != null) {
		                addElement(path, line);
		              }
		              dropTargetDropEvent.dropComplete(true);
		            } else {
		              System.err.println("Unknown type: "
		                  + stream.getClass());
		              dropTargetDropEvent.rejectDrop();
		            }
		          } else if (tr
		              .isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
		            dropTargetDropEvent
		                .acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		            List fileList = (List) tr
		                .getTransferData(DataFlavor.javaFileListFlavor);
		            Iterator iterator = fileList.iterator();
		            while (iterator.hasNext()) {
		              File file = (File) iterator.next();
		              addElement(path, file.toURL());
		            }
		            dropTargetDropEvent.dropComplete(true);
		          } else {
		            System.err.println("Rejected");
		            dropTargetDropEvent.rejectDrop();
		          }
		        } catch (IOException io) {
		          io.printStackTrace();
		          dropTargetDropEvent.rejectDrop();
		        } catch (UnsupportedFlavorException ufe) {
		          ufe.printStackTrace();
		          dropTargetDropEvent.rejectDrop();
		        }
		      } else {
		        System.out.println("Can't drop on a leaf");
		        dropTargetDropEvent.rejectDrop();
		      }
		    }

		    private void addElement(TreePath path, Object element) {
		      DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path
		          .getLastPathComponent();
		      DefaultMutableTreeNode node = new DefaultMutableTreeNode(element);
		      System.out.println("Added: " + node + " to " + parent);
		      DefaultTreeModel model = (DefaultTreeModel) (DndTree.this
		          .getModel());
		      model.insertNodeInto(node, parent, parent.getChildCount());
		    }
		  }

		  private static class MyDragSourceListener implements DragSourceListener {
		    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
		      if (dragSourceDropEvent.getDropSuccess()) {
		        int dropAction = dragSourceDropEvent.getDropAction();
		        if (dropAction == DnDConstants.ACTION_MOVE) {
		          System.out.println("MOVE: remove node");
		        }
		      }
		    }

		    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
		      DragSourceContext context = dragSourceDragEvent
		          .getDragSourceContext();
		      int dropAction = dragSourceDragEvent.getDropAction();
		      if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
		        context.setCursor(DragSource.DefaultCopyDrop);
		      } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
		        context.setCursor(DragSource.DefaultMoveDrop);
		      } else {
		        context.setCursor(DragSource.DefaultCopyNoDrop);
		      }
		    }

		    public void dragExit(DragSourceEvent dragSourceEvent) {
		    }

		    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
		    }

		    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
		    }
		  }
		  public void autoscroll(Point p) {
			    // Only support up/down scrolling
			    top = Math.abs(getLocation().y) + 10;
			    bottom = top + getParent().getHeight() - 20;
			    int next;
			    if (p.y < top) {
			      next = topRow--;
			      bottomRow++;
			      scrollRowToVisible(next);
			    } else if (p.y > bottom) {
			      next = bottomRow++;
			      topRow--;
			      scrollRowToVisible(next);
			    }
			  }
		  public Insets getAutoscrollInsets() {
			    return insets;
			  }
		public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
	      // Can only drag leafs
	      JTree tree = (JTree) dragGestureEvent.getComponent();
	      TreePath path = tree.getSelectionPath();
	      if (path == null) {
	        // Nothing selected, nothing to drag
	        System.out.println("Nothing selected - beep");
	        tree.getToolkit().beep();
	      } else {
	        DefaultMutableTreeNode selection = (DefaultMutableTreeNode) path
	            .getLastPathComponent();
	        if (selection.isLeaf()) {
	          TransferableTreeNode node = new TransferableTreeNode(
	              selection);
	          dragGestureEvent.startDrag(DragSource.DefaultCopyDrop,
	              node, new MyDragSourceListener());
	        } else {
	          System.out.println("Not a leaf - beep");
	          tree.getToolkit().beep();
	        }
	      }
	    }
	  }

			  */
///////////////////////end DnD stuff /////////////////////////////////////////////////
		  
	public DataLister(){
		super(tm);
		this.setRootVisible(false);
		this.addTreeSelectionListener(this);
		this.initMenu();
		this.initDnD();
	}
	public void setDisplayPanel(DataDisplayPanel gsp){this.display=gsp;}
	public DataDisplayPanel getDisplayPanel(){return this.display;}
	public Vector<Integer> getSelectedDataSetIdxs(){return this.selectedDataSetIdxs;}
	public Vector<DataSet> getSelectedDataSets(){
		Vector<DataSet> d=new Vector<DataSet>();
		for(int i : this.selectedDataSetIdxs){
			d.add(this.gsn.getData(i));
		}
		return d;
	}
	
	public GISpatialNet getGSN() {
		return gsn;
	}
	
	public Vector<Matrix> getSelectedMatrices(){
		return this.selectedMatrices;
	}
	
	public void setGSN(GISpatialNet gsn) {
		this.gsn = gsn;
	}
	private void initMenu(){
		JMenuItem additm = new JMenuItem("Add Data...");
		additm.setActionCommand("Add_Data");
		additm.addActionListener(this);
	    popup.add(additm);

	    this.addMouseListener(popupListener);

	}
	private void initDnD(){
/*		DragSource dragSource = DragSource.getDefaultDragSource();
	    dragSource
	        .createDefaultDragGestureRecognizer(this,
	            DnDConstants.ACTION_COPY_OR_MOVE,
	            new TreeDragGestureListener());
	    DropTarget dropTarget = new DropTarget(this,
	        new TreeDropTargetListener());
*/
		}
	public void refresh(){reloadData();}
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
        CSVFileReader csvr = new CSVFileReader(theFile);
		CSVOptionsFrame f = new CSVOptionsFrame(theFile,gsn);
		if(f.userCancelled()) return;
        csvr.setXCol(f.getXColumn());
        csvr.setYCol(f.getYColumn());
        csvr.setHasHeader(f.getHasHeader());
        csvr.setSortByColumn(f.getSortByColumn());
        csvr.setSeperatorChar(f.getSeparatorAsString());
        try {
            Vector<DataSet> vds = csvr.Read( f.getMatrixFormat(),f.getDataSetType(), f.getRowsAsInt(), f.getColumnsAsInt());

            for (int i = 0; i < vds.size(); i++) {
                if (f.getIsPolar()) vds.elementAt(i).PolarToXY();
                gsn.getDataSets().add(vds.elementAt(i));
            }
            //reloadData();
        } catch (Exception ex) {
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
        }
        reloadData();

	}
	
	public void addExcel(String theFile) {
		CSVOptionsFrame f = new CSVOptionsFrame(theFile);
        try {
            Vector<DataSet> vds = Reader.loadExcel(theFile, f.getDataSetType(), f.getMatrixFormat(), f.getColumnsAsInt(), f.getSeparator());
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
        System.out.println("Data Set Type returned: "+f.getDataSetType());
        System.out.println("Matrix Format returned: "+f.getMatrixFormat());
        try {
            vds = Reader.loadDL(theFile, f.getMatrixFormat(), f.getRowsAsInt(), f.getColumnsAsInt());
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
            vds = Reader.loadPajek(theFile, f.getMatrixFormat(), f.getDataSetType(), f.getRowsAsInt(), f.getColumnsAsInt());
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
		this.selectedDataSetIdxs.clear();
		TreePath[] tp = this.getSelectionPaths();
		for (TreePath t : tp){
			DSMTreeNode node = (DSMTreeNode) t.getPath()[t.getPath().length-1];			
			Matrix m = node.getMatrix();
			//TODO: add index to the list of selected nodes so we can access the selection via gsn[idx]
			if(node.hasDataSet()){
				m = util.combine(node.getDataSet().getX(), util.combine(node.getDataSet().getY(), util.combine(node.getDataSet().getAdj(),node.getDataSet().getAttb())));
				this.selectedDataSetIdxs.add(this.topNode.getIndex(node));
			}else{
				this.selectedDataSetIdxs.add(this.topNode.getIndex(node.getParent()));
				//JOptionPane.showMessageDialog(this, "Selected idx is "+this.topNode.getIndex(node.getParent()));
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
    public void clearData(){
    	System.out.println("Clearing data in DataLister");
    	gsn.removeData(-1);
    }
	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Add handler for menu
		if(evt.getActionCommand().equals("Add_Data")){
			System.out.println("Adding Data...");
		}
	}
	private void handleShowMenu(){
		
	}

}
