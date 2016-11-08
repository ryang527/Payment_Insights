# Structure of Project

* README.md
* run.sh- Script to compile and run the source code on the input in paymo_input
* insight_testsuites: Unit tests
* paymo_input:  Hold the input batch and stream data files
* paymo_output: Hold the output data files
* src: Source code of the project
 * Graph.java- Graph is a unweighted, undirected graph of nodes and edges. It supports breadth-first search on the graph, and allows the user to specify the depth of the search.
 * VerifyTransaction.java- VerifyTransaction reads in batch and stream data input, and writes to an output file whether the stream data transactions are verified are not.

# Implementation Details:
* The network of people is represented by a graph of nodes and edges. Each node represents a unique id, and each edge represents a transaction between the two ids. The graph keeps track of a mapping of nodes to the edges they have. Nodes are integers of the id, and edges are a list of integers of the ids for which there is a connection. The representation of the nodes and edges are kept as simple as possible to use less memory. HashMap and HashSet were used because their access and contains operations run in O(1) time.

* To implement the required features 1, 2, and 3, the graph implements a bidirectional breadth-first search that stops searching after the requested maximum depth is reached. This search will start from both of the id nodes in the transaction and switch-off searching one more depth away from each node. While searching on the next level, it checks if any of the new nodes it has encountered were already visited by the search starting from the other id node. If the node was already visited, then there is a path between the two id nodes. At the same time, it keeps track of how many levels each of the searches has gone so far, to see whether the nodes are within the requested degrees of separation.

#Build Details:
* I only used jdk packages and no other open source packages are used. The code compiles with jdk 1.7.0_65.