package processes;

import java.util.Collection;

public interface PeerObserver {

    public void notifyOnPeerDownloadComplete();
    public void notifyOnPeersChoke(Collection<Integer> chokedPeersIds);
    public void notifyOnPeersUnchoke(Collection<Integer> unchokedPeersIds);
}
