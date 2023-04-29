package linker.impl;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractTableModel{
    public final int COLUMNS = 3;
    private final String[] columnNames = {"Directory link", "Link name", "Original path"};
    private final List<List<String>> columnData = new ArrayList<>(COLUMNS);
    public Model(){
        for (int i = 0; i < COLUMNS; i++){
            columnData.add(new ArrayList<>(8));
        }
    }

    @Override
    public int getRowCount(){
        return columnData.get(0).size();
    }

    @Override
    public int getColumnCount(){
        return COLUMNS;
    }

    @Override
    public String getColumnName(int columnIndex){
        if(columnIndex >= COLUMNS){
            throw new ArrayIndexOutOfBoundsException("Queried for index: " + columnIndex + " but COLUMNS=" + COLUMNS);
        }
        return columnNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int col){
        return true;
    }

    @Override
    public Object getValueAt(int row, int col){
        //System.out.println("Gets value from: " + row + " x " + col);
        return columnData.get(col).get(row);
    }

    @Override
    public void setValueAt(Object given, int row, int col){
        if(getRowCount() <= row){
            return;
        }
        List<String> column = columnData.get(col);
        column.set(row, (String) given);
        fireTableCellUpdated(row, col);
    }

    public void addRow(String ... threeStrings){
        if(threeStrings.length != COLUMNS){
            throw new IllegalArgumentException("Invalid number of values");
        }
        int valIndex = 0;
        for (int col = 0; col < COLUMNS; col++){
            List<String> column = columnData.get(col);
            column.add(threeStrings[valIndex++]);
        }
        int rows = getRowCount();
        fireTableRowsInserted(rows-1, rows-1);
    }
    public void removeRow(int row){
        if(getRowCount() <= row){
            return;
        }
        for(List<String> column : columnData){
            column.remove(row);
        }
        fireTableRowsDeleted(row, row);
    }
}
