/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

/**
 * @author sam
 *
 */
public class GSNMenuBar extends JMenuBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2627609337586570432L;
	JMenu 
			fileMenu = new JMenu("File"),
			transformMenu = new JMenu("Transform"), 
			algorithmMenu = new JMenu("Algorithm"), 
			datasetMenu = new JMenu("Data"),
			helpMenu = new JMenu("Help");
	JMenuItem 
			f_open = new JMenuItem("Open"), 
			f_save = new JMenuItem("Save"), 
			f_save_as = new JMenuItem("Save As"), 
			f_quit = new JMenuItem("Quit"),
			t_flip = new JMenuItem("Flip"),
			t_rotate = new JMenuItem("Rotate"), 
			t_resize = new JMenuItem("Resize"),
			t_match_best = new JMenuItem("Match Best"),
			t_find_duplicate = new JMenuItem("Find Duplicate"),
			a_qap = new JMenuItem("QAP"),
			a_snb = new JMenuItem("Spatial Network Bias"),
			d_find =  new JMenuItem("Find..."),
			d_merge = new JMenuItem("Merge"), 
			d_seperate = new JMenuItem("Split"),
			d_add = new JMenuItem("Add DataSet"), 
			d_add_ego = new JMenuItem("Add Ego"),
			h_about = new JMenuItem("About"),
			h_help = new JMenuItem("Help");
	GSNStatusBarInterface status;
	
	public GSNMenuBar(){
		//file menu
		f_open.setMnemonic(KeyEvent.VK_O);
		f_open.setIcon(util.getGeneralSmallIcon("Open"));
		f_open.getAccessibleContext().setAccessibleDescription("Open a file.");
		fileMenu.add(f_open);
		f_save.setMnemonic(KeyEvent.VK_S);
		f_save.setIcon(util.getGeneralSmallIcon("Save"));
		f_save.getAccessibleContext().setAccessibleDescription("Save Currently Selected File");
		fileMenu.add(f_save);
		f_save_as.setMnemonic(KeyEvent.VK_SHIFT+KeyEvent.VK_S);
		f_save_as.setIcon(util.getGeneralSmallIcon("SaveAs"));
		f_save_as.getAccessibleContext().setAccessibleDescription("Save All Files.");
		fileMenu.add(f_save_as);
		fileMenu.addSeparator();
		f_quit.setMnemonic(KeyEvent.VK_Q);
		f_quit.setIcon(util.getGeneralSmallIcon("Remove"));
		f_quit.getAccessibleContext().setAccessibleDescription("Quit");
		fileMenu.add(f_quit);
		
		//transform menu
		t_flip.setMnemonic(KeyEvent.SHIFT_DOWN_MASK+KeyEvent.VK_DOWN);
		t_flip.getAccessibleContext().setAccessibleDescription("Open a file.");
		transformMenu.add(t_flip);
		t_rotate.setMnemonic(KeyEvent.VK_Q);
		t_rotate.getAccessibleContext().setAccessibleDescription("Open a file.");
		transformMenu.add(t_rotate);
		t_resize.setMnemonic(KeyEvent.VK_Q);
		t_resize.getAccessibleContext().setAccessibleDescription("Open a file.");
		transformMenu.add(t_resize);
		t_match_best.setMnemonic(KeyEvent.VK_Q);
		t_match_best.getAccessibleContext().setAccessibleDescription("Open a file.");
		transformMenu.add(t_match_best);
		t_find_duplicate.setMnemonic(KeyEvent.VK_Q);
		t_find_duplicate.getAccessibleContext().setAccessibleDescription("Open a file.");
		transformMenu.add(t_find_duplicate);

		//algorithm menu
		a_qap.setMnemonic(KeyEvent.VK_Q);
		a_qap.getAccessibleContext().setAccessibleDescription("Open a file.");
		algorithmMenu.add(a_qap);
		a_snb.setMnemonic(KeyEvent.VK_Q);
		a_snb.getAccessibleContext().setAccessibleDescription("Open a file.");
		algorithmMenu.add(a_snb);

		//dataset menu
		d_find.setMnemonic(KeyEvent.VK_Q);
		d_find.getAccessibleContext().setAccessibleDescription("Open a file.");
		datasetMenu.add(d_find);
		d_merge.setMnemonic(KeyEvent.VK_Q);
		d_merge.getAccessibleContext().setAccessibleDescription("Open a file.");
		datasetMenu.add(d_merge);
		d_seperate.setMnemonic(KeyEvent.VK_Q);
		d_seperate.getAccessibleContext().setAccessibleDescription("Open a file.");
		datasetMenu.add(d_seperate);
		d_add.setMnemonic(KeyEvent.VK_Q);
		d_add.getAccessibleContext().setAccessibleDescription("Open a file.");
		datasetMenu.add(d_add);
		d_add_ego.setMnemonic(KeyEvent.VK_Q);
		d_add_ego.getAccessibleContext().setAccessibleDescription("Open a file.");
		datasetMenu.add(d_add_ego);
		
		//help menu
		h_about.setMnemonic(KeyEvent.VK_Q);
		h_about.getAccessibleContext().setAccessibleDescription("Open a file.");
		helpMenu.add(h_about);
		h_help.setMnemonic(KeyEvent.VK_Q);
		h_help.getAccessibleContext().setAccessibleDescription("Open a file.");
		helpMenu.add(h_help);
		
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
	
	public void setStatusBar(GSNStatusBarInterface sb){this.status=sb;}
}
