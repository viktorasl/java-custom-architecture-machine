package machine;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
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
		ram = new OperativeMemory(100, 10);
		cpu = new Processor(ram);
		
		getContentPane().setLayout(new GridLayout(1, 3));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Real Machine");
		setSize(500, 300);
		
		initializeMemoryTable();
		initializeRegisters();
		
		setVisible(true);
		
		demo();
	}
	
	private void initializeMemoryTable() {
		String[] columnNames = {"Address", "Content"};
		final DefaultTableModel table = new MemoryTable(columnNames, ram);
		final JTable dataTable = new JTable(table);
		JScrollPane scrollPane = new JScrollPane(dataTable);
		getContentPane().add(scrollPane);
		
		ram.addOperativeMemoryChangeListener(new OperativeMemoryChangeListener() {
			
			@Override
			public void memoryChanged(int track, int idx, String value) {
				int i = track * ram.getTrackSize() + idx;
				table.removeRow(i);
				table.insertRow(i, new Object[]{i, value});
			}
			
		});
	}
	
	private void initializeRegisters() {
		JPanel registersPanel = new JPanel();
		
		final Map<Register, JTextField> registersMap = new HashMap<Register, JTextField>();
		
		for (Register reg : Register.values()) {
			JPanel registerPanel = new JPanel();
			JLabel regLabel = new JLabel(reg.name().toUpperCase());
			final JTextField regField = new JTextField(String.format("%5d", cpu.getValue(reg)));
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
				registersMap.get(reg).setText(String.format("%5d", (int)evt.getNewValue()));
			}
			
		});
		
		JButton stepButton = new JButton("Step");
		stepButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cpu.step();
			}
			
		});
		registersPanel.add(stepButton);
		
		getContentPane().add(registersPanel);
	}
	
	private void demo() {
		
		// Interrupt handler (increasing value in [999] by 1 & setting TI := 10)
		ram.occupyMemory(99, 0, "GV001");
		ram.occupyMemory(99, 1, "AD999");
		ram.occupyMemory(99, 2, "MM999");
		ram.occupyMemory(99, 3, "STI10");
		ram.occupyMemory(99, 4, "RESTR");
		
		// Procedure (adding 2 to GR)
		ram.occupyMemory(2, 2, "2");
		ram.occupyMemory(2, 0, "AD022");
		ram.occupyMemory(2, 1, "RT");
		
		// Real machine setup
		ram.occupyMemory(0, 0, "IH990");
		ram.occupyMemory(0, 1, "STI10");
		ram.occupyMemory(0, 2, "SP984");
		ram.occupyMemory(0, 3, "SMOD1");
		
		// Program
		ram.occupyMemory(0, 4, "CL020");
		ram.occupyMemory(0, 5, "MG030");
		ram.occupyMemory(0, 6, "MM031");
		ram.occupyMemory(0, 7, "GV099");
		ram.occupyMemory(0, 8, "AD030");
		ram.occupyMemory(0, 9, "CP032");
		ram.occupyMemory(1, 0, "JE006");
		ram.occupyMemory(1, 1, "JL006");
		ram.occupyMemory(1, 2, "JG006");
		ram.occupyMemory(2, 9, "GO006");
		
		ram.occupyMemory(3, 0, "12");
		ram.occupyMemory(3, 2, "110");
		
		new Timer().schedule(new TimerTask() {          
		    @Override
		    public void run() {
		    	for (int i = 0; i < 10; i++) {
		    		ram.occupyMemory(12, i, "JE12");
		    	}
		    }
		}, 4000);
	}
	
}
