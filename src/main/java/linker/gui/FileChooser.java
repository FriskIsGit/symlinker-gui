package linker.gui;

import linker.features.FileDropHandler;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileChooser extends JFileChooser{
    public static Font font = new Font("Calibri", Font.BOLD, 15);
    public FileChooser(String defaultPath){
        super(defaultPath);
        UIManager.put("Panel.background", Colors.black);
        //setTransferHandler(new FileDropHandler());
        setOpenWithDetails();
        //by default off
        setDragEnabled(true);
        this.setMultiSelectionEnabled(true);
        setColorsAndFont(this.getComponents());
        this.setPreferredSize(new Dimension(700, 700));
    }

    public void setColorsAndFont(Component[] comps){
        for (Component comp : comps){
            if (comp instanceof Container){
                Container cont = (Container) comp;
                setColorsAndFont(cont.getComponents());
            }
            comp.setBackground(Colors.black);
            comp.setForeground(Colors.darkerWhite);
            comp.setFont(font);
        }
    }

    public FileChooser(){
        this("/");
    }

    private void setOpenWithDetails(){
        //FilePane.VIEWTYPE_DETAILS;
        Action details = this.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);
    }

    public File[] chooseFiles(){
        this.setMultiSelectionEnabled(true);
        this.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = this.showDialog(null, "Select files to link");
        if(result == JFileChooser.CANCEL_OPTION){
            return new File[0];
        }
        return this.getSelectedFiles();
    }
    public File chooseDirectory(){
        this.setMultiSelectionEnabled(false);
        this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //this.setAcceptAllFileFilterUsed(true);
        int result = this.showDialog(null, "Select directory link");
        if(result == JFileChooser.CANCEL_OPTION){
            return null;
        }
        return this.getSelectedFile();
    }
    public File[] chooseDirectories(){
        this.setMultiSelectionEnabled(true);
        this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //this.setAcceptAllFileFilterUsed(true);
        int result = this.showDialog(null, "Select directory links");
        if(result == JFileChooser.CANCEL_OPTION){
            return new File[0];
        }
        return this.getSelectedFiles();
    }
}
