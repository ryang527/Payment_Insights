# Structure of Project

* README.md
* run.sh- script to compile and run the source code on the input in paymo_input
* images: pictures to help understand the relationship between users
* insight_testsuites: contains unit tests
* paymo_input: folder containing the input batch and stream data files
* paymo_output: folder to write the output data files into
* src: folder containing the source code of the project
* *Graph.java- Graph is a unweighted, undirected graph of nodes and edges. It supports breadth-first search on the graph, and allows the user to specify the depth of the search.
* *VerifyTransaction.java- VerifyTransaction reads in batch and stream data input, and writes to an output file whether the stream data transactions are verified are not.

# Implementation Details:
* The network of people is represented by a graph of nodes and edges. Each node represents a unique id, and each edge represents a transaction between the two ids. The graph keeps track of a mapping of nodes to the edges they have. Nodes are integers of the id, and edges are a list of integers of the ids for which there is a connection. The representation of the nodes and edges are kept as simple as possible to use less memory. HashMap and HashSet were used because their access and contains operations run in O(1) time.

* To implement the required features 1, 2, and 3, the graph implements a breadth-first search that stops searching after the requested maximum depth is reached. This search will start from one of the id nodes in the transaction and look sequentially at each level of nodes away from it (1 degree away, 2 degrees away, etc.) It will return true if it finds the other id node in the transaction before passing the maximum depth. Breadth-first search runs in O(|V| + |E|), where |V| is the number of nodes, and |E| is the number of edges.