package com.game.mancala.rule.mancala;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MancalaIndexGenerator {


    // Generates indexes based on the provided parameters.
    public static List<int[]> provideMoveIndexes(int rowNum, int colNum,
                                                 int selectedRow, int selectedCol,
                                                 int stoneCount) {

        // generates indexes for first row(active player pits)
        List<int[]> indexes = IntStream.range(selectedCol + 1, colNum)
                .mapToObj(i -> new int[]{selectedRow, i}).limit(stoneCount).collect(Collectors.toList());

        stoneCount -= colNum - selectedCol - 1;
        if (stoneCount <= 0)
            return indexes;

        // generate index for remaining stones,
        int currentRow = (selectedRow + 1) % rowNum;
        indexes.addAll(IntStream.iterate(currentRow, i -> (i + 1) % rowNum)
                .mapToObj(element -> (element == selectedRow) ?
                        generateRowIndexes(element, colNum) :
                        generateReverseRowIndexes(element, colNum))
                .flatMap(List::stream)
                .limit(stoneCount)
                .toList());

        return indexes;
    }

    // generates index for specific row and its columns
    // (active player traverse through his pits from start to end)
    private static List<int[]> generateRowIndexes(int row, int colNum) {
        return IntStream.range(0, colNum)
                .mapToObj(j -> new int[]{row, j})
                .collect(Collectors.toList());
    }

    //generate index for specific row in reverse order, including last column
    // (active player traverse through other player pits in opposite direction (from end to start) )
    private static List<int[]> generateReverseRowIndexes(int row, int colNum) {
        return IntStream.rangeClosed(0, colNum - 2)
                .mapToObj(j -> new int[]{row, colNum - 2 - j})
                .collect(Collectors.toList());
    }

    public static int[][] initializeGameMatrix(int pitCount, int stonesPerPit, int playersCount) {
        // Create a 2D array with 2 rows and pitCount + 1 columns
        int[][] pitArray = new int[playersCount][pitCount + 1];

        // Initialize values in each row
        for (int i = 0; i < playersCount; i++) {
            for (int j = 0; j < pitCount; j++) {
                pitArray[i][j] = stonesPerPit;
            }
            // Set the last index to 0 for bigPit
            pitArray[i][pitCount] = 0;
        }

        return pitArray;
    }


}
