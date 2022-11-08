package ru.dreremin.memorymanager.memory;

import java.util.Optional;

public class Segment {

    private final int startIndex;
    private int length;
    private boolean busyStatus;
    private Segment prevSegment;
    private Segment nextSegment;

    public Segment(int length) {
        startIndex = 0;
        this.length = length;
        busyStatus = false;
        prevSegment = null;
        nextSegment = null;
    }

    public Segment(int startingIndex,
                   int length,
                   boolean busyStatus) {
        this.startIndex = startingIndex;
        this.length = length;
        this.busyStatus = busyStatus;
    }

    public void setLength(int newLength) { length = newLength; }

    public void setBusyStatus(boolean newStatus) { busyStatus = newStatus; }

    public void setPrevSegment(Segment segment) {prevSegment = segment; }

    public void setNextSegment(Segment segment) {nextSegment = segment; }

    public int getStartIndex() { return startIndex; }

    public int getLength() { return length; }

    public boolean getBusyStatus() { return busyStatus; }

    public Segment getPrevSegment() {
        return prevSegment;
    }

    public Segment getNextSegment() {
        return nextSegment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Segment other = (Segment) o;
        return this.startIndex == other.getStartIndex()
                && this.length == other.getLength()
                && this.busyStatus == other.getBusyStatus()
                && this.prevSegment == other.getPrevSegment()
                && this.nextSegment == other.getNextSegment();
    }

    @Override
    public int hashCode() { return startIndex; }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("(start: ")
                .append(startIndex)
                .append(", length: ")
                .append(length)
                .append(", status: ")
                .append(busyStatus ? "Busy)" : "Free)")
                .toString();
    }
}
