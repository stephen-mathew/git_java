/**
 * 
 */
package horseRaceJava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Program {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommandAnalyzer();
	}

	public static void CommandAnalyzer() {
		try {
			boolean _ExitProgram = false;
			do {
				System.out.println("Enter command: ");
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				String[] args = br.readLine().split(" ");

				// Logic to execute the correct command
				switch (args[0]) {
				case "create":
					HorseRace.CreateHorseRace(Integer.parseInt(args[2]),
							Integer.parseInt(args[3]));
					break;

				case "start":
					HorseRace.Start();
					break;

				case "kick":
					HorseRace.KickHorse(Integer.parseInt(args[2]));
					break;

				case "stop":
					HorseRace.Stop();
					break;

				case "exit":
					_ExitProgram = HorseRace.Exit();
					break;

				case "show":
					HorseRace.Show();
					break;

				}
			} while (!_ExitProgram);
		} catch (IOException e) {

		}
	}
}
