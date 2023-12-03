/*
package com.game.mancala.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrixTraversal {
    public static void main(String[] args) {
        int rowNum = 4;
        int colNum = 7;
        int selectedRow = 1;
        int selectedCol = 4;
        int stoneCount = 15;
        //int[][] a = new int[5][3] ;
        int[][] indexes = new int[stoneCount][2];

        List<int[]> res= new ArrayList<>() ;
        for (int i = selectedCol + 1; i < colNum; i++) {
            res.add(new int[]{selectedRow,i});

            stoneCount--;
        }


        int currentRow = (selectedRow + 1) % rowNum;


       res.addAll(IntStream.iterate(currentRow, i -> (i + 1) % rowNum)
                .mapToObj(i ->
                        {
                            if (i == selectedRow) {
                                return IntStream.range(0, colNum)
                                        .mapToObj(j -> new int[]{i, j})
                                        .collect(Collectors.toList());

                            }

                            List<int[]> a=  IntStream.rangeClosed(0, colNum - 2)
                                    .mapToObj(j -> new int[]{i, colNum - 2 - j})
                                    .collect(Collectors.toList()) ;
                            a.add(new int[]{i ,colNum - 1});

                            return a ;




                        }
                ).flatMap(item->item.stream())
        .limit(stoneCount).collect(Collectors.toList()));
        ;
        res.forEach(item-> System.out.println(Arrays.toString(item)));



        while (stoneCount > 0) {

            if (currentRow == selectedRow) {
                for (int j = 0; j < colNum; j++) {
                    System.out.println(currentRow + ", " + j);
                    stoneCount--;
                }
            } else {
                for (int j = colNum - 2; j >= 0; j--) {
                    System.out.println(currentRow + ", " + j);
                    stoneCount--;
                }

                System.out.println(currentRow + ", " + (colNum - 1));
            }

            stoneCount--;
            currentRow = (currentRow + 1) % rowNum;
        }
    }

}
*/
