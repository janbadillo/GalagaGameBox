package Game.GameStates;

import Game.Galaga.Entities.BaseEntity;
import Game.Galaga.Entities.EnemyBee;
import Game.Galaga.Entities.EntityManager;
import Game.Galaga.Entities.PlayerShip;
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
    public int startCooldown = 60*7;//seven seconds for the music to finish

    public GalagaState(Handler handler){
        super(handler);
        refresh();
        entityManager = new EntityManager(new PlayerShip(handler.getWidth()/2-64,handler.getHeight()- handler.getHeight()/7,64,64,Images.galagaPlayer[0],handler));//Handle things that are dynamic. 
        titleAnimation = new Animation(256,Images.galagaLogo);
        
    }

    ///////////////////ADDED//////////////////////
    public boolean overlap(BaseEntity sprite)
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
    /////////^^^^^^^^^^^^^^^^^^^^^^ADDED^^^^^^^^^^^^^^^^^^^^^///////
    
    @Override
    public void tick() {
    	if (handler.getGameover()) {
    		
    		handler.getScoreManager().setGalagaCurrentScore(0);
    		Mode = "Menu";
    		handler.setGameoverFalse();
    	}
    	
        if (Mode.equals("Stage")){
        	if (handler.getScoreManager().getGalagaCurrentScore() > handler.getScoreManager().getGalagaHighScore()) {
        		handler.getScoreManager().setGalagaHighScore(handler.getScoreManager().getGalagaCurrentScore());
        	}
            if (startCooldown<=0) {
                entityManager.tick();
                ////////////////ADDED THIS///////////////////////////    
                if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)){
                	Random random = new Random();
                	int col[] = {0,1,2,3,4,5,6,7};
                	int row[] = {3,4};
                	EnemyBee test; 
                	do
                	{
    	            	int rando_col = random.nextInt(8);
    	            	int rando_row = random.nextInt(2);
    	            	test = new EnemyBee(1,1,50,50,handler,row[rando_row],col[rando_col]); 
    		        	if(!overlap(test)) {
    		            	entityManager.entities.add(test);
    		        	}
                	} while(!overlap(test)); // must fix this loop
                }
                
                if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_O)){
                	Random random = new Random();
                	int col[] = {0,1,2,3,4,5,6,7};
                	int row[] = {0,1,2};
                	EnemyBee test; 
                	do
                	{
    	            	int rando_col = random.nextInt(8);
    	            	int rando_row = random.nextInt(3);
    	            	test = new EnemyBee(1,1,50,50,handler,row[rando_row],col[rando_col]); 
    		        	if(!overlap(test)) {
    		            	entityManager.entities.add(test);
    		        	}
                	}while(overlap(test));
                }
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
                Mode = "Stage";
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
