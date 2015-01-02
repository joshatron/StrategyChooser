package stratchooser;

import java.util.Random;

/**
 * Created by joshua on 12/18/14.
 */
public class TestBackProp
{
    public static void main(String[] args)
    {
        Random rand = new Random();
        int examples = 100;
        int inputs = 1;
        double[][] testData = new double[examples][inputs + 1];
        for(int k = 0; k < examples; k++)
        {
            for(int a = 0; a < inputs + 1; a++)
            {
                testData[k][a] = k;
            }
        }
        double[] weights = BackPropagation.trainNet(inputs, 3, 100, testData);
        for(int k = 0; k < examples; k++)
        {
            double[] input = new double[inputs];
            for(int a = 0; a < inputs; a++)
            {
                input[a] = testData[k][a];
            }
            System.out.print(testData[k][inputs] + ": ");
            System.out.println(Math.round(BackPropagation.runNet(inputs, 3, 100, weights, input)[0]));
        }
    }
}
