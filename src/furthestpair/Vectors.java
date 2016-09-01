
package furthestpair;


public class Vectors {
    double x,y;
    public Vectors (double setX, double setY){
        x = setX;
        y = setY;
    }
    public double calcAngle(Vectors other){ //calculates angle between 2 vectors with dotproduct
        double dot = this.x*other.x + this.y*other.y;
        double magThis = Math.sqrt(this.x*this.x + this.y*this.y);
        double magOther = Math.sqrt(other.x*other.x + other.y*other.y);

        return Math.acos(dot / (magThis * magOther));
    }
    public void print(){
        System.out.println(this.x + "\t" + this.y); 
    }
    
}
