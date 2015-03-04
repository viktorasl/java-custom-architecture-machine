package machine;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Processor {
	
	String mode; // Machine mode
	String ptr; // Pages table register
	String gr; // General register
	String pc; // Program counter
	String ih; // Interrupt handler
	String cf; // Carry flag
	String pi; // Programming interrupt
	String si; // Supervisor interrupt
	String ti; // Timer interrupt
	OperativeMemory ram;
	
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	public Processor(OperativeMemory ram) {
		this.ram = ram;
		
		this.mode = "0";
		this.ptr = "000";
		this.gr = "00000";
		this.pc = "000";
		this.ih = "000";
		this.cf = "0";
		this.pi = "0";
		this.si = "0";
		this.ti = "0";
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}
	
	public void setMode(String mode) {
		if (this.mode != mode) {
			changes.firePropertyChange(Register.Mode.name(), this.mode, mode);
			this.mode = mode;
		}
	}
	
	public void setPtr(String ptr) {
		if (this.ptr != ptr) {
			changes.firePropertyChange(Register.PTR.name(), this.ptr, ptr);
			this.ptr = ptr;
		}
	}
	
	public void incPc() {
		int i = Integer.parseInt(pc);
		changes.firePropertyChange(Register.PC.name(), i, i++);
		pc = String.valueOf(i++);
	}
	
	public void setPc(String pc) {
		if (this.pc != pc) {
			changes.firePropertyChange(Register.PC.name(), this.pc, pc);
			this.pc = pc;
		}
	}
	
	public void interpretCmd(String cmd) {
		incPc();
		
		if (cmd != null) {
			switch(cmd.substring(0, 2)) {
				case "GO": {
					setPc(cmd.substring(2, 3));
					break;
				}
			}
		}
	}
	
	public String getValue(Register reg) {
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
		return null;
	}
	
}
