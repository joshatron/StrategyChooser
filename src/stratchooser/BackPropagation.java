package stratchooser;

import java.util.Random;

/**
 * Created by joshua on 12/18/14.
 */
public class BackPropagation
{
    //Computes the weights for a neural net given a net architecture and set of examples.
    //Assumes network is feed forward, fully connected, and has one output.
    public static float[] trainNet(int examples, int input, int inner, float[][] data)
    {
        //Initialize random net weights
        Random rand = new Random();
        float[] weights = new float[input * inner + inner];
        for(int k = 0; k < weights.length; k++)
        {
            weights[k] = rand.nextFloat() * 2 - 1;
        }

        //Randomly choose half to be training set and other half test set
        int[] trainExamples = new int[3 * examples / 4];
        int[] testExamples = new int[examples - ( 3 * examples / 4)];

        for(int k = 0; k < trainExamples.length; k++)
        {
            int newLoc = rand.nextInt(examples);
            boolean found = false;
            while(!found)
            {
                found = true;
                for(int a = 0; a < k; a++)
                {
                    if(newLoc == trainExamples[a])
                    {
                        found = false;
                    }
                }

                if(!found)
                {
                    newLoc = rand.nextInt(examples);
                }
            }

            trainExamples[k] = newLoc;
        }

        int t = 0;
        for(int k = 0; k < examples; k++)
        {
            boolean unused = true;
            for(int a = 0; a < trainExamples.length; a++)
            {
                if(k == trainExamples[a])
                {
                    unused = false;
                }
            }

            if(unused)
            {
                testExamples[t] = k;
                t++;
            }
        }

        boolean done = false;
        while(!done)
        {
            for(int k = 0; k < trainExamples.length; k++)
            {
                float[] inputs = new float[input];
                for(int a = 0; a < input; a++)
                {
                    inputs[a] = data[trainExamples[k]][a];
                }
                weights = backPropagate(input, inner, weights, inputs, data[trainExamples[k]][input]);
            }

            done = true;
            for(int k = 0; k < testExamples.length; k++)
            {
                float[] inputs = new float[input];
                for(int a = 0; a < input; a++)
                {
                    inputs[a] = data[testExamples[k]][a];
                }
                if(Math.round(runNet(input, inner, weights, inputs)) != Math.round(data[testExamples[k]][input]))
                {
                    done = false;
                }
            }
        }

        return weights;
    }

    //Update the weights of the neural net given the input, and correct output.
    //Assumes network is feed forward, fully connected, and has one output.
    private static float[] backPropagate(int input, int inner, float[] weights,
                                         float[] inputValues, float output)
    {
        float alpha = (float).0001;

        float[] innerValues = new float[inner];
        float result = 0;

        //compute values
        for(int k = inner; --k >= 0;)
        {
            for(int a = input; --a >= 0;)
            {
                innerValues[k] += inputValues[a] * weights[a * inner + k];
            }
            result += innerValues[k] * weights[(input * inner) + k];
        }

        //compute errors
        float outputError = output - result;

        float[] innerError = new float[inner];
        for(int k = 0; k < inner; k++)
        {
            innerError[k] = weights[(input * inner) + k] * outputError;
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
            weights[(input * inner) + k] += alpha * innerValues[k] * outputError;
        }

        return weights;
    }

    //Compute the output of a neural network given an input, weights, and net architecture.
    //Assumes network is feed forward, fully connected, and has one output.
    public static float runNet(int input, int inner, float[] weights, float[] inputValues)
    {
        float[] innerValues = new float[inner];
        float result = 0;

        for(int k = inner; --k >= 0;)
        {
            for(int a = input; --a >= 0;)
            {
                innerValues[k] += inputValues[a] * weights[a * inner + k];
            }
            result += innerValues[k] * weights[(input * inner) + k];
        }

        return result;
    }
}
