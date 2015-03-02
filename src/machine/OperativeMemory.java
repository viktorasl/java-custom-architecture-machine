package machine;

import java.util.ArrayList;
import java.util.List;

public class OperativeMemory {
	
	private int trackSize;
	private String[] memory;
	private List<OperativeMemoryChangeListener> memChangeListeners;
	
	public OperativeMemory(int tracksCount, int trackSize) {
		this.trackSize = trackSize;
		this.memory = new String[tracksCount * trackSize];
		this.memChangeListeners = new ArrayList<OperativeMemoryChangeListener>();
	}
	
	public void occupyMemory(int track, int idx, String value) {
		this.memory[track * this.trackSize + idx] = value;
		for (OperativeMemoryChangeListener l : memChangeListeners) {
			l.memoryChanged(track, idx, value);
		}
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
	
}
