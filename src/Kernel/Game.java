package Kernel;

import java.util.ArrayList;

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

		
		/* crée une forme au démarrage */
		// A protéger en cas de dépassement des dimensions
//		board[(nbL/2)][(nbC/2)+1]=true;
//		board[(nbL/2)+1][(nbC/2)+3]=true;
//		board[(nbL/2)+2][(nbC/2)]=true;
//		board[(nbL/2)+2][(nbC/2)+1]=true;
//		board[(nbL/2)+2][(nbC/2)+4]=true;
//		board[(nbL/2)+2][(nbC/2)+5]=true;
//		board[(nbL/2)+2][(nbC/2)+6]=true;
		
		
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
		
		stats = new ArrayList<Integer>();
		stats.add(aliveCount);
		maxAlive = aliveCount;
	}
	
	/**
	 * Computes the next step of the game
	 */
	public void step() {
		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				int nhood = nbNeighbours(i,j);
				board[i][j] = nhood == 3 || (nhood == 2 && lastBoard[i][j]);
			}
		}

		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				lastBoard[i][j] = board[i][j];
			}
		}
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
	
	public boolean[][] getBoard() {
		return lastBoard.clone();
	}
	
	public int getL() {
		return nbL;
	}
	
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
	
	public void setPix(int x, int y, boolean alive) {
		lastBoard[y][x] = alive;
	}
	
	public void newGame(int nbL, int nbC, double proba) {
		this.nbL = nbL;
		this.nbC = nbC;
		this.proba = proba;
		this.board = new boolean[nbL][nbC];
		this.lastBoard = new boolean[nbL][nbC];
		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				board[i][j] = Math.random()<proba;
			}
		}

		for(int i=0; i<nbL; i++) {
			for(int j=0; j<nbC; j++) {
				if(board[i][j])
					lastBoard[i][j] = true;
				else
					lastBoard[i][j] = false;
			}
		}
		nbSteps = 0;
	}
	
	public Integer[] getStats() {
		return stats.toArray(new Integer[stats.size()]);
	}
	
	public int maxEver() {
		return maxAlive;
	}
}
