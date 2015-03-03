package machine;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Processor {
	
	int mode; // Machine mode
	int ptr; // Pages table register
	int gr; // General register
	int pc; // Program counter
	int ih; // Interrupt handler
	int cf; // Carry flag
	int pi; // Programming interrupt
	int si; // Supervisor interrupt
	int ti; // Timer interrupt
	
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	public Processor() {
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}
	
	public void setMode(int mode) {
		if (this.mode != mode) {
			changes.firePropertyChange(Register.Mode.name(), this.mode, mode);
			this.mode = mode;
		}
	}
	
	public void setPtr(int ptr) {
		if (this.ptr != ptr) {
			changes.firePropertyChange(Register.PTR.name(), this.ptr, ptr);
			this.ptr = ptr;
		}
	}
	
	public void incPc() {
		changes.firePropertyChange(Register.PC.name(), pc, ++pc);
	}
	
	public void interpretCmd(String cmd) {
		incPc();
	}
	
	public int getValue(Register reg) {
		switch (reg) {
			case CF: return cf;
			case GR: return gr;
			case IH: return ih;
			case Mode: return mode;
			case PC: return pc;
			case PI: return pi;
			case PTR: return ptr;
			case SI: return si;
			case TI: return ti;
		}
		return 0;
	}
	
}
