package ru.dreremin.memorymanager.memory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class MemoryManager {

    private final int sizeMemory;
    private final PriorityQueue<Segment> freeSegments;
    private final HashMap<Integer, Segment> busySegments;

    private static class SegmentComparator implements Comparator<Segment> {

        @Override
        public int compare(Segment s1, Segment s2) {
            return s2.getLength() - s1.getLength();
        }
    }

    public MemoryManager(int sizeMemory) {
        this.sizeMemory = sizeMemory;
        freeSegments = new PriorityQueue<>(new SegmentComparator());
        freeSegments.add(new Segment(sizeMemory));
        busySegments = new HashMap<>((int) (sizeMemory * 0.75));
    }

    public int malloc(int size) {

        Segment oldSegment = freeSegments.peek();

        if (oldSegment == null || oldSegment.getLength() < size) {
            return -1;
        }
        freeSegments.poll();

        int newFreeSegmentLength = oldSegment.getLength() - size;
        Segment prevSegment = oldSegment.getPrevSegment();
        Segment nextSegment = oldSegment.getNextSegment();
        Segment newBusySegment = new Segment(
                oldSegment.getStartIndex(),
                size,
                true);

        newBusySegment.setPrevSegment(prevSegment);
        if (prevSegment != null) {
            prevSegment.setNextSegment(newBusySegment);
        }
        busySegments.put(newBusySegment.getStartIndex(), newBusySegment);
        if (newFreeSegmentLength > 0) {
            Segment newFreeSegment = new Segment(
                    oldSegment.getStartIndex() + size,
                    newFreeSegmentLength,
                    false);
            newFreeSegment.setNextSegment(nextSegment);
            newFreeSegment.setPrevSegment(newBusySegment);
            newBusySegment.setNextSegment(newFreeSegment);
            if (nextSegment != null) {
                nextSegment.setPrevSegment(newFreeSegment);
            }
            freeSegments.add(newFreeSegment);
        } else {
            newBusySegment.setNextSegment(nextSegment);
            if (nextSegment != null) {
                nextSegment.setPrevSegment(newBusySegment);
            }
        }
        return newBusySegment.getStartIndex();
    }

    public int free(int startIndex) {
        if (!busySegments.containsKey(startIndex)) { return -1; }

        Segment freedSegment = busySegments.get(startIndex);

        busySegments.remove(startIndex);
        if (freedSegment == null) { return -1; }
        mergeWithNextSegment(mergeWithPreviousSegment(freedSegment));
        return 0;
    }

    private void mergeWithNextSegment(Segment curSegment) {

        Segment nextSegment = curSegment.getNextSegment();

        curSegment.setBusyStatus(false);
        if (nextSegment != null && !nextSegment.getBusyStatus()) {
            curSegment.setLength(curSegment.getLength() + nextSegment.getLength());
            curSegment.setNextSegment(nextSegment.getNextSegment());
            if (curSegment.getNextSegment() != null) {
                curSegment.getNextSegment().setPrevSegment(curSegment);
            }
            freeSegments.remove(nextSegment);
        }
        freeSegments.add(curSegment);
    }

    private Segment mergeWithPreviousSegment(Segment curSegment) {

        Segment prevSegment = curSegment.getPrevSegment();

        if (prevSegment == null || prevSegment.getBusyStatus()) {
            return curSegment;
        }
        freeSegments.remove(prevSegment);
        prevSegment.setLength(prevSegment.getLength() + curSegment.getLength());
        prevSegment.setNextSegment(curSegment.getNextSegment());
        if (prevSegment.getNextSegment() != null) {
            prevSegment.getNextSegment().setPrevSegment(prevSegment);
        }
        return prevSegment;
    }

    @Override
    public String toString() {
        return new StringBuilder("{ Busy segments: ")
                .append(busySegments)
                .append(" ; Free segments: ")
                .append(freeSegments)
                .append(" }")
                .toString();
    }
}
