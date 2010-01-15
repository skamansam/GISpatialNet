/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.GSNPanel;

//import java.awt.Color;
//import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.ComponentEvent;
//import java.awt.event.ComponentListener;
//import java.awt.event.WindowEvent;
import java.io.File;
import java.util.prefs.Preferences;

//import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.ujmp.core.Matrix;
//import javax.swing.filechooser.FileFilter;

//import us.jonesrychtar.gispatialnet.GISpatialNet;
import us.jonesrychtar.gispatialnet.util;
import us.jonesrychtar.gispatialnet.gui.GSNFileFilter;
import us.jonesrychtar.gispatialnet.gui.GSNStatusBarInterface;
//import us.jonesrychtar.gispatialnet.gui.GUIutil;

/**
 * @author sam
 *
 */
public class GSNPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6627866729098304231L;
	private boolean isTainted = false;
	private DataLister theList=new DataLister();
	private DataDisplayPanel theDisplay = new DataDisplayPanel();
	GSNStatusBarInterface theStatus;
	JScrollPane leftPane,rightPane;
	JSplitPane thePane;
	Preferences prefs;
	//GISpatialNet gsn = new GISpatialNet();

	public GSNPanel(){
		prefs = Preferences.userNodeForPackage(this.getClass());
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		leftPane = new JScrollPane(theList);
		rightPane = new JScrollPane(theDisplay);
		thePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane,rightPane);
		thePane.setDividerLocation(200);
		theList.setDisplayPanel(theDisplay);
		//theList.setGSN(gsn);
		//theDisplay.setGSN(gsn);
		this.add(thePane);
	}
	
	public Preferences getPrefs(){return this.prefs;}
	public void setPrefs(Preferences p){this.prefs = p;}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action on GSNPanel: "+e.getActionCommand());
		//exit has been issued
		if (e.getActionCommand() == "open"){handleOpen();}
		if (e.getActionCommand() == "save"){handleSave();}
		if (e.getActionCommand() == "save_as"){handleSaveAs();}
		if (e.getActionCommand() == "save_all"){handleSaveAll();}
		if (e.getActionCommand() == "exit"){handleClose();}
		
		if (e.getActionCommand() == "transform_flip"){handleTransform("flip");}
		if (e.getActionCommand() == "transform_rotate"){handleTransform("rotate");}
		if (e.getActionCommand() == "transform_resize"){handleTransform("resize");}
		if (e.getActionCommand() == "transform_match_best"){handleTransform("match_best");}
		if (e.getActionCommand() == "find_similar"){handleTransform("find_similar");}

		if (e.getActionCommand() == "algorithm_qap"){handleAlgorithm("qap");}
		if (e.getActionCommand() == "algorithm_snb"){handleAlgorithm("snb");}
		if (e.getActionCommand() == "algorithm_borders"){handleAlgorithm("borders");}
		if (e.getActionCommand() == "algorithm_hilite_edges"){handleAlgorithm("hilite_edges");}
		
		if (e.getActionCommand() == "data_find"){handleFind();}
		if (e.getActionCommand() == "data_merge"){handleData("merge");}
		if (e.getActionCommand() == "data_separate"){handleData("separate");}
		if (e.getActionCommand() == "data_add"){handleData("add");}
		if (e.getActionCommand() == "data_add_ego"){handleData("add_ego");}
		
		if (e.getActionCommand() == "view_pajek"){handleOpenWith("pajek");}
		if (e.getActionCommand() == "view_egonet"){handleOpenWith("egonet");}
		if (e.getActionCommand() == "view_ucinet"){handleOpenWith("ucinet");}
		if (e.getActionCommand() == "view_geotools"){handleOpenWith("geotools");}
		if (e.getActionCommand() == "view_arcgis"){handleOpenWith("arcgis");}
		if (e.getActionCommand() == "view_g_earth"){handleOpenWith("gearth");}
		if (e.getActionCommand() == "view_g_maps"){handleOpenWith("gmaps");}

		if (e.getActionCommand() == "show_about"){showAbout();}
		if (e.getActionCommand() == "show_help"){showHelp();}
	}

	private void showHelp() {
		JOptionPane.showMessageDialog(this, "Sorry!\nHelp is not implemented yet!", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}

	private void showAbout() {
		JOptionPane.showMessageDialog(this, "Sorry!\nAbout is not implemented yet!", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}

	private void handleOpenWith(String progName) {
		//TODO: Make this work!
		if(!prefs.get("OPEN_"+progName, "").equals("")){
			//Matrix m=theList.getSelectedMatrix();
		}else{
			JOptionPane.showMessageDialog(this, "Sorry!\nOpening in "+progName+" is not implemented yet!", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void handleData(String manipulation) {
		JOptionPane.showMessageDialog(this, "Sorry!\n"+manipulation+" is not implemented yet!", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}

	private void handleFind() {
		JOptionPane.showMessageDialog(this, "Sorry!\nSearching is not implemented yet!", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}

	private void handleAlgorithm(String algorithm) {
		JOptionPane.showMessageDialog(this, "Sorry!\nThe "+algorithm+" algorithm is not implemented yet!", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}

	private void handleTransform(String transformName) {
		JOptionPane.showMessageDialog(this, "Sorry!\nThe "+transformName+" transform is not implemented yet!", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}

	private void handleSaveAll() {
		JOptionPane.showMessageDialog(this, "Save All has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
		this.isTainted=false;
	}

	private void handleSaveAs() {
		JOptionPane.showMessageDialog(this, "Save As has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
		
	}

	private void handleSave() {
		JOptionPane.showMessageDialog(this, "Save has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}

	public void saveAllData(){
		JOptionPane.showMessageDialog(this, "Saving All has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}
	
	//when open is called.
	private void handleOpen() {
		//ask for file
		JFileChooser dlg=new JFileChooser();
		dlg.setFileFilter(new GSNFileFilter(""));
		dlg.setMultiSelectionEnabled(true);
		String theFile="";
		dlg.setCurrentDirectory(new File(prefs.get("LAST_OPEN_DIR", ".")));
		if(dlg.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
		prefs.put("LAST_OPEN_DIR", dlg.getSelectedFile().getAbsolutePath());
		theFile = dlg.getSelectedFile().getAbsolutePath();
		System.err.println("Opening "+theFile);

		//determine filetype
		String type=util.determineFileType(new File(theFile));
		System.err.println("File is a "+type);

		if(type==null){
			JOptionPane.showMessageDialog(this, "This file is not supported!", "File Not Supported", JOptionPane.ERROR_MESSAGE);
			return;
		}

		//import
		if(type.equals("csv")){
			this.theList.addCSV(theFile);
		}
		if(type.equals("excel")){
			this.theList.addExcel(theFile);
		}
		if(type.equals("ucinet")){
			this.theList.addUCINet(theFile);
		}
		if(type.equals("pajek")){
			this.theList.addPajek(theFile);
		}
		if(type.equals("shapefile")){
			this.theList.addShapefile(theFile);
		}
	}


	public void handleClose() {
		System.err.println("GSNPanel.handleClose()");
		if(isTainted && JOptionPane.showConfirmDialog(this,"Do you wish to save first?","Data has been edited, but not saved.",JOptionPane.YES_NO_OPTION)==0){
			this.handleSaveAll();
		}else{
			//this.getRootPane().getParent().
			System.exit(0);
		}
		
	}

	
	public void setStatusBar(GSNStatusBarInterface sb){this.theStatus=sb;}

}
