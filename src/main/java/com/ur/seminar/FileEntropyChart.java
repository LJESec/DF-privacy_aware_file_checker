package com.ur.seminar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFileChooser;
import org.json.simple.JSONObject;

public class FileEntropyChart {
    private static final AtomicInteger id = new AtomicInteger(0);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {

        JFileChooser chooser = new JFileChooser();
        // Dialog zum Oeffnen von Dateien anzeigen
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();

        float divergence = 0;

        byte[] bytearray = Files.readAllBytes(file.toPath());
        //Frequenz
        ArrayList<Double> freqlist = new ArrayList<Double>();
        for (int b = 0; b < 128; b++) {
            double ctr = 0;
            for (double bit : bytearray) {
                if (bit == b) {
                    ctr++;
                }

            }
            freqlist.add(ctr / bytearray.length);
        }

        //Entropy
        double ent = 0.0;

        for (double freq : freqlist) {
            if (freq > 0) {
                ent = ent + freq * (Math.log(freq) / Math.log(2));

            }
        }
        ent = -ent;

        float result = (float) (Math.log(5) / Math.log(2));
/*
        System.out.println("Shannon entropy (min bits per byte-character):");
        System.out.println(ent);
        System.out.println(result);
        System.out.println("Min possible file size assuming max theoretical compression efficiency:");
        System.out.println(ent * bytearray.length + " in bits");
        System.out.println((ent * bytearray.length) / 8 + "in bytes");*/

        double minBitFileSize = ent * bytearray.length;
        double minByteFileSize = (ent * bytearray.length)/8;

        //Creating the JSON Object
        JSONObject status = new JSONObject();
        status.put("ID", id.incrementAndGet());
        status.put("Entropy",ent);
        status.put("Result", result);
        status.put("MinBitFileSize", minBitFileSize);
        status.put("MinByteFileSize", minByteFileSize);

        String filepath = "C:\\Users\\Dan\\Desktop\\" +
                "status_output" +
                id.get() +
                ".json";
        File outputFile = new File(filepath);
        FileWriter outputWrite = new FileWriter(outputFile, true);
        outputWrite.write(status.toJSONString());
        outputWrite.flush();
        }
}
