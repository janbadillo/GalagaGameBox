package Game.GameStates;

import Game.Galaga.Entities.BaseEntity;
import Game.Galaga.Entities.EnemyBee;
import Game.Galaga.Entities.EntityManager;
import Game.Galaga.Entities.PlayerShip;
import Game.Galaga.Entities.BeeBoss;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by AlexVR on 1/24/2020.
 */
public class GalagaState extends State {

    public EntityManager entityManager;
    public static String Mode = "Menu";
    private Animation titleAnimation;
    public int selectPlayers = 1;
    public int startCooldown = 60*3;//seven seconds for the music to finish
	private boolean addEnemies = false, addEnemyfinish = false, addEnemies2 = false, addEnemyFinish2 = false;
	private int addEnemycounter = 0, randomSpawn = 80, randomSpawn2= 80, addEnemyCounter2 =0;
	Random random = new Random();
	
    public GalagaState(Handler handler){
        super(handler);
        refresh();
        entityManager = new EntityManager(new PlayerShip(handler.getWidth()/2-64,handler.getHeight()- handler.getHeight()/7,64,64,Images.galagaPlayer[0],handler));//Handle things that are dynamic. 
        titleAnimation = new Animation(256,Images.galagaLogo);
        
    }

    ///////////////////ADDED//////////////////////
    public boolean beeOverlap(BaseEntity sprite)
    {
    
    	for(BaseEntity entity: entityManager.entities )
		{
			if(entity instanceof EnemyBee && sprite instanceof EnemyBee)
			{
				boolean check = ((EnemyBee) entity).get_pos().contentEquals(((EnemyBee) sprite).get_pos());
				if(check)
					return true;
			}
		}
    	return false;
    }
    
    public boolean bossOverlap(BaseEntity sprite)
    {
    
    	for(BaseEntity entity: entityManager.entities )
		{
			if(entity instanceof BeeBoss && sprite instanceof BeeBoss)
			{
				boolean check = ((BeeBoss) entity).get_pos().contentEquals(((BeeBoss) sprite).get_pos());
				if(check)
					return true;
			}
		}
    	return false;
    }
    /////////^^^^^^^^^^^^^^^^^^^^^^ADDED^^^^^^^^^^^^^^^^^^^^^///////
    
    @Override
    public void tick() {
    	
    	if (handler.getGameover()) {
    		entityManager.entities.clear();
    		handler.getScoreManager().setGalagaCurrentScore(0);
    		Mode = "Menu";
    		handler.setGameoverFalse();
    	}
    	
        if (Mode.equals("Stage")){
        	addEnemycounter ++;
        	randomSpawn--;
        	randomSpawn2--;
        	if ((addEnemycounter >= 20 && (!addEnemyfinish || !addEnemyFinish2)) || randomSpawn <= 0 || randomSpawn2 <=0) {
        		addEnemies = true;
        		addEnemies2 = true;
        		randomSpawn = random.nextInt(60*5) + 60*5;
        		randomSpawn2 = random.nextInt(60*5) + 60*5;
        		addEnemycounter = 0;
        		addEnemyCounter2 = 0;
        	}
        	if (handler.getScoreManager().getGalagaCurrentScore() > handler.getScoreManager().getGalagaHighScore()) {
        		handler.getScoreManager().setGalagaHighScore(handler.getScoreManager().getGalagaCurrentScore());
        	}
            if (startCooldown<=0) {
                entityManager.tick();
                ////////////////ADDED THIS///////////////////////////    
                if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_P) || addEnemies){ // Waits for P to be pressed for Bee to spawn

                	EnemyBee test;
                	int col[] = {0,1,2,3,4,5,6,7};
                	int row[] = {3,4};
                	do
                	{
    	            	int rando_col = random.nextInt(8);
    	            	int rando_row = random.nextInt(2);
    	            	test = new EnemyBee(1,1,50,50,handler,row[rando_row],col[rando_col]); 
    		        	if(!beeOverlap(test)) {
    		            	entityManager.entities.add(test);
    		            	break;
    		        	} else {
    		        		int counter = 0;
    		        		for(BaseEntity entity: entityManager.entities ){
    		        			
    		        			if (entity instanceof EnemyBee) {
    		        				counter ++;
    		        			}
    		        		}
    		        		if (counter >= 16) {
    		        			addEnemyfinish = true;
    		        			break;
    		        		}
    		        	}
                	} while(beeOverlap(test));
                	addEnemies = false;
                }
                
                
                // ///////New enemy ////////
                if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_O)||addEnemies2){
                	Random random = new Random();
                	int col[] = {1,2,3,4,5,6};
                	int row[] = {1,2};
                	BeeBoss test; 
                	do
                	{
    	            	int rando_col = random.nextInt(6);
    	            	int rando_row = random.nextInt(2);
    	            	test = new BeeBoss(1,1,50,50,handler,row[rando_row],col[rando_col]); 
    		        	if(!bossOverlap(test)) {
    		            	entityManager.entities.add(test);
    		            	break;
    		        	} else {
    		        		int counter = 0;
    		        		for(BaseEntity entity: entityManager.entities ){
    		        			
    		        			if (entity instanceof BeeBoss) {
    		        				counter ++;
    		        			}
    		        		}
    		        		if (counter >= 12) {
    		        			addEnemyFinish2 = true;
    		        			break;
    		        		}
    		        	}
                	} while(bossOverlap(test));
                }	addEnemies2 = false;
                ///////////////////////////////////////////////////////////////    
                
            }else{
                startCooldown--;
            }
        }
        if(Mode.equals("Menu")){
            titleAnimation.tick();
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
                selectPlayers=1;
            }else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
                selectPlayers=2;
            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
            	addEnemyfinish = false;
                Mode = "Stage";
                addEnemies  = true;
                handler.getMusicHandler().playEffect("Galaga.wav");

            }


        }

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(0,0,handler.getWidth(),handler.getHeight());
        g.setColor(Color.BLACK);
        g.fillRect(handler.getWidth()/4,0,handler.getWidth()/2,handler.getHeight());
        Random random = new Random(System.nanoTime());

        for (int j = 1;j < random.nextInt(15)+60;j++) {
            switch (random.nextInt(6)) {
                case 0:
                    g.setColor(Color.RED);
                    break;
                case 1:
                    g.setColor(Color.BLUE);
                    break;
                case 2:
                    g.setColor(Color.YELLOW);
                    break;
                case 3:
                    g.setColor(Color.GREEN);
                case 4:
                	g.setColor(Color.MAGENTA);
                case 5:
                	g.setColor(Color.WHITE);
            }
            int randX = random.nextInt(handler.getWidth() - handler.getWidth() / 2) + handler.getWidth() / 4;
            int randY = random.nextInt(handler.getHeight());
            g.fillRect(randX, randY, 2, 2);

        }
        if (Mode.equals("Stage")) {
            g.setColor(Color.MAGENTA);//RED
            g.setFont(new Font("TimesRoman", Font.PLAIN, 62));
            g.drawString("HIGH",handler.getWidth()-handler.getWidth()/4,handler.getHeight()/16);
            g.drawString("SCORE:",handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/8);
            g.setColor(Color.MAGENTA);//WHITE
            g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/5);
            g.setColor(Color.RED);//RED
            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
            g.drawString("CURRENT:",handler.getWidth()-handler.getWidth()/2,handler.getHeight()/16);
            g.drawString(String.valueOf(handler.getScoreManager().getGalagaCurrentScore()),handler.getWidth()-handler.getWidth()/3+handler.getWidth()/48,handler.getHeight()/16);
            for (int i = 0; i< entityManager.playerShip.getHealth();i++) {
                g.drawImage(Images.galagaPlayer[0], (handler.getWidth() - handler.getWidth() / 4 + handler.getWidth() / 48) + ((entityManager.playerShip.width*2)*i), handler.getHeight()-handler.getHeight()/4, handler.getWidth() / 18, handler.getHeight() / 18, null);
            }
            if (startCooldown<=0) {
                entityManager.render(g);
            }else{
                g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
                g.setColor(Color.MAGENTA);//RED
                g.drawString("Start",handler.getWidth()/2-handler.getWidth()/18,handler.getHeight()/2);
            }
        }else{

            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));

            g.setColor(Color.MAGENTA);//RED
            g.drawString("HIGH-SCORE:",handler.getWidth()/2-handler.getWidth()/18,32);
           

            g.setColor(Color.MAGENTA);//WHITE
            g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()/2-32,64);
        

            g.drawImage(titleAnimation.getCurrentFrame(),handler.getWidth()/2-(handler.getWidth()/12),handler.getHeight()/2-handler.getHeight()/3,handler.getWidth()/6,handler.getHeight()/7,null);

            g.drawImage(Images.galagaCopyright,handler.getWidth()/2-(handler.getWidth()/8),handler.getHeight()/2 + handler.getHeight()/3,handler.getWidth()/4,handler.getHeight()/8,null);

            g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
            g.drawString("1   PLAYER",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2);
            g.drawString("2   PLAYER",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2+handler.getHeight()/12);
            if (selectPlayers == 1){
                g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2-handler.getHeight()/32,32,32,null);
            }else{
                g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2+handler.getHeight()/18,32,32,null);
            }


        }
    }

    @Override
    public void refresh() {
    	
    }
    
}
