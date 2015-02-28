package machine;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MachineController {
	
	public static void main(String[] argv) {
		new MachineController();
	}
	
	public MachineController() {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new GridLayout(1, 3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Real Machine");
		frame.setSize(500, 600);
		
		// initializing files list table
		String[] columnNames = {"Address", "Content"};
		DefaultTableModel table = new MemoryTable(columnNames, 0);
		JTable dataTable = new JTable(table);
		
		JScrollPane scrollPane = new JScrollPane(dataTable);
		frame.getContentPane().add(scrollPane);
		
		Machine machine = new Machine();
		
		frame.setVisible(true);
	}
	
}
