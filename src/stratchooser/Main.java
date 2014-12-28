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
        new Main().go("/home/joshua/programming/java/StrategyFolder");
    }

    public void go(String configDir)
    {
        if(configDir.charAt(configDir.length() - 1) != '/')
        {
            configDir = configDir.concat("/");
        }
        frame = new JFrame();

        try
        {
            BufferedReader in = new BufferedReader(new FileReader(configDir.concat("config.txt")));

            in.readLine();
            String[] variables = new String[1000];
            int a = 0;
            String line = in.readLine();
            while(!line.equals("STRATEGIES"))
            {
                variables[a] = line.toString();
                a++;
                line = in.readLine();
            }
            String[] finalVariables = new String[a];

            for(int k = 0; k < a; k++)
            {
                finalVariables[k] = variables[k];
            }

            while(!in.readLine().equals("MAPS")){}

            line = in.readLine();
            while(line != null)
            {
                if(line.charAt(0) != '#')
                {
                    String[] mapData = getMap(configDir.concat(line + ".txt"));
                    float[] mapVariables = getVars(configDir.concat(line + ".txt"), finalVariables.length);
                    map = new Map(mapData, mapData[0].length(), mapData.length, finalVariables, mapVariables);
                    frame.add(map);
                    frame.pack();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    frame.remove(map);
                }
                line = in.readLine();
            }
        }
        catch(IOException e){e.printStackTrace();}
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

    private float[] getVars(String file, int vars)
    {
        float[] toReturn = new float[vars];
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(file));

            while(!in.readLine().equals("INFO")){}

            for(int k = 0; k < vars; k++)
            {
                toReturn[k] = Float.parseFloat(in.readLine());
            }
        }
        catch(IOException e){e.printStackTrace();}

        return toReturn;
    }
}
