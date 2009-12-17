/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.GSNPanel;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTree;

import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.GISpatialNet;
import us.jonesrychtar.gispatialnet.cli;
import us.jonesrychtar.gispatialnet.Reader.Reader;
import us.jonesrychtar.gispatialnet.gui.helpers.CSVOptionsFrame;

/**
 * @author sam
 *
 */
public class DataLister extends JTree {
	GISpatialNet gsn = new GISpatialNet();

	public DataLister(){
	}

	public GISpatialNet getGSN() {
		return gsn;
	}

	public void setGSN(GISpatialNet gsn) {
		this.gsn = gsn;
	}
	public void addCSV(String theFile){
		Reader reader = new Reader();
		String[] ops = getCSVOptions();
        System.out.print("What is the field separator? ");

        try {
            Vector<DataSet> vds = Reader.loadTxt(theFile, new Integer(ops[0]), new Integer(ops[1]), new Integer(ops[2]), new Integer(ops[3]), ops[4].charAt(0));

            for (int i = 0; i < vds.size(); i++) {
                if (ops[5].equals("1")) vds.elementAt(i).PolarToXY();
                gsn.getDataSets().add(vds.elementAt(i));
            }
            reloadData();
        } catch (Exception ex) {
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
        }

	}
	
	private void reloadData(){}
	
	public String[] getCSVOptions(){
		//options are :
		//{ ["Node data (nodes with attributes)", "Adjacency data", "Node data only", "Attribute data"],
		// {Full, Upper, Lower},
		//rows,cols,separator,isXYPolar?}
		//JFrame csvOpts=new JFrame("CSV Import Options");
		CSVOptionsFrame f = new CSVOptionsFrame();
		String[] options={"0","0","25","2",",","0"};
		return options;
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
}
