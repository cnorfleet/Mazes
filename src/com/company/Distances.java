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
}