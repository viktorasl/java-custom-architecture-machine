package machine;

public class Processor extends Registerable {
	
	int mode; // Machine mode
	int ptr; // Pages table register
	int gr; // General register
	int pc; // Program counter
	int ih; // Interrupt handler
	int sp; // Stack pointer
	int cf; // Carry flag
	int pi; // Programming interrupt
	int si; // Supervisor interrupt
	int ti; // Timer interrupt
	int io; // I/O address
	
	OperativeMemory ram;
	ChannelSystem chn;
	
	public Processor(OperativeMemory ram, ChannelSystem chn) {
		this.ram = ram;
		this.chn = chn;
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
	
	private void setSi(int si) {
		if (this.si != si) {
			changes.firePropertyChange(Register.SI.name(), this.si, si);
			this.si = si;
		}
	}

	private void setPi(int pi) {
		if (this.pi != pi) {
			changes.firePropertyChange(Register.PI.name(), this.pi, pi);
			this.pi = pi;
		}
	}
	
	private void setSp(int sp) {
		if (this.sp != sp) {
			changes.firePropertyChange(Register.SP.name(), this.sp, sp);
			this.sp = sp;
		}
	}
	
	private void setTi(int ti) {
		if (this.ti != ti) {
			changes.firePropertyChange(Register.TI.name(), this.ti, ti);
			this.ti = ti;
		}
	}
	
	private void setIo(int io) {
		if (this.io != io) {
			changes.firePropertyChange(Register.IO.name(), this.io, io);
			this.io = io;
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
	
	private void push(int value) {
		ram.occupyMemory(sp / 10, sp % 10, String.valueOf(value));
		setSp(sp + 1);
	}
	
	private int pop() {
		setSp(sp - 1);
		return Integer.parseInt(ram.getMemory(sp / 10, sp % 10));
	}
	
	private int buildAddress(String addr) {
		if (this.mode == 0) {
			return Integer.parseInt(addr);
		} else {
			int trackNumber = Integer.parseInt(addr) % 100;
			int x = Math.floorDiv(trackNumber, 10);
			int y = trackNumber % 10;
			int vmTrackNumber = Integer.valueOf(ram.getMemory(ptr, x));
			int readAddress = vmTrackNumber * 10 + y;
			return readAddress;
		}
	}
	
	private String getValueInAddress(int addr) {
		int track = addr / 10;
		int idx = addr % 10;
		ram.markMemory(track, idx);
		return ram.getMemory(track, idx);
	}
	
	private void test() {
		if ((si + pi > 0) || (ti == 0)) {
			setMode(0);
			push(pc);
			push(ptr);
			push(cf);
			push(gr);
			setPc(ih);
		}
	}
	
	private void interpretCmd(String cmd) {
		incPc();
		int cmdLength = 1;
		
		try {
			if (mode == 0) {
				
				if (cmd.length() >= 4) {
					switch(cmd.substring(0, 3)) {
						case "STI": {
							int value = buildAddress(cmd.substring(3, 5));
							setTi(value);
							return;
						}
						case "SSI": {
							int value = buildAddress(cmd.substring(3, 4));
							setSi(value);
							return;
						}
						case "SPI": {
							int value = buildAddress(cmd.substring(3, 4));
							setPi(value);
							return;
						}
						case "SPT": {
							int value = buildAddress(cmd.substring(3, 5));
							setPtr(value);
							return;
						}
					}
				}
				
				switch(cmd.substring(0, 2)) {
					case "SA": {
						chn.setSa(io);
						return;
					}
					case "DA": {
						chn.setDa(io);
						return;
					}
					case "IO": {
						int value = buildAddress(cmd.substring(2, 3));
						chn.setIo(value);
						return;
					}
					case "DV": {
						int value = buildAddress(cmd.substring(2, 3));
						chn.setDv(value);
						return;
					}
					case "IH": {
						int value = buildAddress(cmd.substring(2, 5));
						setIh(value);
						return;
					}
					case "SP": {
						int value = buildAddress(cmd.substring(2, 5));
						setSp(value);
						return;
					}
					case "VM": {
						int value = gr % 1000;
						setPc(value);
						setMode(1);
						return;
					}
					case "LD": {
						String reg = cmd.substring(2, 4);
						switch (reg) {
							case "SI": setGr(si);
								break;
							case "PI": setGr(pi);
								break;
							case "TI": setGr(ti);
								break;
							case "IO": setGr(io);
								break;
						}
						return;
					}
				}
				
				switch(cmd.substring(0, 4)) {
					case "XCHG": {
						if (chn.getIo() == 0) { // output
							int track = Math.floorDiv(chn.getSa(), 10);
							int idx = chn.getSa() % 10;
							chn.outputData(ram.getMemory(track, idx));
						} else if (chn.getIo() == 1){ // input
							System.out.println("input");
						}
						return;
					}
					case "SMOD": {
						int value = buildAddress(cmd.substring(4, 5));
						setMode(value);
						return;
					}
				}
				
				if (cmd.substring(0, 5).equalsIgnoreCase("RESTR")) {
					setGr(pop());
					setCf(pop());
					setPtr(pop());
					setPc(pop());
					setMode(1);
					return;
				}
			}
			
			switch(cmd.substring(0, 2)) {
				case "GO": {
					int addr = buildAddress(cmd.substring(2, 5));
					setPc(addr);
					break;
				}
				case "MG": {
					int addr = buildAddress(cmd.substring(2, 5));
					setGr(Integer.parseInt(getValueInAddress(addr)));
					break;
				}
				case "MM": {
					int addr = buildAddress(cmd.substring(2, 5));
					ram.occupyMemory(addr / 10, addr % 10, String.valueOf(this.gr));
					break;
				}
				case "GV": {
					int value = Integer.valueOf(cmd.substring(2, 5));
					setGr(value);
					break;
				}
				case "AD": {
					int addr = buildAddress(cmd.substring(2, 5));
					int value = Integer.parseInt(ram.getMemory(addr / 10, addr % 10));
					setGr(this.gr + value);
					break;
				}
				case "CP": {
					int addr = buildAddress(cmd.substring(2, 5));
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
					int addr = buildAddress(cmd.substring(2, 5));
					if (this.cf == 0) {
						setPc(addr);
					}
					break;
				}
				case "JL": {
					int addr = buildAddress(cmd.substring(2, 5));
					if (this.cf == 2) {
						setPc(addr);
					}
					break;
				}
				case "JG": {
					int addr = buildAddress(cmd.substring(2, 5));
					if (this.cf == 1) {
						setPc(addr);
					}
					break;
				}
				case "CL": {
					int addr = buildAddress(cmd.substring(2, 5));
					push(pc);
					setPc(addr);
					break;
				}
				case "RT": {
					setPc(pop());
					break;
				}
				case "PT": {
					cmdLength = 3;
					setIo(buildAddress(String.valueOf(gr)));
					setSi(2);
					break;
				}
				case "SC": {
					cmdLength = 3;
					setIo(buildAddress(String.valueOf(gr)));
					setSi(3);
					break;
				}
				case "HT": {
					setSi(1);
					break;
				}
				default: {
					throw new Exception("Unknown command");
				}
			}
			
		} catch (Exception e) {
			System.out.println(((mode == 0)? "Supervisor" : "User") + ": Invalid command");
			//TODO: set si / pi
			if (mode == 1) {
				setPi(2);
			}
		}
		
		if (mode == 1) {
			setTi(Math.max(ti - cmdLength, 0));
			test();
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
			case SP: return sp;
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
