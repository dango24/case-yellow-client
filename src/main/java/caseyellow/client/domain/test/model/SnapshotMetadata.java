package caseyellow.client.domain.test.model;

import caseyellow.client.common.Utils;
import caseyellow.client.exceptions.InternalFailureException;

import java.io.File;

public class SnapshotMetadata {

    private String hash;
    private String s3Path;
    private long timestamp;

    public SnapshotMetadata() {
    }

    public SnapshotMetadata(String hash, String s3Path) {
        this.hash = hash;
        this.s3Path = s3Path;
        this.timestamp = System.currentTimeMillis();
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getS3Path() {
        return s3Path;
    }

    public void setS3Path(String s3Path) {
        this.s3Path = s3Path;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" +
                "hash='" + hash + '\'' +
                ", s3Path='" + s3Path + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
