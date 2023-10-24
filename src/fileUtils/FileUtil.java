package fileUtils;

import loggers.LogWriter;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;


/**
 Includes logic for splitting and combining files.
 */

public class FileUtil {

    // Destination file for the merge
    private final File file;

    private final File pDir; // directory of parts, directory where files are saved

    private final String fName; // file name

    public FileUtil(int peerId, String fName){
        pDir = new File("./PEER_" + peerId + "/files/parts/"+ fName); // building directory path
        pDir.mkdirs(); // creating directory
        this.fName = fName; // setting the  file name to file name

        // Create directory for downloaded files
        File directory = new File("./PEER_" + peerId + "/files/downloaded/");
        directory.mkdirs();
        file = new File("./PEER_" + peerId + "/files/downloaded/"+ fName);
    }


    // function for splitting the file
    public void divide(File inputFile, int partSize){
        FileInputStream fileIS;
        String outputFile;
        FileOutputStream fileOS;
        // Size of file
        int pieceNum = 0;
        int readLen = partSize;
        byte[] piece;
        int size = (int) inputFile.length();
        try {
            fileIS = new FileInputStream(inputFile);

            for(;size > 0; pieceNum++){

                if (size <= partSize) {
                    readLen = size;
                }
                piece = new byte[readLen];
                size  -= fileIS.read(piece, 0, readLen);


                outputFile = pDir + "/" + pieceNum;
                fileOS = new FileOutputStream(outputFile);
                fileOS.write(piece);
                fileOS.flush();
                fileOS.close();
            }
            fileIS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] fetchSegmentAsByteArray(int partId) {
        File file = new File(pDir.getAbsolutePath() + "/" + partId);
        return readSegment(file);
    }

    public void writeFileSegment(byte[] part, int partId) {
        File output = new File(pDir.getAbsolutePath() + "/" + partId);
        try(FileOutputStream fos= new FileOutputStream(output)) {
            fos.write(part);
            fos.flush();
        } catch (Exception e) {
            LogWriter.getLogWriterInstance().warning(e);
        }
    }

    private byte[] readSegment(File piece){
        try(FileInputStream fis = new FileInputStream(piece);) {
            byte[] read = new byte[(int) piece.length()];
            fis.close();
            return read;
        } catch (Exception e) {
            LogWriter.getLogWriterInstance().warning(e);
        }
        return null;

    }

    public void divideFile(int partSize){
        divide(new File(fName), partSize);
    }

    public void combineFile(int numParts) {
        File out = file;
        FileInputStream fis;
        byte[] fileBytes;

        List<File> pieces = new ArrayList<>();

        for (int i = 0; i < numParts; i++) {
            pieces.add(new File(pDir.getPath() + "/" + i));
        }

        try(FileOutputStream fos = new FileOutputStream(out)){
            for (File file : pieces) {
                fis = new FileInputStream(file);
                fileBytes = new byte[(int) file.length()];
                fos.write(fileBytes);
                fos.flush();
                fis.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
