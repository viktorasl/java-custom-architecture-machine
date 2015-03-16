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

			@Override
			public void memoryExecuted(int track, int idx) {
				int row = track * ram.getTrackSize() + idx;
				dataTable.changeSelection(row, 0, false, false);
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
		
		ram.occupyMemory(98, 8, "0");
		
		// Interrupt handler (increasing value in [999] by 1 & setting TI := 10)
		ram.occupyMemory(98, 9, "GV001");
		ram.occupyMemory(99, 0, "AD999");
		ram.occupyMemory(99, 1, "MM999");
		ram.occupyMemory(99, 2, "LDTI");
		ram.occupyMemory(99, 3, "CP988");
		ram.occupyMemory(99, 4, "JG996");
		ram.occupyMemory(99, 5, "STI10");
		ram.occupyMemory(99, 6, "SSI0");
		ram.occupyMemory(99, 7, "SPI0");
		ram.occupyMemory(99, 8, "RESTR");

		// Real machine setup
		ram.occupyMemory(0, 0, "IH989");
		ram.occupyMemory(0, 1, "STI10");
		ram.occupyMemory(0, 2, "SP970");
		ram.occupyMemory(0, 3, "SPT01");
		ram.occupyMemory(0, 4, "VM130");

		// VM paging
		ram.occupyMemory(1, 0, "13");
		ram.occupyMemory(1, 1, "14");
		ram.occupyMemory(1, 2, "15");
		
		// Program
		ram.occupyMemory(13, 0, "CL026");
		ram.occupyMemory(13, 1, "xxxxx");
		ram.occupyMemory(13, 2, "MM021");
		ram.occupyMemory(13, 3, "GV099");
		ram.occupyMemory(13, 4, "AD020");
		ram.occupyMemory(13, 5, "CP022");
		ram.occupyMemory(13, 6, "HT");
		ram.occupyMemory(13, 7, "JL000");
		ram.occupyMemory(13, 8, "JG000");
		ram.occupyMemory(13, 9, "GO000");
		
		// Prorgram data
		ram.occupyMemory(15, 0, "12");
		ram.occupyMemory(15, 1, "110");
		ram.occupyMemory(15, 2, "2");
		
		// Procedure (adding 2 to GR)
		ram.occupyMemory(15, 6, "AD122");
		ram.occupyMemory(15, 7, "RT");

	}
	
}
