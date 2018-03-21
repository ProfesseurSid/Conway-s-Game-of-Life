package UI;

import java.util.ArrayList;
import com.sun.glass.ui.Screen;
import Kernel.Game;
import Kernel.Timer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @brief Main class, creating the graphic interface of the application
 * @author CHANET Zoran
 *
 */
public class Visual extends Application {
	private static Stage stage;
	private static BorderPane fenetre;
	private static BorderPane layout;
	private static Pane jeu;
	private static Scene scene;
	private static Group root;
	private static double dimX;
	/* "Fake" variable = boards will be squares */
	private static double dimY;
	
	/* Components of the game */
	private static Game game;
	private static Timer timer;
	private static int nbPH = 50;
	private static int nbPV = 50;
	private static Rectangle[][] PixList;
	private static double proba = 0.5;
	
	/* Game speed constants */
	private static final int ULTRASMOOTH = 1;
	private static final int SMOOTHER = 10;
	private static final int SMOOTH = 100;
	private static final int FAST = 500;
	private static final int NORMAL = 1000;
	private static final int SLOW = 2000;
	private static int speed = NORMAL;
	
	private static Text score;
	private static Button pauseButton;
	private static Button restartButton;
	private static Button fasterButton;
	private static Button slowerButton;
	private static HBox settings;
	private static boolean firstLaunch;
	private static int pixSize = 10;
	private static boolean pause = false;
	private static TextField XGetter;
	private static TextField PGetter;
	private static HBox getters;
	private static Button colorButton;
	private static Button helpButton;
	
	/* Pixels and graph color */
	private static Color aliveColor = Color.BLUE;
	private static Color deadColor = Color.RED;
	
	/* Used to change the color */
	private static int nbColorSets = 5;
	private static int colorSet = 0;
	
	/* Used for statistics */
	private static ArrayList<Rectangle> plot;
	private static Pane plotWindow;
	private static Color graphColor;
	private static Color graphBGColor;
	private static Button graphButton;
	private static boolean graph = false;
	private static boolean graphOOB;
	private static int countedSteps;
	private static int maxCounted;
	
	private static MenuBar menus;
	
	/**
	 * Used to start the application
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(Visual.class, args);
	}
	
	/**
	 * Creating the stage
	 */
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("Conway's Game of Life");
		plot = new ArrayList<Rectangle>();
		game = new Game(nbPV, nbPH, proba);
		timer = new Timer(game, speed);
		createFields();
		restart();
	}
	
	/**
	 * Used to restart the game when needed, in the given stage 
	 * @param stage the given stage
	 */
	public static void restart(Stage stage) {
		
	}
	
	/**
	 * Displays the game updating the color of every pixel
	 * according to the game data
	 */
	public static void displayGame() {
		boolean[][] board = game.getBoard();
		int blue = game.countAlive();
		
		for(int i=0; i<nbPV; i++) {
			for(int j=0; j<nbPH; j++) {
				if(board[i][j])
					PixList[i][j].setFill(aliveColor);
				else
					PixList[i][j].setFill(deadColor);
			}
		}
		
		score.setText("Step " + game.getNbSteps() + " alive : " + blue + " " + "dead : " + (nbPV*nbPH-blue));
	}
	
	/**
	 * Creates every field of the Visual
	 */
	public static void createFields() {
		plotWindow = new Pane();

		try{
			nbPH = (int)(Math.floor(Double.valueOf(XGetter.getText())));
			if(nbPH > 600) {
				nbPH = 600;
			}
			nbPV = nbPH;
			proba = Double.valueOf(PGetter.getText());
		} catch (Exception e) {}
		
		XGetter = new TextField(""+nbPH);
		XGetter.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				restart();
			}
		});
		
		PGetter = new TextField(""+proba);
		PGetter.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				restart();
			}
		});
		
		getters = new HBox();
		
		settings = new HBox();
		
		fenetre = new BorderPane();
		
		layout = new BorderPane();
		
		jeu = new Pane();
		
		score = new Text();
		
		restartButton = new Button("Start");
		restartButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {	
					if(nbPH != Integer.valueOf(XGetter.getText()) || proba != Double.valueOf(PGetter.getText())) {
						restart();
					} else if(!firstLaunch) {
						restartButton.setText("Start");
						firstLaunch = true;
						pause = false;
						pauseButton.setText(" pause");
						pauseButton.setDisable(true);
						restart();
					} else {
						restartButton.setText("Reset");
						pauseButton.setDisable(false);
						firstLaunch = false;
						timer.start();
					}
				} catch(Exception ex) {
					XGetter.setText(""+nbPH);
					PGetter.setText(""+proba);
				}
			}
		});
		restartButton.setCancelButton(true);
		
		fasterButton = new Button("faster");
		fasterButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				faster();
			}
		});
		
		slowerButton = new Button("slower");
		slowerButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				slower();
			}
		});
		
		pauseButton = new Button(" pause");
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(!pause) {
					timer.stop();
					pause = true;
					pauseButton.setText("resume");
				} else {
					pauseButton.setText(" pause");
					pause = false;
					timer.start();
				}
			}
		});
		pauseButton.setDisable(true);
		pauseButton.setDefaultButton(true);
		
		colorButton = new Button("color");
		colorButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				changeColor(1);
			}
		});
		
		helpButton = new Button("?");
		helpButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				displayHelp();
			}
		});
		
		graphButton = new Button("Graph");
		graphButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				graph = !graph;
				if(graph) {
					stage.setWidth(2*dimX);
					fenetre.setLeft(plotWindow);
					stage.centerOnScreen();
					displayGraph();
				} else {
					fenetre.setLeft(null);
					stage.setWidth(dimX);
					stage.centerOnScreen();
				}
					
			}
		});
		
		getters.getChildren().add(XGetter);
		getters.getChildren().add(PGetter);
		
		settings.getChildren().add(slowerButton);
		settings.getChildren().add(fasterButton);
		settings.getChildren().add(restartButton);
		settings.getChildren().add(pauseButton);
		settings.getChildren().add(graphButton);
		settings.getChildren().add(colorButton);
		settings.getChildren().add(helpButton);
		layout.setLeft(score);
		layout.setRight(getters);
		layout.setBottom(settings);
		settings.setAlignment(Pos.CENTER);
		fenetre.setCenter(jeu);
		fenetre.setBottom(layout);
		fenetre.setMinWidth(Math.max(jeu.getWidth(), 2*settings.getWidth()));
		root = new Group();
		root.getChildren().add(fenetre);
		scene = new Scene(root);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.MINUS || e.getCode() == KeyCode.S)
					slower();
				else if(e.getCode() == KeyCode.PLUS || e.getCode() == KeyCode.F)
					faster();
				else if(e.getCode() == KeyCode.R)
					restart();
				else if(e.isShiftDown() && e.getCode() == KeyCode.C)
					changeColor(-1);
				else if(e.getCode() == KeyCode.C)
					changeColor(1);
			}
		});
	}
	
	/**
	 * putting every bloc into place
	 */
	public static void createWindow() {
		stage.setScene(scene);
		stage.setMinHeight(jeu.getHeight()+layout.getHeight());
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Removes the elements that will be resetted from the panes
	 */
	public static void destructWindow() {
		while(jeu.getChildren().size() > 0) {
			jeu.getChildren().removeAll(jeu.getChildren());
		}
		
		while(plot.size()>0) {
			plotWindow.getChildren().remove(plot.get(0));
			plot.remove(0);
		}
	}
	
	/**
	 * Displays the statistics adding a bar to the bar chart
	 * if steps have been done
	 */
	public static void displayGraph() {
		double width;
		boolean newMax = false;
		Integer[] stats = game.getStats();
		int max = game.maxEver();
		if(max > maxCounted)
			newMax = true;
		
		for(; countedSteps<stats.length; countedSteps++) {
			Rectangle r = new Rectangle();
			if(maxCounted > 0)
				r.setHeight( ((stats[countedSteps]*100)/(maxCounted) * nbPH*pixSize)/100);
			r.setY(nbPH*pixSize-r.getHeight());
			r.setFill(aliveColor);
			plot.add(r);
			if(((double)nbPH*(double)pixSize)/(double)plot.size() <= 1)
				plot.remove(0);
			plotWindow.getChildren().add(r);
		}
		
		if(!graphOOB)
			width = ((double)nbPH*(double)pixSize)/(double)plot.size();
		else
			width = 1;
		
		if(graphOOB || width < 1) {
			graphOOB = true;
			plotWindow.getChildren().remove(plot.get(0));
			plot.remove(0);
			width = 1;
		}
		
		for(int i=0; i<plot.size(); i++) {
			Rectangle rc = plot.get(i);
			rc.setWidth(width);
			rc.setX(i*rc.getWidth());
			if(newMax)
				rc.setHeight((rc.getHeight()*max)/maxCounted);
		}
		maxCounted = max;
	}
	
	/**
	 * Accelerates the timer
	 */
	private static void faster() {
		switch(speed) {
		case (SMOOTHER): speed = ULTRASMOOTH; fasterButton.setDisable(true); break;
		case (SMOOTH): speed = SMOOTHER; break;
		case (FAST): speed = SMOOTH; break;
		case (NORMAL): speed = FAST; break;
		case (SLOW): speed = NORMAL; slowerButton.setDisable(false); break;
		default:;
		}
		timer.updateStep(speed);
	}
	
	/**
	 * Slows the timer
	 */
	private static void slower() {
		switch(speed) {
		case (NORMAL): speed = SLOW; slowerButton.setDisable(true); break;
		case (FAST): speed = NORMAL; break;
		case (SMOOTH): speed = FAST; break;
		case (SMOOTHER): speed = SMOOTH; break;
		case (ULTRASMOOTH): speed = SMOOTHER; fasterButton.setDisable(false); break;
		default:;
		}
		timer.updateStep(speed);
	}
	
	/**
	 * Restarts the game after resetting some variables
	 */
	private static void restart() {
//		destructWindow();
		createFields();
		timer.stop();
		firstLaunch = true;
		pause = false;
		countedSteps = 0;

		pixSize = 10;
		if(pixSize*nbPV > Screen.getMainScreen().getHeight()-80)
			pixSize = (Screen.getMainScreen().getHeight()-80)/nbPV;
		if(pixSize*nbPH > Screen.getMainScreen().getWidth())
			pixSize = Math.min(pixSize, (Screen.getMainScreen().getWidth())/nbPH);
		dimX = nbPH*pixSize;
		dimY = nbPV*pixSize;
		
		game.newGame(nbPV, nbPH, proba);
		PixList = new Rectangle[nbPV][nbPH];
		for(int i=0; i<nbPV; i++) {
			for(int j=0; j<nbPH; j++) {
				Rectangle r = new Rectangle();
				r.setX(j*pixSize);
				r.setY(i*pixSize);
				r.setWidth(pixSize);
				r.setHeight(pixSize);
				r.setFill((game.getPix(j, i)?aliveColor:deadColor));
				r.setId(""+i+" "+j);
				r.addEventHandler(MouseEvent.ANY, new MouseHandler(r));
				PixList[i][j] = r;
				jeu.getChildren().add(r);
			}
		}
		
		graphOOB = false;
		countedSteps = 0;
		maxCounted = 0;
		
		createWindow();
		displayGame();
	}
	
	/**
	 * Changes the color of the pixels and graph circular way within the colorsets
	 * @param modif the way to rotate the colors
	 */
	private static void changeColor(int modif) {
		colorSet = (colorSet + modif) %nbColorSets;
		if(colorSet == 0) {
			aliveColor = Color.BLUE;
			deadColor = Color.RED;
		} else if(colorSet == 1) {
			deadColor = Color.WHITE;
			aliveColor = Color.BLACK;
		} else if(colorSet == 2) {
			deadColor = Color.PINK;
			aliveColor = Color.PURPLE;
		} else if(colorSet == 3) {
			deadColor = Color.CYAN;
			aliveColor = Color.DARKBLUE;
		} else {
			aliveColor = Color.GREEN;
			deadColor = Color.GREENYELLOW;
		}
		
		displayGame();
		if(graph)
			displayGraph();
	}
	
	/**
	 * Displays a little shortcuts window
	 */
	private static void displayHelp() {
		boolean wasRunning = timer.isRunning();
		if(wasRunning)
			timer.stop();
		String helpText = "F / + : Faster\n";
		helpText = helpText + "S / - : Slower\n";
		helpText = helpText + "C : next Color\n";
		helpText = helpText + "shift+C : previous Color\n";
		helpText = helpText + "\n\n\n© CHANET Zoran";
		Alert help = new Alert(Alert.AlertType.INFORMATION, helpText, ButtonType.OK);
		help.setTitle("Help");
		help.setHeaderText("Shortcuts");
		help.showAndWait();
		if(wasRunning)
			timer.start();
	}

	/**
	 * Returns the boolean graph
	 * @return if true, the statistics should be displayed
	 */
	public static boolean getGraph() {
		return graph;
	}
	
	/**
	 * Returns the associated game
	 * @return the associated game
	 */
	public static Game getGame() {
		return game;
	}
	
	/**
	 * Returns the color of alive pixels
	 * @return the color of alive pixels
	 */
	public static Color getAliveColor() {
		return aliveColor;
	}
	
	/**
	 * Returns the color of dead pixels
	 * @return the color of dead pixels
	 */
	public static Color getDeadColor() {
		return deadColor;
	}
	
	/**
	 * Returns the size of the pixels
	 * @return the size of the pixels
	 */
	public static int getPixSize() {
		return pixSize;
	}
	
	/**
	 * Returns the number of pixels by side of the square
	 * @return the number of pixels by side of the square
	 */
	public static int getNbPS() {
		return nbPH;
	}

}
