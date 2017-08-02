package com.company;

public class Main
{
    public static void main(String[] args)
    {
        dijkstra();
    }

    public static void dijkstra()
    {
        DistanceGrid g = new DistanceGrid (6, 6);
        MazeGens.binaryTree(g);

        Cell start = g.getCell(0,0);
        g.setDistances(start.distances());
        System.out.println(g);
    }
}