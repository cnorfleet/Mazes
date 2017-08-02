package com.company;

import java.util.ArrayList;
import java.util.HashMap;

public class Distances
{
    private Cell root;
    private HashMap<Cell, Integer> cells = new HashMap<>();

    public Distances(Cell root)
    {
        this.root = root;
        cells.put(root, 0);
    }

    public boolean contains(Cell c)
    { return cells.containsKey(c); }
    public void put(Cell c, int i)
    { cells.put(c, i); }
    public int get(Cell c)
    { return cells.get(c); }

    public Distances pathTo(Cell goal)
    {
        Cell current = goal;
        Distances d = new Distances(root);
        d.put(current, get(current));
        while (current != root)
        {
            for (Cell c : current.getLinks())
            {
                if(get(c) < get(current))
                {
                    d.put(c, get(c));
                    current = c;
                    break;
                }
            }
        }
        return d;
    }

    public Cell max()
    {
        int maxDist = 0;
        Cell maxCell = root;

        for (Cell c : cells.keySet())
        {
            if (get(c) > maxDist)
            {
                maxCell = c;
                maxDist = get(c);
            }
        }
        return maxCell;
    }
}