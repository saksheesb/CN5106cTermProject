package processes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import loggers.LogWriter;
import utils.GenericProperties;
import utils.Constants;
import utils.LoadPeerInfo;

/**
 This class serves as the starting point for our project.
 It reads configuration settings from CommonInfo.cfg and PeerInfo.cfg,
 sets up the logging system, and initiates the P2P process.
 */

public class Startup {

    public static void main(String[] args) throws SecurityException, IOException {

        if(args.length != 1 ) {
            throw new RuntimeException("Cannot process the peerID, Correct way to start:  java Startup <peerId> \n"
                    + "Here <PeerId> is the Id with which the process should be started");
        }

        final int pId = Integer.parseInt(args[0]); //pId = peerId

        // Initializes the Logger
        LogWriter.initialize(pId);

        // Read props from Common.cfg
        GenericProperties commonProp = new GenericProperties(Constants.COMM_CONFIGURATION_FILE);

        // Read Peer Info
        LoadPeerInfo peerInfo = new LoadPeerInfo();

        // This is an array of all the Peers
        List<Peer> peerArr = peerInfo.loadPeers(Constants.PEER_INFO_FILE);


        // This is array of Peer to connect to in the network
        List<Peer> p2Connect = new ArrayList<>();

        Peer curr = null;  // to assign current peer info

        for(Peer p : peerArr )
        {
            if(p.getpId() < pId )
                p2Connect.add(p);

            else if(p.getpId() == pId)
            {
                curr =  p;
            }
        }

        peerArr.remove(curr);

        for(Peer p :peerArr) {
            System.out.println(p);
        }

        // Invoke Call
        Process process =  new Process(curr, peerArr, commonProp);
        process.setup();

    }
}