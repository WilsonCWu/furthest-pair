
package furthestpair;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JFrame;


public class FurthestPair extends JFrame{

    int width = 800;
    int height = 800;
    int numElements = 100;
    int min = 100;
    int max = 700;
    int radius = 3; //radius of points
    int pauseTime = 50; // in miliseconds
    
    int currentIndex = 0;
    int oldIndex;
    Points[] numArray = new Points[numElements];
    Points[] hullArray = new Points[numElements+1];
    int hullTotal = -1;
    Points[][] antipodalArray;
    int antipodalTotal = -1;
    
    double minAngle = 2*Math.PI;
    int minIndex;
    
    double furthestDistance = 0;
    int distanceIndex = -1;
    
    int bruteIndex1 = -1;
    int bruteIndex2 = -1;
        
    public void genNumbers(Random r){ //creates initial list of points
        for (int i = 0; i < numArray.length; i++){
            numArray[i] = new Points(r.nextInt(max-min+1)+min, r.nextInt(max-min+1)+min);
            //System.out.print(numArray[i][0]);
            //System.out.println("\t" + numArray[i][1]);
        } 
    }
    public void setInitialPoint(){ // finds lowest point
        for (int i = 0; i < numArray.length; i++){
            if(numArray[i].y < numArray[currentIndex].y){
                currentIndex = i;
            }
            else if(numArray[i].y == numArray[currentIndex].y){
                if (numArray[i].x < numArray[currentIndex].x){
                    currentIndex = i;
                }
            }
        }
        addHull (currentIndex);
    }
    public void addHull(int index){ 
        numArray[index].setUsed();
        hullTotal++;
        //System.out.println(hullTotal+"\t"+index);
        hullArray[hullTotal] = numArray[index];  
    }
    public void addAntipodalPair(int index1, int index2){
        antipodalTotal++;
        antipodalArray[antipodalTotal][0] = hullArray[index1%hullTotal];  
        antipodalArray[antipodalTotal][1] = hullArray[index2%hullTotal];  
    }
    public void jarvisMarch(){
        Vectors initVector = new Vectors(1,0);
        Vectors currentVector;
        setInitialPoint();
        double currentAngle;
        
        do{ 
            minAngle = 2*Math.PI;
            this.repaint();
            sleep(pauseTime,0);
            
            for (int i = 0; i < numArray.length; i++){
                if (hullTotal == 0){
                    oldIndex = 0;
                }
                else{
                    oldIndex = hullTotal - 1;
                }

                if ((numArray[i].compare(hullArray[oldIndex])==false) && (numArray[i].compare(hullArray[hullTotal])==false)){
                    //System.out.println(hullTotal+"\t"+i);
                    currentVector = hullArray[hullTotal].subtractPoints(numArray[i]);
                    currentAngle = initVector.calcAngle(currentVector);
                    //System.out.println(currentAngle);
                    //System.out.println(initVector.x+"\t"+initVector.y);
                    //System.out.println(currentVector.x+"\t"+currentVector.y);
                    if (currentAngle < minAngle){
                        minIndex = i;
                        minAngle = currentAngle;
                    } 
                }

            } 
            
            initVector = hullArray[hullTotal].subtractPoints(numArray[minIndex]);
            //System.out.println(initVector.x+"\t"+initVector.y);
            addHull(minIndex);
            currentIndex = minIndex;  
            
        } while(!numArray[currentIndex].compare(hullArray[0]));
        //hullTotal--;//rids of repeat final point 
        
    }
    public void rotatingCalipers(){
        antipodalArray = new Points[(hullTotal)*2+3][2];
        Vectors oldV1 = new Vectors(1,0);
        Vectors oldV2 = new Vectors(-1,0);
        Vectors newV1,newV2;
        //minAngle = 2*Math.PI;
        int index1 = 0; //hull already has bottommost as first point
        int index2 = 0;
        for (int i = 0; i < hullTotal; i++){ //set top point
            if (hullArray[i].y > hullArray[index2].y){
                index2 = i;
            }
        }
        addAntipodalPair(index1, index2);
        int endValue1 = index1 + hullTotal;
        int endValue2 = index2 + hullTotal;
        do { 
            newV1 = hullArray[index1%hullTotal].subtractPoints(hullArray[(index1+1)%hullTotal]); // % in case it goes over, and hullTotal includes extra useless repeat final point
            newV2 = hullArray[index2%hullTotal].subtractPoints(hullArray[(index2+1)%hullTotal]); // % in case it goes over, and hullTotal includes extra useless repeat final point
            double angle1 = newV1.calcAngle(oldV1);
            double angle2 = newV2.calcAngle(oldV2);
            //rotates counterclockwise, drawing vector from one of 2 initial points, depending on which has less angle
            if ( angle1 < angle2 && index1%hullTotal != index2%hullTotal){   
                index1++;
                oldV1 = newV1;
                oldV2 = new Vectors(-newV1.x, -newV1.y);
                addAntipodalPair(index1%hullTotal, index2%hullTotal);
            }
            else if ( angle2 < angle1 && index1%hullTotal != index2%hullTotal){
                index2++;
                oldV2 = newV2;
                oldV1 = new Vectors(-newV2.x, -newV2.y);
                addAntipodalPair(index2%hullTotal, index1%hullTotal);
            }
            else if (index1%hullTotal == index2%hullTotal || angle2 == angle1){ // when same point, or same angle, advance forward first vector
                index1++;
                oldV1 = newV1;
                oldV2 = new Vectors(-newV1.x, -newV1.y);
            }
            this.repaint();
            sleep(pauseTime,0);
            
        } while (index1 <= endValue1 && index2 <= endValue2);
        //System.out.println(hullTotal);
        //System.out.println(antipodalTotal);
                
        //hullArray[index1].print();
        //hullArray[index2].print();
    }
    public void longestDistanceAntipodal(){ //finds longest distance in the antipodal pairs
        furthestDistance = 0;
        double currentDistance;
        for (int i = 0; i <= antipodalTotal; i++){
            currentDistance = antipodalArray[i][0].distance(antipodalArray[i][1]);
            if (currentDistance > furthestDistance){
                furthestDistance = currentDistance;
                distanceIndex = i;
            }
        }
    }
    public void efficientSort(){ //uses jarvis march for hull and rotating calipers for antipodal pairs
        this.jarvisMarch(); //hull drawn in red
        this.rotatingCalipers(); // pairs drawn in black
        this.longestDistanceAntipodal(); // longest line in blue
    }
    public void bruteForceSort(){ // checks all distances, green circle around longest pair
        furthestDistance = 0;
        double currentDistance;
        for (int i = 0; i < numArray.length; i++){
            for (int j = i; j < numArray.length; j++){
                currentDistance = numArray[i].distance(numArray[j]);
                if (currentDistance > furthestDistance){
                    furthestDistance = currentDistance;
                    bruteIndex1 = i;
                    bruteIndex2 = j;
                }
            }
        }
    }
    
    public void initializeWindow(){
        setSize (width,height);
        setTitle ("Furthest Pair");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);
        setVisible(true); 
    }
    public void paint(Graphics brush){
        Image img = createImage();
        brush.drawImage(img, 0, 0,this);
    }
    
    private Image createImage (){
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gBrush = (Graphics2D) bufferedImage.getGraphics();
        
        gBrush.setColor(Color.white);
        gBrush.fillRect(0, 0, width, height);
        
        
        for (int i = 0; i < numArray.length; i++){ //draw dots
            gBrush.setColor(numArray[i].color);
            gBrush.fillOval((int)numArray[i].x - radius, height -((int)numArray[i].y + radius), 2 *radius, 2 * radius);
        } 
        
        
        //gBrush.drawLine((int)hullArray[0].x, (int)hullArray[0].y, (int)hullArray[hullTotal].x, (int)hullArray[hullTotal].y);
        for (int i = 0; i < hullTotal; i++){// draw vector lines
            gBrush.setColor(Color.red); 
            //System.out.println(i);
            gBrush.drawLine((int)hullArray[i].x, height -(int)hullArray[i].y, (int)hullArray[i+1].x, height -(int)hullArray[i+1].y);
        } 
        
        for (int i = 0; i <= antipodalTotal; i++){ // draw antipodal pairs
            gBrush.setColor(Color.black); 
            gBrush.drawLine((int)antipodalArray[i][0].x, height -(int)antipodalArray[i][0].y, (int)antipodalArray[i][1].x, height -(int)antipodalArray[i][1].y);
        } 
        
        if (distanceIndex >= 0){ // draw longest line
            gBrush.setColor(Color.blue); 
            gBrush.drawLine((int)antipodalArray[distanceIndex][0].x, height -(int)antipodalArray[distanceIndex][0].y, (int)antipodalArray[distanceIndex][1].x, height -(int)antipodalArray[distanceIndex][1].y);
        } 
        
        if (bruteIndex1 >= 0 && bruteIndex2 >= 0){ // draw brute force points
            gBrush.setColor(Color.green); 
            gBrush.drawOval((int)numArray[bruteIndex1].x - radius, height - (int)numArray[bruteIndex1].y - radius, 2 * radius, 2 * radius);
            gBrush.drawOval((int)numArray[bruteIndex2].x - radius, height - (int)numArray[bruteIndex2].y - radius, 2 * radius, 2 * radius);
        } 
        
        return bufferedImage;
    }
    public static void sleep(long milli, int nano) {
        try {
            Thread.sleep(milli,nano);
        } 
        catch (Exception e) {}
    }


    public static void main(String[] args) {
        
        Random r = new Random();
        FurthestPair FP = new FurthestPair();
        FP.genNumbers(r);
        FP.initializeWindow();
        FP.bruteForceSort(); //circles furthest
        FP.efficientSort(); //draws line between furthest

        FP.repaint();

                

        
    
    }
    
}
