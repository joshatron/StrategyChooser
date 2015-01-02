package stratchooser;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by joshua on 12/26/14.
 */
public class Main
{
    private JFrame frame;
    private Map map;
    private Menu menu;
    private boolean next;

    private int spot, dataLength;
    private double[][] data;

    public static void main(String[] args)
    {
        new Main().go("/home/joshua/programming/java/StrategyFolder");
    }

    public void go(String configDir)
    {
        next = false;
        spot = 0;
        int strategies = 0;

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

            data = new double[1000][finalVariables.length + 1];
            dataLength = finalVariables.length + 1;

            String[] strats = new String[1000];
            a = 0;
            line = in.readLine();
            while(!line.equals("MAPS"))
            {
                strats[a] = line.toString();
                a++;
                line = in.readLine();
            }
            String[] finalStrats = new String[a];
            strategies = finalStrats.length;

            for(int k = 0; k < a; k++)
            {
                finalStrats[k] = strats[k];
            }

            menu = new Menu(330 + finalVariables.length * 17 + 10, finalStrats);

            frame.setLayout(new GridLayout(1, 2));
            frame.add(menu);

            menu.addPropertyChangeListener(menu.CHOICE, new PropertyChangeListener()
            {
                @Override
                public void propertyChange(PropertyChangeEvent e)
                {
                    int choice = Integer.parseInt(e.getNewValue().toString());
                    next = true;
                    data[spot][dataLength - 1] = choice;
                    spot++;
                }
            });

            line = in.readLine();
            while(line != null)
            {
                if(line.charAt(0) != '#')
                {
                    String[] mapData = getMap(configDir.concat(line + ".txt"));
                    double[] mapVariables = getVars(configDir.concat(line + ".txt"), finalVariables.length);
                    for(int k = 0; k < mapVariables.length; k++)
                    {
                        data[spot][k] = mapVariables[k];
                    }
                    map = new Map(mapData, mapData[0].length(), mapData.length, finalVariables, mapVariables);
                    frame.add(map);
                    frame.pack();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    while(!next)
                    {
                        try
                        {
                            Thread.sleep(10);
                        }
                        catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    next = false;
                    frame.remove(map);
                }
                line = in.readLine();
            }
        }
        catch(IOException e){e.printStackTrace();}

        double[][] finalData = new double[spot][dataLength];
        for(int k = 0; k < spot; k++)
        {
            for(int a = 0; a < dataLength; a++)
            {
                finalData[k][a] = data[k][a];
            }
        }

        System.out.println("Starting back propagation");
        double[] weights = BackPropagation.trainNet(dataLength - 1, 100, strategies, finalData);
        System.out.println("Finished back propagation");
        String output = "Net weights are: ";
        for(int k = 0; k < weights.length; k++)
        {
            output = output.concat(weights[k] + "");
            if(k != weights.length - 1)
            {
                output = output.concat(",");
            }
        }

        //JOptionPane.showMessageDialog(frame, output);
        System.out.println(output);
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

    private double[] getVars(String file, int vars)
    {
        double[] toReturn = new double[vars];
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
