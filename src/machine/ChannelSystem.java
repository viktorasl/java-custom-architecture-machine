package machine;

public class ChannelSystem extends Registerable {
	
	private int sa; // source address
	private int da; // destination address
	private int io; // input = 1/output = 0 type
	private int dv; // device: hard-drive = 0/(io == 0 => monitor, io == 1 => flashdrive)
	
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
		this.sa = sa;
	}

	public int getDa() {
		return da;
	}

	public void setDa(int da) {
		this.da = da;
	}

	public int getIo() {
		return io;
	}

	public void setIo(int io) {
		this.io = io;
	}

	public int getDv() {
		return dv;
	}

	public void setDv(int dv) {
		this.dv = dv;
	}
	
}
