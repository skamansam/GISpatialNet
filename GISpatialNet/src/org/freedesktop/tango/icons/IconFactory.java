/**
 * 
 */
package org.freedesktop.tango.icons;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import us.jonesrychtar.gispatialnet.gui.GUIutil;

/**
 * @author sctyler
 *
 */
public class IconFactory {
	
	public static ImageIcon getActionSmallIcon(String iconName){return getIcon("actions",iconName,16);}
	public static ImageIcon getActionLargeIcon(String iconName){return getIcon("actions",iconName,16);}
	public static ImageIcon getActionIcon(String iconName){return getIcon("actions",iconName,16);}

	public static ImageIcon getAnimationSmallIcon(String iconName){return getIcon("animations",iconName,16);}
	public static ImageIcon getAnimationLargeIcon(String iconName){return getIcon("animations",iconName,16);}
	public static ImageIcon getAnimationIcon(String iconName){return getIcon("animations",iconName,16);}
	
	public static ImageIcon getAppSmallIcon(String iconName){return getIcon("apps",iconName,16);}
	public static ImageIcon getAppLargeIcon(String iconName){return getIcon("apps",iconName,16);}
	public static ImageIcon getAppIcon(String iconName){return getIcon("apps",iconName,16);}

	public static ImageIcon getCategorySmallIcon(String iconName){return getIcon("categories",iconName,16);}
	public static ImageIcon getCategoryLargeIcon(String iconName){return getIcon("categories",iconName,16);}
	public static ImageIcon getCategoryIcon(String iconName){return getIcon("categories",iconName,16);}

	public static ImageIcon getDeviceSmallIcon(String iconName){return getIcon("devices",iconName,16);}
	public static ImageIcon getDeviceLargeIcon(String iconName){return getIcon("devices",iconName,16);}
	public static ImageIcon getDeviceIcon(String iconName){return getIcon("devices",iconName,16);}

	public static ImageIcon getEmblemSmallIcon(String iconName){return getIcon("emblems",iconName,16);}
	public static ImageIcon getEmblemLargeIcon(String iconName){return getIcon("emblems",iconName,16);}
	public static ImageIcon getEmblemIcon(String iconName){return getIcon("emblems",iconName,16);}

	public static ImageIcon getEmoteSmallIcon(String iconName){return getIcon("emotes",iconName,16);}
	public static ImageIcon getEmoteLargeIcon(String iconName){return getIcon("emotes",iconName,16);}
	public static ImageIcon getEmoteIcon(String iconName){return getIcon("emotes",iconName,16);}

	public static ImageIcon getMimeTypeSmallIcon(String iconName){return getIcon("mimetypes",iconName,16);}
	public static ImageIcon getMimeTypeLargeIcon(String iconName){return getIcon("mimetypes",iconName,16);}
	public static ImageIcon getMimeTypeIcon(String iconName){return getIcon("mimetypes",iconName,16);}

	public static ImageIcon getPlaceSmallIcon(String iconName){return getIcon("places",iconName,16);}
	public static ImageIcon getPlaceLargeIcon(String iconName){return getIcon("places",iconName,16);}
	public static ImageIcon getPlaceIcon(String iconName){return getIcon("places",iconName,16);}

	public static ImageIcon getStatusSmallIcon(String iconName){return getIcon("status",iconName,16);}
	public static ImageIcon getStatusLargeIcon(String iconName){return getIcon("status",iconName,16);}
	public static ImageIcon getStatusIcon(String iconName){return getIcon("status",iconName,16);}
	
	
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
		if(size==24)size=22;
		URL url=GUIutil.class.getResource("/org/freedesktop/tango/icons/"+size+'x'+size+"/"+category+"/"+iconName+".png");
		if(url != null){
				return new ImageIcon(url);
		}else{
			//System.err.println("Could not find icon "+"/toolbarButtonGraphics/"+category+"/"+iconName+size+".gif");
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
