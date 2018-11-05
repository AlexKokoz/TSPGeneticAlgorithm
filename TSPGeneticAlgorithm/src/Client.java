/**
 * Client for the {@code GA} class.
 * @author AccelSprinter
 *
 */
public class Client {
	public static void main(String[] args){
		String filename = "IO/att48.tsp";
		TSP tsp = new TSP(filename);
		
		int G = 10000;
		int P = (int) Math.sqrt(tsp.V());
		int EP = 1;
		int TP = 3;
		double CP = 0.7;
		double MP = 0.1;
		
		Settings settings = new Settings(G, P, EP, TP, CP, MP);
		GA ga = new GA(tsp, settings);

		int runs = 100;
		for (int i = 0; i < runs; ++i) {
			System.out.println(i + 1);
			ga.go();
			ga.show();
			System.out.println("----");
		}
		
		ga.champion().twoOptImprove();
		System.out.println("AFTER IMPROVEMENT");
		System.out.println(ga.champion().toString());
		
		ga.champion().draw();
	}
}
