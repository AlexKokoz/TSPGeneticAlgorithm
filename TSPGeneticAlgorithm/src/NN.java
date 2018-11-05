import java.util.Random;

/**
 * The {@code NN} class represents a Nearest Neighbor heuristic for the
 * Traveling Salesman Problem(TSP).
 * 
 * It contains a getter method {@code getTour} for retrieving the nearest
 * neighbor tour as a {@code Tour} object.
 * 
 * This implementation takes as input a {@code TSP} object containing a TSP
 * distance graph, and finds a nearest neighbor tour.
 * 
 * The constructor takes sub-quadratic time. The {@code getTour} method takes
 * constant time.
 * 
 * @author AccelSprinter
 *
 */
public class NN {

	private final Tour tour; // the nearest neighbor tour

	/**
	 * Finds a tour for the given TSP instance, using the nearest neighbor
	 * heuristic.
	 * 
	 * @param tsp
	 *            the TSP instance
	 */
	public NN(TSP tsp) {

		if (tsp == null)
			throw new IllegalArgumentException("argument to constructor is null");

		int V = tsp.V();
		int[] tourArr = new int[V];
		for (int i = 0; i < V; ++i)
			tourArr[i] = i;
		int nearest = rand(V);

		for (int i = 0; i < V - 1; i++) {
			swap(i, nearest, tourArr);
			double min = Double.MAX_VALUE;
			for (int j = i + 1; j < V; j++) {
				int c1 = tourArr[i];
				int c2 = tourArr[j];
				double distance = tsp.dist(c1, c2);
				if (distance < min) {
					min = distance;
					nearest = j;
				}
			}
		}

		this.tour = new Tour(tourArr, tsp);
	}

	/**
	 * Returns the nearest neighbor tour.
	 * 
	 * @return the nearest neighbor tour
	 */
	public Tour getTour() {
		return tour;
	}

	/************************************
	 * Private helper methods
	 ************************************/
	// returns a random integer between 0(inclusive) and n (exclusive)
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

	// debugging
	public static void main(String[] args) {
	}

}
