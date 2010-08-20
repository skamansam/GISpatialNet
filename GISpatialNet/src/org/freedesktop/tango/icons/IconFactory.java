/**
 * 
 */
package org.freedesktop.tango.icons;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.fop.svg.GraphicsConfiguration;
/*import org.jdesktop.jdic.filetypes.AssociationService;
import org.jdesktop.jdic.filetypes.Association;*/
import javax.swing.filechooser.FileSystemView; 
import javax.swing.Icon;
import javax.swing.ImageIcon;

import us.jonesrychtar.gispatialnet.gui.GUIutil;

/**
 * @author sctyler
 *
 */
public class IconFactory {
	
	public static ImageIcon getActionSmallIcon(String iconName){return getIcon("actions",iconName,16);}
	public static ImageIcon getActionLargeIcon(String iconName){return getIcon("actions",iconName,32);}
	public static ImageIcon getActionIcon(String iconName){return getIcon("actions",iconName,22);}

	public static ImageIcon getAnimationSmallIcon(String iconName){return getIcon("animations",iconName,16);}
	public static ImageIcon getAnimationLargeIcon(String iconName){return getIcon("animations",iconName,32);}
	public static ImageIcon getAnimationIcon(String iconName){return getIcon("animations",iconName,22);}
	
	public static ImageIcon getAppSmallIcon(String iconName){return getIcon("apps",iconName,16);}
	public static ImageIcon getAppLargeIcon(String iconName){return getIcon("apps",iconName,32);}
	public static ImageIcon getAppIcon(String iconName){return getIcon("apps",iconName,22);}

	public static ImageIcon getCategorySmallIcon(String iconName){return getIcon("categories",iconName,16);}
	public static ImageIcon getCategoryLargeIcon(String iconName){return getIcon("categories",iconName,32);}
	public static ImageIcon getCategoryIcon(String iconName){return getIcon("categories",iconName,22);}

	public static ImageIcon getDeviceSmallIcon(String iconName){return getIcon("devices",iconName,16);}
	public static ImageIcon getDeviceLargeIcon(String iconName){return getIcon("devices",iconName,16);}
	public static ImageIcon getDeviceIcon(String iconName){return getIcon("devices",iconName,16);}

	public static ImageIcon getEmblemSmallIcon(String iconName){return getIcon("emblems",iconName,16);}
	public static ImageIcon getEmblemLargeIcon(String iconName){return getIcon("emblems",iconName,32);}
	public static ImageIcon getEmblemIcon(String iconName){return getIcon("emblems",iconName,22);}

	public static ImageIcon getEmoteSmallIcon(String iconName){return getIcon("emotes",iconName,16);}
	public static ImageIcon getEmoteLargeIcon(String iconName){return getIcon("emotes",iconName,32);}
	public static ImageIcon getEmoteIcon(String iconName){return getIcon("emotes",iconName,22);}

	public static ImageIcon getMimeTypeSmallIcon(String iconName){return getIcon("mimetypes",iconName,16);}
	public static ImageIcon getMimeTypeLargeIcon(String iconName){return getIcon("mimetypes",iconName,32);}
	public static ImageIcon getMimeTypeIcon(String iconName){return getIcon("mimetypes",iconName,22);}

	public static ImageIcon getPlaceSmallIcon(String iconName){return getIcon("places",iconName,16);}
	public static ImageIcon getPlaceLargeIcon(String iconName){return getIcon("places",iconName,32);}
	public static ImageIcon getPlaceIcon(String iconName){return getIcon("places",iconName,22);}

	public static ImageIcon getStatusSmallIcon(String iconName){return getIcon("status",iconName,16);}
	public static ImageIcon getStatusLargeIcon(String iconName){return getIcon("status",iconName,32);}
	public static ImageIcon getStatusIcon(String iconName){return getIcon("status",iconName,22);}
	
	public static ImageIcon getInfoSmallIcon(){return getIcon("status","dialog-information",16);}
	public static ImageIcon getInfoIcon(){return getIcon("status","dialog-information",22);}
	public static ImageIcon getInfoLargeIcon(){return getIcon("status","dialog-information",32);}

	public static ImageIcon getErrorSmallIcon(){return getIcon("status","dialog-error",16);}
	public static ImageIcon getErrorIcon(){return getIcon("status","dialog-error",22);}
	public static ImageIcon getErrorLargeIcon(){return getIcon("status","dialog-error",32);}
	
	public static ImageIcon getWarningSmallIcon(){return getIcon("status","dialog-warning",16);}
	public static ImageIcon getWarningIcon(){return getIcon("status","dialog-warning",22);}
	public static ImageIcon getWarningLargeIcon(){return getIcon("status","dialog-warning",32);}
	
	
	/** This method returns the ImageIcon which represents one of the Tango Icon files. 
	 * @param category the category name. corresponds to the folder under org.freedesktop.tango.icons/[size]x[size]/
	 * @param iconName the name of the icon. corresponds to the file under org.freedesktop.tango.icons/[size]x[size]/[category].gif
	 * @param size whether we want 16x16 (size=16) or 22x22 (size=24) or 32x32 (size=32) icons. Nearest values will be rounded.
	 * @return an ImageIcon which represents the given parameters
	 */
	public static ImageIcon getIcon(String category, String iconName, int size){
		if(category.equals("_MIME_"))return getFileTypeIcon(iconName,size);
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
	 * This method retrieves the icon for a given filetype. It will return on the first unique match (it fails if the icon is the default "unknown" icon). If there is no unique icon, the last value in the suffix list will be used to call getIcon().
	 * @param suffix A string list of suffixes for the file icon you want. The last value is category/iconName for the fallback icon. (ex: "txt,html,mimetypes/text-html")
	 * @param size the icon size. Only used for fallback icon.
	 * @return the ImageIcon
	 */
	public static ImageIcon getFileTypeIcon(String suffix,int size){
		String[] exts=suffix.split(",");
		//System.out.println("Looking for extensions for "+exts.length);
		ImageIcon ico = new ImageIcon();
		ImageIcon dummy = new ImageIcon();
		String[] fallback=exts[exts.length-1].split("/");
		
		//create dummy icon so we can compare non-existant icons with custom icons
		try{
			File f = File.createTempFile("icon", ".unknown");
			dummy = icon2ImageIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
			f.delete();
		}catch (IOException ioe){
			System.out.println("Cannot create dummy image.");
		}

		for(int i=0;i<exts.length-1;i++){
			//System.out.print("MIME icon for "+exts[i]+"\n");
			try{
				File f = File.createTempFile("icon", "." + exts[i]);
				ico = icon2ImageIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
				f.delete();
				if(ico.getIconHeight()==0 || ico.getIconWidth()==0||ico.getImage().equals(dummy.getImage()))continue;
			}catch (IOException ioe){
				continue;
			}
			break;
		}
		//use fallback icon
		if(ico.getImage().equals(dummy.getImage())){
			ico=getIcon(fallback[0],fallback[1],size);
		}

		return ico;
	}

	public static ImageIcon icon2ImageIcon(Icon icon) {
	    if (icon instanceof ImageIcon) {
	        return ((ImageIcon)icon);
	    } else {
	        int w = icon.getIconWidth();
	        int h = icon.getIconHeight();
	        GraphicsEnvironment ge =
	          GraphicsEnvironment.getLocalGraphicsEnvironment();
	        GraphicsDevice gd = ge.getDefaultScreenDevice();
	        GraphicsConfiguration gc = (GraphicsConfiguration) gd.getDefaultConfiguration();
	        BufferedImage image = gc.createCompatibleImage(w, h);
	        Graphics2D g = image.createGraphics();
	        icon.paintIcon(null, g, 0, 0);
	        g.dispose();
	        return new ImageIcon(image);
	    }
	} 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
