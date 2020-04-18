package com.ur.seminar;

import org.json.simple.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * This class contains the original algorithm created by Marcus
 *  */
public class Differentiator {

    private String outputName;
    private File inputFile;
    private String fileName;
    public Differentiator(File file,String fileName)throws IOException{

        double fThreshold = 0.08;
        double eThreshold = 0.08;
        double variation = 0.02;

        this.inputFile = file;
        this.fileName = fileName;

            String resultPath = "./results/differentiator/";
            String[] splitted = this.fileName.split("/.");
            outputName = "output_" + splitted[0] + ".csv";

            File input = this.inputFile;
            File path = new File("./directory");
            File[] files = path.listFiles();
            //Number of files in the directory
            int fileCount = files.length;
            File calculation = new File(resultPath  + outputName);
            for (int i = 0; i < fileCount; i++) {
                if (files[i].isFile()) { //this line weeds out other directories/folders
                    System.out.println(files[i]);
                    File file2 = files[i];

                    ArrayList<Double> frequencyList1 = getFrequency(input);

                    double entropy1 = getEntropy(frequencyList1);

                    ArrayList<Double> frequencyList2 = getFrequency(file2);
                    double entropy2 = getEntropy(frequencyList2);

                    FileWriter fr = new FileWriter(calculation, true);
                    BufferedWriter br = new BufferedWriter(fr);
                    PrintWriter pr = new PrintWriter(br);
                    //frequency differences
                    double fdifference = determineDifference(frequencyList1, frequencyList2);
                    //entropy differences
                    double edifference = determineEntropyDifference(entropy1, entropy2);
                    //frequency & entropy differnces
                    double fedifference = fdifference - edifference;
                    int fdecision = 0;
                    int edecision = 0;
                    int fedecision = 0;
                    int vdecision = 0;
                    int finaldecision = 0;

                    //frequency decision
                    if (fdifference > fThreshold) {
                        fdecision = 1;
                    }
                    //variation
                    if (((fdifference - fThreshold) < 0.02 && (fdifference - fThreshold) > -0.02) && fedifference > 0) {
                        fdecision = 1;
                    }
                    //entropy decision
                    if (edifference > eThreshold) {
                        edecision = 1;
                    }
                    //fe decision
                    if (fedifference > 0) {
                        fedecision = 1;
                    }

                    int decisionsum = fdecision + edecision + fedecision;

                    //final decision
                    if (decisionsum >= 2) {
                        finaldecision = 1;
                    }

                    pr.println(file2.getName() + ";" + fdifference + ";" + fdecision + ";" + edifference + ";" + edecision + ";" + fedifference + ";" + fedecision + ";" + finaldecision);
                    //pr.println(file2.getName()+" "+determineEntropyDifference(entropy1,entropy2));
                    pr.close();
                    br.close();
                    fr.close();
                    /*
                    double percentage = (double) (i + 1) / fileCount;

                    JSONObject status = new JSONObject();
                    status.put("id", i + 1);
                    status.put("MaxFileCount", fileCount);
                    status.put("FilesToRead", fileCount - (i + 1));
                    status.put("percentage", percentage);
                    String StatusPath = ".\\results\\status\\" + "status" + (i + 1) + ".json";
                    File statusFile = new File(StatusPath);
                    FileWriter statusWriter = new FileWriter(statusFile, false);
                    statusWriter.write(status.toJSONString());
                    statusWriter.flush();
                    statusWriter.close();*/
                }
            }
        }
    /**
     * Calculates the frequency of a file
     * @param file the file to calculate
     * @return an ArrayList containing a number of different double values used to calculate the entropy*/
    private static ArrayList<Double> getFrequency(File file) throws IOException
    {
        byte[] bytearray = Files.readAllBytes(file.toPath());
        //Frequenz
        int t = 0;
        ArrayList<Double> freqlist = new ArrayList<Double>();
        //8 bit = 128 signs
        for(int b= 0; b < 128;b++)
        {
            double ctr = 0;
            for(double bit: bytearray)
            {
                if (bit == b)
                {
                    ctr++;
                }
            }
            freqlist.add(ctr/bytearray.length);
        }
        return freqlist;
    }
    /**
     * Calculates the entropy of a file
     * @param freqlist it needs the values calculated in
     * @see #getFrequency(File)
     * @return the calculated entropy value
     * */
    private static double getEntropy(ArrayList<Double> freqlist)
    {
        //Entropy
        double ent = 0.0;
        for(double freq: freqlist)
        {
            if (freq >0)
            {
                ent = ent + freq * (Math.log(freq)/Math.log(2));

            }
        }
        //ent = -ent;

        float result = (float) (Math.log(5)/Math.log(2));

        return ent;
    }
    /**
     * Calculated the difference between two entropy values
     * @return the value of the difference*/
    private static double determineEntropyDifference(double entropy1, double entropy2)
    {
        //System.out.println(entropy1+" "+entropy2);
        return Math.abs(Math.abs(entropy1) - Math.abs(entropy2));
    }
    /**
     * Calculated the difference between two frequencylists
     * @return the value of the difference*/
    private static double determineDifference(ArrayList<Double> firstfile, ArrayList<Double> secondfile)
    {
        double differgence = 0;

        //determine which file is larger

        if(firstfile.size() > secondfile.size())
        {
            //Compare frequencies
            for(int i = 0; i < secondfile.size(); i++)
            {
                differgence = differgence + Math.abs(firstfile.get(i) - secondfile.get(i));
            }
        }else
        {
            //Compare frequencies
            for(int i = 0; i < firstfile.size(); i++)
            {
                differgence = differgence + Math.abs(firstfile.get(i) - secondfile.get(i));
            }
        }

        return differgence;

    }
    /**
     * Getter method of the outputName variable
     * @return the name of the calculated output*/
    public String getOutputName() {
        return this.outputName;
    }
}
