package Kernel;

import java.util.ArrayList;

/**
 * @brief The core of Conway's Game of Life
 * @author CHANET Zoran
 *
 */
public class Game {
	private int nbC;
	private int nbL;
	private boolean[][] board;
	private boolean[][] lastBoard;
	private int nbSteps;
	private double proba;
	private ArrayList<Integer> stats;
	private int maxAlive;
	
	/**
	 * Creates a game with given numbers of columns and lines
	 * @param nbC the number of columns to create
	 * @param nbL the number of lines to create
	 * @param proba the probability for pixels to be alive
	 */
	public Game(int nbL, int nbC, double proba) {
		int aliveCount = 0;
		this.nbC = nbC;
		this.nbL = nbL;
		this.proba = proba;
		this.board = new boolean[nbL][nbC];
		this.lastBoard = new boolean[nbL][nbC];
		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				board[i][j] = Math.random()<proba;
			}
		}

		
		/* Creating a form at start */
		// TODO Protect for board dimensions
//		board[(nbL/2)][(nbC/2)+1]=true;
//		board[(nbL/2)+1][(nbC/2)+3]=true;
//		board[(nbL/2)+2][(nbC/2)]=true;
//		board[(nbL/2)+2][(nbC/2)+1]=true;
//		board[(nbL/2)+2][(nbC/2)+4]=true;
//		board[(nbL/2)+2][(nbC/2)+5]=true;
//		board[(nbL/2)+2][(nbC/2)+6]=true;
		
		/* Copying the current board to the save board */
		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				if(board[i][j]) {
					lastBoard[i][j] = true;
					aliveCount++;
				} else
					lastBoard[i][j] = false;
			}
		}
		nbSteps = 0;
		
		/* Initializing the statistics */
		stats = new ArrayList<Integer>();
		stats.add(aliveCount);
		maxAlive = aliveCount;
	}
	
	/**
	 * Computes the next step of the game
	 */
	public void step() {
		/* Applying GoL rules */
		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				int nhood = nbNeighbours(i,j);
				board[i][j] = nhood == 3 || (nhood == 2 && lastBoard[i][j]);
			}
		}

		/* Copying the current board to the save board */
		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				lastBoard[i][j] = board[i][j];
			}
		}
		
		/* Updating statistics */
		nbSteps++;
		int aliveCount = countAlive();
		stats.add(aliveCount);
		if(aliveCount > maxAlive)
			maxAlive = aliveCount;
	}
	
	/**
	 * Returns the number of alive neighbours to the cell l c
	 * @param l the line to watch
	 * @param c the column to watch
	 * @return the number of neighbours of the cell
	 */
	public int nbNeighbours(int l, int c) {
		int neighbours = 0;
		if(l > 0 && c > 0 && lastBoard[l-1][c-1])
			neighbours++;
		if(l > 0 && lastBoard[l-1][c])
			neighbours++;
		if(l > 0 && c < nbC-1 && lastBoard[l-1][c+1])
			neighbours++;
		if(c > 0 && lastBoard[l][c-1])
			neighbours++;
		if(c < nbC-1 && lastBoard[l][c+1])
			neighbours++;
		if(l < nbL-1 && c > 0 && lastBoard[l+1][c-1])
			neighbours++;
		if(l < nbL-1 && lastBoard[l+1][c])
			neighbours++;
		if(l < nbL-1 && c < nbC-1 && lastBoard[l+1][c+1])
			neighbours++;
		
		return neighbours;
	}
	
	/**
	 * Returns the number of executed steps
	 * @return the number of executed steps
	 */
	public int getNbSteps() {
		return nbSteps;
	}
	
	/**
	 * Returns a clone of the last completed board
	 * @return a clone of the last completed board
	 */
	public boolean[][] getBoard() {
		return lastBoard.clone();
	}
	
	/**
	 * Returns the dimensions of the board
	 * @return the number of lines
	 */
	public int getL() {
		return nbL;
	}
	
	/**
	 * Returns the dimensions of the board
	 * @return the number of columns
	 */
	public int getC() {
		return nbC;
	}
	
	/**
	 * @return the number of alive pixels
	 */
	public int countAlive() {
		int ret = 0;
		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				if(lastBoard[i][j])
					ret++;
			}
		}
		return ret;
	}
	
	/**
	 * Returns the board under a String form
	 */
	public String toString() {
		String output = "";
		
		for(int i=0; i<getL(); i++) {
			for(int j=0; j<getC(); j++) {
				output = output + "|";
				if(lastBoard[i][j])
					output = output + "x";
				else
					output = output + " ";
			}
			output = output+"|\n";
		}
		return(output);
	}
	
	/**
	 * Displays the String form of the board
	 */
	public void display() {
		System.out.println(toString());
	}
	
	/**
	 * Sets the value of a pixel (true = alive)
	 * @param x the x coordinate of the pixel
	 * @param y the y coordinate of the pixel
	 * @param alive the value of the pixel
	 */
	public void setPix(int x, int y, boolean alive) {
		lastBoard[y][x] = alive;
	}
	
	/**
	 * Starts a new game without creating a new object
	 * @param nbL the number of lines of the new board
	 * @param nbC the number of columns of the new board
	 * @param proba the the probability of being alive for the pixels in the new board
	 */
	public void newGame(int nbL, int nbC, double proba) {
		int aliveCount = 0;
		this.nbL = nbL;
		this.nbC = nbC;
		this.proba = proba;
		board = new boolean[nbL][nbC];
		lastBoard = new boolean[nbL][nbC];
		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				board[i][j] = Math.random()<proba;
			}
		}

		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				if(board[i][j]) {
					lastBoard[i][j] = true;
					aliveCount++;
				} else
					lastBoard[i][j] = false;
			}
		}
		nbSteps = 0;
		stats.clear();
		stats.add(aliveCount);
		maxAlive = aliveCount;
	}
	
	/**
	 * Returns the statistics under array form
	 * @return an array containing the number of alive cells per step
	 */
	public Integer[] getStats() {
		return stats.toArray(new Integer[stats.size()]);
	}
	
	/**
	 * Returns the maximum of alive cells reached during the game
	 * @return the maximum of alive cells reached during the game
	 */
	public int maxEver() {
		return maxAlive;
	}
	
	/**
	 * Returns the value of the given pixel
	 * @param x the x coordinate of the pixel
	 * @param y the y coordinate of the pixel
	 * @return the value of the pixel
	 */
	public boolean getPix(int x, int y) {
		return lastBoard[y][x];
	}
}
