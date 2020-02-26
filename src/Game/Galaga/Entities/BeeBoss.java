package Game.Galaga.Entities;

import java.awt.image.BufferedImage;

import Main.Handler;
import Resources.Animation;
import Resources.Images;
public class BeeBoss extends EnemyBee{

	public BeeBoss(int x, int y, int width, int height, Handler handler, int row, int col) {
		super(x, y, width, height, handler, row, col);
		this.row = row;
        this.col = col;
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.galagaEnemyBee[0];
        idleAnimList[1] = Images.galagaEnemyBee[1];
        idle = new Animation(512,idleAnimList);
        turn90Left = new Animation(128,Images.galagaEnemyBee);
        //spawn();
        speed = 4;
        formationX=(handler.getWidth()/4)+(col*((handler.getWidth()/2)/8))+8;
        formationY=(row*(handler.getHeight()/10))+8;
        
		// TODO Auto-generated constructor stub
	}

}
