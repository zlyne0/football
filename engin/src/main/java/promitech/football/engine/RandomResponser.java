package promitech.football.engine;

import java.util.Random;

public class RandomResponser {
	private static Random random;

	private final int percent;

	public RandomResponser(int percent) {
		if (random == null) {
			random = new Random(System.currentTimeMillis());
		}
		this.percent = percent;
	}

	public boolean response() {
		return random.nextInt(100) < percent;
	}
}
