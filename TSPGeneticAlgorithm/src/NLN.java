
/**
 * The {@code NN} class represents a Nearest Loneliest Neighbor heuristic for
 * the Traveling Salesman Problem(TSP).
 * 
 * It contains a getter method {@code getTour} for retrieving the nearest
 * loneliest neighbor tour as a {@code Tour} object.
 * 
 * This implementation takes as input a {@code TSP} object containing a TSP
 * distance graph, and finds a nearest loneliest neighbor tour.
 * 
 * The constructor takes sub-quadratic time. The {@code getTour} method takes
 * constant time.
 * 
 * @author AccelSprinter
 *
 */
public class NLN {

	private final Tour tour; // the nearest neighbor tour

	/**
	 * Finds a tour for the given TSP instance, using the nearest loneliest neighbor
	 * heuristic.
	 * 
	 * @param tsp
	 *            the TSP instance
	 */
	public NLN(TSP tsp) {
		if (tsp == null)
			throw new IllegalArgumentException("argument to constructor is null");

		int V = tsp.V();

		double[] distSum = new double[V];
		for (int i = 0; i < V; ++i) {
			distSum[i] = 0;
			for (int j = 0; j < V; ++j) {
				distSum[i] += tsp.dist(i, j);
			}
			distSum[i] /= (V - 1);
		}

		double[] minmax = findMinMax(distSum);
		double average = (minmax[0] + minmax[1]) / 2;

		for (int i = 0; i < V; ++i) {
			if (distSum[i] > average)
				distSum[i] = average - (distSum[i] - average);
			else
				distSum[i] = average + (average - distSum[i]);
		}
		double[][] newDistances = new double[V][V];
		for (int i = 0; i < V; ++i) {
			for (int j = 0; j < V; ++j) {
				newDistances[i][j] = (tsp.dist(i, j) + distSum[i]) / 2;
			}
		}

		double minEdge = Double.MAX_VALUE;
		int min_i = -1;
		int min_j = -1;
		for (int i = 0; i < V - 1; ++i) {
			for (int j = i + 1; j < V; ++j) {
				if (newDistances[i][j] < minEdge) {
					minEdge = newDistances[i][j];
					min_i = i;
					min_j = j;
				}
			}
		}

		int[] tourArr = new int[V];
		for (int i = 0; i < V; ++i)
			tourArr[i] = i;
		swap(0, min_i, tourArr);
		swap(1, min_j, tourArr);

		for (int i = 1; i < V - 1; ++i) {
			int nearest = i + 1;
			double min = Double.MAX_VALUE;
			for (int j = i + 1; j < V; ++j) {
				int c1 = tourArr[i];
				int c2 = tourArr[j];
				if (newDistances[c1][c2] < min) {
					min = newDistances[c1][c2];
					nearest = j;
				}
			}
			swap(i + 1, nearest, tourArr);
		}

		this.tour = new Tour(tourArr, tsp);

	}

	/**
	 * Returns the nearest loneliest neighbor tour.
	 * 
	 * @return the nearest loneliest neighbor tour
	 */
	public Tour getTour() {
		return tour;
	}

	/************************************
	 * Private helper methods
	 ************************************/

	// swaps elements between indices i, j at arr array
	private void swap(int i, int j, int[] arr) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	// returns an array containing the min and max of arr array
	private static double[] findMinMax(double[] arr) {
		int n = arr.length;
		double[] mins = new double[(n + 1) / 2];
		double[] maxes = new double[mins.length];
		for (int i = 0; i < n; i += 2) {
			if (i == n - 1) {
				mins[i / 2] = arr[i];
				maxes[i / 2] = arr[i];
				break;
			}
			if (arr[i] < arr[i + 1]) {
				mins[i / 2] = arr[i];
				maxes[i / 2] = arr[i + 1];
			} else {
				mins[i / 2] = arr[i + 1];
				maxes[i / 2] = arr[i];
			}
		}

		double min = Double.MAX_VALUE;
		for (int i = 0, end = mins.length; i < end; ++i) {
			if (mins[i] < min)
				min = mins[i];
		}

		double max = Double.MIN_VALUE;
		for (int i = 0, end = maxes.length; i < end; ++i) {
			if (maxes[i] > max)
				max = maxes[i];
		}

		double[] result = { min, max };
		return result;
	}

	//debugging
	public static void main(String[] args) {
	}

}
