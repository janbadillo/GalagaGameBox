package Game.Galaga.Entities;

import Main.Handler;

import java.awt.image.BufferedImage;

/**
 * Created by AlexVR on 1/25/2020
 */
public class EnemyLaser extends BaseEntity {

    EntityManager enemies;
    int speed = 6;
   

    public EnemyLaser(int x, int y, int width, int height, BufferedImage sprite, Handler handler,EntityManager enemies) {
        super(x, y, width, height, sprite, handler);
        this.enemies=enemies;
  
    }

    @Override
    public void tick() {
    	
        if (!remove) {
            super.tick();
            y += speed;
            bounds.y = y;
            
            if (enemies.playerShip.bounds.intersects(bounds)) {
            	remove = true;
            }
            
        }
        if (y >= handler.getHeight()) {
        	remove = true;
        }
    }
}
