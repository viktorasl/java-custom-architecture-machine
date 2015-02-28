package machine;

import javax.swing.table.DefaultTableModel;

public class MemoryTable extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8052788013480634187L;

	public MemoryTable(String[] columnNames, int i){
		super(columnNames, i);
		
		for (int idx = 0; idx < 1000;idx++) {
			this.addRow(new Object[]{idx, "0"});
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {                
		return false;
	}
	
}
