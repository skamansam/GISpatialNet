/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
//import javax.swing.UIManager;

import us.jonesrychtar.gispatialnet.gui.GSNPanel.GSNPanel;

/**
 * @author sam
 *
 */
public class GSNMenuBar extends JMenuBar implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2627609337586570432L;
	GSNPanel thePanel;
	JMenu 
			fileMenu = new JMenu("File"),
			f_save_as=new JMenu("Save As..."),
			transformMenu = new JMenu("Transform"), 
			algorithmMenu = new JMenu("Algorithm"), 
			datasetMenu = new JMenu("Data"),
			viewMenu = new JMenu("View With"),
			helpMenu = new JMenu("Help");
	JMenuItem 
			f_open,f_save,f_save_all,f_quit,							//file menu
			sa_csv,sa_pajek,sa_shapefile,sa_ucinet,sa_excel,					//save as menu
			t_flip,t_rotate,t_resize,t_match_best,t_find_duplicate,	//transform menu
			a_qap,a_snb,a_borders,a_hilite_edges,								//algorithm menu
			d_clear,d_find,d_merge,d_seperate,d_add,d_add_ego,				//data menu
			v_pajek,v_ucinet,v_g_earth,v_g_maps,v_egonet,v_geotools,v_arcgis,				//view in menu
			h_about,h_help;											//help menu
	
	GSNStatusBarInterface status;			//the staus bar. for rollovers
	
	public GSNMenuBar(){
		//file menu
		f_open = createMenuItem(fileMenu,"Open...",KeyEvent.VK_O,"Open a file.","actions","document-open","open");
		f_save = createMenuItem(fileMenu,"Save...",KeyEvent.VK_S,"Save currently selected data set.","actions","document-save","save");
		//f_save_as = createMenuItem(fileMenu,"Save As...",KeyEvent.VK_SHIFT+KeyEvent.VK_S,"Save as a different file.","general","document-save-as","save_as");
		fileMenu.add(f_save_as);
		f_save_all = createMenuItem(fileMenu,"Save All",KeyEvent.VK_SHIFT+KeyEvent.VK_A,"Save all data sets.","actions","document-save-all","save_all");
		fileMenu.addSeparator();
		f_quit = createMenuItem(fileMenu,"Quit",KeyEvent.VK_Q,"Exit Program","general","Stop","exit");

		sa_csv = createMenuItem(f_save_as,"CSV",KeyEvent.VK_M,"Save as a CSV file.","general","SaveAs","save_as_csv");
		sa_pajek = createMenuItem(f_save_as,"Pajek",KeyEvent.VK_N,"Save as a Pajek file..","general","SaveAs","save_as_pajek");
		sa_shapefile = createMenuItem(f_save_as,"Shapefile",KeyEvent.VK_Z,"Save as a different file.","general","SaveAs","save_as_shapefile");
		sa_ucinet = createMenuItem(f_save_as,"DL / UCINet",KeyEvent.VK_D,"Save as a different file.","general","SaveAs","save_as_ucinet");
		sa_excel = createMenuItem(f_save_as,"Excel",KeyEvent.VK_C,"Save as a different file.","general","SaveAs","save_as_excel");
		
		//transform menu
		t_flip = createMenuItem(transformMenu,"Flip",KeyEvent.VK_T,"Flip","general","Redo","transform_flip");
		t_rotate = createMenuItem(transformMenu,"Rotate",KeyEvent.VK_R,"Rotate","general","Refresh","transform_rotate");
		t_resize = createMenuItem(transformMenu,"Resize",KeyEvent.VK_Q,"Resize","general","ZoomIn","transform_resize");
		t_match_best = createMenuItem(transformMenu,"Match Best",KeyEvent.VK_Q,"Fuzzy logic for finding duplicates","general","Preferences","transform_match_best");
		t_find_duplicate = createMenuItem(transformMenu,"Find Similar",KeyEvent.VK_Q,"Find duplicate items to the selected item","general","Preferences","find_similar");
		

		//algorithm menu
		a_qap = createMenuItem(algorithmMenu,"QAP (Mantel's Test)",KeyEvent.VK_P,"Compare two matrices using Mantel's test.","general","History","algorithm_qap");
		a_snb = createMenuItem(algorithmMenu,"Spatial Network Bias",KeyEvent.VK_E,"Run a spatial network bias on the selected data.","general","History","algorithm_snb");
		a_borders = createMenuItem(algorithmMenu,"Borders",KeyEvent.VK_B,"Borders","general","History","algorithm_borders");
		a_hilite_edges = createMenuItem(algorithmMenu,"Highlight Edges",KeyEvent.VK_H,"Highlight Edges","general","History","algorithm_hilite_edges");

		//dataset menu
		d_find = createMenuItem(datasetMenu,"Clear",KeyEvent.VK_MINUS,"Clear all data","general","Delete","data_clear");
		d_clear = createMenuItem(datasetMenu,"Find...",KeyEvent.VK_F,"Find content within data","general","Search","data_find");
		d_merge = createMenuItem(datasetMenu,"Merge",KeyEvent.VK_M,"Merge this data set with another.","general","Import","data_merge");
		d_seperate = createMenuItem(datasetMenu,"Seperate",KeyEvent.VK_DIVIDE,"Seperate data set into multiple sets.","table","RowDelete","data_separate");
		d_add = createMenuItem(datasetMenu,"Add...",KeyEvent.VK_ADD,"Add data to the existing data.","general","Add","data_add");
		d_add_ego = createMenuItem(datasetMenu,"Add Ego...",KeyEvent.VK_SHIFT+KeyEvent.VK_ADD,"Add an ego file to the current data set.","general","Add","data_add_ego");

		//view with menu
		v_pajek = createMenuItem(viewMenu,"Pajek",KeyEvent.VK_F,"Open current data set in Pajek.","development","WebComponentAdd","view_pajek");
		v_egonet = createMenuItem(viewMenu,"EgoNet",KeyEvent.VK_ADD,"Open current data set in EgoNet.","development","WebComponentAdd","view_egonet");
		v_ucinet = createMenuItem(viewMenu,"UCINet",KeyEvent.VK_SHIFT+KeyEvent.VK_ADD,"Open current data set in ucinet.","development","WebComponentAdd","view_ucinet");
		v_geotools = createMenuItem(viewMenu,"GeoTools",KeyEvent.VK_ADD,"Open current data set in GeoTools.","development","WebComponentAdd","view_geotools");
		v_arcgis = createMenuItem(viewMenu,"ArcGIS",KeyEvent.VK_SHIFT+KeyEvent.VK_ADD,"Open current data set in ArcGIS.","development","WebComponentAdd","view_arcgis");
		v_g_earth = createMenuItem(viewMenu,"Google Earth",KeyEvent.VK_M,"Open current data set in Google Earth.","development","WebComponentAdd","view_g_earth");
		v_g_maps = createMenuItem(viewMenu,"Google Maps",KeyEvent.VK_DIVIDE,"Open current data set in Google Maps.","development","WebComponentAdd","view_g_maps");
		
		
		//help menu
		h_about = createMenuItem(helpMenu,"About...",KeyEvent.VK_F12,"Information about GISpatialnet","general","TipOfTheDay","show_about");
		h_help = createMenuItem(helpMenu,"Help...",KeyEvent.VK_F1,"Display online help","general","About","show_help");
		
		//add top-level items
		this.add(fileMenu);
		this.add(transformMenu);
		this.add(algorithmMenu);
		this.add(datasetMenu);
		this.add(viewMenu);
		this.add(helpMenu);
		
	}
	public JMenu getFileMenu(){return fileMenu;}
	public JMenu getTransformMenu(){return transformMenu;}
	public JMenu getAlgorithmMenu(){return algorithmMenu;}
	public JMenu getDataSetMenu(){return datasetMenu;}
	public JMenu getHelpMenu(){return helpMenu;}
	public JMenu getViewMenu(){return viewMenu;}
	
	public void setActionPanel(GSNPanel gsp){
		this.thePanel=gsp;
		registerPanel();
	}
	private void registerPanel(){
		f_quit.addActionListener(thePanel);
		f_open.addActionListener(thePanel);
		f_save.addActionListener(thePanel);
		f_save_as.addActionListener(thePanel);
		f_save_all.addActionListener(thePanel);
		f_quit.addActionListener(thePanel);
		sa_csv.addActionListener(thePanel);
		sa_pajek.addActionListener(thePanel);
		sa_shapefile.addActionListener(thePanel);
		sa_ucinet.addActionListener(thePanel);
		sa_excel.addActionListener(thePanel);

		t_flip.addActionListener(thePanel);
		t_rotate.addActionListener(thePanel);
		t_resize.addActionListener(thePanel);
		t_match_best.addActionListener(thePanel);
		t_find_duplicate.addActionListener(thePanel);
		a_qap.addActionListener(thePanel);
		a_snb.addActionListener(thePanel);
		a_borders.addActionListener(thePanel);
		a_hilite_edges.addActionListener(thePanel);
		d_clear.addActionListener(thePanel);
		d_find.addActionListener(thePanel);
		d_merge.addActionListener(thePanel);
		d_seperate.addActionListener(thePanel);
		d_add.addActionListener(thePanel);
		d_add_ego.addActionListener(thePanel);
		h_about.addActionListener(thePanel);
		h_help.addActionListener(thePanel);
		v_pajek.addActionListener(thePanel);
		v_ucinet.addActionListener(thePanel);
		v_g_earth.addActionListener(thePanel);
		v_g_maps.addActionListener(thePanel);
		v_egonet.addActionListener(thePanel);
		v_geotools.addActionListener(thePanel);
		v_arcgis.addActionListener(thePanel);
	}

	public JMenuItem createMenuItem(JMenu parent, String label, int mnemonic, String desc, String iconCat,String iconName, String actionCommand){
		JMenuItem tmp = new JMenuItem(label);
		tmp.setMnemonic(mnemonic);
		tmp.getAccessibleContext().setAccessibleDescription(desc);
		tmp.setIcon(GUIutil.getTangoIcon(iconCat,iconName,16));
		tmp.setActionCommand(actionCommand);
		tmp.addMouseListener(this);
		parent.add(tmp);
		return tmp;
	}
	
	public void setStatusBar(GSNStatusBarInterface sb){this.status=sb;}

	public void mouseEntered(MouseEvent e) {
		JMenuItem theItem = (JMenuItem)e.getSource();
		//System.out.println("MouseEnter: Menu: "+theItem.getActionCommand());
		this.status.setStatus(theItem.getAccessibleContext().getAccessibleDescription());
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {
		//JMenuItem theItem = (JMenuItem)e.getSource();
		//System.out.println("MouseExit: Menu: "+theItem.getActionCommand());
		this.status.setStatus(" ");
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
