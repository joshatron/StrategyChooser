package stratchooser;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by joshua on 12/26/14.
 */
public class Main
{
    JFrame frame;
    Map map;

    public static void main(String[] args)
    {
        new Main().go("/home/joshua/programming/java/StrategyFolder/bakedpotato.txt");
    }

    public void go(String configDir)
    {
        frame = new JFrame();
        String[] mapData = getMap(configDir);
        map = new Map(mapData, mapData[0].length(), mapData.length);
        frame.add(map);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private String[] getMap(String file)
    {
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String[] map = new String[200];
            int k = 0;

            String line = in.readLine();
            while(!line.equals("INFO"))
            {
                map[k] = line.toString();
                k++;
                line = in.readLine();
            }

            String[] toReturn = new String[k];
            for(k = 0; k < toReturn.length; k++)
            {
                toReturn[k] = map[k].toString();
            }

            in.close();

            return toReturn;
        }
        catch(IOException e){e.printStackTrace();}

        return null;
    }
}
