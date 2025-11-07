package usecase.maptime;

import java.time.Instant;

public class UpdateMapTimeOutputData {
    private final Instant stamp;

    public UpdateMapTimeOutputData(Instant stamp) {
        this.stamp = stamp;
    }

    public java.time.Instant getStamp() {
        return stamp;
    }
}
