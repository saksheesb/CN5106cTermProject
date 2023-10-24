package processes;

import fileUtils.FileUtil;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import utils.GenericProperties;


/**
 A class responsible for handling files downloaded to local peers.
 This class is observable, and the Process class serves as its observer.
 */
public class FileHandler {

    // A BitSet for Maintaining the Parts
    private BitSet fetchedSegments;

    // FileObservers(Process in our case)
    private final List<FileObserver> obs = new ArrayList<>();

    // The CommProperties
    private GenericProperties props;

    // current Peer
    private Peer peer;

    // FileUtil Object
    private FileUtil fileUtil;


    // Total count of segments to be downloaded
    // This also defines the size of the BitVector
    private int numOfSegments;


    FileHandler(Peer peer, GenericProperties props){
        this.props = props;
        this.peer = peer;
        this.numOfSegments = (int) Math.ceil(props.getSizeOfFile()*1.0/ props.getSegmentSize());
        System.out.println("From FileHandler"+ numOfSegments);
        fetchedSegments = new BitSet(numOfSegments);
        fileUtil = new FileUtil(peer.pId, props.getNameOfFile());
    }


    // Write a segment to the file
    // Synchronized to ensure exclusive access, as multiple threads may access the same fileHandler object
    // A single fileHandler object is instantiated for this purpose
    public synchronized void writePiece (int part_id, byte[] part) {

        // Verify if the bit at the given index in the bitset is set.
        // If it is set, the file segment already exists.
        // If it is not set, proceed to write the file segment.
        final boolean isPresent = !fetchedSegments.get(part_id);
        fetchedSegments.set (part_id);

        //Check if all bits are received and ack the observers
        if (isPresent) {
            fileUtil.writeFileSegment(part, part_id);
            obs.forEach(observer -> observer.notifyFileSegmentArrived(part_id));
        }

        //Check if whole File is complete and ack the observer
        if(hasReceivedAllSegments()) {
            fileUtil.combineFile(fetchedSegments.cardinality());
            obs.forEach(observer -> observer.notifyFileDownload());
        }
    }


    /**
     * Indicates whether all segments have been received.
     *
     * @return true if all segments are received, otherwise false.
     */
    public boolean hasReceivedAllSegments() {
        for(int i = 0; i< numOfSegments; i++) {
            if(!fetchedSegments.get(i)) return false;
        }
        return true;
    }

    /**
     * Determines which segments to request from a given peer.
     * Selects a random index from the list of segments not yet received.
     *
     * @param bitsetAvailableFromPeer The bitset representing segments available from the peer.
     * @return The index of the segment to request.
     */

    public synchronized int getSegmentToReq(BitSet bitsetAvailableFromPeer ) {
        bitsetAvailableFromPeer.andNot((BitSet) fetchedSegments.clone());
        return getRandIndex(bitsetAvailableFromPeer);
    }

    /**
     *  Return if requested segment is present locally
     */
    public synchronized boolean hasPiece(int pieceIndex) {
        return fetchedSegments.get(pieceIndex);
    }

    /**Get a random Index from set elements in Bitset
     *
     * @param bs
     * @return
     */
    public int getRandIndex(BitSet bs) {
        String set = bs.toString();
        String[] indices = set.substring(1, set.length()-1).split(",");
        return Integer.parseInt(indices[(int)(Math.random()*(indices.length-1))].trim());
    }

    /**Setting All Parts to True
     *
     */
    public synchronized void setAllSegmentsToTrue()
    {
        fetchedSegments.set(0, numOfSegments,true);
    }
    /**Gets the currently set element in Bitset
     * This is equal to number of pieces recieved
     *
     * @return
     */
    public synchronized int getNumberOfReceivedParts() {
        return fetchedSegments.cardinality();
    }

    /**
     *
     * @return copy of  downloaded biset
     */
    public synchronized BitSet getFetchedSegments() {
        return (BitSet) fetchedSegments.clone();
    }

    /**Get the piece of file of given partId
     *
     * @param partId
     * @return
     */
    byte[] getSegment(int partId) {
        byte[] piece = fileUtil.fetchSegmentAsByteArray(partId);
        return piece;
    }

    public void attach(FileObserver fileObserver) {
        obs.add (fileObserver);
    }

    /**
     * If it has file. Then split the file into segments
     */
    public void divideFile(){
        fileUtil.divideFile(props.getSegmentSize());
    }

    /**
     *
     * @return The Number of Pieces of a File
     */
    public int getNumOfSegments() {
        return numOfSegments;
    }

}
