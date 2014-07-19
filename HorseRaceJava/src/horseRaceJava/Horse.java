package horseRaceJava;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Horse implements Runnable {
	private int _HorseID;
	private int _HorseDist;
	private Random _LeapMetre;
	private boolean _horseKicked;
	private boolean _horseRunning;
	private int _timeTaken;
	private float _horseSpeed;
	private long _timeMeasure;

	public int get_HorseID() {
		return _HorseID;
	}

	public int get_HorseDist() {
		return _HorseDist;
	}

	public Random get_LeapMetre() {
		return _LeapMetre;
	}

	public boolean is_horseKicked() {
		return _horseKicked;
	}

	public boolean is_horseRunning() {
		return _horseRunning;
	}

	public int get_timeTaken() {
		return _timeTaken;
	}

	public float get_horseSpeed() {
		return _horseSpeed;
	}

	public Horse(int HorseID) {
		this._HorseID = HorseID;
		this._HorseDist = 1;
		this._LeapMetre = new Random();
		this._horseKicked = false;
		this._horseRunning = true;
	}

	public void run() {
		this.HorseRunning();
	}

	// This method has to continuously run for the horse threads, until
	// KickHorse
	public synchronized void HorseRunning() {
		try {
			int nextLeap = _LeapMetre.nextInt(9);
			this._timeMeasure = System.nanoTime();

			while (((this._HorseDist + nextLeap) < HorseRace._RaceDistMetres)
					&& this._horseRunning) {
				this._HorseDist += nextLeap;
				// Console.WriteLine("Thread num {0} and horse distance {1} and horse num {2}",
				// Thread.CurrentThread.Name, this._HorseDist, this._HorseID);
				Thread.sleep(1000);
				nextLeap = this._LeapMetre.nextInt(9);
			}
			if ((this._HorseDist < HorseRace._RaceDistMetres)
					&& this._horseRunning) {
				this._HorseDist = HorseRace._RaceDistMetres;
			}
			this.StopHorse();
		} catch (Exception e) {
		}

	}

	public void KickHorse() {
		if (!this._horseKicked) {
			this._horseKicked = true;
			System.out.println(MessageFormat.format(
					"Horse {0} has been kicked!", this._HorseID));
			this.StopHorse();
		}
	}

	public void StopHorse() {
		if (this._horseRunning) {
			// Thread.CurrentThread.Abort();
			this._horseRunning = false;
			this._timeTaken = (int) TimeUnit.SECONDS.convert(
					(System.nanoTime() - this._timeMeasure),
					TimeUnit.NANOSECONDS);
			this._horseSpeed = this._HorseDist / this._timeTaken;
		}
	}
}
