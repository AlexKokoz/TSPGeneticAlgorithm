
/**
 * The {@code Settings} class represents a bundle of settings for the Traveling
 * Salesman Problem(TSP).
 * 
 * It contains methods for retrieving the number of generations, the population
 * size, the elite population size, the tournament population, the crossover
 * probability and the mutation probability.
 * 
 * All methods take constant time.
 * 
 * @author AccelSprinter
 *
 */
public class Settings {

	private final int nGenerations; // the number of generations
	private final int population; // the population size
	private final int elitePopulation; // the elite population
	private final int tournamentPopulation; // the tournament population
	private final double crossoverProbability; // the crossover probability
	private final double mutationProbability; // the mutation probability

	/**
	 * Initializes a {@code Settings} object with a number of generations, a
	 * population size, an elite population, a tournament population, a crossover
	 * probability and a mutation probability.
	 * 
	 * @param nGenerations the number of generations
	 * @param population the population size
	 * @param elitePopulation the elite population
	 * @param tournamentPopulation the tournament population
	 * @param crossoverProbability the crossover probability
	 * @param mutationProbability the mutation probability
	 */
	public Settings(int nGenerations, int population, int elitePopulation, int tournamentPopulation,
			double crossoverProbability, double mutationProbability) {
		if (nGenerations < 0)
			throw new IllegalArgumentException("generation count should be nonegative");
		if (population < 0)
			throw new IllegalArgumentException("population should be nonegative");
		if (elitePopulation < 0 || elitePopulation > population)
			throw new IllegalArgumentException(
					"elite population should be nonegative and less or equal than the population");
		if (tournamentPopulation < 0 || tournamentPopulation > population)
			throw new IllegalArgumentException(
					"tournament population should be nonegative and less or equal than the population");
		if (crossoverProbability < 0 || crossoverProbability > 1)
			throw new IllegalArgumentException("crossover probability should be between [0..1]");
		if (mutationProbability < 0 || mutationProbability > 1)
			throw new IllegalArgumentException("mutation probability should be between [0..1]");

		this.nGenerations = nGenerations;
		this.population = population;
		this.elitePopulation = elitePopulation;
		this.tournamentPopulation = tournamentPopulation;
		this.crossoverProbability = crossoverProbability;
		this.mutationProbability = mutationProbability;
	}

	/**
	 * Returns the number of generations.
	 * @return the number of generations
	 */
	public int nGenerations() {
		return nGenerations;
	}

	/**
	 * Returns the population.
	 * @return the population
	 */
	public int population() {
		return population;
	}

	/**
	 * Returns the elite population.
	 * @return the elite population
	 */
	public int elitePopulation() {
		return elitePopulation;
	}

	/**
	 * Returns the tournament population.
	 * @return the tournament population
	 */
	public int tournamentPopulation() {
		return tournamentPopulation;
	}

	/**
	 * Returns the crossover probability.
	 * @return the crossover probability
	 */
	public double crossoverProbability() {
		return crossoverProbability;
	}

	/**
	 * Returns the the mutation probability.
	 * @return the the mutation probability
	 */
	public double mutationProbability() {
		return mutationProbability;
	}

}
