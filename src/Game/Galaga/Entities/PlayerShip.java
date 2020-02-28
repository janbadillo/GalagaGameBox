package Game.Galaga.Entities;

//import Game.GameStates.GalagaState;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Created by AlexVR on 1/25/2020
 */
public class PlayerShip extends BaseEntity{
	
	EntityManager enemies;
    private int health = 3,attackCooldown = 5,speed =6,destroyedCoolDown = 60*3, gameoverCooldown = 60*4;
    private boolean attacking = false, destroyed = false;
    private Animation deathAnimation;


     public PlayerShip(int x, int y, int width, int height, BufferedImage sprite, Handler handler) {
        super(x, y, width, height, sprite, handler);
        deathAnimation = new Animation(256,Images.galagaPlayerDeath);

    }
     
     public int getxPosition() {
    	 return x;
     }
     public int getyPosition() {

    	 return y;
     }
     

    @Override
    public void tick() {
        super.tick();
        for (BaseEntity enemy : enemies.entities) {
            if (enemy instanceof EnemyLaser) {
            	if (enemy.bounds.intersects(this.bounds)) {
                    destroyed = true;  
                }
            }
            
        }
   
        if (destroyed){
            if (destroyedCoolDown<=0){
                destroyedCoolDown=60*3;
                destroyed=false;
                deathAnimation.reset();
                bounds.x=x;
            }else{
                deathAnimation.tick();
                destroyedCoolDown--;
            }
        }else {
            if (attacking) {
                if (attackCooldown <= 0) {
                    attacking = false;
                }else {
                    attackCooldown--;
                }
            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && !attacking) {

                
                int lasercount = 0;
                for(BaseEntity entity: handler.getGalagaState().entityManager.entities){
                	
                	if(entity instanceof PlayerLaser){
                		lasercount ++;
                	}
                	if(lasercount >= 2) {
                		break;
                	}
                }
                
                if (lasercount < 2) {
                	handler.getMusicHandler().playEffect("laser.wav");
                    attackCooldown = 5;
                    attacking = true;
                    handler.getGalagaState().entityManager.entities.add(new PlayerLaser(this.x + (width / 2), this.y - 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
                    
                }
                
                //handler.getGalagaState().entityManager.entities.add(new PlayerLaser(this.x + (width / 2), this.y - 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));

            }
            if (handler.getKeyManager().left) {
            	if (x < handler.getWidth()/2 - handler.getWidth()/4 + 23) {}
            	else {
            		x -= (speed);
            	}
            }
            if (handler.getKeyManager().right) {
            	if (x > handler.getWidth()*3/4 - 89) {}
            	else {
            		x += (speed);
            	}
            }
            if (handler.getKeyManager().deadkey) {
                destroyed = true;
                health -= 1;
            }
            if (handler.getKeyManager().healthkey) {
            	health += 1;
            }
            

            bounds.x = x;
        }

    }

    @Override
    public void render(Graphics g) {
	   	 if(health == 0) {
	  		if (gameoverCooldown <= 0) {
	  			gameoverCooldown = 30*4;
	  			handler.setGameoverTrue();
	     		health = 3;
	  		}else {  	
	   		g.setColor(Color.RED);
	         g.setFont(new Font("TimesRoman", Font.PLAIN, 92));
	  		g.drawString("GAME  OVER",handler.getWidth()/2-handler.getWidth()/8,handler.getHeight()/2);
	   		gameoverCooldown --;
	   		}
	 	 }
         if (destroyed){

             if (deathAnimation.end && health >= 1){

                 g.drawString("READY",handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2);
             }else {
                 g.drawImage(deathAnimation.getCurrentFrame(), x, y, width, height, null);
             }
         }else {
             super.render(g);
         }
    }

    @Override
    public void damage(BaseEntity damageSource) {
        if (damageSource instanceof PlayerLaser){
            return;
        }
        health--;
        destroyed = true;
        handler.getMusicHandler().playEffect("explosion.wav");

        bounds.x = -10;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

}
