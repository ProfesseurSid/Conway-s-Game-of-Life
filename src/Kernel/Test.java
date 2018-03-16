package Kernel;

import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application {
	private static Game game;
	private Timer timer;
	
//	public Test(int nbL, int nbC, double proba) {
//		this.game = new Game(nbL, nbC, proba);
//		this.timer = new Timer(game, 0.1);
//		timer.start();
//	}
	
	public void start(Stage stage) {
		this.game = new Game(10, 10, 0.3);
		this.timer = new Timer(game, 1000);
		timer.start();
//		timer.stop();
	}

	public static void main(String[] args) {
		Application.launch(Test.class, args);
	}
}
