package processes;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
import loggers.EventLogger;
import java.net.ServerSocket;
import java.util.List;
import utils.GenericProperties;


public class Process implements Runnable, FileObserver {

    private final Peer current;
    private final GenericProperties properties;
    private final FileHandler fileHandler;
    private final EventLogger eventLogger;
    private final AtomicBoolean downloadCompleted = new AtomicBoolean(false);
    private final AtomicBoolean peersDownloadFinished = new AtomicBoolean(false);
    private final AtomicBoolean finished = new AtomicBoolean(false);

    public Process(Peer current, List<Peer> peers, GenericProperties commonprop) {
        this.current = current;
        this.properties = commonprop;
        System.out.println(Thread.currentThread().getName() +"  "+peers.size());
        fileHandler = new FileHandler(current, commonprop);
        List<Peer> temp = new ArrayList<>(peers);
        eventLogger = new EventLogger(current.pId);
        downloadCompleted.set(current.filePresent);
    }

    void setup() {
        attachObservers();
        splitFile();
    }

    void attachObservers() {
        fileHandler.attach(this);
    }


    void splitFile() {
        if(current.filePresent) {
            fileHandler.divideFile();
            fileHandler.setAllSegmentsToTrue();
        }
    }

    @Override
    public void run() {
        try(ServerSocket server = new ServerSocket(current.portNum)){
            System.out.println("Reached process run");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyFileDownload() {
        System.out.println("In notifyFileDownload method in Process");
    }

    @Override
    public void notifyFileSegmentArrived(int part_index) {
        System.out.println("In notifyFileSegmentArrived method in Process");
    }
}

