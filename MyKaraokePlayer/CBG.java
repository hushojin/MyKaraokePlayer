import java.awt.*;
import java.awt.event.*;

public class CBG extends CheckboxGroup{
    PnList pnl;
    Checkbox cbSong = new Checkbox("�P��",this,true);
    Checkbox cbPlayList = new Checkbox("�v���C���X�g",this,false);
    
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
