import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author rena
 * Graph is a unweighted, undirected graph of nodes and edges.
 * It supports breadth-first search on the graph, including specifying the depth of the search 
 */
/**
 * @author rena
 *
 */
/**
 * @author rena
 *
 */
public class Graph {
	private HashMap<Integer, HashSet<Integer>> nodes; // Key is the id representing the node, Value is the list of edges for the node
	
	/**
	 * Default constructor, initialize empty graph
	 */
	public Graph() {
		this.nodes = new HashMap<Integer, HashSet<Integer>>();
	}

	/**Constructor- initialize graph with given nodes, deep copy
	 * @param nodes
	 */
	public Graph(HashMap<Integer, HashSet<Integer>> nodes) {
		this.nodes = new HashMap<Integer, HashSet<Integer>>();
		
		for(Entry<Integer, HashSet<Integer>> entry : nodes.entrySet()){
			this.nodes.put(entry.getKey(), new HashSet<Integer>(entry.getValue()));
		}
	}

	/** Returns this graph's nodes
	 * @return
	 */
	public HashMap<Integer, HashSet<Integer>> getNodes() {
		return nodes;
	}
	
	/**Adds a node with the given id to the graph. The node has no edges.
	 * @param nodeId
	 */
	public void addNode(Integer nodeId) {
		nodes.put(nodeId, new HashSet<Integer>());
	}

	
	/**Returns true if there is a node with the given id in the graph.
	 * @param id
	 * @return
	 */
	public boolean containsNode(Integer id) {
		return nodes.containsKey(id);
	}

	/**Adds an edge from node with id nodeId1 and node with id nodeId2
	 * @param nodeId1
	 * @param nodeId2
	 */
	public void addEdge(Integer nodeId1, Integer nodeId2) {
		if (!nodes.containsKey(nodeId1) || !nodes.containsKey(nodeId2)) {
			throw new IllegalArgumentException();
		}
		
		nodes.get(nodeId1).add(nodeId2);
		nodes.get(nodeId2).add(nodeId1);
	}

	/**Returns true if there is an edge between node with id nodeId1 and node with id nodeId2.
	 * @param nodeId1
	 * @param nodeId2
	 * @return
	 */
	public boolean containsEdge(Integer nodeId1, Integer nodeId2) {
		// Check if both nodes in the graph
		if (!nodes.containsKey(nodeId1) || !nodes.containsKey(nodeId2)) {
			return false;
		}

		return nodes.get(nodeId1).contains(nodeId2);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * Returns a string representing the nodes and edges of the graph.
	 */
	public String toString() {
		StringBuilder str = new StringBuilder("");
		for (Entry<Integer, HashSet<Integer>> node : nodes.entrySet()) {
			str.append(node.getKey());
			str.append("- ");
			for (Integer neighbors : node.getValue()) {
				str.append(neighbors);
				str.append(",");
			}
			str.append("\n");
		}
		return str.toString();
	}

	/**Performs breadth-first search on the graph starting at the node with the id startNodeId.
	 * Returns true if a node with id endNodeId is discovered within degreeSeparation depth from
	 * the starting node.
	 * @param startNodeId
	 * @param endNodeId
	 * @param degreeSeparation
	 * @return
	 */
	public boolean BFS(int startNodeId, int endNodeId, int degreeSeparation) {
		if (degreeSeparation < 0 || !nodes.containsKey(startNodeId)) {
			return false;
		}

		if(!nodes.containsKey(endNodeId)){
			return false;
		}

		//In queue, hold tuple (nodeId, depth of node from startNodeId)
		ArrayDeque<int[]> queue = new ArrayDeque<int[]>();

		HashSet<Integer> visited = new HashSet<Integer>();// The set of nodes already visited

		queue.add(new int[]{startNodeId, 0});
	    
		while(!queue.isEmpty()){
			int[] current = queue.poll();
			int curId = current[0];
			int curDepth = current[1];
			
			// Found the end node
			if (curId == endNodeId) {
				return true;
			}

			visited.add(curId);
			curDepth++;
			//Don't add nodes to queue if they are too many degrees of separation from the start node 
			if (curDepth <= degreeSeparation) {
				for (Integer friendId : nodes.get(curId)) {
					// Don't add nodes to queue if already visited that node
					if (!visited.contains(friendId)) {
						queue.push(new int[]{friendId, curDepth});
					}
				}
			}
		}

		return false;
	}
}
