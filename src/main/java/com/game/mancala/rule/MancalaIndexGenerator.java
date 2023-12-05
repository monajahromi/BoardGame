package com.game.mancala.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MancalaIndexGenerator {


    // Generates indexes based on the provided parameters.
    public static List<int[]> generateIndexes(int rowNum, int colNum,
                                              int selectedRow, int selectedCol,
                                              int stoneCount) {
         List<int[]> indexes = new ArrayList<>();

        // generates indexes for first row
        indexes.addAll(IntStream.range(selectedCol + 1, colNum)
                .mapToObj(i -> new int[]{selectedRow, i}).limit(stoneCount)
                .collect(Collectors.toList()));

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
                .collect(Collectors.toList()));

        return indexes;
    }

    // generates index for specific row and its columns
    private static List<int[]> generateRowIndexes(int row, int colNum) {
        return IntStream.range(0, colNum)
                .mapToObj(j -> new int[]{row, j})
                .collect(Collectors.toList());
    }

    //generate index for specific row in reverse order, including last column
    private static List<int[]> generateReverseRowIndexes(int row, int colNum) {
        List<int[]> indexes = IntStream.rangeClosed(0, colNum - 2)
                .mapToObj(j -> new int[]{row, colNum - 2 - j})
                .collect(Collectors.toList());
        return indexes;
    }


    public static void main(String[] args) {
        int rowNum = 2;
        int colNum = 7;
        int selectedRow = 1;
        int selectedCol = 4;
        int stoneCount = 15;

        List<int[]> result = generateIndexes(rowNum, colNum, selectedRow, selectedCol, stoneCount);
        result.forEach(item -> System.out.println(Arrays.toString(item)));
    }



}
