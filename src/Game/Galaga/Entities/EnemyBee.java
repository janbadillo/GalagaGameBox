package Game.Galaga.Entities;


import Main.Handler;
import Resources.Animation;
import Resources.Images;
import Game.Galaga.Entities.EntityManager;
//import Game.Galaga.Entities.PlayerShip;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyBee extends BaseEntity {
    int row,col;//row 3-4, col 0-7
    boolean justSpawned=true,attacking=false, positioned=false,hit=false,centered = false;
    Animation idle,turn90Left;
    int spawnPos;//0 is left 1 is top, 2 is right, 3 is bottom
    int formationX,formationY,speed,centerCoolDown=60;//The enemy stay in the center for a bit.
    int timeAlive=0;
    int randomAttack = random.nextInt(60*10) + 60*5;
    int attackX, attackY;
    
    public EnemyBee(int x, int y, int width, int height, Handler handler,int row, int col) {
        super(x, y, width, height, Images.galagaEnemyBee[0], handler);
        this.row = row;
        this.col = col;
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.galagaEnemyBee[0];
        idleAnimList[1] = Images.galagaEnemyBee[1];
        idle = new Animation(512,idleAnimList);
        turn90Left = new Animation(128,Images.galagaEnemyBee);
        spawn();
        speed = 7;
        formationX=(handler.getWidth()/4)+(col*((handler.getWidth()/2)/8))+8;
        formationY=(row*(handler.getHeight()/10))+8;
    }
    private void spawn() {
        spawnPos = random.nextInt(4);
        switch (spawnPos){
            case 0://left
                x = (handler.getWidth()/4)-width;
                y = random.nextInt(handler.getHeight()-handler.getHeight()/8);
                break;
            case 1://top
                x = random.nextInt((handler.getWidth()-handler.getWidth()/2))+handler.getWidth()/4;
                y = -height;
                break;
            case 2://right
                x = (handler.getWidth()/2)+ width + (handler.getWidth()/4);
                y = random.nextInt(handler.getHeight()-handler.getHeight()/8);
                break;
            case 3://down
                x = random.nextInt((handler.getWidth()/2))+handler.getWidth()/4;
                y = handler.getHeight()+height;
                break;
        }
        bounds.x=x;
        bounds.y=y;
    }
    
    public String get_pos()
    {
    	String x = String.valueOf(this.col);
    	String y = String.valueOf(this.row);
    	return x+y;
    }
    
    public int getAttackX()
    {
		return handler.getGalagaState().entityManager.playerShip.x;
    }
    
    public int getAttackY()
    {
		return handler.getGalagaState().entityManager.playerShip.y;
    }
    @Override
    public void tick() {
        super.tick();
        idle.tick();
        if (hit){
            if (enemyDeath.end) {
                remove = true;
                handler.getScoreManager().setSumScore();
                return;
            }
            enemyDeath.tick();
        }
        if (justSpawned){
            if (!centered && Point.distance(x,y,handler.getWidth()/2,handler.getHeight()/2)>speed + 1){//reach center of screen
                switch (spawnPos){
                    case 0://left
                        x+=speed;
                        if (Point.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;
                    case 1://top
                        y+=speed;
                        if (Point.distance(x,y,handler.getWidth()/2,y)>speed) {
                            if (x > handler.getWidth() / 2) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        break;
                    case 2://right
                        x-=speed;
                        if (Point.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;
                    case 3://down
                        y-=speed;
                        if (Point.distance(x,y,handler.getWidth()/2,y)>speed) {
                            if (x > handler.getWidth() / 2) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        break;
                }

            }else {//move to formation
                if (!centered){
                    centered = true;
                }
                if (centerCoolDown<=0){
                    if (Point.distance(x, y, formationX, formationY) > speed) {//reach center of screen
                        if (Math.abs(y-formationY)>6) {
                            y -= speed;
                        }
                        if (Point.distance(x,y,formationX,y)>speed/2) {
                            if (x >formationX) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                    }else {
                    	positioned = true;
                    }
                }else{
                    centerCoolDown--;
                }

            }
        }if (positioned){
        	
           	// In a random amount of time, change attacking to true
        	randomAttack--;
        	if (randomAttack <= 0) {
        		attacking = true;
        		randomAttack = random.nextInt(60*10) + 60*5;
        		attackX = handler.getGalagaState().entityManager.playerShip.x;
        		attackY = handler.getGalagaState().entityManager.playerShip.y;
        		positioned = false;
        		justSpawned = false;
        	}
        	
        }if (attacking){

        	// Move towards enemy position
        	if (Point.distance(x, y, attackX, attackY) > speed) {
                if (Math.abs(y - attackY) > 6) {
                    y += speed;
                }
                if (Point.distance(x,y,attackX,y)>speed/2) {
                    if (x > attackX) {
                        x -= speed;
                    } else {
                        x += speed;
                    }
                }    
            }
        	else { 
        		attacking = false;
        		justSpawned = true;
        		
        	}
        }       
        bounds.x=x;
        bounds.y=y;
    }

    @Override
    public void render(Graphics g) {
        ((Graphics2D)g).draw(new Rectangle(formationX,formationY,32,32));
        if (arena.contains(bounds)) {
            if (hit){
                g.drawImage(enemyDeath.getCurrentFrame(), x, y, width, height, null);
            }else{
                g.drawImage(idle.getCurrentFrame(), x, y, width, height, null);

            }
        }
    }

    @Override
    public void damage(BaseEntity damageSource) {
        super.damage(damageSource);
        if (damageSource instanceof PlayerLaser){
            hit=true;
            handler.getMusicHandler().playEffect("explosion.wav");
            damageSource.remove = true;
        }
    }
    
    	
    }

