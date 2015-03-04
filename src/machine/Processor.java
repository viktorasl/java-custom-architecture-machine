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
		this.ptr = "00";
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
	
	private String format(String s, int l) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < l - s.length(); i++) {
			sb.append("0");
		}
		sb.append(s);
		return sb.toString();
	}
	
	private void setMode(String mode) {
		if (this.mode != mode) {
			changes.firePropertyChange(Register.Mode.name(), this.mode, mode);
			this.mode = mode;
		}
	}
	
	private void setGr(String gr) {
		String formattedVal = format(gr, 5);
		if (! this.gr.equalsIgnoreCase(formattedVal)) {
			changes.firePropertyChange(Register.GR.name(), this.gr, formattedVal);
			this.gr = formattedVal;
		}
	}
	
	private void setPtr(String ptr) {
		if (this.ptr != ptr) {
			changes.firePropertyChange(Register.PTR.name(), this.ptr, ptr);
			this.ptr = ptr;
		}
	}
	
	private void setPc(String pc) {
		String formattedVal = format(pc, 3);
		if (! this.pc.equalsIgnoreCase(formattedVal)) {
			changes.firePropertyChange(Register.PC.name(), this.pc, formattedVal);
			this.pc = formattedVal;
		}
	}
	
	private void incPc() {
		int i = Integer.parseInt(pc);
		changes.firePropertyChange(Register.PC.name(), i, i++);
		setPc(String.valueOf(i++));
	}
	
	private String buildAddress(String addr) {
		if (this.mode.equalsIgnoreCase("0")) {
			return addr;
		} else {
			//TODO: paging mechanism
			return addr;
		}
	}
	
	private String getValueInAddress(String addr) {
		int a1 = Character.getNumericValue(addr.charAt(0));
		int a2 = Character.getNumericValue(addr.charAt(1));
		int a3 = Character.getNumericValue(addr.charAt(2));
		int track = a1 * 10 + a2;
		return ram.getMemory(track, a3);
	}
	
	private void interpretCmd(String cmd) {
		incPc();
		
		try {
			switch(cmd.substring(0, 2)) {
				case "GO": {
					String addr = buildAddress(cmd.substring(2, 5));
					setPc(addr);
					break;
				}
				case "MG": {
					String addr = buildAddress(cmd.substring(2, 5));
					setGr(getValueInAddress(addr));
				}
			}
		} catch (Exception e) {
			System.out.println("Invalid command");
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
	
	public void step() {
		int a1 = Character.getNumericValue(pc.charAt(0));
		int a2 = Character.getNumericValue(pc.charAt(1));
		int a3 = Character.getNumericValue(pc.charAt(2));
		String cmd = getValueInAddress(pc);
		System.out.println(a1 * 10 + "" + a2 + ":" + a3 + "\t" + cmd);
		interpretCmd(cmd);
	}
}
