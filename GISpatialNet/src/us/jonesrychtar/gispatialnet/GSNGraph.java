/**
 * 
 */
package us.jonesrychtar.gispatialnet;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * @author Sam
 * This class handles the JUNG graph, for graph display
 */
public class GSNGraph {
	Matrix x=MatrixFactory.emptyMatrix();
	Matrix y=MatrixFactory.emptyMatrix();
	Matrix names=MatrixFactory.emptyMatrix();

	Graph<Float, String> theGraph=new SparseMultigraph<Float,String>();
	GSNGraph(){
		
	}
	GSNGraph(Matrix x, Matrix y){
		this.x=x;
		this.y=y;
		createGraph();
	}
	GSNGraph(Matrix xy){
		if(xy.getColumnCount()<2){
			System.out.println("GSNGraph needs a matrix with at least 2 columns!");
			return;
		}
		this.x=xy.selectColumns(Calculation.Ret.NEW, 0);
		this.y=xy.selectColumns(Calculation.Ret.NEW, 1);
		createGraph();
	}
	GSNGraph(DataSet ds){
		if(ds.hasXY() /*&& ds.hasAdj()*/){
			System.out.println("GSNGraph needs a DataSet with XY values "/*AND an adjacency matrix!"*/);
			return;
		}
		this.x=ds.getX();
		this.y=ds.getY();
		createGraph();
	}
	public void setX(Matrix x){this.x=x;}
	public void setY(Matrix y){this.y=y;}
	public void setNames(Matrix n){this.names=n;}
	public Graph<Float,String> getGraph(){return theGraph;}
	public void createGraph(){
		//only go to smallest set
		//int maxRow=(int) Math.max(x.getRowCount(),y.getRowCount());
		
		//loop through each row
		for(int row=0;row<Math.max(x.getRowCount(),y.getRowCount());row++){
			float xn=x.getAsFloat(0,row);
			float yn=y.getAsFloat(0,row);
			theGraph.addVertex(xn);
			theGraph.addEdge(""+row, xn, yn);
		}
	}
}
