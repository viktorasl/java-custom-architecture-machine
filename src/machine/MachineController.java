package machine;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class MachineController extends JFrame {
	
	private static final long serialVersionUID = 3795986469706534809L;
	private Processor cpu;
	private OperativeMemory ram;
	private ChannelSystem chn;
	private Printer printer;
	private Keyboard keyboard;
	
	public static void main(String[] argv) {
		new MachineController();
	}
	
	public MachineController() {
		ram = new OperativeMemory(100, 10);
		printer = new Printer(3, 30);
		keyboard = new Keyboard(5);
		HardDrive hdd = new HardDrive(100, 10);
		chn = new ChannelSystem(hdd, printer, keyboard);
		cpu = new Processor(ram, chn);
		
		getContentPane().setLayout(new GridLayout(1, 3));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Machine");
		setSize(1200, 600);
		setResizable(false);
		
		getContentPane().add(initializeMemoryTable(ram));
		getContentPane().add(initializeMemoryTable(hdd));
		
		JPanel registersPanel = new JPanel();
		registersPanel.setLayout(new BoxLayout(registersPanel, BoxLayout.PAGE_AXIS));
		
		registersPanel.add(initializeRegisters());
		registersPanel.add(initializeChannelSystem());
		
		JButton stepButton = new JButton("Step");
		stepButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cpu.step();
			}
			
		});
		
		registersPanel.add(stepButton);
		
		getContentPane().add(registersPanel);
		
		setVisible(true);
		
		inputDemo();
	}
	
	private JScrollPane initializeMemoryTable(MemoryListable memory) {
		String[] columnNames = {"Address", "Content"};
		final DefaultTableModel table = new OperativeMemoryTable(columnNames, memory);
		final JTable dataTable = new JTable(table);
		JScrollPane scrollPane = new JScrollPane(dataTable);
		
		scrollPane.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                memory.getTitle(),
                TitledBorder.CENTER,
                TitledBorder.TOP));
		
		memory.addOperativeMemoryChangeListener(new OperativeMemoryChangeListener() {
			
			@Override
			public void memoryChanged(int track, int idx, String value) {
				int i = track * memory.getTrackSize() + idx;
				table.removeRow(i);
				table.insertRow(i, new Object[]{i, value});
			}

			@Override
			public void memoryExecuted(int track, int idx) {
				int row = track * memory.getTrackSize() + idx;
				dataTable.changeSelection(row, 0, false, false);
			}
			
		});
		
		return scrollPane;
	}
	
	private JPanel initializeRegisters() {
		JPanel cpuRegistersPanel = new JPanel();
		cpuRegistersPanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Processor",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		
		final Map<ProcessorRegister, JTextField> registersMap = new HashMap<ProcessorRegister, JTextField>();
		
		for (ProcessorRegister reg : ProcessorRegister.values()) {
			JPanel registerPanel = new JPanel();
			JLabel regLabel = new JLabel(reg.name().toUpperCase());
			final JTextField regField = new JTextField(String.format("%5d", cpu.getValue(reg)));
			registersMap.put(reg, regField);
			regField.setEditable(false);
			registerPanel.add(regLabel);
			registerPanel.add(regField);
			cpuRegistersPanel.add(registerPanel);
		}
		
		cpu.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ProcessorRegister reg = ProcessorRegister.valueOf(evt.getPropertyName());
				registersMap.get(reg).setText(String.format("%5d", (int)evt.getNewValue()));
			}
			
		});
		
		return cpuRegistersPanel;
	}
	
	private JPanel initializeChannelSystem() {
		JPanel registersPanel = new JPanel();
		registersPanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                "Channel System",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		
		final Map<ChannelSystemRegister, JTextField> registersMap = new HashMap<ChannelSystemRegister, JTextField>();
		
		for (ChannelSystemRegister reg : ChannelSystemRegister.values()) {
			JPanel registerPanel = new JPanel();
			JLabel regLabel = new JLabel(reg.name().toUpperCase());
			final JTextField regField = new JTextField(String.format("%5d", chn.getValue(reg)));
			registersMap.put(reg, regField);
			regField.setEditable(false);
			registerPanel.add(regLabel);
			registerPanel.add(regField);
			registersPanel.add(registerPanel);
		}
		
		chn.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ChannelSystemRegister reg = ChannelSystemRegister.valueOf(evt.getPropertyName());
				registersMap.get(reg).setText(String.format("%5d", (int)evt.getNewValue()));
			}
			
		});
		
		registersPanel.add(printer);
		registersPanel.add(keyboard);
		
		return registersPanel;
	}
	
	private void inputDemo() {
		ram.occupyMemory(0, 0, "DV1");
		ram.occupyMemory(0, 1, "IO1");
		ram.occupyMemory(0, 2, "DV1");
		ram.occupyMemory(0, 3, "LDSI");
		ram.occupyMemory(0, 4, "CP020");
		ram.occupyMemory(0, 5, "JE007");
		ram.occupyMemory(0, 6, "GO003");
		ram.occupyMemory(0, 7, "GV021");
		ram.occupyMemory(0, 8, "SA");
		ram.occupyMemory(0, 9, "XCHG");
		ram.occupyMemory(1, 0, "SSI0");
		ram.occupyMemory(1, 1, "GO003");
		
		ram.occupyMemory(2, 0, "4");
	}
	
	private void demo() {
		
		// Interrupt flag compares
		ram.occupyMemory(94, 6, "0");
		ram.occupyMemory(94, 7, "1");
		ram.occupyMemory(94, 8, "2");
		ram.occupyMemory(94, 9, "3");
		
		// Interrupt handler (increasing value in [999] by 1 & setting TI := 10)
		ram.occupyMemory(95, 0, "GV001");
		ram.occupyMemory(95, 1, "AD999");
		ram.occupyMemory(95, 2, "MM999");
		ram.occupyMemory(95, 3, "LDTI");
		ram.occupyMemory(95, 4, "CP946");
		ram.occupyMemory(95, 5, "JG960");
		ram.occupyMemory(95, 6, "STI10");
		ram.occupyMemory(95, 7, "GO960");
		
		// Checking I/O
		ram.occupyMemory(96, 0, "DV0");
		ram.occupyMemory(96, 1, "LDSI");
		
		// Output handler
		ram.occupyMemory(96, 2, "CP948");
		ram.occupyMemory(96, 3, "JE966");
		ram.occupyMemory(96, 4, "GO970");
		ram.occupyMemory(96, 6, "IO0");
		ram.occupyMemory(96, 7, "GO980");
		
		// Input handler
		ram.occupyMemory(97, 0, "CP949");
		ram.occupyMemory(97, 1, "JL983");
		ram.occupyMemory(97, 2, "IO1");
		ram.occupyMemory(97, 3, "GO980");
		
		// I/O handler
		ram.occupyMemory(98, 0, "SA");
		ram.occupyMemory(98, 1, "XCHG");
		
		ram.occupyMemory(98, 2, "SSI0");
		ram.occupyMemory(98, 3, "SPI0");
		ram.occupyMemory(98, 4, "RESTR");

		// Real machine setup
		ram.occupyMemory(0, 0, "IH950");
		ram.occupyMemory(0, 1, "STI10");
		ram.occupyMemory(0, 2, "SP900");
		ram.occupyMemory(0, 3, "SPT01");
		ram.occupyMemory(0, 4, "GV130");
		ram.occupyMemory(0, 5, "VM");

		// VM paging
		ram.occupyMemory(1, 0, "13");
		ram.occupyMemory(1, 1, "14");
		ram.occupyMemory(1, 2, "15");
		ram.occupyMemory(1, 3, "16");
		
		// Program
		ram.occupyMemory(13, 0, "CL026");
		ram.occupyMemory(13, 1, "xxxxx");
		ram.occupyMemory(13, 2, "MM021");
		ram.occupyMemory(13, 3, "GV099");
		ram.occupyMemory(13, 4, "AD020");
		ram.occupyMemory(13, 5, "CP022");
		
		ram.occupyMemory(13, 6, "GV030");
		ram.occupyMemory(13, 7, "PT");
		ram.occupyMemory(13, 8, "AD023");
		ram.occupyMemory(13, 9, "CP024");
		ram.occupyMemory(14, 0, "JL007");
		
		ram.occupyMemory(14, 1, "HT");
		
		// Prorgram data
		ram.occupyMemory(15, 0, "12");
		ram.occupyMemory(15, 1, "110");
		ram.occupyMemory(15, 2, "2");
		ram.occupyMemory(15, 3, "1");
		ram.occupyMemory(15, 4, "035");
		
		ram.occupyMemory(16, 0, "Kanal");
		ram.occupyMemory(16, 1, "u ire");
		ram.occupyMemory(16, 2, "ngini");
		ram.occupyMemory(16, 3, "o ban");
		ram.occupyMemory(16, 4, "dymas");
		
		// Procedure (adding 2 to GR)
		ram.occupyMemory(15, 6, "AD122");
		ram.occupyMemory(15, 7, "RT");
		
	}
	
}
