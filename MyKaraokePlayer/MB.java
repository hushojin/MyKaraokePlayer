import java.awt.*;
import java.awt.event.*;

public class MB extends MenuBar {
    Gamen parent;
    Menu mnNew = new Menu("�V�K�쐬");
    MenuItem miNewSong = new MenuItem("��(S)",new MenuShortcut(KeyEvent.VK_S, true));
    MenuItem miNewList = new MenuItem("�v���C���X�g(L)",new MenuShortcut(KeyEvent.VK_L, true));
    
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