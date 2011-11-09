package eu.choreos.wp2.sia.analysis.detectors;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import edu.uci.ics.jung.graph.DirectedGraph;
import eu.choreos.wp2.sia.graph.entity.DegreeCentrality;
import eu.choreos.wp2.sia.graph.entity.Edge;
import eu.choreos.wp2.sia.graph.entity.Vertex;


public class ButterflyDetector extends AbstractNodeDetector{
	
	private DirectedGraph<Vertex, Edge> graph;
	private int localThreshold;
	private double globalThreshold;
	
	public ButterflyDetector(DirectedGraph<Vertex, Edge> graph){
		this.graph = graph;
		setThresholds();
	}

	public Set<Vertex> detectLocalButterflies(){
		return detectLocalNodes(graph);
	}
	
	public Set<Vertex> detectGlobalButterflies(){
		return detectLocalNodes(graph);
	}
	
	@Override
	protected boolean isLocal(DegreeCentrality degreeCentrality) {
		return degreeCentrality.getOutDegree() > localThreshold;
	}

	@Override
	protected boolean isGlobal(DegreeCentrality degreeCentrality) {
		int totalNodes = graph.getVertexCount();
		double percentTotal = degreeCentrality.getOutDegree() / totalNodes; 
		return  percentTotal > globalThreshold;
	}


	private void setThresholds(){
		try{
			Properties properties = new Properties();
			properties.load(new FileInputStream("./resources/thresholds.properties"));
			
			this.localThreshold = 
					Integer.parseInt(
							properties.getProperty("local_butterfly"));
			
			this.globalThreshold = 
					Double.parseDouble(
							properties.getProperty("global_butterfly"));
		}catch(IOException e){
			System.out.println("Could not read properties file");
			e.printStackTrace();
		}
	}
}