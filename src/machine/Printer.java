package machine;

import javax.swing.JTextArea;

public class Printer extends JTextArea {

	private static final long serialVersionUID = -5073085362453529973L;

	public Printer (int rows, int columns) {
		super(rows, columns);
		setEditable(false);
	}
	
	public void printData (String data) {
		append(data);
	}
	
}
