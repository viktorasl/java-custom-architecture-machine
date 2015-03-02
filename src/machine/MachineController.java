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
		
		initializeMemoryTable();
		initializeRegisters();
		
		this.setVisible(true);
	}
	
	private void initializeMemoryTable() {
		String[] columnNames = {"Address", "Content"};
		final DefaultTableModel table = new MemoryTable(columnNames, 0);
		final JTable dataTable = new JTable(table);
		JScrollPane scrollPane = new JScrollPane(dataTable);
		this.getContentPane().add(scrollPane);
		
		ram.addOperativeMemoryChangeListener(new OperativeMemoryChangeListener() {
			
			@Override
			public void memoryChanged(int track, int idx, String value) {
				table.removeRow(track * 10 + idx);
				table.insertRow(track * 10 + idx, new Object[]{track * 10 + idx, value});
			}
			
		});
		
		new Timer().schedule(new TimerTask() {          
		    @Override
		    public void run() {
		    	for (int i = 0; i < 10; i++) {
		    		ram.occupyMemory(12, i, "JE12");
		    	}
		    }
		}, 4000);
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
