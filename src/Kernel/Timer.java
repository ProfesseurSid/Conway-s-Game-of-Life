package Kernel;

import java.util.Date;

import UI.Visual;
import javafx.animation.AnimationTimer;

/**
 * @brief The timer that forwards the game step by step
 * @author CHANET Zoran
 *
 */
public class Timer extends AnimationTimer {
	private double step;
	private Game game;
	private long lastTime = new Date().getTime();
	private boolean running;
	
	/**
	 * Instanciates the Timer with a given timestep to use on a given game
	 * @param game the game asking for the timer
	 * @param step the timestep to use
	 */
	public Timer(Game game, double step) {
		this.step = step;
		this.game = game;
		running = false;
	}
	
	@Override
	public void start() {
		running = true;
		super.start();
	}
	
	@Override
	public void stop() {
		running = false;
		super.stop();
	}
	
	@Override
	public void handle(long arg0) {
		long now = new Date().getTime();
		
		if(now-lastTime > step) {
			game.step();
			Visual.displayGame();
			if(Visual.getGraph())
				Visual.displayGraph();
			lastTime = now;
		}
	}
	
	public void updateStep(double newStep) {
		step = newStep;
	}
	
	public void updateGame(Game newGame) {
		game = newGame;
	}

	public boolean isRunning() {
		return running;
	}
}
