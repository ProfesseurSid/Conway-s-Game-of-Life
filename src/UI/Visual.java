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
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Visual extends Application {
	private static Stage stage;
	private static BorderPane fenetre;
	private static BorderPane layout;
	private static Pane jeu;
	private static Scene scene;
	private static Group root;
	private static double dimX;
	private static double dimY;
	private static Game game;
	private static Timer timer;
	private static int nbPH = 50;
	private static int nbPV = 50;
	private static Rectangle[][] PixList;
	private static double proba = 0.5;
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
	private static Color aliveColor = Color.BLUE;
	private static Color deadColor = Color.RED;
	private static int nbColorSets = 5;
	private static int colorSet = 0;
	
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
	
	public static void main(String[] args) {
		Application.launch(Visual.class, args);
	}
	
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("Conway's Game of Life");
		plot = new ArrayList<Rectangle>();
		restart(stage);
	}
	
	public static void restart(Stage stage) {
		firstLaunch = true;
		pixSize = 10;
		if(nbPH > 600) {
			nbPH = 600;
			nbPV = 600;
		}
		
		plotWindow = new Pane();
		plotWindow.setMinWidth(dimX);
		
		plot.clear();
		graphOOB = false;
		countedSteps = 0;
		maxCounted = 0;
		
		if(pixSize*nbPV > Screen.getMainScreen().getHeight()-80)
			pixSize = (Screen.getMainScreen().getHeight()-80)/nbPV;
		if(pixSize*nbPH > Screen.getMainScreen().getWidth())
			pixSize = Math.min(pixSize, (Screen.getMainScreen().getWidth())/nbPH);
		dimX = nbPH*pixSize;
		
		
		dimY = nbPV*pixSize+30;
		PixList = new Rectangle[nbPV][nbPH];
		game = new Game(nbPV, nbPH, proba);
		displayGraph();
		
		timer = new Timer(game, speed);
		
		XGetter = new TextField(""+nbPH);
		XGetter.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				restart();
			}
		});
		XGetter.setMaxWidth(55);
		PGetter = new TextField(""+proba);
		PGetter.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				restart();
			}
		});
		PGetter.setMaxWidth(55);
		getters = new HBox();
		getters.getChildren().add(XGetter);
		getters.getChildren().add(PGetter);
		
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
						timer.stop();
						game.newGame(nbPV, nbPH, proba);
						timer.updateGame(game);
						displayGame();
						restartButton.setText("Start");
						firstLaunch = true;
						pauseButton.setDisable(true);
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
		if(speed == ULTRASMOOTH)
			fasterButton.setDisable(true);
		slowerButton = new Button("slower");
		slowerButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				slower();
			}
		});
		if(speed == SLOW)
			slowerButton.setDisable(true);
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
		
		for(int i=0; i<nbPV; i++) {
			for(int j=0; j<nbPH; j++) {
				Rectangle r = new Rectangle();
				r.setX(j*pixSize);
				r.setY(i*pixSize);
				r.setWidth(pixSize);
				r.setHeight(pixSize);
				r.setFill(Color.BLUE);
				r.setId(""+i+" "+j);
				r.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent e) {
						if(e.getEventType() == MouseEvent.MOUSE_CLICKED && e.getButton() == MouseButton.PRIMARY) { 
							if(r.getFill() == aliveColor) {
								game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize, false);
								displayGame();
							} else {
								game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize, true);
								displayGame();
							}
						} else if(e.isSecondaryButtonDown()) { 
							/* Forme de U */
//							if((int)r.getX()/pixSize > 0 && (int)r.getY()/pixSize > 0)
//								game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize-1, true);
//							if((int)r.getX()/pixSize < nbPH-1 && (int)r.getY()/pixSize > 0)
//								game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize-1, true);
//							if((int)r.getX()/pixSize > 0)
//								game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize, true);
//							if((int)r.getX()/pixSize < nbPH-1)
//								game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize, true);
//							if((int)r.getX()/pixSize > 0 && (int)r.getY()/pixSize < nbPH-1)
//								game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize+1, true);
//							if((int)r.getY()/pixSize < nbPH-1)
//								game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize+1, true);
//							if((int)r.getX()/pixSize < nbPH-1 && (int)r.getY()/pixSize < nbPH-1)
//								game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize+1, true);
							
							/* Space Invader */
							if((int)r.getX()/pixSize > 3 && (int)r.getY()/pixSize < nbPH-2)
								game.setPix((int)r.getX()/pixSize-4, (int)r.getY()/pixSize+2, true);
							if((int)r.getX()/pixSize > 2 && (int)r.getY()/pixSize < nbPH-2)
								game.setPix((int)r.getX()/pixSize-3, (int)r.getY()/pixSize+2, true);
							if((int)r.getX()/pixSize > 0 && (int)r.getY()/pixSize < nbPH-2)
								game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize+2, true);
							if((int)r.getX()/pixSize < nbPH-1 && (int)r.getY()/pixSize < nbPH-2)
								game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize+2, true);
							if((int)r.getX()/pixSize < nbPH-3 && (int)r.getY()/pixSize < nbPH-2)
								game.setPix((int)r.getX()/pixSize+3, (int)r.getY()/pixSize+2, true);
							if((int)r.getX()/pixSize < nbPH-4 && (int)r.getY()/pixSize < nbPH-2)
								game.setPix((int)r.getX()/pixSize+4, (int)r.getY()/pixSize+2, true);
							
							if((int)r.getX()/pixSize > 2 && (int)r.getY()/pixSize < nbPH-1)
								game.setPix((int)r.getX()/pixSize-3, (int)r.getY()/pixSize+1, true);
							if((int)r.getX()/pixSize > 1 && (int)r.getY()/pixSize < nbPH-1)
								game.setPix((int)r.getX()/pixSize-2, (int)r.getY()/pixSize+1, true);
							if((int)r.getX()/pixSize > 0 && (int)r.getY()/pixSize < nbPH-1)
								game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize+1, true);
							if((int)r.getY()/pixSize < nbPH-1)
								game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize+1, true);
							if((int)r.getX()/pixSize < nbPH-1 && (int)r.getY()/pixSize < nbPH-1)
								game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize+1, true);
							if((int)r.getX()/pixSize < nbPH-2 && (int)r.getY()/pixSize < nbPH-1)
								game.setPix((int)r.getX()/pixSize+2, (int)r.getY()/pixSize+1, true);
							if((int)r.getX()/pixSize < nbPH-3 && (int)r.getY()/pixSize < nbPH-1)
								game.setPix((int)r.getX()/pixSize+3, (int)r.getY()/pixSize+1, true);
							
							if((int)r.getX()/pixSize > 1)
								game.setPix((int)r.getX()/pixSize-2, (int)r.getY()/pixSize, true);
							game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize, true);
							if((int)r.getX()/pixSize < nbPH-2)
								game.setPix((int)r.getX()/pixSize+2, (int)r.getY()/pixSize, true);
							
							if((int)r.getX()/pixSize > 1 && (int)r.getY()/pixSize > 0)
								game.setPix((int)r.getX()/pixSize-2, (int)r.getY()/pixSize-1, true);
							if((int)r.getY()/pixSize > 0)
								game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize-1, true);
							if((int)r.getX()/pixSize < nbPH-2 && (int)r.getY()/pixSize > 0)
								game.setPix((int)r.getX()/pixSize+2, (int)r.getY()/pixSize-1, true);
							
							if((int)r.getX()/pixSize > 1 && (int)r.getY()/pixSize > 1)
								game.setPix((int)r.getX()/pixSize-2, (int)r.getY()/pixSize-2, true);
							if((int)r.getX()/pixSize > 0 && (int)r.getY()/pixSize > 1)
								game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize-2, true);
							if((int)r.getY()/pixSize > 1)
								game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize-2, true);
							if((int)r.getX()/pixSize < nbPH-1 && (int)r.getY()/pixSize > 1)
								game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize-2, true);
							if((int)r.getX()/pixSize < nbPH-2 && (int)r.getY()/pixSize > 1)
								game.setPix((int)r.getX()/pixSize+2, (int)r.getY()/pixSize-2, true);
							
							if((int)r.getX()/pixSize > 0 && (int)r.getY()/pixSize > 2)
								game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize-3, true);
							if((int)r.getX()/pixSize < nbPH-1 && (int)r.getY()/pixSize > 2)
								game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize-3, true);
							
							displayGame();
						} else if(e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
							game.setPix((int)e.getX()/pixSize, (int)e.getY()/pixSize, true);
							displayGame();
						}
					}
				});
				PixList[i][j] = r;
				jeu.getChildren().add(PixList[i][j]);
			}
		}
		
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
		fenetre.setRight(jeu);
		fenetre.setBottom(layout);
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
		stage.setScene(scene);
		stage.setMinHeight(jeu.getHeight()+layout.getHeight());
		stage.centerOnScreen();
		stage.setResizable(false);
		displayGame();
		stage.show();
	}
	
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
	
	private static void restart() {
		try {
			timer.stop();
			firstLaunch = true;
			nbPH = Integer.valueOf(XGetter.getText());
			nbPV = nbPH;
			proba = Double.valueOf(PGetter.getText());
			restart(stage);
		} catch(Exception ex) {
			XGetter.setText(""+nbPH);
			PGetter.setText(""+proba);
		}
	}
	
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
		
		for(int i=0; i<plot.size(); i++) {
			plot.get(i).setFill(aliveColor);
		}
		
		displayGame();
		if(graph)
			displayGraph();
	}
	
	private static void displayHelp() {
		timer.stop();
		String helpText = "F / + : Faster\n";
		helpText = helpText + "S / - : Slower\n";
		helpText = helpText + "C : next Color\n";
		helpText = helpText + "shift+C : previous Color\n";
		Alert help = new Alert(Alert.AlertType.INFORMATION, helpText, ButtonType.OK);
		help.setTitle("Help");
		help.setHeaderText("Shortcuts");
		help.showAndWait();
		timer.start();
	}
	
	public static boolean getGraph() {
		return graph;
	}
	
}
