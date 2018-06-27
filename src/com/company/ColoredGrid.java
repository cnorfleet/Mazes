package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColoredGrid extends DistanceGrid
{
    public ColoredGrid(int r, int c)
    { super(r, c); }

    @Override
    public void draw() throws IOException
    { draw(15, true); }
    @Override
    public void draw(int cellSize) throws IOException
    { draw(cellSize, true); }
    public void draw(int cellSize, boolean showOutline) throws IOException
    {
        try
        {
            int maxDist = getDistances().get(getDistances().max());
            int width = cellSize*cols()+1, height = cellSize*rows()+1;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setPaint(Color.white);
            g.fillRect(0,0,width,height);
            for(int r = 0; r < rows(); r++)
            {
                for(int c = 0; c < cols(); c++)
                {
                    int x1 = c * cellSize;
                    int y1 = r * cellSize;
                    int x2 = (c+1) * cellSize;
                    int y2 = (r+1) * cellSize;
                    int intensity = (350 * getDistances().get(getCell(r,c))) / maxDist;
                    int R = 255-intensity, G = 510 - intensity, B = 255 - intensity;
                    if(R > 255) { R = 255; } if( R < 0) { R = 0; }
                    if(G > 255) { G = 255; } if( G < 0) { G = 0; }
                    if(B > 255) { B = 255; } if( B < 0) { B = 0; }
                    g.setPaint(new Color(R,G,B));
                    g.fillRect(x1,y1,x2,y2);
                }
            }
            if(showOutline)
            {
                g.setPaint(Color.black);
                for (int r = 0; r < rows(); r++)
                {
                    for (int c = 0; c < cols(); c++)
                    {
                        int x1 = c * cellSize;
                        int y1 = r * cellSize;
                        int x2 = (c + 1) * cellSize;
                        int y2 = (r + 1) * cellSize;

                        if (r == 0) { g.drawLine(x1, y1, x2, y1); }
                        if (c == 0) { g.drawLine(x1, y1, x1, y2); }
                        if (c == cols() - 1 || !getCell(r, c).isLinked(getCell(r, c).east)) { g.drawLine(x2, y1, x2, y2); }
                        if (r == rows() - 1 || !getCell(r, c).isLinked(getCell(r, c).north)) { g.drawLine(x1, y2, x2, y2); }
                    }
                }
            }
            ImageIO.write(image, "JPEG", new File("maze.jpg"));
            //ImageIO.write(image, "PNG", new File("maze.png"));
        }
        catch (IOException e)
        { System.out.println("whoops"); }
    }
}
