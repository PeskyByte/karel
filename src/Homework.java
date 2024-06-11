import stanford.karel.SuperKarel;

public class Homework extends SuperKarel {

    private int width = 1, length = 1;
    private int usedBeepers = 0, moves = 0;
    private int currentX = 1, currentY = 1;

    private void placeBeeper() {
        if (noBeepersPresent()) {
            putBeeper();
            usedBeepers++;
        }
    }

    private void moveKarel() {
        move();
        moves++;

        if (facingNorth()) currentY++;
        else if (facingSouth()) currentY--;
        else if (facingEast()) currentX++;
        else currentX--;
    }

    private void measureDimensions() {
        while (frontIsClear()) {
            moveKarel();
            width++;
        }
        turnLeft();
        while (frontIsClear()) {
            moveKarel();
            length++;
        }
    }

    private void turnNorth() {
        while (notFacingNorth()) turnLeft();
    }

    private void turnSouth() {
        while (notFacingSouth()) turnLeft();
    }

    private void turnEast() {
        while (notFacingEast()) turnLeft();
    }

    private void turnWest() {
        while (notFacingWest()) turnLeft();
    }

    private void moveToEdge(boolean placeBeepers) {
        if(placeBeepers) placeBeeper();
        while (frontIsClear()) {
            moveKarel();
            if(placeBeepers) placeBeeper();
        }
    }

    private void moveToCell(int x, int y) {
        if (currentX < x) turnEast();
        else if (currentX > x) turnWest();

        while (currentX != x) moveKarel();

        if (currentY < y) turnNorth();
        else if (currentY > y) turnSouth();

        while (currentY != y) moveKarel();
    }

    private void cross(boolean x_axis, boolean y_axis) {
        if (x_axis) {
            int closerX = 1;
            if(width - currentX < currentX - 1) closerX = width;
            System.out.println("closer x = " + closerX);
            moveToCell(closerX, currentY);
            turnAround();
            moveToEdge(true);
        }

        if (y_axis) {
            int closerY = 1;
            if(length - currentY < currentY - 1) closerY = length;
            moveToCell(currentX, closerY);
            turnAround();
            moveToEdge(true);
        }
    }

    private void diagonal(boolean left, boolean right) {
        if(left){
            moveToCell(1, length);
            placeBeeper();
            while(currentX != width && currentY != 1){
                moveToCell(currentX+1, currentY-1);
                placeBeeper();
            }
        }
        if(right){
            moveToCell(width, length);
            placeBeeper();
            while(currentX != 1 && currentY != 1){
                moveToCell(currentX-1, currentY-1);
                placeBeeper();
            }
        }
    }

    private void end() {
        moveToCell(1, 1);
        System.out.println("Number of moves: " + moves);
        System.out.println("Used beepers: " + usedBeepers);
        width = 1;
        length = 1;
        usedBeepers = 0;
        moves = 0;
    }

    //=====================================================================================

    private void zigZagY(boolean x_axis, boolean y_axis) {
        int alternate = 0;
        boolean direction = true;
        while (!cornerColorIs(GREEN)) {
            if (direction) {
                turnRight();
            } else {
                turnLeft();
            }
            moveKarel();
            alternate++;
            if (alternate % 2 == 0) {
                placeBeeper();
                direction = !direction;
                alternate = 0;
            }
        }
    }

    private void zigZagX() {
        placeBeeper();
        int alternate = 0;
        boolean direction = width % 2 != 0;
        while (!cornerColorIs(GREEN)) {
            if (direction) {
                turnRight();
            } else {
                turnLeft();
            }
            moveKarel();
            alternate++;
            if (alternate % 2 == 0) {
                placeBeeper();
                direction = !direction;
                alternate = 0;
            }
        }
    }

    public void run() {
        setBeepersInBag(10000);
        measureDimensions();
        System.out.println("Width: " + width + "  Length: " + length);
        
        end();
    }
}