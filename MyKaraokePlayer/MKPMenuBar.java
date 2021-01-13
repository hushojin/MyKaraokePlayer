import java.awt.*;
import java.awt.event.*;

public class MKPMenuBar extends MenuBar {
    Menu mnNew = new Menu("新規作成");
    MenuItem miNewSong = new MenuItem("曲(S)",new MenuShortcut(KeyEvent.VK_S, true));
    MenuItem miNewList = new MenuItem("プレイリスト(L)",new MenuShortcut(KeyEvent.VK_L, true));
    
    MKPMenuBar(){
        add(mnNew);
        mnNew.add(miNewSong);
        miNewSong.addActionListener((e)->new SongDataCreateDialog());
        mnNew.add(miNewList);
        miNewList.addActionListener((e)->new ListDataCreateDialog());
    }
}