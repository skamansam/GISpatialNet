/**
 * 
 */
package us.jonesrychtar.gispatialnet;

/**
 * @author Sam
 *
 */
public class Enums {
	
	public static enum MatrixInputType{ 
		FULL, LOWER, UPPER;
    	public static MatrixInputType fromInt(int num) {
    		switch (num) {
    			case 0: { return FULL; }
    			case 1: { return LOWER; }
    			case 2: { return UPPER; }
    			default: { return null; }
    		}
    	}
		public static int toInt(MatrixInputType mit) {
			switch (mit) {
				case FULL: { return 0; }
				case LOWER: { return 1; }
				case UPPER: { return 2; }
				default: { return -1; }
			}
		}
		public static String toString(MatrixInputType mit) {
			switch (mit) {
				case FULL: { return "FULL"; }
				case LOWER: { return "LOWER"; }
				case UPPER: { return "UPPER"; }
				default: { return null; }
			}		
		}
		public static MatrixInputType fromString(String str) {
			if (str.toUpperCase().equals("FULL")) return FULL;
			if (str.toUpperCase().equals("LOWER")) return LOWER;
			if (str.toUpperCase().equals("UPPER")) return UPPER;
			return null;
		}
	}
	
    public static enum MatrixType{ 
    	COORD_ATT, ADJACENCY, COORDINATE, ATTRIBUTE;
    	public static MatrixType fromInt(int num) {
    		switch (num) {
    			case 0: { return COORD_ATT; }
    			case 1: { return ADJACENCY; }
    			case 2: { return COORDINATE; }
    			case 3: { return ATTRIBUTE; }
    			default: { return null; }
    		}
    	}
		public static int toInt(MatrixType mt) {
			switch (mt) {
				case COORD_ATT: { return 0; }
				case ADJACENCY: { return 1; }
				case COORDINATE: { return 2; }
				case ATTRIBUTE: { return 3; }
				default: { return -1; }
			}
    	}
		public static String toString(MatrixType mt) {
			switch (mt) {
				case COORD_ATT: { return "COORD_ATT"; }
				case ADJACENCY: { return "ADJACENCY"; }
				case COORDINATE: { return "COORDINATE"; }
				case ATTRIBUTE: { return "ATTRIBUTE"; }
				default: { return null; }
			}		
		}
		public static MatrixType fromString(String str) {
			if (str.toUpperCase().equals("COORD_ATT")) return COORD_ATT;
			if (str.toUpperCase().equals("ADJACENCY")) return ADJACENCY;
			if (str.toUpperCase().equals("COORDINATE")) return COORDINATE;
			if (str.toUpperCase().equals("ATTRIBUTE")) return ATTRIBUTE;
			return null;
		}
    }

}
