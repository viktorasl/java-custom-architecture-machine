package machine;

import java.util.ArrayList;
import java.util.List;

public class OperativeMemory {
	
	private int trackSize;
	private int tracksCount;
	private String[] memory;
	private List<OperativeMemoryChangeListener> memChangeListeners;
	
	public OperativeMemory(int tracksCount, int trackSize) {
		this.trackSize = trackSize;
		this.tracksCount = tracksCount;
		this.memory = new String[tracksCount * trackSize];
		this.memChangeListeners = new ArrayList<OperativeMemoryChangeListener>();
		
		for (int i = 0; i < tracksCount; i++) {
			for (int j = 0; j < trackSize; j++) {
				occupyMemory(i, j, "");
			}
		}
	}
	
	private String format(String s) {
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < 5 - s.length(); i++) {
			sb.append("0");
		}
		return sb.toString();
	}
	
	public void occupyMemory(int track, int idx, String value) {
		String formattedVal = format(value);
		this.memory[track * this.trackSize + idx] = formattedVal;
		for (OperativeMemoryChangeListener l : memChangeListeners) {
			l.memoryChanged(track, idx, formattedVal);
		}
	}
	
	public String getMemory(int track, int idx) {
		return memory[track * this.trackSize + idx];
	}
	
	public void addOperativeMemoryChangeListener(OperativeMemoryChangeListener l) {
		memChangeListeners.add(l);
	}
	
	public void removeOperativeMemoryChangeListener(OperativeMemoryChangeListener l) {
		memChangeListeners.remove(l);
	}
	
	public int getTrackSize() {
		return trackSize;
	}
	
	public int getTracksCount() {
		return tracksCount;
	}
	
	public int getTotalSize() {
		return memory.length;
	}
	
}
