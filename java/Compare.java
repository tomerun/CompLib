import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Compare {

	static final double NA = -1e300;

	static ArrayList<Double> read(Scanner sc) {
		int seed = 0;
		ArrayList<Double> v = new ArrayList<Double>();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.indexOf("seed:") != -1) {
				seed = Integer.parseInt(line.substring(5));
				while (v.size() <= seed) {
					v.add(NA);
				}
			}
			if (line.indexOf("score:") != -1) {
				v.set(seed, Double.parseDouble(line.substring(6)));
			}
		}
		return v;
	}

	public static void main(String[] args) throws Exception {
		ArrayList<Double> res1 = read(new Scanner(new File(args[0])));
		ArrayList<Double> res2 = read(new Scanner(new File(args[1])));
		double sum = 0;
		int count = 0;
		int win = 0;
		int lose = 0;
		for (int i = 0; i < Math.min(res1.size(), res2.size()); ++i) {
			double v1 = res1.get(i);
			double v2 = res2.get(i);
			if (v1 == NA || v2 == NA) continue;
			++count;
			double diff = v2 - v1;
			sum += diff;
			if (diff > 0) ++win;
			if (diff < 0) ++lose;
			System.out.printf("%3d % 6f\n", i, diff);
		}
		System.out.println();
		System.out.println(" win:" + win);
		System.out.println("lose:" + lose);
		System.out.println("even:" + (count - win - lose));
		System.out.println(" ave:" + (sum / count));
	}

}
