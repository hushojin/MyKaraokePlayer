import java.awt.*;
import java.awt.event.*;

public class CBG extends CheckboxGroup{
    PnList pnl;
    Checkbox cbSong = new Checkbox("単曲",this,true);
    Checkbox cbPlayList = new Checkbox("プレイリスト",this,false);
    
    CBG(PnList pnl){
        this.pnl = pnl;
        ItemListener il=new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                pnl.update(cbSong.getState());
            }
        };
        cbSong.addItemListener(il);
        cbPlayList.addItemListener(il);
    }
    
    boolean isSong(){
        return cbSong.getState();
    }
}
