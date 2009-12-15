package us.jonesrychtar.gispatialnet.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.net.URL;
import java.util.Enumeration;
import javax.swing.UIDefaults;

/**
 * @author sam
 *
 */
public class util {

	//convenience functions for retrieving icons from the jlfgr-1_0.jar file
	public static ImageIcon getGeneralSmallIcon(String iconName){return getIcon("general",iconName,16);}
	public static ImageIcon getGeneralIcon(String iconName){return getIcon("general",iconName,24);}
	public static ImageIcon getDevelopmentSmallIcon(String iconName){return getIcon("development",iconName,16);}
	public static ImageIcon getDevelopmentIcon(String iconName){return getIcon("development",iconName,24);}
	public static ImageIcon getMediaSmallIcon(String iconName){return getIcon("media",iconName,16);}
	public static ImageIcon getMediaIcon(String iconName){return getIcon("media",iconName,24);}
	public static ImageIcon getNavigationSmallIcon(String iconName){return getIcon("navigation",iconName,16);}
	public static ImageIcon getNavigationIcon(String iconName){return getIcon("navigation",iconName,24);}
	public static ImageIcon getTableSmallIcon(String iconName){return getIcon("table",iconName,16);}
	public static ImageIcon getTableIcon(String iconName){return getIcon("table",iconName,24);}
	public static ImageIcon getTextSmallIcon(String iconName){return getIcon("text",iconName,16);}
	public static ImageIcon getTextIcon(String iconName){return getIcon("text",iconName,24);}

	
	/**Retrieves icons from the jlfgr-1_0.jar file. There are several convenience methods for this function.
	 * They are in the form of getXXXIcon(iconName), where XXX is the category name (folder name)
	 * and whether we want small (16x16) icons. For example, if we want to get the file 
	 * /toolbarButtonGraphics/general/SaveAs16.gif,
	 * we would use getGeneralSmallIcon("SaveAs"). If we want the 24x24 size version, we would use 
	 * getGeneralIcon("SaveAs"). You can find these icons for download, as well as more information, from 
	 * http://java.sun.com/developer/techDocs/hi/repository/
	 * @param category the category name. corresponds to the folder under /toolbarButtonGraphics/
	 * @param iconName the name of the icon. corresponds to the file under /toolbarButtonGraphics/[category]/iconName[16|24].gif
	 * @param size whether we want 16x16 (size=16) or 24x24 (size=24) icons
	 * @return an ImageIcon which represents the given parameters
	 */
	public static ImageIcon getIcon(String category, String iconName, int size){
		URL url=util.class.getResource("/toolbarButtonGraphics/"+category+"/"+iconName+size+".gif");
		if(url != null){
				return new ImageIcon(url);
		}else{
			System.err.println("Could not find icon "+"/toolbarButtonGraphics/"+category+"/"+iconName+size+".gif");
			return null;
		}
	}

	/** Creates and adds a toolbar button to the specified JToolBar.
	 * @param tb the JToolBar to add the button to
	 * @param label the label of the button
	 * @param icon the icon name for the icon (see {@link getIcon} for icon name criteria)
	 * @param actionCmd	the ActionCommand for the button
	 * @return a reference to the added JButton
	 */
	public static JButton addToolBarButton(JToolBar tb,String label, String desc, String icon, String actionCmd){
		JButton tmp = new JButton(label);
		tmp.setActionCommand(actionCmd);
		tmp.setIcon(util.getGeneralIcon(icon));
		tmp.setVerticalTextPosition(JButton.BOTTOM);
		tmp.setHorizontalTextPosition(JButton.CENTER);
		tmp.setToolTipText(desc);
		tb.add(tmp);
		return tmp;
	}

	public static void printIconKeys() {
		UIManager.LookAndFeelInfo looks[] = UIManager
				.getInstalledLookAndFeels();

		for (UIManager.LookAndFeelInfo info : looks) {
			try {
				UIManager.setLookAndFeel(info.getClassName());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			UIDefaults defaults = UIManager.getDefaults();
			Enumeration newKeys = defaults.keys();

			while (newKeys.hasMoreElements()) {
				Object obj = newKeys.nextElement();
				System.out.printf("%50s : %s\n", obj, UIManager.get(obj));
			}

		}
	}

	public static void main(String[] args){
		printIconKeys();
	}
}
