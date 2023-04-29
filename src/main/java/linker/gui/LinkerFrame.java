package linker.gui;

import linker.impl.Model;
import linker.impl.Symlinker;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;

public class LinkerFrame extends JFrame{
    private JPanel linkerPanel;
    private JButton selectFilesButton;
    private JButton whereToLinkButton;
    private JButton linkButton;
    private JTable table;
    private JScrollPane scrollablePane;

    public LinkerFrame(String title) {
        this();
        this.setTitle(title);
    }
    public LinkerFrame() {
        this.add(linkerPanel);
        this.setMinimumSize(new Dimension(500, 400));
        this.setLocation(10,20);
        this.getContentPane().setBackground(Color.black);
        setFeels();
        this.model = new Model();
        table.setModel(model);
        customizeComponents();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setFeels(){
        UIManager.LookAndFeelInfo[] feels = UIManager.getInstalledLookAndFeels();
        if(DEBUG){
            for(UIManager.LookAndFeelInfo feel : feels){
                System.out.println(feel.getClassName());
            }
        }
        setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    }
    public void setLookAndFeel(String lookAndFeel){
        try{
            UIManager.setLookAndFeel(lookAndFeel);
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                UnsupportedLookAndFeelException e){
            e.printStackTrace();
        }
    }


    public void customizeComponents(){
        scrollablePane.setBackground(Color.BLACK);
        TableColumnModel tableModel = table.getColumnModel();
        //tableModel.getColumn(0).setPreferredWidth(120);
        //tableModel.getColumn(1).setPreferredWidth(30);
        //tableModel.getColumn(2).setWidth(420);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        for (int i = 0; i < model.COLUMNS; i++){
            //cell editor applies per column
            tableModel.getColumn(i).setCellEditor(newCellEditor());
        }
        table.setRowHeight(20);
        table.setFillsViewportHeight(true);
        table.setOpaque(true);
        table.getTableHeader().setFont(cellFont);
        table.getTableHeader().setBackground(Colors.black);
        table.getTableHeader().setForeground(Colors.darkerWhite);
        table.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                int code = e.getKeyCode();
                //delete or backspace || code == 8
                if(code == 127){
                    int selectedIndex = table.getSelectedRow();
                    if(selectedIndex == -1){
                        return;
                    }
                    //TODO remove many selected rows at once
                    model.removeRow(selectedIndex);
                    //reselect after deletion, instead of triggering editing due to framework quirks
                    if(selectedIndex == 0)
                        selectedIndex = 1;
                    table.getSelectionModel().setSelectionInterval(selectedIndex-1, selectedIndex-1);
                    e.consume();
                }
            }
        });

        selectFilesButton.addActionListener(e -> {
            File[] selected = chooser.chooseFiles();
            for (File file : selected){
                String original = file.toString();
                model.addRow("", file.getName(), original);
            }
            for (int i = 0; i < model.COLUMNS; i++){
                tableModel.getColumn(i).sizeWidthToFit();
            }
            this.repaint();
            if(DEBUG){
                System.out.println("SELECTED: " + selected.length);
                System.out.println(Arrays.toString(selected));
                table.setSize(table.getPreferredSize());
            }
        });

        whereToLinkButton.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();
            File selectedDir = chooser.chooseDirectory();
            if(selectedDir == null){
                return;
            }
            String dirPath = selectedDir.toString();
            //if no rows are present add
            if(selectedRows.length == 0 && table.getRowCount() == 0){
                model.addRow(dirPath, "", "");
                return;
            }
            System.out.println(Arrays.toString(selectedRows));
            //attach to chosen rows the same directory
            if(selectedRows.length > 0){
                for (int selectedRow : selectedRows){
                    table.setValueAt(dirPath, selectedRow, 0);
                }
                return;
            }
            //attach to all empty - if no selections but entries exist
            if(table.getRowCount() > 0){
                for (int row = 0; row < model.getRowCount(); row++){
                    String val = (String) table.getValueAt(row, 0);
                    if(val.isEmpty()){
                        table.setValueAt(dirPath, row, 0);
                    }
                }
            }

        });
        linkButton.addActionListener(e -> {
            int success = 0, fail = 0;
            int rows = model.getRowCount();
            for(int row = 0; row < rows; row++){
                String[] paths = new String[model.COLUMNS];
                for (int col = 0; col < model.COLUMNS; col++){
                    paths[col] = (String) model.getValueAt(row, col);
                }
                if(paths[0].isEmpty() || paths[1].isEmpty() || paths[2].isEmpty()){
                    fail++;
                    continue;
                }
                boolean res = Symlinker.createSymlink(paths[0], paths[1], paths[2]);

                if(res)
                    success++;
                else
                    fail++;
            }
            System.out.println("Success:"+success);
            System.out.println("Fail:"+fail);
        });

        //System.out.println(originalsGui);
    }


    private DefaultCellEditor newCellEditor(){
        JTextField field = new JTextField();
        field.setFont(cellFont);
        return new DefaultCellEditor(field);
    }

    private final FileChooser chooser = new FileChooser();
    private final Model model;
    private final Font cellFont = new Font("Consolas", Font.ITALIC, 16);

    private static final boolean DEBUG = true;
}

