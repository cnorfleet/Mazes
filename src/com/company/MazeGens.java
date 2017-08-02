package com.company;

import java.util.ArrayList;

public class MazeGens
{
    public static void binaryTree(Grid g)
    {
        int rows = g.rows();
        int cols = g.cols();
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                Cell cell = g.getCell(r,c);
                cell.neighbors = new ArrayList<>();
                if (cell.north != null) { cell.neighbors.add(cell.north); }
                if (cell.east != null) { cell.neighbors.add(cell.east); }

                if(cell.neighbors.size() == 0) { continue; }
                int idx = (int) (Math.random() * (double) cell.neighbors.size());
                Cell n = cell.neighbors.get(idx);
                cell.link(n);
            }
        }
        return;
    }
}