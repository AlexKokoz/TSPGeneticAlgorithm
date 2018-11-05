import java.util.Scanner;

/**
 * This class represents the distance graph of a Traveling Salesman Problem data
 * file in the formatting of the TSPLIB library.
 * 
 * TSPLIB can be found here:
 * http://elib.zib.de/pub/mp-testdata/tsp/tsplib/tsplib.html
 * 
 * More specifically, this class uses a TSPLIBParser to parse the data of said
 * file and store: -the raw data of the file -the data instance's
 * dimension(number of vertices) -the distance graph, given either explicitly as
 * a matrix or implicitly as vertex coordinates -the vertex coordinates, if given
 * 
 * This class provides methods for retrieving the dimension of the instance, the
 * distance between two specified vertices and the the vertex corresponding to a
 * specified index.
 * 
 * The constructor takes linear time proportioned to the dimension of the TSP
 * data file. The methods dist(), V(), getVertex() take constant time.
 * 
 * @author AccelSprinter
 *
 */
public class TSP {

	private final String data; // raw input from data file
	private final int V; // dimension(number of vertices)
	private final double[][] G; // the distance graph
	private Vertex[] vertices; // the vertices' coordinates, if given

	/**
	 * Parses a file indicated by the specified filename, by the TSPLIB format, and
	 * initializes the instance variables for the raw data, the dimension, the
	 * distance graph and the vertices'coordinates, if given.
	 * 
	 * If the data file doesn't follow the TSPLIB formatting rules an exception is
	 * thrown.
	 * 
	 * @param filename
	 *            the filename
	 * @throws IllegalArgumentException
	 *             if cannot open file
	 * @throws IllegalArgumentException
	 *             if argument is null
	 */
	public TSP(String filename) {
		if (filename == null)
			throw new IllegalArgumentException("filename is null");

		In in = new In(filename);
		data = in.readAll();
		assertValidTSPData(data);
		V = Integer.parseInt(TSPLIBParser.getDimension(data));
		G = new double[V][V];
		double[][] tempG = buildGraph(data, V);
		deepCopy(tempG, G);
	}

	/**
	 * Returns the dimension of the TSP data file.
	 * 
	 * @return the dimension of the TSP data file
	 */
	public int V() {
		return V;
	}

	/**
	 * Returns the distance between two specified vertex indices.
	 * 
	 * @param i
	 *            the one vertex index
	 * @param j
	 *            the other vertex index
	 * @return the distance between two specified vertex indices
	 */
	public double dist(int i, int j) {
		if (i < 0 || j < 0 || i >= V || j >= V)
			throw new IndexOutOfBoundsException("graph index out of bounds");
		return G[i][j];
	}

	/**
	 * Returns the vertex corresponding to the vertex index specified.
	 * 
	 * @return the vertex corresponding to the vertex index specified
	 */
	public boolean hasVertices() {
		return !(vertices == null);
	}

	/**
	 * Returns the vertex corresponding to the vertex index specified.
	 * 
	 * @param v
	 *            the vertex index;
	 * @return the vertex corresponding to the vertex index specified
	 */
	public Vertex getVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex index out of bounds");
		if (!hasVertices())
			throw new IllegalArgumentException("this TSP instance doesn't provide coordinates");
		return vertices[v];
	}

	@Override
	public String toString() {
		String str = "";
		str += "V = " + V();
		str += "\n";

		int max = 0;
		for (int r = 1, V = V(); r < V - 1; ++r)
			for (int c = r + 1; c < V; ++c)
				if (G[r][c] > max)
					max = (int) G[r][c];

		for (int r = 0, V = V(); r < V; ++r) {
			for (int c = 0; c < V; ++c) {
				str += G[r][c] + " | ";
			}
			str += "\n";
		}

		return str;
	}

	/**
	 * Returns the distance graph corresponding to the given text data of a TSP file
	 * of {@code size} dimension. Currently this method supports two cases. First it
	 * searches if there is a valid EDGE_WEIGHT_SECTION to parse. If the previous
	 * fails, it searches for a valid NODE_COORD_SECTION to parse and then compute
	 * the distance graph. Finally if all fail, it throws an exception.
	 * 
	 * @param text
	 *            the raw data of a TSP data file
	 * @param size
	 *            the dimension of the TSP data file
	 * @return the distance graph corresponding to the given text data of a TSP file
	 *         of {@code size} dimension
	 */
	private double[][] buildGraph(String text, int size) {
		if (TSPLIBParser.hasEdgeWeightSection(text)) {
			return buildGraphEWS(text, size);
		} else if (TSPLIBParser.hasNodeCoordSection(text)) {
			return buildGraphNCS(text, size);
		} else {
			throw new IllegalArgumentException(
					"TSP instance contains neither an edge weight section nor a node coord section");
		}

	}

	/**
	 * Returns the parsed distance graph from the EDGE_WEIGHT_SECTION of a TSP data
	 * file's raw text data, if it exists; throws an
	 * {@link IllegalArgumentException} otherwise.
	 * 
	 * @param text
	 *            the TSP textual data
	 * @param n
	 *            the TSP textual data's dimension
	 * @return the parsed distance graph from the EDGE_WEIGHT_SECTION of a TSP data
	 *         file's raw text data, if it exists; throws an
	 *         {@link IllegalArgumentException} otherwise
	 */
	private double[][] buildGraphEWS(String text, int n) {

		double[][] graph = new double[n][n];

		if (TSPLIBParser.hasEdgeWeightSection(text)) {

			String edgeWeightSection = TSPLIBParser.getEdgeWeightSection(text);
			String edgeWeightFormat = TSPLIBParser.getEdgeWeightFormat(text);

			switch (edgeWeightFormat) {
			case "UPPER_ROW":
				graph = UR(edgeWeightSection, n);
				break;
			case "LOWER_ROW":
				graph = LR(edgeWeightSection, n);
				break;
			case "UPPER_DIAG_ROW":
				graph = UDR(edgeWeightSection, n);
				break;
			case "LOWER_DIAG_ROW":
				graph = LDR(edgeWeightSection, n);
				break;
			case "UPPER_COL":
				graph = UC(edgeWeightSection, n);
				break;
			case "LOWER_COL":
				graph = LC(edgeWeightSection, n);
				break;
			case "UPPER_DIAG_COL":
				graph = UDC(edgeWeightSection, n);
				break;
			case "LOWER_DIAG_COL":
				graph = LDC(edgeWeightSection, n);
				break;
			case "FULL_MATRIX":
				graph = FM(edgeWeightSection, n);
				break;
			default:
				throw new IllegalArgumentException("invalid value for edge weight format: " + edgeWeightFormat);
			}

		}
		if (graph != null)
			return graph;
		else
			throw new IllegalArgumentException("there is no EDGE_WEIGHT_SECTION");
	}

	/**
	 * Returns the parsed distance graph from the NODE_COORD_SECTION of a TSP data
	 * file's raw text data, if it exists; throws an
	 * {@link IllegalArgumentException} otherwise.
	 * 
	 * @param text
	 *            the TSP textual data
	 * @param n
	 *            the TSP textual data's dimension
	 * @return the parsed distance graph from the NODE_COORD_SECTION of a TSP data
	 *         file's raw text data, if it exists; throws an
	 *         {@link IllegalArgumentException} otherwise
	 */
	private double[][] buildGraphNCS(String text, int n) {

		double[][] graph = new double[n][n];

		if (TSPLIBParser.hasNodeCoordSection(text)) {

			vertices = new Vertex[n];
			String nodeCoordSection = TSPLIBParser.getNodeCoordSection(text);
			Scanner scanner = new Scanner(nodeCoordSection);

			for (int i = 0; i < n; ++i) {
				Vertex v;

				scanner.next(); // skip the leading index
				double x = Double.parseDouble(scanner.next());
				double y = Double.parseDouble(scanner.next());

				if (is3D(text)) {
					double z = Double.parseDouble(scanner.next());
					v = new Vertex(x, y, z);
				} else {
					v = new Vertex(x, y);
				}

				vertices[i] = v;
			}

			String edgeWeightType = TSPLIBParser.getEdgeWeightType(text);

			for (int i = 0; i < n - 1; ++i) {
				for (int j = i + 1; j < n; ++j) {

					Vertex v1 = vertices[i];
					Vertex v2 = vertices[j];

					switch (edgeWeightType) {
					case "EUC_2D":
						graph[i][j] = DistanceFunctions.nintEuc2D(v1.x(), v1.y(), v2.x(), v2.y());
						break;
					case "EUC_3D":
						graph[i][j] = DistanceFunctions.nintEuc3D(v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z());
						break;
					case "MAX_2D":
						graph[i][j] = DistanceFunctions.max2D(v1.x(), v1.y(), v2.x(), v2.y());
						break;
					case "MAX_3D":
						graph[i][j] = DistanceFunctions.max3D(v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z());
						break;
					case "MAN_2D":
						graph[i][j] = DistanceFunctions.man2D(v1.x(), v1.y(), v2.x(), v2.y());
						break;
					case "MAN_3D":
						graph[i][j] = DistanceFunctions.man3D(v1.x(), v1.y(), v1.z(), v2.x(), v2.y(), v2.z());
						break;
					case "CEIL_2D":
						graph[i][j] = DistanceFunctions.ceil2D(v1.x(), v1.y(), v2.x(), v2.y());
						break;
					case "GEO":
						graph[i][j] = DistanceFunctions.geo(v1.x(), v1.y(), v2.x(), v2.y());
						break;
					case "ATT":
						graph[i][j] = DistanceFunctions.att2D(v1.x(), v1.y(), v2.x(), v2.y());
						break;
					default:
						throw new IllegalArgumentException("invalid edge weight type: " + edgeWeightType);
					}

					graph[j][i] = graph[i][j];

				}
			}

			scanner.close();

		}

		return graph;

	}

	/**
	 * Asserts the basic validity of a TSP textual data.
	 * 
	 * @param data
	 *            the TSP textual data
	 */
	private void assertValidTSPData(String data) {	
		
		if (!isPositiveInteger(TSPLIBParser.getDimension(data))) {
			String msg = "Dimension spec is not a positive integer";
			throw new IllegalArgumentException(msg);
		}

		if (!TSPLIBParser.getType(data).equals("TSP")) {
			String msg = "TYPE spec is not TSP";
			throw new IllegalArgumentException(msg);
		}

		if (TSPLIBParser.hasEdgeWeightSection(data) && !TSPLIBParser.hasEdgeWeightFormat(data)) {
			String msg = "TSP instance file has edge weight section but no edge weight format section";
			throw new IllegalArgumentException(msg);
		}

		if (!TSPLIBParser.hasEdgeWeightSection(data) && !TSPLIBParser.hasNodeCoordSection(data)) {
			String msg = "TSP instance file has neither edge weight section nor node coord section";
			throw new IllegalArgumentException(msg);
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Helper methods for parsing the distance graph from a specified EDGE_WEIGH
	// SECTION of specified dimension.
	////////////////////////////////////////////////////////////////////////////////

	// EDGE_WEIGHT_FORMAT = UPPER_ROW
	private double[][] UR(String text, int n) {
		Scanner scanner = new Scanner(text);
		double[][] G = new double[n][n];

		for (int row = 0; row < n - 1; ++row) {
			for (int col = row + 1; col < n; ++col) {
				double val = Double.parseDouble(scanner.next());
				G[row][col] = val;
				G[col][row] = val;
			}
		}

		scanner.close();
		return G;
	}

	// EDGE_WEIGHT_FORMAT = LOWER_ROW
	private double[][] LR(String text, int n) {
		Scanner scanner = new Scanner(text);
		double[][] G = new double[n][n];

		for (int row = 1; row < n; ++row) {
			for (int col = 0; col < row; ++col) {
				double val = Double.parseDouble(scanner.next());
				G[row][col] = val;
				G[col][row] = val;
			}
		}

		scanner.close();
		return G;
	}

	// EDGE_WEIGHT_FORMAT = UPPER_DIAG_ROW
	private double[][] UDR(String text, int n) {
		Scanner scanner = new Scanner(text);
		double[][] G = new double[n][n];

		for (int row = 0; row < n; ++row) {
			for (int col = row; col < n; ++col) {
				double val = Double.parseDouble(scanner.next());
				G[row][col] = val;
				if (row != col)
					G[col][row] = val;
			}
		}

		scanner.close();
		return G;
	}

	// EDGE_WEIGHT_FORMAT = UPPER_DIAG_ROW
	private double[][] LDR(String text, int n) {
		Scanner scanner = new Scanner(text);
		double[][] G = new double[n][n];

		for (int row = 0; row < n; ++row) {
			for (int col = 0; col <= row; ++col) {
				double val = Double.parseDouble(scanner.next());
				G[row][col] = val;
				if (row != col)
					G[col][row] = val;
			}
		}

		scanner.close();
		return G;
	}

	// EDGE_WEIGHT_FORMAT = UPPER_COL
	private double[][] UC(String text, int n) {
		Scanner scanner = new Scanner(text);
		double[][] G = new double[n][n];

		for (int col = 1; col < n; ++col) {
			for (int row = 0; row < col; ++row) {
				double val = Double.parseDouble(scanner.next());
				G[row][col] = val;
				G[col][row] = val;
			}
		}

		scanner.close();
		return G;
	}

	// EDGE_WEIGHT_FORMAT = LOWER_COL
	private double[][] LC(String text, int n) {
		Scanner scanner = new Scanner(text);
		double[][] G = new double[n][n];

		for (int col = 0; col < n - 1; ++col) {
			for (int row = col + 1; row < n; ++row) {
				double val = Double.parseDouble(scanner.next());
				G[row][col] = val;
				G[col][row] = val;
			}
		}

		scanner.close();
		return G;
	}

	// EDGE_WEIGHT_FORMAT = UPPER_DIAG_COL
	private double[][] UDC(String text, int n) {
		Scanner scanner = new Scanner(text);
		double[][] G = new double[n][n];

		for (int col = 0; col < n; ++col) {
			for (int row = 0; row <= col; ++row) {
				double val = Double.parseDouble(scanner.next());
				G[row][col] = val;
				if (row != col)
					G[col][row] = val;
			}
		}

		scanner.close();
		return G;
	}

	// EDGE_WEIGHT_FORMAT = LOWER_DIAG_COL
	private double[][] LDC(String text, int n) {
		Scanner scanner = new Scanner(text);
		double[][] G = new double[n][n];

		for (int col = 0; col < n; ++col) {
			for (int row = col; row < n; ++row) {
				double val = Double.parseDouble(scanner.next());
				G[row][col] = val;
				if (row != col)
					G[col][row] = val;
			}
		}

		scanner.close();
		return G;
	}

	// EDGE_WEIGHT_FORMAT = FULL_MATRIX
	private double[][] FM(String text, int n) {
		Scanner scanner = new Scanner(text);
		double[][] G = new double[n][n];

		for (int row = 0; row < n; ++row) {
			for (int col = 0; col < n; ++col) {
				double val = Double.parseDouble(scanner.next());
				G[row][col] = val;
			}
		}

		scanner.close();
		return G;
	}

	// Is the EDGE_WEIGHT_TYPE 3-dimensional?
	private boolean is3D(String data) {
		String edgeWeightType = TSPLIBParser.getEdgeWeightType(data);

		if (TSPLIBParser.hasEdgeWeightSection(data) && (edgeWeightType.equals("EUC_3D")
				|| edgeWeightType.equals("MAX_3D") || edgeWeightType.equals("MAN_3D"))) {
			return true;
		}

		return false;
	}
	
	// is the given string a positive integer?
	public static boolean isPositiveInteger(String str) {
		return str.matches("[^0]\\d*"); 
	}
	
	// creates a deep copy of {@code from} array to {@code to} array
	public  void deepCopy(double[][] from, double[][] to) {
		if (from == null || to == null)
			throw new IllegalArgumentException("argument is null");
				
		if (from.length != to.length)
			throw new IllegalArgumentException("row dimensions don't match");
		
		int nRows = from.length;
		
		for (int r = 0; r < nRows; ++r) {
			if (from[r].length != to[r].length)
				throw new IllegalArgumentException("column dimension doesn't match at " + r + " row");
		}
		
		for (int r = 0; r < nRows; ++r) {
			int nCols = from[r].length;
			for (int c = 0; c < nCols; ++c) {
				to[r][c] = from[r][c];
			}
		}
	}

	// debugging client
	public static void main(String[] args) {

		TSP tsp = new TSP("IO/st70.tsp");
		System.out.println(tsp.toString());
	}

}
