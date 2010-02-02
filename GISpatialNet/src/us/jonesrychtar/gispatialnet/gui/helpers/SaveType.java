/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui.helpers;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import us.jonesrychtar.gispatialnet.Enums;

/**
 * @author Sam
 *
 */
public class SaveType extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JRadioButton csv = null;
	private JRadioButton excel = null;
	private JRadioButton shp = null;
	private JRadioButton pajek = null;
	private JRadioButton dl = null;
	private JLabel jLabel = null;
	private ButtonGroup group = new ButtonGroup();
	private Enums.FileType type=Enums.FileType.CSV;  //  @jve:decl-index=0:
	private JButton jButton = null;
	/**
	 * @param owner
	 */
	public SaveType() {
		//super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(348, 237);
		this.setTitle("Save As...");
		this.setName("SaveAsDialog");
		this.setModal(true);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jLabel.setText("Please select the file format you would like to save as.");
			jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
			jContentPane = new JPanel();
			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.Y_AXIS));
			jContentPane.add(jLabel, null);
			jContentPane.add(getCsv(), null);
			jContentPane.add(getExcel(), null);
			jContentPane.add(getShp(), null);
			jContentPane.add(getPajek(), null);
			jContentPane.add(getDl(), null);
			jContentPane.add(getJButton(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes csv	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCsv() {
		if (csv == null) {
			csv = new JRadioButton();
			csv.setText("Comma Separated Value [.csv]");
			csv.setSelected(true);
			csv.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setType(Enums.FileType.CSV);
				}
			});
			group.add(csv);
		}
		return csv;
	}

	/**
	 * This method initializes excel	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getExcel() {
		if (excel == null) {
			excel = new JRadioButton();
			excel.setText("Excel Spreadsheel [.xls]");
			csv.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setType(Enums.FileType.EXCEL);
				}
			});
			group.add(excel);
		}
		return excel;
	}

	/**
	 * This method initializes shp	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getShp() {
		if (shp == null) {
			shp = new JRadioButton();
			shp.setText("Sahepfile [.shp]");
			csv.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setType(Enums.FileType.SHAPEFILE);
				}
			});
			group.add(shp);
		}
		return shp;
	}

	/**
	 * This method initializes pajek	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getPajek() {
		if (pajek == null) {
			pajek = new JRadioButton();
			pajek.setText("Pajek");
			csv.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setType(Enums.FileType.PAJEK);
				}
			});
			group.add(pajek);
		}
		return pajek;
	}

	/**
	 * This method initializes dl	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDl() {
		if (dl == null) {
			dl = new JRadioButton();
			dl.setText("DL/UCINet");
			csv.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setType(Enums.FileType.UCINET);
				}
			});
			group.add(dl);
		}
		return dl;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("OK");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					((JButton)e.getSource()).getParent().getParent().getParent().setVisible(false);
				}
			});
		}
		return jButton;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Enums.FileType type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Enums.FileType getType() {
		return type;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
