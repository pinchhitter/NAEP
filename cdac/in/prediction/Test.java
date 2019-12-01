package cdac.in.prediction;

import weka.core.Instances;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;

/**
 *
 * @author Shekhar 
 */
public class Test {

    public static void main(String args[]) throws FileNotFoundException, IOException, Exception {

	/* Load Model */	

        Classifier algo = (MultilayerPerceptron) weka.core.SerializationHelper.read("./naep.model");

	/* Testing The Model */

        Instances data = null;

        try{
		BufferedReader reader = new BufferedReader(new FileReader("./testing.arff"));
            	data = new Instances( reader );
        	data.setClassIndex( data.numAttributes() - 1 );

        	for (int i = 0; i < data.numInstances(); i++) {
            		double pred = algo.classifyInstance( data.instance(i) );
            		System.out.println(","+pred);
		}

	}catch(Exception e){
		e.printStackTrace();
	}
    }
}

