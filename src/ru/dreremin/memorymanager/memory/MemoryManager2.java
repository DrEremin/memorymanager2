package ru.dreremin.memorymanager.memory;

import java.util.Map;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;

public class MemoryManager2 {

    private final TreeMap<Integer, ArrayList<Segment>> freeSegments;
    private final HashMap<Integer, Segment> busySegments;

    public MemoryManager2(int sizeMemory) {

        freeSegments = new TreeMap<>();
        ArrayList<Segment> array = new ArrayList<>(3);
        array.add(new Segment(sizeMemory));
        freeSegments.put(sizeMemory, array);
        busySegments = new HashMap<>((int) (sizeMemory * 0.75));
    }

    private void addFreeSegment(Segment segment) {

        int length = segment.getLength();

        if (freeSegments.containsKey(length)) {
            freeSegments.get(length).add(segment);
        } else {
            ArrayList<Segment> segments = new ArrayList<>(3);
            segments.add(segment);
            freeSegments.put(length, segments);
        }
    }

    private void removeFreeSegment(Segment segment) {

        ArrayList<Segment> segments = freeSegments.get(segment.getLength());

        segments.remove(segment);
        if (segments.isEmpty()) {
            freeSegments.remove(segment.getLength());
        }
    }

    public int malloc(int size) {

        Map.Entry<Integer, ArrayList<Segment>> entry = freeSegments.lastEntry();

        if (entry == null) { return -1; }

        ArrayList<Segment> segments = entry.getValue();
        Segment oldSegment = segments.get(segments.size() - 1);

        if (oldSegment == null || oldSegment.getLength() < size) { return -1; }
        removeFreeSegment(oldSegment);

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
            addFreeSegment(newFreeSegment);
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
        ArrayList<Segment> segments;

        curSegment.setBusyStatus(false);
        if (nextSegment != null && !nextSegment.getBusyStatus()) {
            curSegment.setLength(curSegment.getLength() + nextSegment.getLength());
            curSegment.setNextSegment(nextSegment.getNextSegment());
            if (curSegment.getNextSegment() != null) {
                curSegment.getNextSegment().setPrevSegment(curSegment);
            }
            removeFreeSegment(nextSegment);
        }
        addFreeSegment(curSegment);
    }

    private Segment mergeWithPreviousSegment(Segment curSegment) {

        Segment prevSegment = curSegment.getPrevSegment();

        if (prevSegment == null || prevSegment.getBusyStatus()) {
            return curSegment;
        }
        removeFreeSegment(prevSegment);
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
