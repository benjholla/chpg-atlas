package chpg.atlas.support;

import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {

    // reserving negative values for ephemeral elements (which are not serializable)
    private AtomicInteger nextNodeId = new AtomicInteger(0);
    private AtomicInteger nextEdgeId = new AtomicInteger(0);

    public int createNodeId() {
        return nextNodeId.getAndIncrement();
    }

    public int createEdgeId() {
        return nextEdgeId.getAndIncrement();
    }
}
