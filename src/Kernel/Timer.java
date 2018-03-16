package Kernel;

import java.util.Date;

import UI.Visual;
import javafx.animation.AnimationTimer;

/**
 * Goes one step forward in the game
 * @author drzed
 *
 */
public class Timer extends AnimationTimer {
	private double step;
	private Game game;
	private long lastTime = new Date().getTime();
	
	/**
	 * Instanciates the Timer with a given timestep to use on a given game
	 * @param game the game asking for the timer
	 * @param step the timestep to use
	 */
	public Timer(Game game, double step) {
		this.step = step;
		this.game = game;
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

}
