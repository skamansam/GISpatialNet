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

/**
 * @author sam
 *
 */
public class DataDisplayPanel extends JPanel {
	GraphPanel theGraph = new GraphPanel(MatrixFactory.emptyMatrix());
	MatrixEditorPanel theEditor = new MatrixEditorPanel(new MatrixGUIObject(MatrixFactory.emptyMatrix()));
	JTabbedPane tabs = new JTabbedPane();
	public DataDisplayPanel(){
		super();
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		this.add(tabs);
		tabs.addTab("Data Editor", theEditor);
		tabs.addTab("Data Graph", theGraph);
//		this.add(theEditor);
	}
	
	public void displayMatrix(Matrix m){
//		GraphPanel g = new GraphPanel(m);
		theEditor.setMatrix(new MatrixGUIObject(m));
//		this.add(g);
	}
}
