package com.company;

import java.awt.Color;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        for(int i = 1; i <= 3; i++)
        {
            long startTime = System.currentTimeMillis();
            System.out.print("creating maze...");
            int size = 6000;
            ColoredGrid g = new ColoredGrid(size,size, new Color(0, 255, 0));
            MazeGens.aldousBroder(g);//, g.getCell(size/2,size/2));
            //System.out.println(g);
            //MazeGens.printLongestPath(g);
            long nextTime = System.currentTimeMillis(); System.out.println((nextTime - startTime)/1000F + " sec"); startTime = nextTime;
            System.out.print("getting distances...");
            Cell start = g.getCell(size/2,size/2);//size/2,size/2);
            g.setDistances(start.distances());
            nextTime = System.currentTimeMillis(); System.out.println((nextTime - startTime)/1000F + " sec"); startTime = nextTime;
            System.out.print("drawing...");
            //g.draw(1,false);
            g.fastDraw("maze" + (i+0) + ".jpg", true);
            nextTime = System.currentTimeMillis(); System.out.println((nextTime - startTime)/1000F + " sec");
        }
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
        g.printLongestPath();
    }

    public static void drawMaze() throws IOException
    {
        DistanceGrid g = new DistanceGrid(20,20);
        MazeGens.aldousBroder(g);
        //MazeGens.recursiveBacktracker(g);
        //g.braid(1);
        System.out.println(g);
        //MazeGens.printLongestPath(g);
        g.draw();
    }
}