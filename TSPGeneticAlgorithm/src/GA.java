
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * The {@code GA} class represents a Genetic Algorithm for the Traveling
 * Salesman Problem(TSP).
 * 
 * It contains methods for running the Genetic Algorithm and obtaining the
 * shortest tour found.
 * 
 * This implementation takes as input a {@code TSP} instance and a
 * {@code Settings} instance and runs the genetic algorithm based on them.
 * 
 * More specifically, the scheme used is the following: Initially a starter
 * generation is populated with random tours and a couple of quality tours found
 * via heuristics(Nearest Neighbor, Nearest Loneliest Neighbor). Its shortest
 * tour is recorded. Then the iterative process begins: in each generation, the
 * previous generation is producing offspring, replaced by said offspring and
 * then evaluated to determine if a shortest tour than the current one has been
 * produced.
 * 
 * The evolution of a generation consists of retaining a few of the currently
 * shortest tours, producing offspring by the Partially Mapped Crossover
 * crossover operator and mutating some of them with the 2-opt operator by a
 * small probability to explore the solution space.
 * 
 * @author AlexandrosKokozidis
 */
public class GA {

	private final TSP tsp; // the tsp
	private final Settings settings; // the settings
	private Tour champion; // the shortest tour found

	public GA(TSP tsp, Settings settings) {
		this.tsp = tsp;
		this.settings = settings;
	}

	/**
	 * Runs the Genetic Algorithm
	 */
	public void go() {
		Generation generation = new Generation(tsp, settings.population());
		initialize(generation);
		evaluate(generation);
		for (int i = 0; i < settings.nGenerations(); ++i) {
			evolve(generation);
			evaluate(generation);
		}
	}

	// Updates shortest tour currently found with the shortest tour of given
	// generation
	private void evaluate(Generation generation) {
		Tour contender = generation.champion();
		if (champion == null || contender.D() < champion.D()) {
			champion = contender;
		}
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(champion.toString());
		return s.toString();
	}

	/**
	 * Prints to the console the shortest tour found.
	 */
	public void show() {
		System.out.println(toString());
	}

	/**
	 * Validates the shortest tour found.
	 * 
	 * @throws IllegalArgumentException
	 *             if the Genetic Algorithm has not been ran yet
	 */
	public void validateSolution() {
		if (champion == null)
			throw new IllegalArgumentException("genetic algorithm has not been ran yet");
		champion.validate();
	}

	/**
	 * Returns the best tour that has been encountered.
	 * 
	 * @return the best tour that has been encountered
	 * @throws IllegalArgumentException
	 *             if the Genetic Algorithm has not been ran yet
	 */
	public Tour champion() {
		if (champion == null)
			throw new IllegalArgumentException("genetic algorithm has not been ran yet");
		return champion;
	}

	// initializes current generation with two tours from NN and NLN heuristics and
	// all the rest from randomly shuffled tours
	private void initialize(Generation generation) {
		List<Tour> tours = new LinkedList<>();
		Tour nln = new NLN(tsp).getTour();
		Tour nn = new NN(tsp).getTour();
		tours.add(nln);
		tours.add(nn);
		if (tours.size() > settings.population())
			throw new IllegalArgumentException("tour list size exceeds population");
		while (tours.size() < settings.population()) {
			Tour tour = new Tour(tsp);
			tour.shuffle();
			tours.add(tour);
		}
		generation.populate(tours);
	}

	// replaces tours in given generation with their offspring;
	// the given generation is mutated
	private void evolve(Generation generation) {
		if (generation == null)
			throw new IllegalArgumentException("argument to evolve() is null");
		if (!generation.isFull())
			throw new IllegalArgumentException("generation should be full");
		List<Tour> tours = new LinkedList<>();
		Iterable<Tour> elite = generation.kMin(settings.elitePopulation());
		for (Tour tour : elite) {
			tours.add(tour);
		}
		while (tours.size() < generation.P()) {
			Tour offspring = createOffspring(generation);
			tours.add(offspring);
		}

		generation.populate(tours);

	}

	// creates an offspring by applying the Partially Mapped Crossover over two
	// randomly selected tours
	private Tour createOffspring(Generation generation) {
		int idx = rand(generation.P());
		Tour offspring = generation.cloneTour(idx);

		Double p = Math.random();
		if (p < settings.crossoverProbability()) {
			int tournamentPopulation = settings.tournamentPopulation();
			Tour parent1 = generation.getTournamentWinner(tournamentPopulation);
			Tour parent2 = generation.getTournamentWinner(tournamentPopulation);
			offspring = parent1.PMX(parent2);
		}

		p = Math.random();
		if (p < settings.mutationProbability()) {
			mutate(offspring);
		}

		return offspring;
	}

	// mutates given tour with the 2-opt operator
	private void mutate(Tour tour) {
		if (tour == null)
			throw new IllegalArgumentException("argument to mutate() is null");
		if (tsp.V() == 1)
			return;
		int i = rand(tsp.V());
		int j = rand(tsp.V());
		while (i == j) {
			i = rand(tsp.V());
			j = rand(tsp.V());
		}
		tour.syncSwap2Opt(i, j);
	}

	/************************************
	 * Private helper methods
	 ************************************/

	// return a random integer between 0(including) and n(excluding)
	private static int rand(int n) {
		Random rnd = new Random();
		return rnd.nextInt(n);
	}

}
