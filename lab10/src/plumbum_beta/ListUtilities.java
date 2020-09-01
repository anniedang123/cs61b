package plumbum_beta;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ListUtilities {

	/**
	 * Filters the given list according to the predicate function.
	 * Returns the input list for easy chaining.
	 *
	 * @param input the list to filter
	 * @param predicate a predicate function, returns 'true' if an element should be kept
	 */
	public static <T> List<T> filter(List<T> input, Predicate<T> predicate) {
		int i = 0;
		while (i < input.size()) {
			if (!predicate.test(input.get(i))) {
				input.remove(i);
			} else {
				i++;
			}
		}

		return input;
	}
}
