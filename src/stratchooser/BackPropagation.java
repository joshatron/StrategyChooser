package stratchooser;

import java.util.Random;

/**
 * Created by joshua on 12/18/14.
 */
public class BackPropagation
{
    //Computes the weights for a neural net given a net architecture and set of examples.
    //Assumes network is feed forward, fully connected, and has one output.
    public static double[] trainNet(int input, int inner, int output, double[][] data)
    {
        //Initialize random net weights
        Random rand = new Random();
        double[] weights = new double[input * inner + inner * output];
        for(int k = 0; k < weights.length; k++)
        {
            weights[k] = rand.nextFloat() * 2 - 1;
        }

        boolean done = false;
        while(!done)
        {
            for(int k = 0; k < data.length; k++)
            {
                double[] inputs = new double[input];
                for(int a = 0; a < input; a++)
                {
                    inputs[a] = data[k][a];
                }
                double[] expectedOutput = new double[output];
                expectedOutput[(int)data[k][input] - 1] = 1.;
                weights = backPropagate(input, inner, output, weights, inputs, expectedOutput);
            }

            done = true;
            for(int k = 0; k < data.length; k++)
            {
                double[] inputs = new double[input];
                for(int a = 0; a < input; a++)
                {
                    inputs[a] = data[k][a];
                }
                double[] result = runNet(input, inner, output, weights, inputs);
                int checks = 0;
                for(int a = 0; a < result.length; a++)
                {
                    if(result[a] > .75)
                    {
                        checks++;
                        if(Math.round(data[k][input]) - 1 != a)
                        {
                            done = false;
                        }
                    }
                }
                if(checks == 0)
                {
                    done = false;
                }
            }
        }

        return weights;
    }

    //Update the weights of the neural net given the input, and correct output.
    //Assumes network is feed forward, fully connected, and has one output.
    private static double[] backPropagate(int input, int inner, int output, double[] weights,
                                         double[] inputValues, double[] expectedOutput)
    {
        double alpha = .001;

        double[] innerValues = new double[inner];
        double[] outputValues = new double[output];

        for(int k = inner; --k >= 0;)
        {
            for(int a = input; --a >= 0;)
            {
                innerValues[k] += inputValues[a] * weights[a * inner + k];
            }
            innerValues[k] = (1. / (1. + Math.pow(Math.E, innerValues[k] * -1)));
        }

        for(int k = output; --k >= 0;)
        {
            for(int a = inner; --a >= 0;)
            {
                outputValues[k] += innerValues[a] * weights[(input * inner) + a * output + k];
            }
            outputValues[k] = (1. / (1. + Math.pow(Math.E, outputValues[k] * -1)));
        }

        //compute errors
        double[] outputError = new double[outputValues.length];
        for(int k = 0; k < output; k++)
        {
            for(int a = 0; a < inner; a++)
            {
                outputError[k] += innerValues[a] * weights[(input * inner) + (a * output) + k];
            }
            outputError[k] = (Math.pow(Math.E, outputError[k]) /
                             Math.pow(Math.pow(Math.E, outputError[k]) + 1, 2)) *
                             (expectedOutput[k] - outputValues[k]);
        }

        double[] innerError = new double[inner];
        for(int k = 0; k < inner; k++)
        {
            double sum = 0;
            for(int a = 0; a < input; a++)
            {
                sum += inputValues[a] * weights[(a * inner) + k];
            }
            for(int a = 0; a < output; a++)
            {
                innerError[k] += weights[(input * inner) + (k * output) + a] * outputError[a];
            }
            innerError[k] *= (Math.pow(Math.E, sum) / Math.pow(Math.pow(Math.E, sum) + 1, 2));
        }

        //update weights
        for(int k = 0; k < input; k++)
        {
            for(int a = 0; a < inner; a++)
            {
                weights[(k * inner) + a] += alpha * inputValues[k] * innerError[a];
            }
        }

        for(int k = 0; k < inner; k++)
        {
            for(int a = 0; a < output; a++)
            {
                weights[(input * inner) + (k * output) + a] += alpha * innerValues[k] * outputError[a];
            }
        }

        return weights;
    }

    public static double[] runNet(int input, int inner, int output, double[] weights, double[] inputValues)
    {
        double[] innerValues = new double[inner];
        double[] outputValues = new double[output];

        for(int k = inner; --k >= 0;)
        {
            for(int a = input; --a >= 0;)
            {
                innerValues[k] += inputValues[a] * weights[a * inner + k];
            }
            innerValues[k] = (1. / (1. + Math.pow(Math.E, innerValues[k] * -1)));
        }

        for(int k = output; --k >= 0;)
        {
            for(int a = inner; --a >= 0;)
            {
                outputValues[k] += innerValues[a] * weights[(input * inner) + a * output + k];
            }
            outputValues[k] = (1. / (1. + Math.pow(Math.E, outputValues[k] * -1)));
        }

        return outputValues;
    }
}
