import java.awt.*;
import java.awt.event.*;

public class MB extends MenuBar {
    Gamen parent;
    Menu mnNew = new Menu("新規作成");
    MenuItem miNewSong = new MenuItem("曲(S)",new MenuShortcut(KeyEvent.VK_S, true));
    MenuItem miNewList = new MenuItem("プレイリスト(L)",new MenuShortcut(KeyEvent.VK_L, true));
    
    MB(Gamen parent){
        this.parent = parent;
        add(mnNew);
         mnNew.add(miNewSong);
          miNewSong.addActionListener(
              new ActionListener(){
                  public void actionPerformed(ActionEvent e){
                      new CDSNewer(parent);
                  }
              }
          );
         mnNew.add(miNewList);
          miNewList.addActionListener(
              new ActionListener(){
                  public void actionPerformed(ActionEvent e){
                      new CDLNewer(parent);
                  }
              }
          );
    }
}