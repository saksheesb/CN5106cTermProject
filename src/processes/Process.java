package processes;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import loggers.EventLogger;
import loggers.LogWriter;
import utils.CommonProperties;


public class Process implements Runnable, FileObserver {

    private final Peer current;
    private final CommonProperties properties;
    private final FileHandler fileHandler;
    private final EventLogger eventLogger;
    private final AtomicBoolean downloadCompleted = new AtomicBoolean(false);
    private final AtomicBoolean peersDownloadFinished = new AtomicBoolean(false);
    private final AtomicBoolean finished = new AtomicBoolean(false);

    public Process(Peer current, List<Peer> peers, CommonProperties commonprop) {
        this.current = current;
        this.properties = commonprop;
        System.out.println(Thread.currentThread().getName() +"  "+peers.size());
        fileHandler = new FileHandler(current, commonprop);
        List<Peer> temp = new ArrayList<>(peers);
        //peerHandler = new PeerHandler(current, temp, fileHandler.getNumberOfPieces(), commonprop);
        eventLogger = new EventLogger(current.peerId);
        downloadCompleted.set(current.hasFile);
    }

    void setup() {
        attachObservers();
        splitFile();
        //runPeerHandler();
    }

    void attachObservers() {
        fileHandler.attach(this);
        //peerHandler.attach(this);
    }



//    void runPeerHandler() {
//        Thread t = new Thread(peerHandler);
//        t.setName(peerHandler.getClass().getName());
//        t.start();
//    }



    void splitFile() {
        if(current.hasFile) {
            fileHandler.splitFile();
            fileHandler.setAllParts();
        }
    }



    @Override
    public void run() {
        try(ServerSocket server = new ServerSocket(current.port)){
            System.out.println("Reached proccess run");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFileDownloadFinished() {
        System.out.println("In updateFileDownloadFinished method in Process");
    }

    @Override
    public void updateFilePartArrived(int part_index) {
        System.out.println("In updateFilePartArrived method in Process");
    }
}

