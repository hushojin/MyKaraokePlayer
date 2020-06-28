import java.awt.*;
import java.awt.event.*;

public class TD extends Panel {
    PnPlayer pnp;
    Label label = new Label("0:00/0:00",Label.CENTER);
    
    TD(PnPlayer pnp){
        setLayout(null);
        this.pnp = pnp;
        add(label);
        label.setBounds(1,3,65,23);
        
    }
    
    public void paint(Graphics g){
        label.setText(txgen(pnp.valueMicro,pnp.maxMicro));
    }
    String txgen(int a,int b){
        return a/60000000+":"+
        (((a/1000000%60)<10)?"0":"")+a/1000000%60+"/"+
        b/60000000+":"+
        (((b/1000000%60)<10)?"0":"")+b/1000000%60;
    }
}