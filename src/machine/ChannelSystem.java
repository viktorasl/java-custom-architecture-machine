package machine;

public class ChannelSystem extends Registerable {
	
	private int sa; // source address
	private int da; // destination address
	private int io; // input = 1/output = 0 type
	private int dv; // device: hard-drive (dv=0) / external device(dv=1)printer(io=0) keyboard(io=1)
	
	public ChannelSystem (HardDrive hdd, Printer printer, Keyboard keyboard) {
		
	}
	
	private ChannelSystemProtocol protocol;
	
	public int getValue(ChannelSystemRegister reg) {
		switch (reg) {
		case SA: return sa;
		case DA: return da;
		case IO: return io;
		case DV: return dv;
		}
		return 0;
	}
	
	public int getSa() {
		return sa;
	}
	
	public void setSa(int sa) {
		if (this.sa != sa) {
			changes.firePropertyChange(ChannelSystemRegister.SA.name(), this.sa, sa);
			this.sa = sa;
		}
	}

	public int getDa() {
		return da;
	}

	public void setDa(int da) {
		if (this.da != da) {
			changes.firePropertyChange(ChannelSystemRegister.DA.name(), this.da, da);
			this.da = da;
		}
	}

	public int getIo() {
		return io;
	}

	public void setIo(int io) {
		if (this.io != io) {
			changes.firePropertyChange(ChannelSystemRegister.IO.name(), this.io, io);
			this.io = io;
		}
	}

	public int getDv() {
		return dv;
	}

	public void setDv(int dv) {
		if (this.dv != dv) {
			changes.firePropertyChange(ChannelSystemRegister.DV.name(), this.dv, dv);
			this.dv = dv;
		}
	}
	
	public void setChannelSystemProtocol(ChannelSystemProtocol csp) {
		this.protocol = csp;
	}
	
	public void outputData(String data) {
		if (this.protocol != null) {
			this.protocol.systemSentData(data);
		}
	}
	
}
