package com.company;

import java.lang.reflect.Array;
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

    public static void aldousBroder(Grid g)
    {
        Cell c = g.randCell();
        int cellsLeft = g.size() - 1;

        while(cellsLeft > 0)
        {
            Cell n = c.randNeighbor();
            if(n.getLinks().size() == 0)
            {
                c.link(n);
                cellsLeft--;
            }
            c = n;
        }
    }

    public static void recursiveBacktracker(Grid g)
    { recursiveBacktracker(g, g.randCell()); }
    public static void recursiveBacktracker(Grid g, Cell start)
    {
        ArrayList<Cell> stack = new ArrayList<>();
        stack.add(start);

        while (stack.size() > 0)
        {
            Cell c = stack.get(stack.size() - 1);
            ArrayList<Cell> neighbors = c.unlinkedNeighbors();
            if (neighbors.size() == 0)
            { stack.remove(stack.size() - 1); }
            else
            {
                Cell n = c.randUnlinkedNeighbor();
                c.link(n);
                stack.add(n);
            }
        }
    }

    public static void printLongestPath(DistanceGrid g)
    {
        Cell start = g.getCell(0,0).distances().max();
        Distances d = start.distances();
        Cell end = d.max();
        g.setDistances(d.pathTo(end));
        System.out.println(g);
    }
}