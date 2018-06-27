package com.company;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        ColoredGrid g = new ColoredGrid(1000,1000);
        MazeGens.aldousBroder(g);
        //MazeGens.recursiveBacktracker(g);
        //MazeGens.printLongestPath(g);
        Cell start = g.getCell(0,0);
        g.setDistances(start.distances());
        g.draw(1,false);
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

    public static void drawMaze() throws IOException
    {
        DistanceGrid g = new DistanceGrid(20,20);
        MazeGens.aldousBroder(g);
        //MazeGens.recursiveBacktracker(g);
        //g.braid(1);
        //System.out.println(g);
        //MazeGens.printLongestPath(g);
        g.draw();
    }
}