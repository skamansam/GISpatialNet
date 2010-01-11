package us.jonesrychtar.gispatialnet.gui;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class GSNStatusBar extends JPanel implements GSNStatusBarInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = -220976292251375812L;
	private Vector<String> text;
	private JLabel theLabel=new JLabel("GSNSpatialnet");
	private int displayTime = 3000;

	public int getDisplayTime() {return displayTime;}
	public void setDisplayTime(int displayTime) {this.displayTime = displayTime;}

	private void setup(){
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		theLabel.setAlignmentX(LEFT_ALIGNMENT);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		this.add(theLabel);
	}
	
	public GSNStatusBar(){
		setup();
	}

	public GSNStatusBar(String startText){
		setup();
		setStatus(startText);
	}

	/**
	 * @param text the text to set
	 */
	public void setStatus(String text) {
		theLabel.setText(text);
	}

	/**
	 * @return the text
	 */
	public String getStatus() {return text.get(text.size()-1);}
	
	//private void pop(){text.remove(text.size()-1);}
	//private void push(String s){text.add(s);}

}
