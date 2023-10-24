package utils;

import java.text.ParseException;
import java.io.IOException;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Map;
import java.io.File;

/**
 A Plain Old Java Object (POJO) class for holding properties
 sourced from the Common.cfg file.
 */

public class GenericProperties {
    /** Each variable is property
     * which is to defined in
     * Common.cfg
     *
     */
    private int PreferredNeighborCount;
    private int UnchokeInterval;
    private int OptimisticUnchokeInterval;
    private String NameOfFile;
    private  int SizeOfFile;
    private int SegmentSize;


    private final String[] propertiesName = {"NumberOfPreferredNeighbors","UnchokingInterval","OptimisticUnchokingInterval"
            ,"FileName","FileSize","PieceSize"};

    public int getPreferredNeighborCount() {
        return PreferredNeighborCount;
    }


    public int getUnchokeInterval() {
        return UnchokeInterval;
    }


    public int getOptimisticUnchokeInterval() {
        return OptimisticUnchokeInterval;
    }


    public String getNameOfFile() {
        return NameOfFile;
    }


    public int getSizeOfFile() {
        return SizeOfFile;
    }


    public int getSegmentSize() {
        return SegmentSize;
    }


    public String[] getPropertiesName() {
        return propertiesName;
    }



    /** Initializes a common property object
     *  by Reading Property from Common.cfg
     */
    public GenericProperties(String propertiesFile) {
        try {
            Map<String,String> pmap = read(propertiesFile);
            this.PreferredNeighborCount = Integer.parseInt(pmap.get("PreferredNeighborCount"));
            this.UnchokeInterval = Integer.parseInt(pmap.get("UnchokeInterval"));
            this.OptimisticUnchokeInterval = Integer.parseInt(pmap.get("OptimisticUnchokeInterval"));
            this.NameOfFile = pmap.get("NameOfFile");
            this.SizeOfFile = Integer.parseInt(pmap.get("SizeOfFile"));
            this.SegmentSize = Integer.parseInt(pmap.get("SegmentSize"));
        }catch(Exception e) {
            throw new RuntimeException("Unable to Read from Configuration File :\n"+ e);
        }
    }


    /**
     Reads key-value pairs from a property file and returns them
     as a map.
     */

    private Map<String,String> read(String filename) throws IOException {
        Map<String,String> pmap = new HashMap<>();
        File file = new File(filename);

        String line;
        BufferedReader br  = new BufferedReader(new FileReader(file));
        int i = 0;  // Keeping track of lines
        while((line = br.readLine()) != null ) {
            line = line.trim();

            if ((line.length() <= 0)) {
                continue;
            }
            // segregate tokens by space
            String[] tokens = line.split("\\s+");
            if (tokens.length != 2) {
                throw new IOException (new ParseException (line, i));
            }

            pmap.put(tokens[0].trim(), tokens[1].trim());
        }

        br.close();
        return pmap;
    }
}