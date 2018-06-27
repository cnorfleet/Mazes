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

    public static void recursiveDivision(Grid g)
    {
        int rows = g.rows(), cols = g.cols();
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                ArrayList<Cell> n = g.getCell(r,c).neighbors;
                for (Cell i : n)
                { g.getCell(r,c).link(i,true); }
            }
        }
        divide(g, 0, 0, rows, cols);
    }
    private static void divide(Grid g, int row, int col, int height, int width)
    {
        int maxRoomDim = 6;
        if (height <= 1 || width <= 1 || (height < maxRoomDim && width < maxRoomDim && Math.random() < .3))
        { return; }

        if (height > width || (height == width && Math.random() < 0.5)) //divide horizontally
        {
            int divideBelow = (int) (Math.random() * (height - 1));
            int passageLoc = (int) (Math.random() * width);

            for (int c = col; c < col + width; c++)
            {
                if (c == col + passageLoc) { continue; }
                Cell cell = g.getCell(row + divideBelow, c);
                cell.unlink(cell.north);
            }

            divide(g, row, col, divideBelow+1, width);
            divide(g, row+divideBelow+1, col, height-divideBelow-1, width);
        }
        else //divide vertically
        {
            int divideRightOf = (int) (Math.random() * (width - 1));
            int passageLoc = (int) (Math.random() * height);

            for (int r = row; r < row + height; r++)
            {
                if (r == row + passageLoc) { continue; }
                Cell cell = g.getCell(r, col + divideRightOf);
                cell.unlink(cell.east);
            }

            divide(g, row, col, height, divideRightOf+1);
            divide(g, row, col+divideRightOf+1, height, width-divideRightOf-1);
        }
    }

    public static void primsAlgorithm(Grid g)
    { primsAlgorithm(g, g.randCell()); }
    public static void primsAlgorithm(Grid g, Cell start)
    {
        ArrayList<Cell> stack = new ArrayList<>(); //the frontier
        start.visted = true;
        for(Cell c : start.neighbors)
        { stack.add(c); }

        while (stack.size() > 0)
        {
            int cIdx = (int) (Math.random() * stack.size());
            Cell c = stack.get(cIdx);
            ArrayList<Cell> options = new ArrayList<>();
            for(Cell n : c.neighbors)
            {
                if(n.visted)
                { options.add(n); }
            }
            if (options.size() == 0)
            { stack.remove(cIdx); }
            else
            {
                int nIdx = (int) (Math.random() * options.size());
                Cell n = options.get(nIdx);
                c.link(n); c.visted = true;
                for(Cell cell : c.neighbors)
                {
                    if(!cell.visted)
                    { stack.add(cell); }
                }
            }
        }
    }
}