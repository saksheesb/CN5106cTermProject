package processes;

import java.util.BitSet;

/**
 *  A Model for a PEER in the P2P System
 */


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A POJO Class for Modelling the  Peer
 * @author
 *
 */
public class Peer {

    public final int pId;

    public final String IP;

    public final int portNum;

    public final boolean filePresent;

    public AtomicInteger downloadedBytes;

    public BitSet receivedSegment;

    private final AtomicBoolean interested;

    // Constructor

    public Peer(int pId, String IP, int portNum, boolean filePresent) {

        this.pId = pId;

        this.IP = IP;

        this.portNum = portNum;

        this.filePresent = filePresent;

        this.downloadedBytes = new AtomicInteger (0);

        this.receivedSegment = new BitSet();

        this.interested = new AtomicBoolean (false);
    }

    // Getters and Setters

    public AtomicInteger getDownloadedBytes() {
        return downloadedBytes;
    }

    public void setDownloadedBytes(int bytes) {
        this.downloadedBytes.set(bytes);
    }

    public BitSet getReceivedSegment() {
        return receivedSegment;
    }

    public void setReceivedSegment(BitSet receivedSegment) {
        this.receivedSegment = receivedSegment;
    }

    public int getpId() {
        return pId;
    }

    public String getIP() {
        return IP;
    }

    public int getPortNum() {
        return portNum;
    }

    public boolean HasFile() {
        return filePresent;
    }

    public Boolean isInterested() {
        return interested.get();
    }


    public void setInterested(Boolean val) {
        interested.set (val);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Peer)) return false;
        return  ((Peer) obj).getpId() == this.getpId();
    }

    @Override
    public int hashCode() {
        //Can use peer ID as hashcode
        return this.pId;
    }

    @Override
    public String toString() {
        return "Peer : "+this.pId +"  Address : "+ this.IP +"  Port : "+ this.portNum;
    }
}
