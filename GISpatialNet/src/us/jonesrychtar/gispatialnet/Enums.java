/**
 * 
 */
package us.jonesrychtar.gispatialnet;

/**
 * @author Sam
 *
 */
public class Enums {
	
	public static enum MatrixFormat{ 
		FULL, LOWER, UPPER;
    	public static MatrixFormat fromInt(int num) {
    		switch (num) {
    			case 0: { return FULL; }
    			case 1: { return LOWER; }
    			case 2: { return UPPER; }
    			default: { return null; }
    		}
    	}
		public static int toInt(MatrixFormat mit) {
			switch (mit) {
				case FULL: { return 0; }
				case LOWER: { return 1; }
				case UPPER: { return 2; }
				default: { return -1; }
			}
		}
		public static String toString(MatrixFormat mit) {
			switch (mit) {
				case FULL: { return "FULL"; }
				case LOWER: { return "LOWER"; }
				case UPPER: { return "UPPER"; }
				default: { return null; }
			}		
		}
		public static MatrixFormat fromString(String str) {
			if (str.toUpperCase().equals("FULL")) return FULL;
			if (str.toUpperCase().equals("LOWER")) return LOWER;
			if (str.toUpperCase().equals("UPPER")) return UPPER;
			return null;
		}
	}
	
    public static enum DataSetMatrixType{ 
    	COORD_ATT, ADJACENCY, COORDINATE, ATTRIBUTE;
    	public static DataSetMatrixType fromInt(int num) {
    		switch (num) {
    			case 0: { return COORD_ATT; }
    			case 1: { return ADJACENCY; }
    			case 2: { return COORDINATE; }
    			case 3: { return ATTRIBUTE; }
    			default: { return null; }
    		}
    	}
		public static int toInt(DataSetMatrixType mt) {
			switch (mt) {
				case COORD_ATT: { return 0; }
				case ADJACENCY: { return 1; }
				case COORDINATE: { return 2; }
				case ATTRIBUTE: { return 3; }
				default: { return -1; }
			}
    	}
		public static String toString(DataSetMatrixType mt) {
			switch (mt) {
				case COORD_ATT: { return "COORD_ATT"; }
				case ADJACENCY: { return "ADJACENCY"; }
				case COORDINATE: { return "COORDINATE"; }
				case ATTRIBUTE: { return "ATTRIBUTE"; }
				default: { return null; }
			}		
		}
		public static DataSetMatrixType fromString(String str) {
			if (str.toUpperCase().equals("COORD_ATT")) return COORD_ATT;
			if (str.toUpperCase().equals("ADJACENCY")) return ADJACENCY;
			if (str.toUpperCase().equals("COORDINATE")) return COORDINATE;
			if (str.toUpperCase().equals("ATTRIBUTE")) return ATTRIBUTE;
			return null;
		}
    }

    public static enum FileType{ 
    	EXCEL, CSV, SHAPEFILE, UCINET,PAJEK;
    	public static FileType fromInt(int num) {
    		switch (num) {
    			case 0: { return EXCEL; }
    			case 1: { return CSV; }
    			case 2: { return SHAPEFILE; }
    			case 3: { return UCINET; }
    			case 4: { return PAJEK; }
    			default: { return null; }
    		}
    	}
		public static int toInt(FileType mt) {
			switch (mt) {
				case EXCEL: { return 0; }
				case CSV: { return 1; }
				case SHAPEFILE: { return 2; }
				case UCINET: { return 3; }
				case PAJEK: { return 4; }
				default: { return -1; }
			}
    	}
		public static String toString(FileType mt) {
			switch (mt) {
				case EXCEL: { return "EXCEL"; }
				case CSV: { return "CSV"; }
				case SHAPEFILE: { return "SHAPEFILE"; }
				case UCINET: { return "UCINET"; }
				case PAJEK: { return "PAJEK"; }
				default: { return null; }
			}		
		}
		public static FileType fromString(String str) {
			if (str.toUpperCase().equals("EXCEL")) return EXCEL;
			if (str.toUpperCase().equals("CSV")) return CSV;
			if (str.toUpperCase().equals("SHAPEFILE")) return SHAPEFILE;
			if (str.toUpperCase().equals("UCINET")) return UCINET;
			if (str.toUpperCase().equals("PAJEK")) return PAJEK;
			return null;
		}
    }

    public static enum AlgorithmType{ 
    	QAP, SNB, BORDERS, HILIGHT_EDGES,CONVERSION;
       	public static AlgorithmType fromInt(int num) {
    		switch (num) {
    			case 0: { return QAP; }
    			case 1: { return SNB; }
    			case 2: { return BORDERS; }
    			case 3: { return HILIGHT_EDGES; }
    			case 4: { return CONVERSION; }
    			default: { return null; }
    		}
    	}
		public static int toInt(AlgorithmType mt) {
			switch (mt) {
				case QAP: { return 0; }
				case SNB: { return 1; }
				case BORDERS: { return 2; }
				case HILIGHT_EDGES: { return 3; }
				case CONVERSION: { return 4; }
				default: { return -1; }
			}
    	}
		public static String toString(AlgorithmType mt) {
			switch (mt) {
				case QAP: { return "QAP"; }
				case SNB: { return "SNB"; }
				case BORDERS: { return "BORDERS"; }
				case HILIGHT_EDGES: { return "UCINET"; }
				case CONVERSION: { return "PAJEK"; }
				default: { return null; }
			}		
		}
		public static AlgorithmType fromString(String str) {
			if (str.toUpperCase().equals("QAP")) return QAP;
			if (str.toUpperCase().equals("SNB")) return SNB;
			if (str.toUpperCase().equals("BORDERS")) return BORDERS;
			if (str.toUpperCase().equals("HILIGHT_EDGES")) return HILIGHT_EDGES;
			if (str.toUpperCase().equals("CONVERSION")) return CONVERSION;
			return null;
		}
    }

}
