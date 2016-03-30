package dmytro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import model.Output;

public class XO {

	private static final List<String> DEFALUT_PLACEHOLDERS = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8");
	private List<String> placeholders = new ArrayList<String>(DEFALUT_PLACEHOLDERS);

	private Set<String> userInputs = new HashSet<String>();
	private Set<String> computerInputs = new HashSet<String>();

	private List<String> deck;

	private Output output;

	public List<String> getDeck() {
		return deck;
	}

	public XO() {
		output = new Output();
		deck = new ArrayList<String>();
		deck.add("blank");
		deck.add("blank");
		deck.add("blank");
		deck.add("blank");
		deck.add("blank");
		deck.add("blank");
		deck.add("blank");
		deck.add("blank");
		deck.add("blank");
	}

	public Output makeTurn(String userInput) {
		if (output.isGameFinished()) {
			return output;
		}

		if (valid(userInput)) {
			userInputs.add(userInput);
			deck.set(Integer.parseInt(userInput), "x");
			placeholders.remove(userInput);
			computerTurnNormal();
		} else {
			String errorMessage = "Your input is not valid. Try one more time.";
			output.setMessage(errorMessage);
		}

		boolean userWon = isWinner(userInputs);
		boolean computerWon = isWinner(computerInputs);
		if ((userWon && computerWon) || (!userWon && !computerWon && placeholders.size() == 0)) {
			output.setMessage("Draw!!! No winners!");
			output.setGameFinished(true);
		} else if (userWon) {
			output.setMessage("Congratulations! You won!!!");
			output.setGameFinished(true);
		} else if (computerWon) {
			output.setMessage("Computer won! Try to win one more time!!!");
			output.setGameFinished(true);
		}
		output.setDeck(deck);
		return output;
	}

	private boolean isWinner(Collection<String> inputs) {
		List<String> winResult1 = Arrays.asList("0", "1", "2");
		List<String> winResult2 = Arrays.asList("3", "4", "5");
		List<String> winResult3 = Arrays.asList("6", "7", "8");
		List<String> winResult4 = Arrays.asList("0", "3", "6");
		List<String> winResult5 = Arrays.asList("1", "4", "7");
		List<String> winResult6 = Arrays.asList("2", "5", "8");
		List<String> winResult7 = Arrays.asList("0", "4", "8");
		List<String> winResult8 = Arrays.asList("2", "4", "6");
		List<List<String>> winResultsAll = Arrays.asList(winResult1, winResult2, winResult3, winResult4, winResult5, winResult6, winResult7, winResult8);
		boolean isWinner = false;
		for (List<String> winResult : winResultsAll) {
			if (inputs.containsAll(winResult)) {
				isWinner = true;
				break;
			}
		}
		return isWinner;
	}

	private Random random = new Random();

	private void computerTurnNormal() {
		List<String> winResult1 = new ArrayList<>(Arrays.asList("0", "1", "2"));
		List<String> winResult2 = new ArrayList<>(Arrays.asList("3", "4", "5"));
		List<String> winResult3 = new ArrayList<>(Arrays.asList("6", "7", "8"));
		List<String> winResult4 = new ArrayList<>(Arrays.asList("0", "3", "6"));
		List<String> winResult5 = new ArrayList<>(Arrays.asList("1", "4", "7"));
		List<String> winResult6 = new ArrayList<>(Arrays.asList("2", "5", "8"));
		List<String> winResult7 = new ArrayList<>(Arrays.asList("0", "4", "8"));
		List<String> winResult8 = new ArrayList<>(Arrays.asList("2", "4", "6"));
		List<List<String>> winResultsAll = new ArrayList<List<String>>(
				Arrays.asList(winResult1, winResult2, winResult3, winResult4, winResult5, winResult6, winResult7, winResult8));

		List<List<String>> dangerList = new ArrayList<List<String>>();
		for (List<String> winResult : winResultsAll) {
			boolean hasComputerElement = false;
			for (String computerInput : computerInputs) {
				if (winResult.contains(computerInput)) {
					hasComputerElement = true;
					break;
				}
			}
			if (!hasComputerElement) {
				int userElementsCount = 0;
				for (String userInput : userInputs) {
					if (winResult.contains(userInput)) {
						userElementsCount++;
					}
				}
				if (userElementsCount == 2) {
					dangerList.add(new ArrayList<>(winResult));
				}
			}
		}

		List<List<String>> goalList = new ArrayList<List<String>>();
		for (List<String> winResult : winResultsAll) {
			boolean hasUserElement = false;
			for (String userInput : userInputs) {
				if (winResult.contains(userInput)) {
					hasUserElement = true;
					break;
				}
			}
			if (!hasUserElement) {
				int computerElementsCount = 0;
				for (String computerInput : computerInputs) {
					if (winResult.contains(computerInput)) {
						computerElementsCount++;
					}
				}
				if (computerElementsCount >= 1) {
					goalList.add(new ArrayList<>(winResult));
				}
			}
		}

		if (goalList.size() > 0 && dangerList.size() == 0) {
			List<String> offenciveTurnList = new ArrayList<>(goalList.get(0));
			offenciveTurnList.removeAll(computerInputs);
			String offenciveTurn = offenciveTurnList.get(0);
			deck.set(Integer.parseInt(offenciveTurn), "o");
			placeholders.remove(offenciveTurn);
			computerInputs.add(offenciveTurn);
		} else {

			if (dangerList.size() > 0) {
				List<String> defensiveTurnList = new ArrayList<>(dangerList.get(0));
				defensiveTurnList.removeAll(userInputs);
				String defenciveTurn = defensiveTurnList.get(0);
				deck.set(Integer.parseInt(defenciveTurn), "o");
				placeholders.remove(defenciveTurn);
				computerInputs.add(defenciveTurn);
			} else {
				if (placeholders.size() > 0) {
					String computerInput = placeholders.get(random.nextInt(placeholders.size()));
					deck.set(Integer.parseInt(computerInput), "o");
					placeholders.remove(computerInput);
					computerInputs.add(computerInput);
				}
			}
		}
	}

	private boolean valid(String userInput) {
		return placeholders.contains(userInput);
	}
}
