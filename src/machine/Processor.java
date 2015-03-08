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
	OperativeMemory ram;
	
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	public Processor(OperativeMemory ram) {
		this.ram = ram;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}
	
	private void setMode(int mode) {
		if (this.mode != mode) {
			changes.firePropertyChange(Register.Mode.name(), this.mode, mode);
			this.mode = mode;
		}
	}
	
	private void setGr(int gr) {
		if (this.gr != gr) {
			changes.firePropertyChange(Register.GR.name(), this.gr, gr);
			this.gr = gr;
		}
	}
	
	private void setCf(int cf) {
		if (this.cf != cf) {
			changes.firePropertyChange(Register.CF.name(), this.cf, cf);
			this.cf = cf;
		}
	}
	
	private void setIh(int ih) {
		if (this.ih != ih) {
			changes.firePropertyChange(Register.IH.name(), this.ih, ih);
			this.ih = ih;
		}
	}
	
	private void setTi(int ti) {
		if (this.ti != ti) {
			changes.firePropertyChange(Register.TI.name(), this.ti, ti);
			this.ti = ti;
		}
	}
	
	private void setPtr(int ptr) {
		if (this.ptr != ptr) {
			changes.firePropertyChange(Register.PTR.name(), this.ptr, ptr);
			this.ptr = ptr;
		}
	}
	
	private void setPc(int pc) {
		if (this.pc != pc) {
			changes.firePropertyChange(Register.PC.name(), this.pc, pc);
			this.pc = pc;
		}
	}
	
	private void incPc() {
		changes.firePropertyChange(Register.PC.name(), this.pc, this.pc + 1);
		setPc(this.pc + 1);
	}
	
	private String buildAddress(String addr) {
		if (this.mode == 0) {
			return addr;
		} else {
			//TODO: paging mechanism
			return addr;
		}
	}
	
	private String getValueInAddress(int addr) {
		int track = addr / 10;
		int idx = addr % 10;
		return ram.getMemory(track, idx);
	}
	
	private void interpretCmd(String cmd) {
		incPc();
		
		try {
			if (mode == 0) {
				switch(cmd.substring(0, 2)) {
					case "IH": {
						int value = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
						setIh(value);
						return;
					}
				}
				
				switch(cmd.substring(0, 3)) {
					case "STI": {
						int value = Integer.parseInt(buildAddress(cmd.substring(3, 5)));
						setTi(value);
						return;
					}
				}
			}
			
			int cmdLength = 1;
			
			switch(cmd.substring(0, 2)) {
				case "GO": {
					int addr = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
					setPc(addr);
					break;
				}
				case "MG": {
					int addr = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
					setGr(Integer.parseInt(getValueInAddress(addr)));
					break;
				}
				case "MM": {
					int addr = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
					ram.occupyMemory(addr / 10, addr % 10, String.valueOf(this.gr));
					break;
				}
				case "GV": {
					int value = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
					setGr(value);
					break;
				}
				case "AD": {
					int addr = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
					int value = Integer.parseInt(ram.getMemory(addr / 10, addr % 10));
					setGr(this.gr + value);
					break;
				}
				case "CP": {
					int addr = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
					int value = Integer.parseInt(ram.getMemory(addr / 10, addr % 10));
					if (this.gr == value) {
						setCf(0);
					} else if (this.gr > value) {
						setCf(1);
					} else {
						setCf(2);
					}
					break;
				}
				case "JE": {
					int addr = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
					if (this.cf == 0) {
						setPc(addr);
					}
					break;
				}
				case "JL": {
					int addr = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
					if (this.cf == 2) {
						setPc(addr);
					}
					break;
				}
				case "JG": {
					int addr = Integer.parseInt(buildAddress(cmd.substring(2, 5)));
					if (this.cf == 1) {
						setPc(addr);
					}
					break;
				}
			}
			
			setTi(Math.max(ti - cmdLength, 0));
		} catch (Exception e) {
			System.out.println(((mode == 0)? "Supervisor" : "User") + ": Invalid command");
		}
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
	
	public void step() {
		int track = pc / 10;
		int idx = pc % 10;
		String cmd = getValueInAddress(pc);
		System.out.println(track / 10 + "" + track % 10 + ":" + idx + "\t" + cmd);
		interpretCmd(cmd);
	}
}
