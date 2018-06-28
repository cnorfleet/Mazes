package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Cell
{
    private int row, col;
    public Cell north, south, east, west;
    public ArrayList<Cell> neighbors = new ArrayList<>();
    private ArrayList<Cell> links = new ArrayList<>();
    public boolean visted = false; //useful for some maze generators

    public Cell(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public void link(Cell c)
    { link(c, false); }
    public void link(Cell c, boolean done)
    {
        if (c == null) { return; }
        if(!links.contains(c)) { links.add(c); }
        if(!done) { c.link(this, true); }
    }

    public void unlink(Cell c)
    { unlink(c, false); }
    public void unlink(Cell c, boolean done)
    {
        while(links.contains(c)) { links.remove(c); }
        if(!done) { c.unlink(this, true); }
    }

    public ArrayList<Cell> getLinks()
    { return links; }
    public boolean isLinked(Cell c)
    {
        if (c == null) { return false; }
        return links.contains(c);
    }

    public Distances distances()
    {
        Distances distances = new Distances(this);
        ArrayList<Cell> frontier = new ArrayList<>();
        frontier.add(this);

        while(frontier.size() > 0)
        {
            ArrayList<Cell> newFrontier = new ArrayList<>();

            for (Cell c : frontier)
            {
                for (Cell link : c.getLinks())
                {
                    if (distances.contains(link))
                    { continue; }

                    distances.put(link, distances.get(c) + 1);
                    newFrontier.add(link);
                }
            }
            frontier = newFrontier;
        }
        return distances;
    }

    public void updateNeighbors()
    {
        neighbors = new ArrayList<>();
        if (north!=null) { neighbors.add(north); }
        if (south!=null) { neighbors.add(south); }
        if (east!=null) { neighbors.add(east); }
        if (west!=null) { neighbors.add(west); }
    }
    public Cell randNeighbor()
    {
        int idx = (int) (Math.random() * neighbors.size());
        return neighbors.get(idx);
    }

    public ArrayList<Cell> disconnectedNeighbors()
    {
        ArrayList<Cell> n = new ArrayList<>();
        for (Cell c : neighbors)
        {
            if (!c.getLinks().contains(this))
            { n.add(c); }
        }
        return n;
    }

    public ArrayList<Cell> unlinkedNeighbors()
    {
        ArrayList<Cell> n = new ArrayList<>();
        for (Cell c : neighbors)
        {
            if (c.getLinks().size()==0)
            { n.add(c); }
        }
        return n;
    }
    public Cell randUnlinkedNeighbor()
    {
        ArrayList<Cell> n = unlinkedNeighbors();
        int idx = (int) (Math.random() * n.size());
        return n.get(idx);
    }

    public int getRow()
    { return row; }
    public int getCol()
    { return col; }
}