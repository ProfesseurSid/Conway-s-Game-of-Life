package UI;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Kernel.Game;
import javafx.event.EventHandler;

public class MouseHandler implements EventHandler<MouseEvent> {
	Game game;
	Rectangle r;
	Color aliveColor;
	int pixSize;
	int nbPH;
	
	
	public MouseHandler(Rectangle r) {
		game = Visual.getGame();
		this.r = r;
		aliveColor = Visual.getAliveColor();
		pixSize = Visual.getPixSize();
		nbPH = Visual.getNbPS();
	}
	
	@Override
	public void handle(MouseEvent e) {
		if(e.getEventType() == MouseEvent.MOUSE_CLICKED && e.getButton() == MouseButton.PRIMARY) { 
			if(game.getPix((int)r.getX()/pixSize, (int)r.getY()/pixSize)) {
				game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize, false);
				Visual.displayGame();
			} else {
				game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize, true);
				Visual.displayGame();
			}
		} else if(e.isSecondaryButtonDown()) { 
			/* U */
//			if((int)r.getX()/pixSize > 0 && (int)r.getY()/pixSize > 0)
//				game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize-1, true);
//			if((int)r.getX()/pixSize < nbPH-1 && (int)r.getY()/pixSize > 0)
//				game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize-1, true);
//			if((int)r.getX()/pixSize > 0)
//				game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize, true);
//			if((int)r.getX()/pixSize < nbPH-1)
//				game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize, true);
//			if((int)r.getX()/pixSize > 0 && (int)r.getY()/pixSize < nbPH-1)
//				game.setPix((int)r.getX()/pixSize-1, (int)r.getY()/pixSize+1, true);
//			if((int)r.getY()/pixSize < nbPH-1)
//				game.setPix((int)r.getX()/pixSize, (int)r.getY()/pixSize+1, true);
//			if((int)r.getX()/pixSize < nbPH-1 && (int)r.getY()/pixSize < nbPH-1)
//				game.setPix((int)r.getX()/pixSize+1, (int)r.getY()/pixSize+1, true);
			
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
			
			Visual.displayGame();
		} else if(e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
			game.setPix((int)e.getX()/pixSize, (int)e.getY()/pixSize, true);
			Visual.displayGame();
		}
	}

}
