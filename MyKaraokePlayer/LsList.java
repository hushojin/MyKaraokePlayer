import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LsList extends List{
    ChooseData cda[];
    
    LsList(){
        addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    CDPlayer.set(cda[getSelectedIndex()]);
                    transferFocus();
                }
            }
        );
        addMouseListener(
            new MouseAdapter(){
                LsList lsl;
                public void mouseReleased(MouseEvent e){
                    if(e.getButton()==MouseEvent.BUTTON3&&lsl.getSelectedIndex()!=-1){
                        ((PnList)(lsl.getParent())).popupInfo(MouseInfo.getPointerInfo().getLocation(),cda[lsl.getSelectedIndex()]);
                    }
                }
                MouseAdapter setLsl(LsList lsl){
                    this.lsl = lsl;
                    return this;
                }
            }.setLsl(this)
        );
    }
    
    void lupdate(ChooseData cda[]){
        this.cda = cda;
        removeAll();
        for(ChooseData cd:cda){
            add(cd.getName());
        }
    }
}