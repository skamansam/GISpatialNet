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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

//import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
//import javax.swing.filechooser.FileFilter;

//import us.jonesrychtar.gispatialnet.GISpatialNet;
import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.Enums;
import us.jonesrychtar.gispatialnet.cli;
import us.jonesrychtar.gispatialnet.util;
import us.jonesrychtar.gispatialnet.Writer.CSVwriter;
import us.jonesrychtar.gispatialnet.Writer.Writer;
import us.jonesrychtar.gispatialnet.gui.GSNFileFilter;
import us.jonesrychtar.gispatialnet.gui.GSNStatusBarInterface;
import us.jonesrychtar.gispatialnet.gui.helpers.SaveType;
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
		if (e.getActionCommand().startsWith("save_as")){handleSaveAs(e.getActionCommand());}
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
		if(algorithm.equals("qap")){
			Matrix m=theList.getSelectedMatrices().elementAt(0);
			String[] options  = new String[]{"Simple Mantel Test", "SMT with exact permutation",
                    "Partial Mantel Test", "PMT with exact permutation", "PMT with raw values",
                    "PMT with exact permutation and raw values"};
			String theChoice = (String)JOptionPane.showInputDialog(this, "What type of computation would you like to perform?", 
					"Choose Algorithm", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if(theChoice == null) return;
			System.out.println("Running QAP as "+theChoice+"...");
			if(theList.getSelectionRows().length!=2){ JOptionPane.showMessageDialog(this, "You must select two matrices in order to run this algorithm.");return;}
			
		}else
		JOptionPane.showMessageDialog(this, "Sorry!\nThe "+algorithm+" algorithm is not implemented yet!", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}

	private void handleTransform(String transformName) {
		JOptionPane.showMessageDialog(this, "Sorry!\nThe "+transformName+" transform is not implemented yet!", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
	}

	private void handleSaveAll() {
		JOptionPane.showMessageDialog(this, "Save All has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
		this.isTainted=false;
	}

	private void handleSaveAs(String type) {
		if(theList.getSelectedMatrices().size()==0)return;
		System.out.println("Saving as "+type);
		type=type.replace("save_as_", "");
		Enums.FileType t = Enums.FileType.fromString(type.toUpperCase());
		JFileChooser c = new JFileChooser();
		int d = c.showSaveDialog(this);
		if (d!=JFileChooser.APPROVE_OPTION) return;
		File f=c.getSelectedFile();
		switch(t){
			case CSV:
			try {
				new CSVwriter(theList.getSelectedMatrices().elementAt(0), f.getAbsolutePath(), ',').WriteFile();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(theList,
					    "Cannot locate file. Please check permissions.");
			}
				break;
			case EXCEL:
				JOptionPane.showMessageDialog(this, "Save As Excel has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
				break;
			case SHAPEFILE:
				//TODO: save all or merge and save if no selection
				if(theList.getSelectedMatrices().size()<1){
					JOptionPane.showMessageDialog(this, "Please select one or more matrices to be exported.");
					return;
					//String choices[]={"Single","Merge","Cancel"};
					//int answer = JOptionPane.showOptionDialog(this, "Do you want to export all data sets?", "Export", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[1]);
					//if(answer==2)return;
					//if(answer==0)handleExportAll();
					//if(answer==1)handleExport();
				}
				
				if(theList.getSelectedMatrices().size()>=1){
					//if(theList.getSelectedMatrices().size()>1){
					//	int doMerge=JOptionPane.showConfirmDialog(this, "Do you want to merge the selected matrices before exporting?");
					//}
					for(int curSelectedIdx=0;curSelectedIdx<theList.getSelectedMatrices().size();curSelectedIdx++){
						
					DataSet tmp = theList.getSelectedDataSets().elementAt(curSelectedIdx);

	                if (!(tmp.hasX() && tmp.hasY())) {
						//TODO: get this to work!
	                	String choices[]={"Create XY data", "Write only Edge file"};
						int answer = JOptionPane.showOptionDialog(this, "Node data not found. \nWhat do you want to do?", "Export", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[1]);
	                    
						JFileChooser dlg=new JFileChooser("Select Node File");
						dlg.setFileFilter(new GSNFileFilter(""));
						dlg.setCurrentDirectory(new File(prefs.get("LAST_OPEN_DIR", ".")));
						if(dlg.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
						String fnn = dlg.getSelectedFile().getAbsolutePath();
						//System.err.println("Opening "+theFile);

						JFileChooser dlg2=new JFileChooser("Select Edge File");
						dlg2.setFileFilter(new GSNFileFilter(""));
						dlg2.setCurrentDirectory(new File(prefs.get("LAST_OPEN_DIR", ".")));
						if(dlg2.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
						String efn = dlg2.getSelectedFile().getAbsolutePath();
						//System.err.println("Opening "+theFile);

	                    switch (answer) {
	                        case 0: {
	                        	int ht = new Integer(JOptionPane.showInputDialog("Enter max height of network (usually 10xNumber of rows): "));
	                            int wd = new Integer(JOptionPane.showInputDialog("Enter max width of network (usually 10xNumber of columns): "));
	                            try {
	                            	Writer.saveShapefileUnknown(fnn, efn, 1, ht, wd, tmp);
	                            } catch (IllegalArgumentException ex) {
	                                System.out.println(ex.getMessage());
	                            } catch (MalformedURLException ex) {
	                                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
	                            } catch (IOException ex) {
	                                System.out.println(ex.getMessage());
	                            } catch (SchemaException ex) {
	                                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
	                            }
	                        }
	                        case 1: {
	                            try {
	                            	Writer.saveShapefile(fnn, efn, tmp);
	                            } catch (IllegalArgumentException ex) {
	                                System.out.println(ex.getMessage());
	                            } catch (MalformedURLException ex) {
	                                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
	                            } catch (IOException ex) {
	                                System.out.println(ex.getMessage());
	                            } catch (SchemaException ex) {
	                                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
	                            }
	                        }
	                    }
	                }
				}
				}
				//JOptionPane.showMessageDialog(this, "Save As Shapefile has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
				break;
			case UCINET:
				JOptionPane.showMessageDialog(this, "Save As DL/UCINet has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
				break;
			case PAJEK:
				JOptionPane.showMessageDialog(this, "Save As Pajek has not yet been implemented.", "Not Yet Implemented", JOptionPane.ERROR_MESSAGE);
				break;
			default:
					
		}
		
	}

	private void handleSave() {
		//SaveType s = new SaveType();
		//s.setVisible(true);
		//System.out.println("Saving as "+Enums.FileType.toString(s.getType()));

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
