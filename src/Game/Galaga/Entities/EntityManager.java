package Game.Galaga.Entities;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by AlexVR on 1/25/2020
 */
public class EntityManager {

    public ArrayList<BaseEntity> entities;
    public PlayerShip playerShip;
    
    public ArrayList<BaseEntity> Adder;
    
    public EntityManager(PlayerShip playerShip) {
        entities = new ArrayList<>();
        this.playerShip = playerShip;
        Adder = new ArrayList<>();
    }
    
    public int getxPosition() {
   	 return playerShip.x;
    }
    public int getyPosition() {
   	 return playerShip.y;
    }
    
    public void tick(){
        playerShip.tick();
        ArrayList<BaseEntity> toRemove = new ArrayList<>();
        
        for (BaseEntity entity: entities){
            if (entity.remove){
                toRemove.add(entity);
                continue;
            }
            entity.tick();
            
            if (entity.bounds.intersects(playerShip.bounds)){
                playerShip.damage(entity);
            }
        }
        for (BaseEntity bop: Adder) entities.add(bop);
        Adder.clear();
        
        for (BaseEntity toErase:toRemove){
            entities.remove(toErase);
        }
        
    }
   
    public void render(Graphics g){
        for (BaseEntity entity: entities){
            entity.render(g);
        }
        playerShip.render(g);
    }

}
