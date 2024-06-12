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
        if (placeBeepers) placeBeeper();
        while (frontIsClear()) {
            moveKarel();
            if (placeBeepers) placeBeeper();
        }
    }

    private void moveToCell(int x, int y, boolean placeBeepers) {
        if (x > width || x < 1 || y > length || y < 1) return;
        if (currentX < x) turnEast();
        else if (currentX > x) turnWest();
        while (currentX != x) moveKarel();
        if (currentY < y) turnNorth();
        else if (currentY > y) turnSouth();
        while (currentY != y) moveKarel();
        if (placeBeepers) placeBeeper();
    }

    private void cross(boolean x_axis, boolean y_axis) {
        if (x_axis) {
            int closerX = 1;
            if (width - currentX < currentX - 1) closerX = width;
            System.out.println("closer x = " + closerX);
            moveToCell(closerX, currentY, false);
            if (currentX == 1) turnEast();
            else turnWest();
            moveToEdge(true);
        }

        if (y_axis) {
            int closerY = 1;
            if (length - currentY < currentY - 1) closerY = length;
            moveToCell(currentX, closerY, false);
            if (currentY == 1) turnNorth();
            else turnSouth();
            moveToEdge(true);
        }
    }

    private void zigzag(boolean x_axis, boolean y_axis) {
        if (x_axis) {
            for (int i = 1; i < width - 1; i += 2) {
                moveToCell(currentX - 1, currentY - 1, true);
                moveToCell(currentX - 1, currentY + 1, true);
            }
            if (frontIsClear()) moveToCell(currentX - 1, currentY - 1, true);
        }
        if (y_axis) {
            for (int i = 1; i < length - 1; i += 2) {
                moveToCell(currentX + 1, currentY - 1, true);
                moveToCell(currentX - 1, currentY - 1, true);
            }
            if (frontIsClear()) moveToCell(currentX + 1, currentY - 1, true);
        }
    }

    private void initial() {
        setBeepersInBag(10000);
        width = 1;
        length = 1;
        usedBeepers = 0;
        moves = 0;
    }

    //=====================================================================================

    public void run() {
        initial();
        measureDimensions();
        System.out.println("Width: " + width + "  Length: " + length);

        if (length >= 3 && width >= 3) {
            moveToCell(width, length / 2 + 1, true);
            if (length % 2 == 0) zigzag(true, false);
            else cross(true, false);

            if (width % 2 == 0) {
                moveToCell(width / 2, length, true);
                zigzag(false, true);
            } else {
                moveToCell(width / 2 + 1, length, true);
                cross(false, true);
            }
        }

        moveToCell(1, 1, false);
        System.out.println("Number of moves: " + moves);
        System.out.println("Used beepers: " + usedBeepers);
    }
}