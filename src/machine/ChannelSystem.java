package machine;

public class ChannelSystem extends Registerable {
	
	private int sa; // source address
	private int da; // destination address
	private int io; // input = 1/output = 0 type
	private int dv; // device: hard-drive = 0/1(io == 0 => monitor, io == 1 => flashdrive)
	
	public int getValue(ChannelRegisters reg) {
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
			changes.firePropertyChange(ChannelRegisters.SA.name(), this.sa, sa);
			this.sa = sa;
		}
	}

	public int getDa() {
		return da;
	}

	public void setDa(int da) {
		if (this.da != da) {
			changes.firePropertyChange(ChannelRegisters.DA.name(), this.da, da);
			this.da = da;
		}
	}

	public int getIo() {
		return io;
	}

	public void setIo(int io) {
		if (this.io != io) {
			changes.firePropertyChange(ChannelRegisters.IO.name(), this.io, io);
			this.io = io;
		}
	}

	public int getDv() {
		return dv;
	}

	public void setDv(int dv) {
		if (this.dv != dv) {
			changes.firePropertyChange(ChannelRegisters.DV.name(), this.dv, dv);
			this.dv = dv;
		}
	}
	
	public void outputData(String data) {
		System.out.println(data);
	}
	
}
