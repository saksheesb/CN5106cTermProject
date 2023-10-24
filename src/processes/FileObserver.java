package processes;

public interface FileObserver {

    public void notifyFileDownload();
    public void notifyFileSegmentArrived(int part_index);
}
