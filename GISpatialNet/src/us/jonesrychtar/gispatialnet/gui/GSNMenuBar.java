/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

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
			transformMenu = new JMenu("Transform"), 
			algorithmMenu = new JMenu("Algorithm"), 
			datasetMenu = new JMenu("Data"),
			helpMenu = new JMenu("Help");
	JMenuItem 
			f_open,f_save,f_save_as,f_quit,							//file menu
			t_flip,t_rotate,t_resize,t_match_best,t_find_duplicate,	//transform menu
			a_qap,a_snb,a_borders,									//algorithm menu
			d_find,d_merge,d_seperate,d_add,d_add_ego,				//data menu
			h_about,h_help;											//help menu
	
	GSNStatusBarInterface status;			//the staus bar. for rollovers
	
	public GSNMenuBar(){
		//file menu
		f_open = createMenuItem(fileMenu,"Open",KeyEvent.VK_O,"Open a file.","general","Open","open");
		f_save = createMenuItem(fileMenu,"Save",KeyEvent.VK_S,"Save currently selected data set.","general","Save","save");
		f_save_as = createMenuItem(fileMenu,"Save As",KeyEvent.VK_SHIFT+KeyEvent.VK_S,"Save all data sets.","general","SaveAs","save_as");
		fileMenu.addSeparator();
		f_quit = createMenuItem(fileMenu,"Quit",KeyEvent.VK_Q,"Exit Program","general","Stop","exit");
		
		//transform menu
		t_flip = createMenuItem(transformMenu,"Flip",KeyEvent.VK_T,"Flip","general","Stop","transform_flip");
		t_rotate = createMenuItem(transformMenu,"Rotate",KeyEvent.VK_R,"Rotate","general","Stop","transform_rotate");
		t_resize = createMenuItem(transformMenu,"Resize",KeyEvent.VK_Q,"Resize","general","Stop","transform_resize");
		t_match_best = createMenuItem(transformMenu,"Match Best",KeyEvent.VK_Q,"Fuzzy logic for finding duplicates","general","Stop","transform_match_best");
		t_find_duplicate = createMenuItem(transformMenu,"Find Similar",KeyEvent.VK_Q,"Find duplicate items to the selected item","general","Stop","find_similar");
		

		//algorithm menu
		a_qap = createMenuItem(algorithmMenu,"QAP",KeyEvent.VK_P,"Exit Program","general","Stop","algorithm_qap");
		a_snb = createMenuItem(algorithmMenu,"Spatial Network Bias",KeyEvent.VK_E,"Exit Program","general","Stop","algorithm_snb");
		a_borders = createMenuItem(algorithmMenu,"Borders",KeyEvent.VK_B,"Borders","general","Stop","algorithm_borders");

		//dataset menu
		d_find = createMenuItem(datasetMenu,"Find",KeyEvent.VK_F,"Find content within data","general","Stop","data_find");
		d_merge = createMenuItem(datasetMenu,"Merge",KeyEvent.VK_M,"Merge this data set with another.","general","Stop","data_merge");
		d_seperate = createMenuItem(datasetMenu,"Seperate",KeyEvent.VK_DIVIDE,"Seperate data set into multiple sets.","general","Stop","data_seperate");
		d_add = createMenuItem(datasetMenu,"Add",KeyEvent.VK_ADD,"Add data to the existing data.","general","Add","data_add");
		d_add_ego = createMenuItem(datasetMenu,"Add Ego",KeyEvent.VK_SHIFT+KeyEvent.VK_ADD,"Add an ego file to the current data set.","general","Add","data_add_ego");

		
		//help menu
		h_about = createMenuItem(helpMenu,"About",KeyEvent.VK_F12,"Information about GISpatialnet","general","TipOfTheDay","show_about");
		h_help = createMenuItem(helpMenu,"Help",KeyEvent.VK_F1,"Display online help","general","About","show_help");
		
		//add top-level items
		this.add(fileMenu);
		this.add(transformMenu);
		this.add(algorithmMenu);
		this.add(datasetMenu);
		this.add(helpMenu);
		
	}
	public JMenu getFileMenu(){return fileMenu;}
	public JMenu getTransformMenu(){return transformMenu;}
	public JMenu getAlgorithmMenu(){return algorithmMenu;}
	public JMenu getDataSetMenu(){return datasetMenu;}
	public JMenu getHelpMenu(){return helpMenu;}
	
	public void setActionPanel(GSNPanel gsp){
		this.thePanel=gsp;
		registerPanel();
	}
	private void registerPanel(){
		f_quit.addActionListener(thePanel);
		f_open.addActionListener(thePanel);
		f_save.addActionListener(thePanel);
		f_save_as.addActionListener(thePanel);
		f_quit.addActionListener(thePanel);
		t_flip.addActionListener(thePanel);
		t_rotate.addActionListener(thePanel);
		t_resize.addActionListener(thePanel);
		t_match_best.addActionListener(thePanel);
		t_find_duplicate.addActionListener(thePanel);
		a_qap.addActionListener(thePanel);
		a_snb.addActionListener(thePanel);
		a_borders.addActionListener(thePanel);
		d_find.addActionListener(thePanel);
		d_merge.addActionListener(thePanel);
		d_seperate.addActionListener(thePanel);
		d_add.addActionListener(thePanel);
		d_add_ego.addActionListener(thePanel);
		h_about.addActionListener(thePanel);
		h_help.addActionListener(thePanel);
		
	}

	public JMenuItem createMenuItem(JMenu parent, String label, int mnemonic, String desc, String iconCat,String iconName, String actionCommand){
		JMenuItem tmp = new JMenuItem(label);
		tmp.setMnemonic(mnemonic);
		tmp.getAccessibleContext().setAccessibleDescription(desc);
		tmp.setIcon(util.getIcon(iconCat,iconName,16));
		tmp.setActionCommand(actionCommand);
		tmp.addMouseListener(this);
		parent.add(tmp);
		return tmp;
	}
	
	public void setStatusBar(GSNStatusBarInterface sb){this.status=sb;}

	@Override
	public void mouseEntered(MouseEvent e) {
		JMenuItem theItem = (JMenuItem)e.getSource();
		//System.out.println("MouseEnter: Menu: "+theItem.getActionCommand());
		this.status.setStatus(theItem.getAccessibleContext().getAccessibleDescription());
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {
		//JMenuItem theItem = (JMenuItem)e.getSource();
		//System.out.println("MouseExit: Menu: "+theItem.getActionCommand());
		this.status.setStatus("");
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
