import stanford.karel.SuperKarel;

public class Homework extends SuperKarel {

    private int width = 1, length = 1;
    private int usedBeepers = 0;
    private int moves = 0;

    private void placeBeeper(){
        putBeeper();
        usedBeepers++;
    }

    private void moveKarel(){
        move();
        moves++;
    }

    private void measureDimensions(){
        while(frontIsClear()){
            moveKarel();
            width++;
        }
        turnLeft();
        while(frontIsClear()){
            moveKarel();
            length++;
        }
    }

    private void zigZagY(){
        placeBeeper();
        turnRight();
        int alternate = 0;
        boolean direction = true;
        while(!cornerColorIs(GREEN)){
            if(direction){
                turnRight();
            } else{
                turnLeft();
            }
            moveKarel();
            alternate++;
            if(alternate % 2 == 0){
                placeBeeper();
                direction = !direction;
                alternate = 0;
            }
        }
    }

    private void zigZagX(){
        placeBeeper();
        int alternate = 0;
        boolean direction = false;
        if(width%2 != 0){
            direction = true;
        }
        while(!cornerColorIs(GREEN)){
            if(direction){
                turnRight();
            } else{
                turnLeft();
            }
            moveKarel();
            alternate++;
            if(alternate % 2 == 0){
                placeBeeper();
                direction = !direction;
                alternate = 0;
            }
        }
    }
    // turn north, south, east ,west
    public void run(){
        paintCorner(GREEN);
        measureDimensions();
        System.out.println("Width: " + width + "  Length: " + length);

        if(width < 3 || length < 3){
            if(length > width){
                zigZagY();
            }
            else{
                // check 2 rows 3 cols
                zigZagX();
            }
        }
        else{
            turnLeft();
            for(int i=0;i<width/2;i++){
                moveKarel();
            }
            turnLeft();
            while(frontIsClear()){
                placeBeeper();
                moveKarel();
            }
            placeBeeper();
            turnLeft();
            while(frontIsClear())
                moveKarel();
            turnLeft();
            for(int i=0;i<length/2;i++){
                moveKarel();
            }
            turnLeft();
            while(frontIsClear()){
                placeBeeper();
                moveKarel();
            }
            placeBeeper();
            turnLeft();
            while(!cornerColorIs(GREEN))
                moveKarel();
        }
        paintCorner(null);
        System.out.println("Used beepers: " + usedBeepers);
        System.out.println("Number of moves: " + moves);
        width = 1;
        length = 1;
        usedBeepers = 0;
        moves = 0;
    }
}