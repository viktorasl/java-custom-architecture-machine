package machine;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MachineController extends JFrame {
	
	private static final long serialVersionUID = 3795986469706534809L;
	private Processor cpu;
	private OperativeMemory ram;
	
	public static void main(String[] argv) {
		new MachineController();
	}
	
	public MachineController() {
		cpu = new Processor();
		ram = new OperativeMemory(999, 10);
		
		this.getContentPane().setLayout(new GridLayout(1, 3));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Real Machine");
		this.setSize(500, 300);
		
		// initializing files list table
		String[] columnNames = {"Address", "Content"};
		DefaultTableModel table = new MemoryTable(columnNames, 0);
		JTable dataTable = new JTable(table);
		JScrollPane scrollPane = new JScrollPane(dataTable);
		this.getContentPane().add(scrollPane);
		
		initializeRegisters();
		
		this.setVisible(true);
	}
	
	private void initializeRegisters() {
		JPanel registersPanel = new JPanel();
		
		final Map<Register, JTextField> registersMap = new HashMap<Register, JTextField>();
		
		for (Register reg : Register.values()) {
			JPanel registerPanel = new JPanel();
			JLabel regLabel = new JLabel(reg.name().toUpperCase());
			final JTextField regField = new JTextField(String.format("%04d", cpu.getValue(reg)));
			registersMap.put(reg, regField);
			regField.setEditable(false);
			registerPanel.add(regLabel);
			registerPanel.add(regField);
			registersPanel.add(registerPanel);
		}
		
		cpu.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Register reg = Register.valueOf(evt.getPropertyName());
				registersMap.get(reg).setText(String.format("%04d", (int)evt.getNewValue()));
			}
			
		});
		
		new Timer().schedule(new TimerTask() {          
		    @Override
		    public void run() {
		    	cpu.setPtr(23);    
		    }
		}, 2000);
		
		new Timer().schedule(new TimerTask() {          
		    @Override
		    public void run() {
		    	cpu.setMode(1);  
		    }
		}, 2000);
		
		this.getContentPane().add(registersPanel);
	}
	
}
