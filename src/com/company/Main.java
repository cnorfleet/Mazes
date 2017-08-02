package com.company;

public class Main
{
    public static void main(String[] args)
    {
        DistanceGrid g = new DistanceGrid(6,6);
        MazeGens.recursiveBacktracker(g);
        System.out.println(g);
    }

    public static void dijkstra()
    {
        DistanceGrid g = new DistanceGrid (6, 6);
        MazeGens.binaryTree(g);

        Cell start = g.getCell(0,0);
        g.setDistances(start.distances());
        //System.out.println(g);

        g.setDistances(g.getDistances().pathTo(g.getCell(g.rows()-1, 0)));
        System.out.println(g);
    }

    public static void longestPath()
    {
        DistanceGrid g = new DistanceGrid(6,6);
        MazeGens.binaryTree(g);
        MazeGens.printLongestPath(g);
    }
}