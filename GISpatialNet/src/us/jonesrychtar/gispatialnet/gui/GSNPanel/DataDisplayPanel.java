/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.GSNPanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.gui.MatrixGUIObject;
import org.ujmp.gui.panels.GraphPanel;
import org.ujmp.gui.panels.MatrixEditorPanel;
import org.ujmp.jung.MatrixGraphPanel;
import org.ujmp.jung.JungGraphPanel;

/**
 * @author sam
 *
 */
public class DataDisplayPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7223760456685131311L;
	//MatrixGraphPanel theGraph = new MatrixGraphPanel();
	//MatrixGraphPanel theGraph = new MatrixGraphPanel();
	MatrixEditorPanel theEditor = new MatrixEditorPanel(new MatrixGUIObject(MatrixFactory.emptyMatrix()));
	JTabbedPane tabs = new JTabbedPane();
	public DataDisplayPanel(){
		super();
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		this.add(tabs);
		tabs.addTab("Data Editor", theEditor);
		//tabs.addTab("Data Graph", theGraph);
//		this.add(theEditor);
	}
	
	public void displayMatrix(Matrix m){
		//GraphPanel g = new GraphPanel(m);
		theEditor.setMatrix(new MatrixGUIObject(m));
		//if(tabs.getComponentCount()>=2) tabs.remove(1);
		//GraphPanel theGraph = new GraphPanel(m);
		//tabs.addTab("Data Graph", theGraph);		
		//theGraph.setMatrix(new MatrixGUIObject(m));
//		this.add(g);
	}
}
