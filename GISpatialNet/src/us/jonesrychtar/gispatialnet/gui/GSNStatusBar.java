package us.jonesrychtar.gispatialnet.gui;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GSNStatusBar extends JPanel implements GSNStatusBarInterface{
	private Vector<String> text;
	private JLabel theLabel=new JLabel();
	private int displayTime = 3000;

	public int getDisplayTime() {return displayTime;}
	public void setDisplayTime(int displayTime) {this.displayTime = displayTime;}

	private void setup(){
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
		
	}

	/**
	 * @return the text
	 */
	public String getStatus() {return text.get(text.size()-1);}
	
	private void pop(){text.remove(text.size()-1);}
	private void push(String s){text.add(s);}

}
