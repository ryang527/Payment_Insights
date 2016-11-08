import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author rena
 * VerifyTransaction reads in batch and stream data input, and writes to an output file 
 * whether the stream data transactions are verified are not.
 */
public class VerifyTransaction {

	/**Initialize Graph g using batch transactions from filename
	 * @param filename
	 * @param g
	 */
	public static void initializeTransactions(String filename, Graph g) {
		String csvFile = filename;
		String line = "";

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			br.readLine(); // Read past the header line
			while ((line = br.readLine()) != null) {
				String[] transaction = line.split(",");
				try{
					int id1 = Integer.parseInt(transaction[1].replaceAll("\\s+",""));
					int id2 = Integer.parseInt(transaction[2].replaceAll("\\s+",""));

					if(!g.containsNode(id1)){
						g.addNode(id1);
					}
					if(!g.containsNode(id2)){
						g.addNode(id2);
					}
					
					g.addEdge(id1, id2);
				}
				catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
					//pass over invalid data
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Takes given val and returns its mapped string
	 * Maps: True -> trusted; False -> unverified
	 * @param val
	 * @return
	 */
	private static String mapSolution(boolean val){
		if (val){
			return "trusted";
		}
		else{
			return "unverified"; 
		}	
	}
	
	/**For each transaction in the input file, check in Graph g whether that the participants in the
	 * transaction are within 'degreeSeparation' from each other. 
	 * If they are, output 'Trusted' to outputfile, else output 'unverified'
	 *
	 * degreeSeparation: ex. 1 = friend, 2 = friend of friend, 4 = 4th degree friend
	 * @param inputFile
	 * @param outputFile
	 * @param g
	 * @param degreeSeparation
	 */
	public static void processTransactions(String inputFile, String outputFile, Graph g, int degreeSeparation) {
		Path read_path = Paths.get(inputFile);
		Path write_path = Paths.get(outputFile);
		String line = "";

		try {
			BufferedReader reader = Files.newBufferedReader(read_path, Charset.defaultCharset());
			reader.readLine(); // Read past the header line
			
			BufferedWriter writer = Files.newBufferedWriter(write_path, Charset.defaultCharset());
			
			while ((line = reader.readLine()) != null) {
				String[] transaction = line.split(",");
				try{
					int id1 = Integer.parseInt(transaction[1].replaceAll("\\s+",""));
					int id2 = Integer.parseInt(transaction[2].replaceAll("\\s+",""));

					boolean isValid = false;
					//If both nodes are in graph, check if they are within degreeSeparation from each other
					if(g.containsNode(id1) && g.containsNode(id2)){
						isValid = g.BFS(id1, id2, degreeSeparation);
					}
					else{//If one or more nodes not in graph, the transaction is unverified. Add new nodes to graph.
						if(!g.containsNode(id1)){
							g.addNode(id1);
						}
						if(!g.containsNode(id2)){
							g.addNode(id2);
						}
					}
					g.addEdge(id1, id2);// The new transaction may add another edge to the graph
					
				    writer.write(mapSolution(isValid));
				    writer.newLine();
				}
				catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
					//pass over invalid data
				}
			}
			
			reader.close();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**Initialize graphs with batch data and use streamed data to test all three features
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length >= 5) {
			//args[0] is the batch datafile, args[1] is the stream data file
			String batchDataFile = args[0];
			String streamDataFile = args[1];
			
			//Initialize 3 graphs
			Graph g1 = new Graph();
			initializeTransactions(batchDataFile, g1);

			Graph g2 = new Graph(g1.getNodes());
			Graph g3 = new Graph(g1.getNodes());
			
			//args[2], args[3], args[4] are the output datafiles names
			String[] outputNames = new String[]{args[2], args[3], args[4]};
			int[] separationDegrees = new int[]{1,2,4};
			Graph[] graphs = new Graph[]{g1, g2, g3};
			
			//Process each feature (#1- friend, #2- friend of friend, #3- 4th degree friend)
			for(int i = 0; i < outputNames.length; i++){
				processTransactions(streamDataFile, outputNames[i], graphs[i], separationDegrees[i]);
			}
		}
	}
}
