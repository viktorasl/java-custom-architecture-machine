package machine;

import javax.swing.table.DefaultTableModel;

public class OperativeMemoryTable extends DefaultTableModel {

	private static final long serialVersionUID = -8052788013480634187L;

	public OperativeMemoryTable(String[] columnNames, OperativeMemory ram){
		super(columnNames, 0);
		
		for (int i = 0; i < ram.getTracksCount(); i++) {
			for (int j = 0; j < ram.getTrackSize(); j++) {
				this.addRow(new Object[]{i * ram.getTrackSize() + j, ram.getMemory(i, j)});
			}
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {                
		return false;
	}
	
}
