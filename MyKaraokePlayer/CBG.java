import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CBG extends CheckboxGroup{
    PnList pnl;
    Checkbox cbSong = new Checkbox("単曲",this,true);
    Checkbox cbPlayList = new Checkbox("プレイリスト",this,false);
    
    
    CBG(PnList pnl){
        this.pnl = pnl;
        cbSong.addItemListener(new ItListener());
        cbPlayList.addItemListener(new ItListener());
    }
    
    class ItListener implements ItemListener{
        public void itemStateChanged(ItemEvent e){
            pnl.update(cbSong.getState());
        }
    }
    
    boolean isSong(){
        return cbSong.getState();
    }
}
