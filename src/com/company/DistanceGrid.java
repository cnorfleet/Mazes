package com.company;

public class DistanceGrid extends Grid
{
    private Distances distances;

    public DistanceGrid(int r, int c)
    { super(r, c); }

    @Override
    public String contentsOf(Cell c)
    {
        if (distances != null && distances.contains(c))
        { return Integer.toString(distances.get(c), 36); }
        else { return super.contentsOf(c); }
    }

    public void setDistances(Distances d)
    { distances = d; }
}