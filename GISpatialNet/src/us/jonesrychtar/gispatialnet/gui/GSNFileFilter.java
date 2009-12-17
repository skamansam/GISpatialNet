/**
 * 
 */
package us.jonesrychtar.gispatialnet.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import us.jonesrychtar.gispatialnet.util;

/**
 * @author sam
 *
 */
public class GSNFileFilter extends FileFilter {
	public final String[] types = {"csv","txt","dl","net","clu","mac","xls","kml","shapefile"};
	public final String[] typeNames = {"csv","text","ucinet","payek","payek","payek","excel","kml","shapefile"};

    public String extension="";
    
    public GSNFileFilter(){super();}
    public GSNFileFilter(String extension){
    	super();
    	this.extension=extension;
    }

	public boolean accept(File f) {
		//accept folders!
		if (f.isDirectory()) {
			return true;
	    }

		//get file extension
		String ext = util.getFileExtension(f);

		//test for the extensions
		if (ext != null) {
	    	for(String filetype : types)
	    		if(ext.equals(filetype))
	    			return true;
	    	if (!this.extension.equals("") && ext.equals(this.extension))
	    		return true;
	    }
	    return false;
	}
	
	@Override
	public String getDescription() {
		return "All readable files";
	}

}
