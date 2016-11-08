package com.paymo;

import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Graph is a unweighted, undirected graph of nodes and edges.
 * It supports breadth-first search on the graph, including specifying the depth of the search
 * 
 * @author rena
 */
public class Graph {
    //Dummy id for oneDepthBFS to indicate when finished looking at all nodes in current level in queue
    private static Integer LEVEL_END_NODE_ID = null; 
    // Key is the id representing the node, Value is the list of edges for the node
    private HashMap<Integer, HashSet<Integer>> nodes; 
    
    /**
     * Default constructor, initialize empty graph
     */
    public Graph() {
        this.nodes = new HashMap<Integer, HashSet<Integer>>();
    }

    /**
     * Constructor- initialize graph with given nodes, deep copy
     * @param nodes
     */
    public Graph(Graph otherGraph) {
        if(otherGraph == null){
            throw new IllegalArgumentException("Invalid Graph");
        }

        this.nodes = new HashMap<Integer, HashSet<Integer>>();
        
        for(Entry<Integer, HashSet<Integer>> entry : otherGraph.nodes.entrySet()){
            this.nodes.put(entry.getKey(), new HashSet<Integer>(entry.getValue()));
        }
    }
    
    /**
     * Adds a node with the given id to the graph. The node has no edges.
     * @param nodeId
     */
    public void addNode(Integer nodeId) {
        nodes.put(nodeId, new HashSet<Integer>());
    }
    
    /**
     * Returns true if there is a node with the given id in the graph.
     * @param id
     * @return
     */
    public boolean containsNode(Integer id) {
        return nodes.containsKey(id);
    }

    /**
     * Adds an edge from node with id nodeId1 and node with id nodeId2
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

    /**
     * Returns true if there is an edge between node with id nodeId1 and node with id nodeId2.
     *
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

    /**
     * Traverse one more degree away in BFS, from the node corresponding to queue.
     * visitedThisNode is the list of nodes already reached from the node corresponding to queue.
     * visitedOtherNode is the list of nodes already reached from the searching starting at the other node.
     *
     * @param queue
     * @param visitedThisNode
     * @param visitedOtherNode
     * @return Return true if found a node already visited by other BFS
     */
    private boolean oneDepthBFS(Queue<Integer> queue, Set<Integer> visitedThisNode, Set<Integer> visitedOtherNode) {
        //Keep going through all nodes in queue at BFSDepth, until reach dummy node indicating finished seaching this level
        while (!queue.isEmpty()) {
            Integer curId = queue.poll();

            //If finished seaching this level, but there are more nodes to search in next level
            //Add dummy node to end of next level's nodes in queue
            if(curId == LEVEL_END_NODE_ID){
                if(!queue.isEmpty()){
                    queue.add(LEVEL_END_NODE_ID);
                }
                break;
            }
            
            for (Integer friendId : nodes.get(curId)) {
                //Check if friendId node is in set of visitedOthers
                //If true, the two BFS overlap and there is a path between the start and end nodes
                if (visitedOtherNode.contains(friendId)) {
                      return true;
                }
                
                // Don't add friendId to queue if already visited that node
                if (!visitedThisNode.contains(friendId)) {
                    visitedThisNode.add(friendId);
                    queue.add(friendId);
                }
            }
        }
        return false;
    }
    
    /**
     * Performs breadth-first search on the graph starting at the node with the id startNodeId.
     * Returns true if a node with id endNodeId is discovered within degreeSeparation depth from
     * the starting node.
     * @param startNodeId
     * @param endNodeId
     * @param degreeSeparation
     * @return
     */
    public boolean BFS(int startNodeId, int endNodeId, int degreeSeparation) {
        if (degreeSeparation < 0 || !nodes.containsKey(startNodeId) || !nodes.containsKey(endNodeId)) {
            return false;
        }

        if(startNodeId == endNodeId){
            return true;
        }

        //In queue, hold tuple (nodeId, depth of node from startNodeId [or endNodeId])
        Queue<Integer> queueStart = new LinkedList<>();
        Queue<Integer> queueEnd = new LinkedList<>();

        Set<Integer> visitedStart = new HashSet<>();// The set of nodes already visited from Start
        Set<Integer> visitedEnd = new HashSet<>();// The set of nodes already visited from End

        queueStart.add(startNodeId);
        queueEnd.add(endNodeId);
        queueStart.add(LEVEL_END_NODE_ID);
        queueEnd.add(LEVEL_END_NODE_ID);

        visitedStart.add(startNodeId);
        visitedEnd.add(endNodeId);
        
        int curDepth = 0;
        while ((curDepth < degreeSeparation) && !queueStart.isEmpty() && !queueEnd.isEmpty()) {
            //Do one level BFS from start node
            if (oneDepthBFS(queueStart, visitedStart, visitedEnd)){
                return true;
            }
            curDepth++;
            
            //Do one level BFS from end node
            if ((curDepth < degreeSeparation) && oneDepthBFS(queueEnd, visitedEnd, visitedStart)){
                return true;
            }
            curDepth++;
        }
        return false;
    }
}
