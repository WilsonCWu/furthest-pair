
package furthestpair;

import java.awt.Color;

/**
 *
 * @author Wilson
 */
public class Points {
    double x,y;
    boolean used;
    Color color;
    
    public Points(double setX, double setY){
        this.x = setX;
        this.y = setY;
        used = false;
        color = Color.black;
    }
    
    public void setUsed(){
        used = true;
        color = Color.red;
    }
    
    public Vectors subtractPoints(Points other){ //creates vector, with "this" point as origin, other as direction
        Vectors pointVector = new Vectors(other.x-this.x, other.y-this.y);
        
        return pointVector;
    }
    
    public boolean compare(Points other){ //returns true if points are the same 
        return (this.x == other.x && this.y == other.y);
    }
    
    public double distance(Points other){ //distance formula of 2 points 
        return Math.sqrt(Math.pow(this.x-other.x, 2)+Math.pow(this.y-other.y, 2));
    }
    public void print(){ 
        System.out.println(this.x + "\t" + this.y); 
    }

    

  

   
    


    
}
