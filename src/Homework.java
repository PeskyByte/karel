import stanford.karel.SuperKarel;

public class Homework extends SuperKarel {

    private int width = 1, length = 1;
    private int usedBeepers = 0, moves = 0;
    private int currentX = 1, currentY = 1;

    private void initial() {
        setBeepersInBag(10000);
        width = 1;
        length = 1;
        usedBeepers = 0;
        moves = 0;
    }

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
            placeBeeper();
            for (int i = 1; i <= width - 1; i += 2) {
                moveToCell(currentX - 1, currentY - 1, true);
                moveToCell(currentX - 1, currentY + 1, true);
            }
        }
        if (y_axis) {
            placeBeeper();
            for (int i = 1; i <= length - 1; i += 2) {
                moveToCell(currentX + 1, currentY - 1, true);
                moveToCell(currentX - 1, currentY - 1, true);
            }
        }
    }

    private void oneBlock(boolean x_axis, boolean y_axis) {
        if (x_axis && y_axis) return;

        int axisToWorkWith = x_axis ? length : width;
        if (axisToWorkWith <= 8) {
            if (x_axis) {
                while (currentY != 1) {
                    moveToCell(1, currentY - 1, true);
                    moveToCell(1, currentY - 1, false);
                }
            } else if (y_axis) {
                while (currentX != 1) {
                    moveToCell(currentX - 1, 1, true);
                    moveToCell(currentX - 1, 1, false);
                }
            }
        } else {
            int close = axisToWorkWith % 4;
            System.out.println(close);
            placeBeeper();
            for (int i = 0; i < close - 1; i++) {
                if (x_axis) moveToCell(1, currentY - 1, true);
                else if (y_axis) moveToCell(currentX - 1, 1, true);
            }
            int remain = axisToWorkWith - close;
            System.out.println(remain);
            if (x_axis) {
                while (currentY != 1) {
                    for (int i = 0; i < remain / 4; i++) {
                        moveToCell(1, currentY - 1, false);
                    }
                    placeBeeper();
                }
            } else if (y_axis) {
                while (currentX != 1) {
                    for (int i = 0; i < remain / 4; i++) {
                        moveToCell(currentX - 1, 1, false);
                    }
                    placeBeeper();
                }
            }
        }
    }

    private void twoBlock(boolean x_axis, boolean y_axis) {
        int axisToWorkWith = x_axis ? length : width;
        if (axisToWorkWith <= 8) {
            if (x_axis) {
                while (currentY != 1) {
                    moveToCell(currentX, currentY - 1, false);
                    cross(true, false);
                    moveToCell(currentX, currentY - 1, false);
                }
            } else if (y_axis) {
                while (currentX != 1) {
                    moveToCell(currentX - 1, currentY, false);
                    cross(false, true);
                    moveToCell(currentX - 1, currentY, false);
                }
            }
        } else {
            int close = axisToWorkWith % 4;
            System.out.println(close);
            if (x_axis) cross(true, false);
            else cross(false, true);
            for (int i = 0; i < close - 1; i++) {
                if (x_axis) {
                    moveToCell(currentX, currentY - 1, false);
                    cross(true, false);
                } else if (y_axis) {
                    moveToCell(currentX - 1, currentY, false);
                    cross(false, true);
                }
            }
            int remain = axisToWorkWith - close;
            System.out.println(remain);
            if (x_axis) {
                while (currentY != 1) {
                    for (int i = 0; i < remain / 4; i++) {
                        moveToCell(currentX, currentY - 1, false);
                    }
                    cross(true, false);
                }
            } else if (y_axis) {
                while (currentX != 1) {
                    for (int i = 0; i < remain / 4; i++) {
                        moveToCell(currentX - 1, currentY, false);
                    }
                    cross(false, true);
                }
            }
        }
    }

    private void special1xk() {
        if (width == 1 && length == 1) oneBlock(true, true);
        else if ((width == 1 && length == 2) || (length == 1 && width == 2)) oneBlock(true, true);
        else if (width == 1) oneBlock(true, false);
        else if (length == 1) oneBlock(false, true);
    }

    private void special2xk() {
        if (width == 2 && length <= 4) zigzag(false, true);
        else if (length == 2 && width <= 4) zigzag(true, false);

        else if (width == 2) {
            twoBlock(true, false);
        } else if (length == 2) {
            twoBlock(false, true);
        }
    }

    private void divide() {
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
    }

    //=====================================================================================
    public void run() {
        initial();
        measureDimensions();
        System.out.println("Width: " + width + "  Length: " + length);
        special1xk();
        special2xk();
        divide();
        moveToCell(1, 1, false);
        System.out.println("Number of moves: " + moves);
        System.out.println("Used beepers: " + usedBeepers);
    }
}