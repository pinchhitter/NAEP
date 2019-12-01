package alibaba;

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
import weka.classifiers.trees.J48;

/**
 *
 * @author prakash
 */
public class Predict2TrainTestNom {

    public static void main(String args[]) throws FileNotFoundException, IOException, Exception {
        String basepath = "/home/prakash/daytoday/ml/alibaba/data/data_format2/trained/training/";
        String trainingdata = basepath + "trainNominal15-may.arff";
        String testingdata = basepath + "testNominal15-may.arff";
        String testingCustMerch = basepath + "test.um";
        String savemodelto = basepath + "trained.model";
        String prediction = basepath + "prediction.csv";
        

        //DATA
        Instances data;
        try (BufferedReader reader = new BufferedReader(new FileReader(trainingdata))) {
            data = new Instances(reader);
        }

        // setting class attribute
        data.setClassIndex(data.numAttributes() - 6);
        System.out.println(data.attribute(3));
        data.attribute(3);

        //TRAIN *********************************************
        String[] options = new String[1];
        options[0] = "";            // debug
        MultilayerPerceptron algo = new MultilayerPerceptron();
        //algo.setOptions(options);     // set the options
        System.out.println("Training...");
        algo.buildClassifier(data);   // build classifier
	System.out.println("Evaluating...");
	//EVALUATION OPTIONAL *******************************************
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(algo, data);
        System.out.println(eval.toSummaryString("Result Summary", false));
 	
        //SAVE MODEL ******************************************
        weka.core.SerializationHelper.write(savemodelto, algo);
	System.out.println("Saved Model");


       //LOAD MODEL ********************************************
        //reading a saved model, if already trained, skip above training part
        // Classifier algo = (Classifier) weka.core.SerializationHelper.read(savemodelto);
        //TESTING *******************************************
        try (BufferedReader reader = new BufferedReader(new FileReader(testingdata))) {
            data = new Instances(reader);
        }

        data.setClassIndex(data.numAttributes() - 6);
        PrintWriter pw = new PrintWriter(prediction);
        System.out.println("TEST these many:" + data.numInstances());
        Scanner sc = new Scanner(new File(testingCustMerch));
        System.out.println("Testing...");
        for (int i = 0; i < data.numInstances(); i++) {
            String cutmerline = sc.nextLine().trim();
            //System.out.println("Instance:"+data.instance(i));
            double pred = algo.classifyInstance(data.instance(i));
            //System.out.println("PRED:"+pred);
            //System.out.println(data.classAttribute());
            //System.out.println("Predcted Class: " + data.classAttribute().value((int) pred));
            pw.println(cutmerline+","+data.classAttribute().value((int) pred));
            pw.flush();
        }
        pw.close();

	

    }


}

