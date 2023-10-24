package utils;

import processes.Peer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;

/**
 A class responsible for loading all peer details.
 Each set of peer details is encapsulated within a Peer object.
 */
public class LoadPeerInfo {

    public List<Peer> loadPeers(String fileName){

        try {
            return retrieve(fileName);

        } catch (Exception e) {

            throw new RuntimeException("Failed to Read Remote Peer Information"+ e);
        }
    }

    /**Read the PeerInfo.cfg file, associate peer data with Peer objects,
     * and return a list of these Peer instances.
     */

    private List<Peer> retrieve(String fileName) throws IOException, ParseException {

        File file = new File(fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));

        List<Peer> pArr = new ArrayList<>(); // peer list

        String newLine;

        int i = 0;

        while ((newLine = br.readLine()) != null) {
            newLine = newLine.trim();

            if ((newLine.length() == 0)) {
                continue;
            }
            String[] tokens = newLine.split("\\s+");

            if (tokens.length != 4) {
                throw new ParseException (newLine, i);
            }

            i++;

            int pId = Integer.parseInt(tokens[0].trim());
            String IP  = tokens[1].trim();
            int port = Integer.parseInt(tokens[2].trim());
            boolean filePresent = (tokens[3].trim().compareTo("1") == 0);
            Peer peer = new Peer(pId, IP, port, filePresent);
            pArr.add(peer);
        }

        return pArr;
    }

}
