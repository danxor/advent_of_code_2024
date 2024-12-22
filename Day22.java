import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Day22 extends DayRunner {
	private List<MonkeyBuyer> buyers;
	private List<String> lines;

	Day22(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.buyers = new ArrayList<>();
		this.lines = getLines(fileName);
		for(String line: lines) {
			buyers.add(new MonkeyBuyer(Integer.valueOf(line.strip())));
		}

		if (this.debug) {
			System.out.println("Secrets:");

			MonkeyBuyer buyer = new MonkeyBuyer(123);
			for(int i = 0; i < 10; i++) {
				int x = buyer.getNextSecretNumber();
				System.out.println(Integer.valueOf(x));
			}

			System.out.println("Delta:");

			int[] delta = getPriceDelta(buyer, 9);
			for(int i = 0; i < delta.length; i++) {
				System.out.println(Integer.toString(delta[i]));
			}

			System.out.println("");
		}
	}

	protected Integer getDayNumber() {
		return 22;
	}

	protected String getExpectedFirstResult() {
		return "37327623";
	}

	protected String getExpectedSecondResult() {
		return "24";
	}

	protected String getFirstResult() {
		Long sum = 0L;

		for(MonkeyBuyer buyer: this.buyers) {
			int value = 0;

			for(int i = 0; i < 2000; i++) {
				value =  buyer.getNextSecretNumber();
			}

			sum += value;
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		Map<String, List<Integer>> bestPrices = new HashMap<>();
		Set<String> found = new HashSet<>();

		for(MonkeyBuyer buyer: this.buyers) {
			int[] prices = buyer.getPrices(2000);
			int[] delta = new int[prices.length];
	
			int cur = buyer.id % 10;

			for(int i = 0; i < prices.length; i++) {
				delta[i] = prices[i] - cur;
				cur = prices[i];

				if (i >= 3) {
					String k = Integer.toString(delta[i - 3]) + "," + Integer.toString(delta[i - 2]) + "," + Integer.toString(delta[i - 1]) + "," + Integer.toString(delta[i]);
					String mk = Integer.toString(buyer.id) + ":" + k;
					if (found.contains(mk)) {
						continue;
					}

					if (!bestPrices.containsKey(k)) {
						bestPrices.put(k, new ArrayList<Integer>());
					}

					List<Integer> patternPrices = bestPrices.get(k);
					patternPrices.add(prices[i]);

					found.add(mk);
				}
			}
		}

		Integer best = 0;

		for(String pattern: bestPrices.keySet()) {
			List<Integer> prices = bestPrices.get(pattern);

			int sum = sum(prices);
			if (best < sum) {
				best = sum;
			}
		}

		return best.toString();
	}

	private static int sum(List<Integer> numbers) {
		int sum = 0;

		for(int i = 0; i < numbers.size(); i++) sum += numbers.get(i);

		return sum;
	}

	private static int[] getPriceDelta(MonkeyBuyer buyer, int count) {
		int cur = buyer.id % 10;

		int[] prices = buyer.getPrices(count);
		int[] delta = new int[count];

		for(int i = 0; i < delta.length; i++) {
			delta[i] = prices[i] - cur;
			cur = prices[i];
		}

		return delta;
	}

	public static void main(String[] args) {
		Boolean debug = false;
		Boolean runTests = true;
		Boolean runActual = true;

		for(String arg: args) {
			if (arg.equals("--skip-tests")) {
				runTests = false;
			} else if (arg.equals("--only-tests"))  {
				runActual = false;
			} else if (arg.equals("--debug")) {
				debug = true;
			}
		}

		new Day22(debug, runTests, runActual).Run();
	}

	public class MonkeyBuyer {
		private Map<Integer, Integer> savedNumbers;
		public final int id;
		public int secret;
		private int idx;

		public MonkeyBuyer(int id) {
			this.idx = 0;
			this.id = id;
			this.secret = id;
			this.savedNumbers = new HashMap<>();

		}

		public int getNextSecretNumber() {
			int a = prune(mix(secret, secret * 64));
			int b = prune(mix(a, a / 32));
			int c = prune(mix(b, b * 2048));
			this.savedNumbers.put(this.idx, c);
			this.idx = this.idx + 1;
			this.secret = c;
			return c;
		}

		public int[] getPrices(int count) {
			while(this.idx < count) {
				getNextSecretNumber();
			}

			int[] prices = new int[count];
			for(int i = 0; i < count; i++) {
				prices[i] = this.savedNumbers.get(i) % 10;
			}

			return prices;
		}

		private static long mix(long secret, long mix) {
			return secret ^ mix;
		}

		private static int prune(long secret) {
			return (int)(secret & 16777215);
		}
	}
}
