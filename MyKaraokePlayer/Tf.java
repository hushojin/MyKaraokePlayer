import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Tf extends TextField{
    PnList pnl;
    Tf(PnList pnl){
        this.pnl = pnl;
        addTextListener(
            new TextListener(){
                public void textValueChanged(TextEvent e){
                    pnl.update(getText());
                }
            }
        );
    }
    
}