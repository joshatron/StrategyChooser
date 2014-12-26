package stratchooser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 12/26/14.
 */
public class Map extends JLabel
{
    private int width, height, picWidth, picHeight;
    private String[] map;

    public Map(String[] map, int width, int height)
    {
        this.map = map;
        this.width = width;
        this.height = height;
        this.picWidth = width * 5;
        this.picHeight = height * 5;
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(picWidth, picHeight);
    }

    @Override
    protected void paintComponent(Graphics backg)
    {
        super.paintComponent(backg);

        for(int k = 0; k < height; k++)
        {
            for(int a = 0; a < width; a++)
            {
                if(map[k].charAt(a) == 'n')
                {
                    backg.setColor(Color.YELLOW);
                }
                else if(map[k].charAt(a) == 'v')
                {
                    backg.setColor(new Color(139, 69, 19));
                }
                else if(map[k].charAt(a) == 'r')
                {
                    backg.setColor(Color.GRAY);
                }
                else if(map[k].charAt(a) == 'a')
                {
                    backg.setColor(Color.BLUE);
                }
                else if(map[k].charAt(a) == 'b')
                {
                    backg.setColor(Color.RED);
                }

                backg.fillRect(a * 5, k * 5, 5, 5);
            }
        }
    }
}
