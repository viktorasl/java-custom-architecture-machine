package machine;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class Keyboard extends JTextField {

	private static final long serialVersionUID = 1524020953985578588L;
	private KeyboardProtocol protocol;
	
	public Keyboard (int columns){
		super(columns);
		
		Keyboard k = this;
		
		this.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
				if (k.getText().length() >= 5) {
					if (protocol != null) {
						protocol.deliverData(k.getText().substring(0, 5));
					}
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							k.setText("");
						}
						
					});
				}
			}
			
		});
	}
	
	public void addKeyboardProtocol (KeyboardProtocol p) {
		this.protocol = p;
	}
	
}
