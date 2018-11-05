import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The {@code Generation} class represents a generation of tours for the
 * Traveling Salesman Problem(TSP).
 * 
 * It contains simple methods for getting the generation's population, number of
 * vertices, champion tour and corresponding TSP instance.
 * 
 * It also contains methods for populating the generation, finding the
 * k-shortest tours in the generation, cloning a tour in the generation and
 * selecting the shortest tour via a tournament selection of a specified number
 * of participant tours.
 * 
 * This implementation uses an array to store the generation's tours. By
 * convention, the only way to populate a generation instance with tours is by
 * providing a list, the size of which corresponds to the generation's
 * population. Also, all tours in the same generation must refer to the same
 * {@code TSP} instance and, therefore, have the same dimensions.
 * 
 * This implementation uses the {@code champion} field to keep track of the
 * shortest tour currently in the generation.
 * 
 * 
 * The constructor and the methods champion(), P(), V(), D(), and isFull() take
 * constant time. The method populate() takes time proportional to the
 * population size. The method cloneTour() takes time proportional to the number
 * of vertices. The method kMin() takes linearithmic time. The method
 * getTournamentWinner() takes time proportional to TPlogTP where TP is the
 * tournament population size.
 * 
 * 
 * @author AccelSprinter
 *
 */
public class Generation {

	private final TSP tsp; // the tsp
	private final Tour[] generation; // the generation
	private final int P; // the population
	private final int V; // the number of vertices
	private int count; // the number of tours currently in the generation
	private Tour champion; // the shortest tour

	/**
	 * Creates a generation of {@code P} population refering to the {@code tsp} TSP.
	 * 
	 * @param tsp
	 *            the tsp
	 * @param P
	 *            the population
	 * @throws IllegalArgumentException
	 *             if tsp is null or the population is negative
	 */
	public Generation(TSP tsp, int P) {
		if (tsp == null)
			throw new IllegalArgumentException("first argument to constructor is null");
		if (P < 0)
			throw new IllegalArgumentException("second argument to constructor should be nonnegative");
		this.tsp = tsp;
		this.P = P;
		this.V = tsp.V();
		count = 0;
		generation = new Tour[P];
	}

	/**
	 * Fills the generation with the tours in the provided list.
	 * 
	 * @param tours
	 *            the tour list
	 * @throws IllegalArgumentException
	 *             if the argument is null
	 * @throws IllegalArgumentException
	 *             if the population is not equal to the list's size
	 */
	public void populate(List<Tour> tours) {
		if (tours == null)
			throw new IllegalArgumentException("argument to populate() is null");
		if (tours.size() != P)
			throw new IllegalArgumentException(
					"list should have size equal to population: " + P + " , instead of " + tours.size());

		champion = null;
		count = 0;

		int pos = 0;
		for (Tour tour : tours) {
			set(pos++, tour);
		}
	}

	// inserts the tour in the i index of the generation
	private void set(int i, Tour t) {
		validate(i);
		if (t == null)
			throw new IllegalArgumentException("second argument to set() is null");
		if (t.TSP() != this.tsp)
			throw new IllegalArgumentException("incompatible tsp");

		generation[i] = t;
		count++;
		if (champion == null || t.D() < champion.D()) {
			champion = t;
		}
	}

	/**
	 * Returns a deep copy of the shortest tour in the generation.
	 * 
	 * @return a deep copy of the shortest tour in the generation
	 * @throws IllegalArgumentException
	 *             if the generation is empty
	 */
	public Tour champion() {
		if (!isFull())
			throw new IllegalArgumentException("generation has not been populated yet");
		return new Tour(champion);
	}

	/**
	 * Returns the population size.
	 * 
	 * @return the population size
	 */
	public int P() {
		return P;
	}

	/**
	 * Returns the number of vertices.
	 * 
	 * @return the number of vertices
	 */
	public int V() {
		return V;
	}

	/**
	 * Returns the distance of the tour at index {@code i}.
	 * 
	 * @param i
	 *            the index
	 * @return the distance of the tour at index {@code i}
	 * @throws IllegalArgumentException
	 *             if the generation is empty
	 */
	public double D(int i) {
		validate(i);
		if (generation[i] == null)
			throw new NullPointerException("null tour");
		return generation[i].D();
	}

	/**
	 * Is the generation fully populated?
	 * 
	 * @return true if the generation is fully populated; false otherwise
	 */
	public boolean isFull() {
		return count == P;
	}

	/**
	 * Returns the {@code k} shortest tours in the generation as an
	 * {@code Iterable}.
	 * 
	 * @param k
	 *            the number of shortest tours
	 * @return the {@code k} shortest tours in the generation as an {@code Iterable}
	 */
	public Iterable<Tour> kMin(int k) {
		validate(k - 1);
		if (!isFull())
			throw new IllegalArgumentException("generation should be full");
		PriorityQueue<Tour> queue = new PriorityQueue<>(Collections.reverseOrder());
		for (int i = 0; i < k; ++i) {
			queue.add(generation[i]);
		}
		for (int i = k; i < P; ++i) {
			if (generation[i].D() < queue.peek().D()) {
				queue.poll();
				queue.add(generation[i]);
			}
		}
		return queue;
	}

	/**
	 * Returns a deep copy of the tour at index {@code i}.
	 * 
	 * @param i
	 *            the index
	 * @return a deep copy of the tour at index {@code i}
	 */
	public Tour cloneTour(int i) {
		validate(i);
		Tour src = generation[i];
		if (src == null)
			throw new NullPointerException("tour is null; cannot be cloned");
		Tour clone = new Tour(src);
		return clone;
	}

	/**
	 * Returns the shortest tour from {@code TP} randomly selected unique tours in
	 * the generation.
	 * 
	 * @param TP
	 *            the tournament population
	 * @return the shortest tour from {@code TP} randomly selected unique tours in
	 *         the generation
	 */
	public Tour getTournamentWinner(int TP) {
		validate(TP - 1);
		if (!isFull())
			throw new IllegalArgumentException("generation should be full");

		int[] indices = new int[P];
		for (int i = 0; i < P; ++i) {
			indices[i] = i;
		}
		for (int i = 0; i < TP; ++i) {
			int j = (int) Math.floor(Math.random() * (P - i) + i);
			swap(i, j, indices);
		}

		Tour winner = generation[indices[0]];
		for (int i = 1; i < TP; ++i) {
			Tour participant = generation[indices[i]];
			if (winner.D() > participant.D())
				winner = participant;
		}

		return winner;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("");
		s.append("POPULATION: ");
		s.append(P);
		s.append("\n");
		s.append("DIMENSION: ");
		s.append(V);
		s.append("\n");
		s.append("GENERATION_SECTION\n");
		for (int i = 0; i < P; ++i) {
			if (generation[i] == null) {
				s.append(i + " null\n");
				continue;
			}
			s.append(i);
			s.append(" ");
			s.append("|" + generation[i].D() + "|");
			s.append("\n");
		}
		return s.toString();
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

	// throws IllegalArgumentException, unless 0 <= i < P
	private void validate(int i) {
		if (i < 0 || i >= P)
			throw new IllegalArgumentException("index out of bounds");
	}
}
