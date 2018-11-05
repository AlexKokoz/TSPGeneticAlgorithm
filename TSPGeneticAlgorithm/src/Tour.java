import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * The {@code Tour} class represents a tour for the Traveling Salesman
 * Problem(TSP).
 * 
 * It contains simple methods for getting the tour's number of vertices, the
 * tour's total distance(including returning to the depot), a vertex index at a
 * specified tour cell, and the TSP instance the tour's referring to.
 * 
 * It also contains methods for shuffling the vertices of the tour, swapping two
 * vertices, 2-opt swapping a swath of the tour, creating an offspring by using
 * the Partially Mapped Crossover(PMX) operator, improving the tour using simple
 * swaps or 2-opt swaps and drawing the tour if its vertex coordinates are
 * available in {@code TSP} instance it refers to.
 * 
 * This implementation uses an array to store a permutation of vertex indices
 * from 0 to {@code V} - 1 that represent a tour, where {@code V} is the number
 * of vertices. It also has a {@code D} field that is updated to the current
 * tour's total distance after each major operation on the tour.
 * 
 * The constructors and the methods shuffle(), syncSwap2Opt(), PMX(),
 * validate(), and draw() take time proportional to the number of vertices. The
 * methods V(), get(), D(), TSP() and syncSwap() take constant time. The methods
 * twoOptImprove() and swapImprove() take quadratic time.
 * 
 * 
 * @author AccelSprinter
 *
 */
public class Tour implements Comparable<Tour> {

	private TSP tsp; // the tsp this tour refers to
	private final int[] tour; // the tour
	private final int V; // dimension(number of vertices)
	private double D; // total distance of the tour

	/**
	 * Initializes a tour from a {@code TSP} instance. The tour array is initialized
	 * sequentially from 0 to {@code V} - 1.
	 * 
	 * @param tsp
	 *            the TSP
	 */
	public Tour(TSP tsp) {
		if (tsp == null)
			throw new IllegalArgumentException("argument to constructor is null");

		this.tsp = tsp;
		this.V = tsp.V();
		tour = new int[V];
		for (int i = 0; i < V; ++i)
			tour[i] = i;
		D = calcDistance();
	}

	/**
	 * Initializes a tour from a TSPLIB file of type: TOUR referring to a
	 * {@code TSP} instance.
	 * 
	 * @param filename
	 *            the TSPLIB tour file
	 * @param tsp
	 *            the TSP
	 * @throws IllegalArgumentException
	 *             if TSPLIB file cannot be parsed
	 */
	public Tour(String filename, TSP tsp) {
		if (filename == null)
			throw new IllegalArgumentException("first argument to constructor is null");

		if (tsp == null)
			throw new IllegalArgumentException("second argument to constructor is null");

		In in = new In(filename);
		String data = in.readAll();

		// check if tour file has both a dimension and a tour section
		if (!TSPLIBParser.hasDimension(data) || !TSPLIBParser.hasTourSection(data))
			throw new IllegalArgumentException("tour file doesn't have dimension or a tour section");

		// check for consistent dimensions between tsp and tour file
		try {
			if (tsp.V() != Integer.parseInt(TSPLIBParser.getDimension(data)))
				throw new IllegalArgumentException("tsp anf tour file have different dimensions");
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("could not parse dimension from tour file", nfe);
		}

		this.tsp = tsp;
		this.V = tsp.V();
		tour = new int[V];

		// parse tour
		try {
			String tourSection = TSPLIBParser.getTourSection(data);
			Scanner scanner = new Scanner(tourSection);
			for (int i = 0; i < tsp.V(); ++i) {
				int index = scanner.nextInt() - 1; // converting tour file vertices from 1-based to 0-based
				validate(index);
				tour[i] = index;
			}
			scanner.close();
		} catch (NumberFormatException nfe) {

			throw new IllegalArgumentException("could not parse tour from file", nfe);
		}

		D = calcDistance();

	}

	/**
	 * Deep copies a given tour.
	 * 
	 * @param that
	 *            the tour
	 */
	public Tour(Tour that) {
		if (that == null)
			throw new IllegalArgumentException("argument to constructor is null");

		this.tsp = that.tsp;
		this.V = that.V;
		this.D = that.D;
		tour = new int[V];
		for (int i = 0; i < V; ++i)
			this.tour[i] = that.tour[i];
	}

	/**
	 * Initializes a tour from an explicit vertex array referring to a {@code TSP}
	 * instance.
	 * 
	 * @param arr
	 *            the vertex array
	 * @param tsp
	 *            the TSP
	 * @throws IllegalArgumentException
	 *             if either of the arguments is null
	 * @throws IllegalArgumentException
	 *             if dimensions do not match
	 */
	public Tour(int[] arr, TSP tsp) {
		if (arr == null)
			throw new IllegalArgumentException("first argument to constructor is null");
		if (tsp == null)
			throw new IllegalArgumentException("second argument to constructor is null");
		if (arr.length != tsp.V())
			throw new IllegalArgumentException("dimensions do not match");
		this.tsp = tsp;
		this.V = tsp.V();
		this.tour = new int[V];
		for (int i = 0; i < V; ++i)
			this.tour[i] = arr[i];
		validate();
		this.D = calcDistance();
	}

	/**
	 * Returns the number of vertices in the tour.
	 * 
	 * @return the number of vertices in the tour
	 */
	public int V() {
		return V;
	}

	/**
	 * Returns the vertex index in the {@code i} index of the tour array.
	 * 
	 * @param i
	 *            the tour array index
	 * @return the vertex index in the {@code i} index of the tour array
	 */
	public int get(int i) {
		validate(i);
		return tour[i];
	}

	/**
	 * Returns the current total distance of the tour, including returning to the
	 * depot.
	 * 
	 * @return the current total distance of the tour, including returning to the
	 *         depot
	 */
	public double D() {
		return D;
	}

	/**
	 * Returns the {@code TSP} TSP this tour refers to.
	 * 
	 * @return the {@code TSP} TSP this tour refers to
	 */
	public TSP TSP() {
		return tsp;
	}

	/**
	 * Shuffles the tour and updates its total distance.
	 */
	public void shuffle() {
		for (int i = 0; i < V - 1; ++i) {
			int j = (int) Math.floor(Math.random() * (V - i) + i);
			swap(i, j);
		}
		D = calcDistance();
	}

	/**
	 * Swaps vertices between two given tour indices.
	 * 
	 * @param i
	 *            the first tour index
	 * @param j
	 *            the second tour index
	 */
	public void syncSwap(int i, int j) {
		D -= prevDist(i) + nextDist(i) + prevDist(j) + nextDist(j);
		swap(i, j);
		D += prevDist(i) + nextDist(i) + prevDist(j) + nextDist(j);

	}

	/**
	 * Using the 2-opt operator, it reverses the swath between two given tour
	 * indices.
	 * 
	 * @param i
	 *            the first tour index
	 * @param j
	 *            the second tour index
	 */
	public void syncSwap2Opt(int i, int j) {
		int hi, lo;
		if (i > j) {
			hi = i;
			lo = j;
		} else {
			hi = j;
			lo = i;
		}
		D -= prevDist(lo) + nextDist(hi);
		swap(lo, hi);
		D += prevDist(lo) + nextDist(hi);
		swap(lo, hi);
		while (lo < hi) {
			swap(lo, hi);
			lo++;
			hi--;
		}
	}

	/**
	 * Performs the Partially Mapped Crossover operation between this tour and a
	 * specified tour to produce and return a new tour. The two parent tours are not
	 * mutated.
	 * 
	 * @param that
	 *            the specified tour
	 * @return a new tour as the result of the Partially Mapped Crossover between
	 *         this tour and a specified tour
	 */
	public Tour PMX(Tour that) {
		if (that == null)
			throw new IllegalArgumentException("argument to PMX is null");
		if (this.TSP() != that.TSP())
			throw new IllegalArgumentException("tour argument does not refer to same tsp instance");

		Tour p1 = this;
		Tour p2 = that;

		int N = p1.V();
		Tour offspring = new Tour(p2);

		int swathSize = rand(N - 1) + 1;
		int lo = rand(N - swathSize);
		int hi = lo + swathSize - 1;

		Map<Integer, Integer> p1ToP2 = new HashMap<>();
		for (int i = lo; i <= hi; ++i) {
			p1ToP2.put(p1.tour[i], p2.tour[i]);
		}

		for (int i = lo; i <= hi; ++i) {
			offspring.tour[i] = p1.tour[i];
		}

		for (int i = 0; i < N; ++i) {
			if (i >= lo && i <= hi) {
				continue;
			}

			int val = p2.tour[i];
			if (p1ToP2.containsKey(val)) {
				val = p1ToP2.get(val);
				while (p1ToP2.containsKey(val)) {
					val = p1ToP2.get(val);
				}
			}

			offspring.tour[i] = val;
		}

		offspring.D = offspring.calcDistance();
		return offspring;
	}

	/**
	 * Improves the tour and its distance by using the 2-opt operator.
	 */
	public void twoOptImprove() {

		boolean improved = true;
		int maxRuns = 1000;
		int runs = 0;
		while (improved && runs <= maxRuns) {
			improved = false;

			int v = 0;
			int w = 0;
			double minDistance = Double.MAX_VALUE;

			for (int lo = 0; lo < V - 1; ++lo) {
				for (int hi = lo + 1; hi < V; ++hi) {
					double currDistance = nextDist(lo) + prevDist(hi);
					swap(lo + 1, hi - 1);
					double newDistance = nextDist(lo) + prevDist(hi);
					swap(lo + 1, hi - 1);
					if (currDistance > newDistance && newDistance < minDistance) {
						v = lo + 1;
						w = hi - 1;
						minDistance = newDistance;
						improved = true;
						runs++;
					}

				}
			}
			if (improved)
				syncSwap2Opt(v, w);
		}

	}

	/**
	 * Improves the tour and its distance by using the simple swap operator.
	 */
	public void swapImprove() {

		boolean improved = true;

		while (improved) {
			improved = false;
			for (int lo = 0; lo < V - 1; ++lo) {
				for (int hi = lo + 1; hi < V; ++hi) {
					double currDistance = prevDist(lo) + nextDist(lo) + prevDist(hi) + nextDist(hi);
					swap(lo, hi);
					double newDistance = prevDist(lo) + nextDist(lo) + prevDist(hi) + nextDist(hi);
					swap(lo, hi);
					if (currDistance > newDistance) {
						improved = true;
						syncSwap(lo, hi);
					}

				}
			}
		}

	}

	/**
	 * Validates this tour. More specifically checks if all indices between 0 and {@code V} - 1
	 * (including) are present in the tour.
	 */
	public void validate() {

		int n = tsp.V();
		int[] arr = new int[n];
		for (int i = 0; i < n; ++i)
			arr[i] = get(i);
		for (int i = 0; i < n; ++i) {
			while (arr[i] < n && arr[i] > 0 && arr[i] != arr[arr[i]] && arr[i] != i) {
				swap(i, arr[i], arr);
			}
		}
		for (int i = 0; i < n; ++i) {
			if (arr[i] != i)
				throw new IllegalArgumentException("invalid tour");
		}
	}

	@Override
	public int compareTo(Tour that) {
		if (this.D() > that.D())
			return 1;
		if (this.D() < that.D())
			return -1;
		return 0;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("");
		s.append("TYPE: TOUR");
		s.append("\n");
		s.append("DIMENSION: ");
		s.append(V);
		s.append("\n");
		s.append("DISTANCE: ");
		s.append(D);
		s.append("\n");
		s.append("TOUR_SECTION\n");
		for (int i = 0; i < V; ++i) {
			s.append(get(i) + 1); // converts to 1-based
			s.append(" ");
		}
		return s.toString();
	}

	/**
	 * Draws the tour provided by the {@code tour} array. It sets the canvas's size
	 * based on the provided minimum and maximum x and y values of the cities from
	 * the {@code points} array.
	 * 
	 * @param minX
	 *            the minimum x coordinate
	 * @param minY
	 *            the minimum y coordinate
	 */
	public void draw() {
		if (!tsp.hasVertices())
			throw new IllegalArgumentException("no vertex coordinates exist in the data file");
		int width = 800;
		int height = 800;
		double minX = minX();
		double minY = minY();
		double maxX = maxX();
		double maxY = maxY();

		StdDraw.setCanvasSize(width, height);
		StdDraw.setScale(Math.min(minX, minY), Math.max(maxX, maxY));

		for (int i = 0; i < V(); i++) {
			StdDraw.setPenColor(StdDraw.BLACK);
			Vertex v1 = tsp.getVertex(get(i));
			Vertex v2 = tsp.getVertex(get((i + 1) % V()));
			StdDraw.circle(v1.x(), v1.y(), 0.1);
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(v1.x(), v1.y(), v2.x(), v2.y());
		}
	}

	/************************************
	 * Private helper methods
	 ************************************/
	// swaps elements between indices i, j
	private void swap(int i, int j) {
		validate(i);
		validate(j);
		int temp = tour[i];
		tour[i] = tour[j];
		tour[j] = temp;
	}

	// calculates the total distance of the tour including returning to the depot
	private double calcDistance() {
		int d = 0;
		for (int i = 0; i < V; ++i)
			d += nextDist(i);
		return d;
	}

	// get the previous tour index of the specified tour index
	private int prev(int i) {
		return (i - 1 + V) % V;
	}

	// get the next tour index of the specified tour index
	private int next(int i) {
		return (i + 1) % V;
	}

	// get the vertex index at the previous tour index of the specified tour index
	private int getPrev(int i) {
		return tour[prev(i)];
	}

	// get the vertex index at the next tour index of the specified tour index
	private int getNext(int i) {
		return tour[next(i)];
	}

	// get the distance between vertex at given tour index and previous vertex
	private double prevDist(int i) {
		int v = get(i);
		int prev = getPrev(i);
		return tsp.dist(v, prev);
	}

	// get the distance between vertex at given tour index and next vertex
	private double nextDist(int i) {
		int v = get(i);
		int next = getNext(i);
		return tsp.dist(v, next);
	}

	// throws IllegalArgumentException, unless 0 <= i < V
	private void validate(int i) {
		if (i < 0 || i >= V)
			throw new IllegalArgumentException("index out of bounds");
	}

	// return a random integer between 0(including) and n(excluding)
	private static int rand(int n) {
		Random rnd = new Random();
		return rnd.nextInt(n);
	}

	// swaps elements between indices i, j at arr array
	private void swap(int i, int j, int[] arr) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	// returns the minimum x coordinate of the tour
	private double minX() {
		double minX = Integer.MAX_VALUE;
		for (int i = 0, n = V(); i < n; i++) {
			Vertex city = tsp.getVertex(i);
			if (city.x() < minX)
				minX = city.x();
		}
		return minX;
	}

	// returns the minimum y coordinate of the tour
	private double minY() {
		double minY = Integer.MAX_VALUE;
		for (int i = 0, n = V(); i < n; i++) {
			Vertex city = tsp.getVertex(i);
			if (city.y() < minY)
				minY = city.y();
		}
		return minY;
	}

	// returns the maximum x coordinate of the tour
	private double maxX() {
		double maxX = Integer.MIN_VALUE;
		for (int i = 0, n = V(); i < n; i++) {
			Vertex city = tsp.getVertex(i);
			if (city.x() > maxX)
				maxX = city.x();
		}
		return maxX;
	}

	// returns the maximum y coordinate of the tour
	private double maxY() {
		double maxY = Integer.MIN_VALUE;
		for (int i = 0, n = V(); i < n; i++) {
			Vertex city = tsp.getVertex(i);
			if (city.y() > maxY)
				maxY = city.y();
		}
		return maxY;
	}

	// debugging
	public static void main(String[] args) {
	}

}
