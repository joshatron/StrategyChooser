package stratchooser;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by joshua on 12/25/14.
 */
public class MapReader
{
    //Given a base directory(basedir), update a config folder(newDir)
    //Will take map data into mapname.txt and one letter codes
    //n is normal
    //v is void
    //r is road
    //a is first hq
    //b is second hq
    //The function will also append all the maps to the config file
    public static void groupAdd(String baseDir, String newDir)
    {
        File folder = new File(baseDir);
        for(File currentFile : folder.listFiles())
        {
            //Generate name for the mapfile.txt
            StringTokenizer token = new StringTokenizer(currentFile.getName());
            String dirName = newDir.toString();
            if(dirName.charAt(dirName.length() - 1) != '/')
            {
                dirName = dirName.concat("/");
            }
            String mapName = token.nextToken(".");
            String name = dirName.concat(mapName);
            name = name.concat(".txt");

            try
            {
                PrintWriter configOut = new PrintWriter(new BufferedWriter(
                                        new FileWriter(dirName.concat("config.txt"), true)));
                configOut.write(mapName + "\n");
                configOut.close();

                //Read in basic info
                BufferedReader in = new BufferedReader(new FileReader(currentFile.getAbsolutePath()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(name)));
                while(!in.readLine().equals("<![CDATA[")){}
                in.mark(100000);

                int width = 0;
                boolean gotWidth = false;
                int height = 0;
                String line = in.readLine();
                while(!line.equals("]]>"))
                {
                    height++;
                    token = new StringTokenizer(line);
                    if(!gotWidth)
                    {
                        width = token.countTokens();
                        gotWidth = true;
                    }
                    line = in.readLine();
                }

                int[][] cows = new int[height][width];
                float totalCows = 0;
                int hq1X = 0;
                int hq1Y = 0;
                int hq2X = 0;
                int hq2Y = 0;
                in.reset();
                line = in.readLine();
                for(int k = 0; k < height; k++)
                {
                    token = new StringTokenizer(line);

                    for(int a = 0; a < width; a++)
                    {
                        String node = token.nextToken();
                        out.write("" + node.charAt(0));
                        if(node.charAt(0) == 'a')
                        {
                            hq1X = k;
                            hq1Y = a;
                        }
                        if(node.charAt(0) == 'b')
                        {
                            hq2X = k;
                            hq2Y = a;
                        }
                        cows[k][a] = Integer.parseInt("" + node.charAt(node.length() - 1));
                        totalCows += cows[k][a];
                    }

                    out.write("\n");
                    line = in.readLine();
                }
                out.write("INFO\n");

                //Do calculations to put in map files
                totalCows = totalCows / (width * height);
                float distHQs = (float)Math.sqrt(Math.pow(hq1X - hq2X, 2) + Math.pow(hq1Y - hq2Y, 2));

                float nearHQs = 0;
                int numSpots = 400;
                for(int k = hq1X / 2 - 10; k < hq1X / 2 + 10; k++)
                {
                    for(int a = hq1Y / 2 - 10; a < hq1Y / 2 + 10; a++)
                    {
                        if(k < 0 || k >= height || a < 0 || a >= width)
                        {
                            numSpots--;
                        }
                        else
                        {
                            nearHQs += cows[k][a];
                        }
                    }
                }
                nearHQs = nearHQs / numSpots;

                float corners = 0;
                for(int k = 0; k < 10; k++)
                {
                    for(int a = 0; a < 10; a++)
                    {
                        corners += cows[k][a];
                    }
                    for(int a = width - 1; a >= width - 10; a--)
                    {
                        corners += cows[k][a];
                    }
                }
                for(int k = height - 1; k >= height - 10; k--)
                {
                    for(int a = 0; a < 10; a++)
                    {
                        corners += cows[k][a];
                    }
                    for(int a = width - 1; a >= width - 10; a--)
                    {
                        corners += cows[k][a];
                    }
                }
                corners = corners / 400;

                float center = 0;
                for(int k = height / 2 - 10; k < height / 2 + 10; k++)
                {
                    for(int a = width / 2 - 10; a < width / 2 + 10; a++)
                    {
                        center += cows[k][a];
                    }
                }
                center = center / 400;

                out.write(width + "\n");
                out.write(height + "\n");
                out.write(totalCows + "\n");
                out.write(nearHQs + "\n");
                out.write(corners + "\n");
                out.write(center + "\n");
                out.write(distHQs + "\n");


                in.close();
                out.close();
            }
            catch(IOException e){e.printStackTrace();}
        }
    }
}
