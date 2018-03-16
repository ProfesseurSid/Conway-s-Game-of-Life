package Kernel;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @brief Used to test the kernel
 * @author CHANET Zoran
 *
 */
public class Test extends Application {
	private static Game game;
	private Timer timer;

	/**
	 * Starting the app, launches the game and timer
	 * to check if everything works correctly
	 */
	public void start(Stage stage) {
		this.game = new Game(10, 10, 0.3);
		this.timer = new Timer(game, 1000);
		timer.start();
	}

	/**
	 * launching the app
	 * @param args the parameters of the command line. Unused.
	 */
	public static void main(String[] args) {
		Application.launch(Test.class, args);
	}
}
