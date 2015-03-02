package machine;

public enum Register {
	Mode,
	PTR,
	GR,
	PC,
	IH,
	CF,
	PI,
	SI,
	TI;
	
	public String format() {
		switch(this) {
			case Mode:
			case CF:
			case PI:
			case SI:
			case TI:
				return "%01d";
			case PTR:
			case GR:
			case IH:
			case PC:
			default:
				return "%04d";
		}
	}
}
