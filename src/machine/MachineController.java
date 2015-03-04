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
			final JTextField regField = new JTextField(cpu.getValue(reg));
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
				registersMap.get(reg).setText((String)evt.getNewValue());
			}
			
		});
		
		JButton stepButton = new JButton("Step");
		stepButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				step();
			}
			
		});
		registersPanel.add(stepButton);
		
		getContentPane().add(registersPanel);
	}
	
	public void step() {
		int addr = Integer.parseInt(cpu.getValue(Register.PC));
		int track = addr / 10;
		int idx = addr % 10;
		System.out.println(track + " " + idx);
		String cmd = ram.getMemory(track, idx);
		cpu.interpretCmd(cmd);
		System.out.println(cmd);
	}
	
	private void demo() {
		
		ram.occupyMemory(1, 1, "GO000");
		
//		 Timer timer = new Timer();
//		 timer.schedule(new TimerTask() {
//			
//			@Override
//			public void run() {
//				step();
//			}
//			
//		}, 1000, 1000);
		
		new Timer().schedule(new TimerTask() {          
		    @Override
		    public void run() {
		    	for (int i = 0; i < 10; i++) {
		    		ram.occupyMemory(12, i, "JE12");
		    	}
		    }
		}, 4000);
		
		new Timer().schedule(new TimerTask() {          
		    @Override
		    public void run() {
		    	cpu.setPtr("23");    
		    }
		}, 2000);
		
		new Timer().schedule(new TimerTask() {          
		    @Override
		    public void run() {
		    	cpu.setMode("1");  
		    }
		}, 2000);
	}
	
}
