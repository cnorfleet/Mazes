package com.company;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MazeGens
{
    public static void binaryTree(Grid g)
    {
        for (int r = 0; r < g.rows(); r++)
        {
            for (int c = 0; c < g.cols(); c++)
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
    }

    public static void sidewinder(Grid g)
    {
        for (int r = 0; r < g.rows(); r++)
        {
            ArrayList<Cell> group = new ArrayList<>();
            for (int c = 0; c < g.cols(); c++)
            {
                Cell cell = g.getCell(r,c);
                if(cell.north == null && cell.east == null) { continue; }
                if(cell.north == null) { cell.link(cell.east); continue; }

                if(cell.east == null || Math.random() >= 0.5) //connect with one cell in group
                {
                    group.add(cell);
                    int idx = (int) (Math.random() * (double) group.size());
                    group.get(idx).link(group.get(idx).north);
                    group = new ArrayList<>();
                }
                else //connect with next cell
                { group.add(cell); cell.link(cell.east); }
            }
        }
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
            stack.remove(cIdx);
            ArrayList<Cell> options = new ArrayList<>();
            for(Cell n : c.neighbors)
            {
                if(n.visted)
                { options.add(n); }
            }
            if (options.size() != 0)
            {
                int nIdx = (int) (Math.random() * options.size());
                Cell n = options.get(nIdx);
                c.link(n); c.visted = true;
                for(Cell cell : c.neighbors)
                {
                    if(!cell.visted && !stack.contains(cell))
                    { stack.add(cell); }
                }
            }
        }
    }

    public static void kruskalsAlgorithm(Grid g)
    {
        //generate edge list
        ArrayList<Cell[]> edges = new ArrayList<>();
        for(int r = 0; r < g.rows(); r++)
        {
            for (int c = 0; c < g.cols(); c++)
            {
                Cell cell = g.getCell(r,c);
                if(cell.north != null) { Cell[] temp1 = {cell, cell.north}; edges.add(temp1); }
                if(cell.east != null) { Cell[] temp2 = {cell, cell.east}; edges.add(temp2); }
                /* for(Cell n : cell.neighbors)
                { Cell[] temp = {cell, n}; edges.add(temp); } */
            }
        }

        //generate set list - use cantor pairing funtion to provide unique id for each set
        Map<Cell, Integer> dictionary = new HashMap<>();
        Map<Integer, ArrayList<Cell>> reverseDict = new HashMap<>();
        for(int r = 0; r < g.rows(); r++)
        {
            for(int c = 0; c < g.cols(); c++)
            {
                int cantorPairingID = (((r+c)*(r+c+1))/2)+c;
                dictionary.put(g.getCell(r,c), cantorPairingID);
                ArrayList<Cell> temp = new ArrayList<>();
                temp.add(g.getCell(r,c));
                reverseDict.put(cantorPairingID, temp);
            }
        }

        while(!edges.isEmpty())
        {
            if(edges.size() % 2000 == 0)
            { System.out.println(edges.size()); }
            Cell[] edge = edges.remove((int) (Math.random() * edges.size()));
            Cell c1 = edge[0];
            Cell c2 = edge[1];
            if(dictionary.get(c1).equals(dictionary.get(c2))) //already in the same set
            { continue; }
            c1.link(c2);
            int newID = dictionary.get(c1);
            int oldID = dictionary.get(c2);
            for(Cell c : reverseDict.get(oldID))
            {
                dictionary.put(c, newID);
                reverseDict.get(newID).add(c);
            }
        }
    }

    public static void ellersAlgorithm(Grid g)
    {
        //set up first row
        int nextID = 1;
        Object[][] row = new Object[g.cols()][2];
        for(int c = 0; c < g.cols(); c++)
        { row[c][0] = g.getCell(0,c); row[c][1] = nextID; nextID++; }

        for(int r = 0; r < g.rows() - 1; r++)
        {
            //connect horizontally
            for(int c = 0; c < g.cols() - 1; c++)
            {
                if(!row[c][1].equals(row[c+1][1]) && Math.random() >= 0.5)
                {
                    ((Cell) row[c][0]).link((Cell) row[c+1][0]);
                    Integer newID = (Integer) row[c][1];
                    Integer oldId = (Integer) row[c+1][1];
                    for(Object[] i : row)
                    {
                        if(((Integer) i[1]).equals(oldId))
                        { i[1] = newID; }
                    }
                }
            }

            //set up sets of same-numbered things
            HashMap<Integer, ArrayList<Cell>> dict = new HashMap<>();
            for(int c = 0; c < g.cols(); c++)
            {
                if(dict.containsKey(row[c][1]))
                { dict.get(row[c][1]).add((Cell) row[c][0]); }
                else
                { ArrayList<Cell> temp = new ArrayList<>(); temp.add((Cell) row[c][0]); dict.put((Integer) row[c][1], temp); }
            }

            Object[][] nextRow = new Object[g.cols()][2];
            //set up next row
            for(int c = 0; c < g.cols(); c++)
            { nextRow[c][0] = g.getCell(r+1,c); nextRow[c][1] = 0; }

            //connect vertically
            for(Integer key : dict.keySet())
            {
                ArrayList<Cell> set = dict.get(key);
                Collections.shuffle(set);
                int num = 1 + (int) (Math.random() * (set.size() - 1));
                for(int i = 0; i < num; i++)
                {
                    //System.out.println(set.get(i).north.getCol() + ", " + set.get(i).getCol() + ": " + row[set.get(i).getCol()][1]);
                    set.get(i).link(set.get(i).north);
                    nextRow[set.get(i).north.getCol()][1] = row[set.get(i).getCol()][1];
                }
            }

            //assign ids to next row
            for(int c = 0; c < g.cols(); c++)
            {
                if((int) nextRow[c][1] == (0))
                { nextRow[c][1] = nextID; nextID++; }
            }
            row = nextRow;
        }

        //connect last row horizontally
        for(int c = 0; c < g.cols() - 1; c++)
        {
            if(!row[c][1].equals(row[c+1][1]))
            {
                //see if there are other spots where they could be linked
                int numOtherSpots = 0; boolean justFound = false;
                for(int c2 = c+2; c2 < g.cols(); c2++)
                {
                    if(!justFound && row[c][1].equals(row[c2][1]))
                    { numOtherSpots++; justFound = true; }
                    else if (justFound && !row[c][1].equals(row[c2][1]))
                    { justFound = false; }
                }

                //link the two
                if (1 > Math.random() * (numOtherSpots + 1))
                {
                    ((Cell) row[c][0]).link((Cell) row[c + 1][0]);
                    Integer newID = (Integer) row[c][1];
                    Integer oldId = (Integer) row[c + 1][1];
                    for (Object[] i : row) {
                        if (((Integer) i[1]).equals(oldId)) {
                            i[1] = newID;
                        }
                    }
                }
            }
        }
    }

    public static void growingForest(Grid g)
    { growingForest(g, 20); }
    public static void growingForest(Grid g, int numTrees)
    {
        //mark active cells
        ArrayList<Cell> activeCells = new ArrayList<>();
        HashMap<Cell, Integer> treeIDs = new HashMap<>();
        Cell firstActive = g.randCell();
        activeCells.add(firstActive);
        firstActive.visted = true;
        treeIDs.put(firstActive, 1);
        int nextID = 2;
        for(int r = 0; r < g.rows(); r++)
        {
            for(int c = 0; c < g.cols(); c++)
            {
                if(Math.random() * (g.rows() * g.cols()) < numTrees)
                {
                    activeCells.add(g.getCell(r,c));
                    treeIDs.put(g.getCell(r,c), nextID);
                    g.getCell(r,c).visted = true;
                    nextID++;
                }
            }
        }

        //generate stuff
        while(activeCells.size() > 0)
        {
            Cell c = activeCells.get((int) (Math.random() * activeCells.size()));
            ArrayList<Cell> next = new ArrayList<>();
            for(Cell n : c.neighbors)
            {
                if(!n.visted || !(treeIDs.get(c).equals(treeIDs.get(n))))
                { next.add(n); }
            }
            if(next.isEmpty())
            { activeCells.remove(c); continue; }
            Cell n = next.get((int) (Math.random() * next.size()));
            if(treeIDs.containsKey(n) && !treeIDs.get(c).equals(treeIDs.get(n)))
            {
                int oldID = treeIDs.get(n);
                for(Cell key : treeIDs.keySet())
                {
                    if(treeIDs.get(key).equals(oldID))
                    { treeIDs.put(key, treeIDs.get(c)); }
                }
            }
            else
            { activeCells.add(n); n.visted = true; }
            c.link(n);
            treeIDs.put(n, treeIDs.get(c));
        }
    }
}