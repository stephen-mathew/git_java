package horseRaceJava;

import java.text.MessageFormat;
import java.util.*;

public class HorseRace implements Runnable {
	static int _NumHorses;
	public static int _RaceDistMetres;
	static boolean _raceStarted;
	static boolean _raceStopped;
	static Map<Integer, Horse> _HorseList;
	static List<Horse> _Ranking;
	static List<Horse> _SpeedRanking;

	// Create the race - inputting the number of horses and race distance
	public static void CreateHorseRace(int NumHorses, int RaceDistMetres) {
		_NumHorses = NumHorses;
		_RaceDistMetres = RaceDistMetres;
		_raceStarted = false;
		_HorseList = new HashMap<Integer, Horse>();
		_raceStopped = false;
		_Ranking = new ArrayList<Horse>();
		_SpeedRanking = new ArrayList<Horse>();
	}

	// Start the race. Start threads for each horse in this method
	public static void Start() {
		Horse horse;
		Thread horseThread;
		for (int i = 0; i < _NumHorses; i++) {
			// Start the threads
			horse = new Horse(i);
			_HorseList.put(i, horse);
			horseThread = new Thread(horse);
			horseThread.start();
		}
		_raceStarted = true;

		// Separate thread to continuously check the status of the race
		new Thread(new HorseRace()).start();
	}

	// Stop the race. Stop all the threads if running in this method. Print the
	// appropriate status messages to the console
	public static void Stop() {
		for (int i = 0; i < _NumHorses; i++) {
			_HorseList.get(i).StopHorse();
		}
		_raceStopped = true;
		System.out
				.println("All horses have stopped running. The race has been stopped");
	}

	public static boolean Exit() {
		if (_raceStopped) {
			return true;
		} else {
			System.out
					.println("You cannot exit if a race is going on. You have to first stop the race.");
			return false;
		}
	}

	public static void KickHorse(int HorseID) {
		Horse horseToBeKicked = _HorseList.get(HorseID);
		horseToBeKicked.KickHorse();
	}

	public static void Show() {
		Horse horseDisplay;
		String horseStatus;
		String raceStatus;
		int runningHorses = _NumHorses;
		_Ranking.clear();
		_SpeedRanking.clear();
		float averageSpeed = 0;
		String aboveAverageHorses = "";
		int aboveAverageHorseCount = 0;

		// Displaying results before the race has started
		if (!_raceStarted) {
			raceStatus = "Race: Ready to start";
			System.out.println(raceStatus);
			horseStatus = String.format("*" + "%" + _RaceDistMetres + "s", "");
			for (int i = 0; i < _NumHorses; i++) {
				System.out.println(MessageFormat.format("horse {0} [{1}]", i,
						horseStatus));
			}
		}
		// Calculating the ranking and speed of the horses who've currently
		// completed and displaying results of in progress or completed races
		else {
			// Adding completed horses and completed+stopped horses to separate
			// lists for later ranking
			for (int i = 0; i < _NumHorses; i++) {
				horseDisplay = _HorseList.get(i);
				if (!horseDisplay.is_horseRunning()) {
					runningHorses--;
					if (!horseDisplay.is_horseKicked()) {
						_Ranking.add(horseDisplay);
					}
					_SpeedRanking.add(horseDisplay);
					averageSpeed += horseDisplay.get_horseSpeed();
				}
			}

			// Sorting all completed horses according to time taken
			Collections.sort(_Ranking, new Comparator<Horse>() {
				public int compare(Horse h1, Horse h2) {
					Integer h1_TimeTaken = h1.get_timeTaken();
					Integer h2_TimeTaken = h2.get_timeTaken();
					return h1_TimeTaken.compareTo(h2_TimeTaken);
				}
			});
			Collections.sort(_SpeedRanking, new Comparator<Horse>() {
				public int compare(Horse h1, Horse h2) {
					Float h1_HorseSpeed = h1.get_horseSpeed();
					Float h2_HorseSpeed = h2.get_horseSpeed();
					return h1_HorseSpeed.compareTo(h2_HorseSpeed);
				}
			});
			// Calculating average speed of completed/stopped horses
			averageSpeed = averageSpeed / _SpeedRanking.size();

			// Checking if race is in progress or completed
			if (runningHorses == 0) {
				raceStatus = "Race: Completed! - Final standings:-";
			} else {
				raceStatus = "In Progress";
			}
			System.out.println(raceStatus);

			// Displaying the results for each horse
			for (int i = 0; i < _NumHorses; i++) {
				horseDisplay = _HorseList.get(i);

				// Adding the number of * to display the distance covered
				horseStatus = String.format(
						"%" + horseDisplay.get_HorseDist() + "s", "").replace(
						' ', '*');
				// Adding spaces to show the completion status of the horse
				horseStatus = String.format(horseStatus + "%"
						+ (_RaceDistMetres - horseDisplay.get_HorseDist() + 1)
						+ "s", "");
				// Displaying the result for a running horse
				if (horseDisplay.is_horseRunning()) {
					System.out.println(MessageFormat.format("horse {0} [{1}]",
							i, horseStatus));
				}
				// Displaying the result for a completed or kicked horse
				else {
					if (horseDisplay.is_horseKicked()) {
						horseStatus = String.format(
								"%" + horseDisplay.get_HorseDist() + "s", "k")
								.replace(' ', '*');
						horseStatus = String
								.format(horseStatus
										+ "%"
										+ (_RaceDistMetres
												- horseDisplay.get_HorseDist() + 1)
										+ "s", "");
						System.out
								.println(MessageFormat
										.format("horse {0} [{1}] Stopped running after {2} seconds",
												i, horseStatus,
												horseDisplay.get_timeTaken()));
					} else {
						System.out.println(MessageFormat.format(
								"horse {0} [{1}] {2} (Time: {3} seconds)", i,
								horseStatus,
								(_Ranking.indexOf(horseDisplay) + 1),
								horseDisplay.get_timeTaken()));
					}

					// Finding the above average speed horses
					if (horseDisplay.get_horseSpeed() > averageSpeed) {
						aboveAverageHorseCount++;
						aboveAverageHorses = aboveAverageHorses
								+ Integer.toString(horseDisplay.get_HorseID())
								+ ", ";
					}
				}
			}

			// Displaying the highest,lowest and average speed results if race
			// is completed
			if (runningHorses == 0) {
				System.out.println(MessageFormat.format(
						"Highest speed :- {0} m/s (Horse {1})", _SpeedRanking
								.get(_NumHorses - 1).get_horseSpeed(),
						_SpeedRanking.get(_NumHorses - 1).get_HorseID()));
				System.out.println(MessageFormat.format(
						"Lowest speed :- {0} m/s (Horse {1})", _SpeedRanking
								.get(0).get_horseSpeed(), _SpeedRanking.get(0)
								.get_HorseID()));
				aboveAverageHorses = aboveAverageHorses.replace(',', ' ')
						.trim();
				if (aboveAverageHorseCount > 0) {
					System.out
							.println(MessageFormat
									.format("Average speed:- {0} m/s (Horses {1} are above average!)",
											averageSpeed, aboveAverageHorses));
				} else {
					System.out.println(MessageFormat.format(
							"Average speed:- {0} m/s", averageSpeed));
				}
			}
		}
	}

	public void run() {
		CheckHorsesStatus();
	}

	public synchronized static void CheckHorsesStatus() {
		try {
			int runningHorses = _NumHorses;
			while (!_raceStopped) {
				for (int i = 0; i < _NumHorses; i++) {
					if (!_HorseList.get(i).is_horseRunning()) {
						runningHorses--;
					}
				}

				if (runningHorses <= 0) {
					_raceStopped = true;
				} else {
					Thread.sleep(1000);
				}
			}
		} catch (Exception e) {
		}
	}

}
