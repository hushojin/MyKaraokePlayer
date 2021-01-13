import java.awt.*;
import java.awt.event.*;
import datalibrary.*;

public class MKPMenuBar extends MenuBar {
    private Menu createMenu = new Menu("�V�K�쐬");
    private MenuItem SongCreateItem = new MenuItem("��(S)",new MenuShortcut(KeyEvent.VK_S, true));
    private MenuItem ListCreateItem = new MenuItem("�v���C���X�g(L)",new MenuShortcut(KeyEvent.VK_L, true));
    
    MKPMenuBar(DataLibrary library){
        add(createMenu);
        createMenu.add(SongCreateItem);
        SongCreateItem.addActionListener((e)->new SongDataCreateDialog(library));
        createMenu.add(ListCreateItem);
        ListCreateItem.addActionListener((e)->new ListDataCreateDialog(library));
    }
}