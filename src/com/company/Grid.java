package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Grid
{
    private int rows, cols;
    private Cell[][] grid;

    public Grid(int r, int c)
    {
        rows = r;
        cols = c;
        grid = makeGrid();
        setupCells();
    }

    private Cell[][] makeGrid()
    {
        Cell[][] g = new Cell[rows][cols];
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            { g[r][c] = new Cell(r, c); }
        }
        return g;
    }

    public Cell getCell(int r, int c)
    {
        if (r < 0 || c < 0 || r >= rows || c >= cols)
        { return null; }
        return grid[r][c];
    }
    private void setupCells()
    {
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                grid[r][c].north = getCell(r+1,c);
                grid[r][c].south = getCell(r-1,c);
                grid[r][c].east = getCell(r,c+1);
                grid[r][c].west = getCell(r,c-1);
                grid[r][c].updateNeighbors();
            }
        }
    }

    public Cell randCell()
    {
        int r = (int) (Math.random() * rows);
        int c = (int) (Math.random() * cols);
        return grid[r][c];
    }

    public int size()
    { return rows * cols; }
    public int rows()
    { return rows; }
    public int cols()
    { return cols; }

    @Override
    public String toString()
    {
        String s = "+";
        for (int i = 0; i < cols; i++)
        { s += "---+"; }
        s += "\n";

        for (int r = 0; r < rows; r++)
        {
            String top = "|", bottom = "+";
            for (int c = 0; c < cols; c++)
            {
                Cell cell = getCell(r,c);
                if (cell == null) { cell = new Cell(-1,-1); }

                String body = " " + contentsOf(cell) + " ";
                String east_boundary = ((cell.east != null && cell.isLinked(cell.east)) ? " " : "|");
                top += body + east_boundary;

                String south_boundary = ((cell.north != null && cell.isLinked(cell.north)) ? "   " : "---");
                String corner = "+";
                bottom += south_boundary + corner;
            }

            s += top + "\n";
            s += bottom + "\n";
        }
        return s;
    }
    public String contentsOf(Cell c)
    { return " "; }

    public ArrayList<Cell> deadEnds()
    {
        ArrayList<Cell> l = new ArrayList<>();
        for (Cell[] a : grid)
        {
            for (Cell c : a)
            {
                if(c.getLinks().size() == 1)
                { l.add(c); }
            }
        }
        return l;
    }

    public void braid()
    { braid(1); }
    public void braid(double percent)
    {
        ArrayList<Cell> deadEnds = deadEnds();
        for (Cell c : deadEnds)
        {
            if(c.getLinks().size() != 1 || Math.random() > percent)
            { continue; }

            ArrayList<Cell> best = new ArrayList<>();
            for (Cell n : c.disconnectedNeighbors())
            {
                if (n.getLinks().size()==1)
                { best.add(n); }
            }
            if(best.size()==0)
            { best = c.disconnectedNeighbors(); }

            int idx = (int) (Math.random() * best.size());
            c.link(best.get(idx));
        }
    }

    public void draw() throws IOException
    { draw(15); }
    public void draw(int cellSize) throws IOException
    {
        try
        {
            int width = cellSize*cols+1, height = cellSize*rows+1;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setPaint(Color.white);
            g.fillRect(0,0,width,height);
            g.setPaint(Color.black);
            for(int r = 0; r < rows; r++)
            {
                for(int c = 0; c < cols; c++)
                {
                    int x1 = c * cellSize;
                    int y1 = r * cellSize;
                    int x2 = (c+1) * cellSize;
                    int y2 = (r+1) * cellSize;

                    if(r == 0) { g.drawLine(x1,y1,x2,y1); }
                    if(c == 0) { g.drawLine(x1,y1,x1,y2);}
                    if(c == cols-1 || !grid[r][c].isLinked(grid[r][c].east)) { g.drawLine(x2,y1,x2,y2); }
                    if(r == rows-1 || !grid[r][c].isLinked(grid[r][c].north)) { g.drawLine(x1,y2,x2,y2); }
                }
            }
            ImageIO.write(image, "JPEG", new File("maze.jpg"));
            //ImageIO.write(image, "PNG", new File("maze.png"));
        }
        catch (IOException e)
        { System.out.println("whoops"); }
    }
}