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

    private int distanceToCell(int x, int y) {
        return Math.abs(currentX - x) + Math.abs(currentY - y);
    }

    private void moveToEdge(boolean placeBeepers) {
        if (placeBeepers) placeBeeper();
        while (frontIsClear()) {
            moveKarel();
            if (placeBeepers) placeBeeper();
        }
    }

    private void cross(boolean x_axis, boolean y_axis, boolean presetCoordinates) {
        if (x_axis) {
            if (presetCoordinates) moveToCell(width, length / 2 + 1, true);
            int closerX = 1;
            if (width - currentX < currentX - 1) closerX = width;
            System.out.println("closer x = " + closerX);
            moveToCell(closerX, currentY, false);
            if (currentX == 1) turnEast();
            else turnWest();
            moveToEdge(true);
        }
        if (y_axis) {
            if (presetCoordinates) moveToCell(width / 2 + 1, length, true);
            int closerY = 1;
            if (length - currentY < currentY - 1) closerY = length;
            moveToCell(currentX, closerY, false);
            if (currentY == 1) turnNorth();
            else turnSouth();
            moveToEdge(true);
        }
    }

    private void doubleCross(boolean x_axis, boolean y_axis, boolean presetCoordinates) {
        if (x_axis) {
            int closerX = 1;
            if (width - currentX < currentX - 1) closerX = width;
            System.out.println("closer x = " + closerX);
            if (presetCoordinates) moveToCell(width, length / 2, true);
            else moveToCell(closerX, currentY, true);
            for (int i = 1; i <= width; i++) {
                if (i % 2 != 0) moveToCell(currentX, currentY + 1, true);
                else moveToCell(currentX, currentY - 1, true);
                moveToCell(currentX - 1, currentY, true);
            }
        }
        if (y_axis) {
            int closerY = 1;
            if (length - currentY < currentY - 1) closerY = length;
            if (presetCoordinates) moveToCell(width / 2, length, true);
            else moveToCell(currentX, closerY, true);
            for (int i = 1; i <= length; i++) {
                if (i % 2 != 0) moveToCell(currentX + 1, currentY, true);
                else moveToCell(currentX - 1, currentY, true);
                moveToCell(currentX, currentY - 1, true);
            }
        }
    }

    private void zigzag(boolean x_axis, boolean y_axis, boolean presetCoordinate) {
        if (x_axis) {
            if (presetCoordinate) moveToCell(width, length / 2 + 1, true);
            placeBeeper();
            for (int i = 1; i <= width - 1; i++) {
                if (i % 2 != 0) moveToCell(currentX - 1, currentY - 1, true);
                else {
                    moveToCell(currentX - 1, currentY + 1, true);
                }
            }
        }
        if (y_axis) {
            if (presetCoordinate) moveToCell(width / 2, length, true);
            placeBeeper();
            for (int i = 1; i <= length - 1; i++) {
                if (i % 2 != 0) moveToCell(currentX + 1, currentY - 1, true);
                else moveToCell(currentX - 1, currentY - 1, true);
            }
        }
    }

    private void oneBlock(boolean x_axis, boolean y_axis) {
        if (x_axis && y_axis) return;
        int axisToWorkWith = x_axis ? length : width;
        if (axisToWorkWith <= 6) {
            while (x_axis ? currentY != 1 : currentX != 1) {
                moveToCell(x_axis ? 1 : currentX - 1, x_axis ? currentY - 1 : 1, true);
                moveToCell(x_axis ? 1 : currentX - 1, x_axis ? currentY - 1 : 1, false);
            }
        } else {
            int close = axisToWorkWith % 4;
            if (close == 3) {
                for (int i = 0; i < axisToWorkWith / 4; i++) {
                    moveToCell(x_axis ? 1 : currentX - 1, x_axis ? currentY - 1 : 1, false);
                }
                while (x_axis ? currentY != 1 : currentX != 1) {
                    if (!(distanceToCell(1, 1) == 0)) placeBeeper();
                    for (int i = 0; i < axisToWorkWith / 4 + 1; i++) {
                        moveToCell(x_axis ? 1 : currentX - 1, x_axis ? currentY - 1 : 1, false);
                    }
                }
            } else {
                placeBeeper();
                for (int i = 0; i < close - 1; i++) {
                    moveToCell(x_axis ? 1 : currentX - 1, x_axis ? currentY - 1 : 1, true);
                }
                int remain = axisToWorkWith - close;
                System.out.println(remain);
                while (x_axis ? currentY != 1 : currentX != 1) {
                    for (int i = 0; i < remain / 4; i++) {
                        moveToCell(x_axis ? 1 : currentX - 1, x_axis ? currentY - 1 : 1, false);
                    }
                    if (!(axisToWorkWith % 4 == 0 && distanceToCell(1, 1) == 0)) placeBeeper();
                }
            }
        }
    }

    private void twoBlock(boolean x_axis, boolean y_axis) {
        int axisToWorkWith = x_axis ? length : width;
        if (axisToWorkWith <= 8) {
            for (int i = 0; i < axisToWorkWith - 4; i++) {
                cross(x_axis, y_axis, false);
                moveToCell(x_axis ? currentX : currentX - 1, x_axis ? currentY - 1 : currentY, false);
            }
            zigzag(!x_axis, !y_axis, false);
        } else {
            int close = axisToWorkWith % 4;
            if (close == 3) {
                for (int i = 0; i < axisToWorkWith / 4; i++) {
                    moveToCell(x_axis ? 1 : currentX - 1, x_axis ? currentY - 1 : 1, false);
                }
                while (x_axis ? currentY != 1 : currentX != 1) {
                    if (!(distanceToCell(1, 1) <= 1)) cross(x_axis, y_axis, false);
                    for (int i = 0; i < axisToWorkWith / 4 + 1; i++) {
                        moveToCell(x_axis ? currentX : currentX - 1, x_axis ? currentY - 1 : currentY, false);
                    }
                }
            } else {
                cross(x_axis, y_axis, false);
                for (int i = 0; i < close - 1; i++) {
                    moveToCell(x_axis ? currentX : currentX - 1, x_axis ? currentY - 1 : currentY, false);
                    cross(x_axis, y_axis, false);
                }
                int remain = axisToWorkWith - close;
                while (x_axis ? currentY != 1 : currentX != 1) {
                    for (int i = 0; i < remain / 4; i++) {
                        moveToCell(x_axis ? currentX : currentX - 1, x_axis ? currentY - 1 : currentY, false);
                    }
                    if (!(axisToWorkWith % 4 == 0 && distanceToCell(1, 1) <= 1))
                        cross(x_axis, y_axis, false);
                }
            }
        }
    }

    private void special1xk() {
        if (width == 1 && length == 1) return;
        else if ((width == 1 && length == 2) || (length == 1 && width == 2)) return;
        else if (width == 1) oneBlock(true, false);
        else if (length == 1) oneBlock(false, true);
    }

    private void special2xk() {
        if (width == 2 && length <= 4 && length > 1) {
            moveToCell(currentX - 1, currentY, false);
            zigzag(false, true, false);
        } else if (length == 2 && width <= 4 && width > 1) zigzag(true, false, false);
        else if (width == 2 && length != 1) twoBlock(true, false);
        else if (length == 2 && width != 1) twoBlock(false, true);
    }

    private void divide() {
        if (length >= 3 && width >= 3) {
            if (length % 2 == 0 && width % 2 == 0) {
                if (length == 4 || width == 4) {
                    doubleCross(true, true, true);
                } else {
                    zigzag(width == length, length == width, true);
                    zigzag(width < length, length < width, true);
                    doubleCross(width > length, length > width, true);
                }
            } else {
                if (length == 4 || width == 4) {
                    doubleCross(length == 4, width == 4, true);
                    cross(length != 4, width != 4, true);
                } else {
                    cross(length % 2 != 0, width % 2 != 0, true);
                    doubleCross(length % 2 == 0, width % 2 == 0, true);
                }
            }
        }
    }

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