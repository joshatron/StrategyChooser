package stratchooser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 12/26/14.
 */
public class Map extends JLabel
{
    private int width, height, picWidth, picHeight, multiplier;
    private String[] map, infoNames;
    private float[] info;

    public Map(String[] map, int width, int height, String[] infoNames, float[] info)
    {
        this.map = map;
        this.width = width;
        this.height = height;
        this.infoNames = infoNames;
        this.info = info;

        this.picWidth = 330;
        this.picHeight = 330 + info.length * 17 + 10;
        this.multiplier = 1;
        while(multiplier * width <= 300 && multiplier * height <= 300){multiplier++;}
        multiplier--;
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

        backg.setColor(Color.LIGHT_GRAY);
        backg.fillRect(0, 0, picWidth, picHeight);

        int currentY = 165 - ((height / 2) * multiplier);

        for(int k = 0; k < height; k++)
        {
            int currentX = 165 - ((width / 2) * multiplier);
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

                backg.fillRect(currentX, currentY, multiplier, multiplier);
                currentX += multiplier;
            }
            currentY += multiplier;
        }

        //print map info
        backg.setFont(new Font("Monospaced", Font.PLAIN, 15));
        backg.setColor(Color.BLACK);

        int textX = 15;
        int textY = 345;

        for(int k = 0; k < info.length; k++)
        {
            backg.drawString(infoNames[k] + ": " + info[k], textX, textY);
            textY += 17;
        }
    }
}
