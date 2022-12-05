// Names: Curtis Kokuloku & Atakilt Massengill
// x500s:kokul003 & masse171

import java.util.Random;
import java.util.Scanner;

public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {

        this.startRow = startRow;
        this.endRow = endRow;

        maze = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = new Cell();
                boolean endPath = i == endRow && j == cols - 1;
                if (endPath) {
                    maze[i][j].setRight(false);
                }
            }
        }
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze() {
        Scanner s = new Scanner(System.in);
        System.out.println("\nEnter rows and columns (rows and columns must be between 5 and 20, inclusive): ");
        int rows = s.nextInt();
        int cols = s.nextInt();

        boolean inBound = checkInBounds(rows, cols);

        if (!inBound) {
            System.out.println("\nInvalid rows and columns. Please try again.\n");
            makeMaze();
        }

        int sRow = getRandomRow(rows-1);
        int eRow = getRandomRow(rows-1);

        MyMaze myMaze = new MyMaze(rows, cols, sRow, eRow);

        Stack1Gen<int[]> mazeStack = new Stack1Gen<>();
        int[] startCell = {myMaze.startRow, 0};
        mazeStack.push(startCell);

        myMaze.maze[myMaze.startRow][0].setVisited(true);

        while (!mazeStack.isEmpty()) {

            startCell = mazeStack.top();
            int currRow = startCell[0];
            int currCol = startCell[1];

            boolean noPathFound = (currRow - 1 < 0 || myMaze.maze[currRow - 1][currCol].getVisited())
                    && (currRow + 1 > myMaze.maze.length - 1 || myMaze.maze[currRow + 1][currCol].getVisited())
                    && (currCol - 1 < 0 || myMaze.maze[currRow][currCol - 1].getVisited())
                    && (currCol + 1 > myMaze.maze[0].length - 1 || myMaze.maze[currRow][currCol + 1].getVisited());

            if (noPathFound) {
                mazeStack.pop();
            }

            if (!noPathFound) {
                boolean upPathFound = (currRow - 1) >= 0 && !myMaze.maze[currRow - 1][currCol].getVisited();
                boolean downPathFound = (currRow + 1) <= myMaze.maze.length - 1 && !myMaze.maze[currRow + 1][currCol].getVisited();
                boolean leftPathFound = (currCol - 1) >= 0 && !myMaze.maze[currRow][currCol - 1].getVisited();
                boolean rightPathFound = (currCol + 1) <= myMaze.maze[0].length - 1 && !myMaze.maze[currRow][currCol + 1].getVisited();
                String newPath = getRandomNeighbor();
                switch (newPath) {
                    case "up" -> {
                        if (upPathFound) {
                            myMaze.maze[currRow - 1][currCol].setBottom(false);
                            myMaze.maze[currRow - 1][currCol].setVisited(true);
                            currRow--;
                            int[] newCell = {currRow, currCol};
                            mazeStack.push(newCell);
                        }
                    }
                    case "down" -> {
                        if (downPathFound) {
                            myMaze.maze[currRow][currCol].setBottom(false);
                            myMaze.maze[currRow + 1][currCol].setVisited(true);
                            currRow++;
                            int[] newCell = {currRow, currCol};
                            mazeStack.push(newCell);
                        }
                    }
                    case "left" -> {
                        if (leftPathFound) {
                            myMaze.maze[currRow][currCol - 1].setRight(false);
                            myMaze.maze[currRow][currCol - 1].setVisited(true);
                            currCol--;
                            int[] newCell = {currRow, currCol};
                            mazeStack.push(newCell);
                        }
                    }
                    case "right" -> {
                        if (rightPathFound) {
                            myMaze.maze[currRow][currCol].setRight(false);
                            myMaze.maze[currRow][currCol + 1].setVisited(true);
                            currCol++;
                            int[] newCell = {currRow, currCol};
                            mazeStack.push(newCell);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < myMaze.maze.length; i++) {
            for (int j = 0; j < myMaze.maze[i].length; j++) {
                myMaze.maze[i][j].setVisited(false);
            }
        }
        return myMaze;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze(boolean path) {
        for (int i = 0; i < (maze.length * 2) + 1; i++) {
            StringBuilder board = new StringBuilder();
            if (i == startRow) {
                board.append(" ");
            } else {
                board.append("|");
            }
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[startRow][j].getVisited()) {
                    board.append(" * ");
                }
                if (i == 0 || i == maze.length * 2) {
                    board.append(">>>|");
                } else {
                    if (i % 2 == 1) {
                        if (path && maze[i / 2][j].getVisited()) {
                            board.append(" * ");
                        } else {
                            board.append("   ");
                        }
                        if (maze[i / 2][j].getRight()) {
                                board.append("|");
                        } else {
                                board.append(" ");
                        }
                    } else {
                        if (maze[i / 2 - 1][j].getBottom()) {
                            board.append(">>>|");
                        } else {
                            board.append("   |");
                        }
                    }
                }
            }
            System.out.println(board);
        }
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        Q1Gen<int[]> mazeQ = new Q1Gen<>();
        int[] startCell = {startRow, 0};
        mazeQ.add(startCell);

        boolean isEmpty = mazeQ.length() == 0;

        while (!isEmpty) {
            startCell = mazeQ.remove();

            int currRow = startCell[0];
            int currCol = startCell[1];

            maze[currRow][currCol].setVisited(true);

            boolean stop = (currRow == endRow && currCol == maze[0].length - 1);

            if (!stop) {
                boolean upPathFound = currRow - 1 >= 0 && !maze[currRow - 1][currCol].getVisited() && !maze[currRow - 1][currCol].getBottom();
                boolean downPathFound = currRow + 1 <= maze.length - 1 && !maze[currRow + 1][currCol].getVisited() && !maze[currRow][currCol].getBottom();
                boolean leftPathFound = currCol - 1 >= 0 && !maze[currRow][currCol - 1].getVisited() && !maze[currRow][currCol - 1].getRight();
                boolean rightPathFound = currCol + 1 <= maze[0].length - 1 && !maze[currRow][currCol + 1].getVisited() && !maze[currRow][currCol].getRight();

                if (upPathFound) {
                    int[] currCell = {currRow - 1, currCol};
                    mazeQ.add(currCell);
                }
                if (downPathFound) {
                    int[] currCell = {currRow + 1, currCol};
                    mazeQ.add(currCell);
                }
                if (leftPathFound) {
                    int[] currCell = {currRow, currCol - 1};
                    mazeQ.add(currCell);
                }
                if (rightPathFound) {
                    int[] currCell = {currRow, currCol + 1};
                    mazeQ.add(currCell);
                }
            } else {
                isEmpty = true;
            }
        }
         printMaze(true);
    }

    public static String getRandomNeighbor() {
        // helper method to choose a random neighbor to the corresponding cell
        Random rand = new Random();
        String[] movements = {"up", "down", "left", "right"};
        int randomIndex = rand.nextInt(4);
        return movements[randomIndex];
    }
    public static int getRandomRow(int r) {
        // helper method to choose a random start and end row, based on the row
        int[] rows = new int[r];
        int count = 0;
        for (int i = 0; i < rows.length; i++) {
            if (r % 2 == 1) {   // makes sure row is odd
                rows[i] = r;
                count++;
            }
            r--;
        }
        int[] newRows = new int[count];
        int idx = 0;
        int i = 0;
        while (idx < count) {
            if (rows[i] != 0) {
                newRows[idx++] = rows[i];
            }
            i++;
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(newRows.length);
        return newRows[randomIndex];
    }
    public static boolean checkInBounds(int r, int c) {
        // helper method to check if the rows and columns from user are in bounds
        boolean rowInBound = r >= 5 && r <= 20;
        boolean columnInBound = c >= 5 && c <= 20;
        return rowInBound && columnInBound;
    }

    public static void main(String[] args) {
        /*Make and solve maze */
        Scanner s = new Scanner(System.in);
        MyMaze maze;

        boolean stop = false;
        while (!stop) {
            System.out.println("\nWould you like to create a maze? (Yes/No): ");
            String response = s.nextLine();
            switch (response) {
                case "Yes" -> {
                    maze = makeMaze();
                    System.out.println("\nThe maze before solving...\n__________________________\n");
                    maze.printMaze(true);
                    System.out.println("\nThe maze after solving...\n_________________________\n");
                    maze.solveMaze();
                }
                case "No" -> {
                    System.out.println("\nOkay, Goodbye!");
                    stop = true;
                }
                default -> System.out.println("\nInvalid response. Please try again.");
            }
        }
         s.close();
    }
}
