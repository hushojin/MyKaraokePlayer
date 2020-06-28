import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SB extends Panel {
    Panel pn = this;
    PnPlayer pnp;
    Label label = new Label("0:00:00");
    Pop pop;
    SB(PnPlayer pnp){
        this.pnp = pnp;
        addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    double order = (getMousePosition().getX()-5)*pnp.maxFrame/(getWidth()-10);
                    if(order<0){
                        order=0;
                    }
                    else if(pnp.maxFrame<order){
                        order=pnp.maxFrame;
                    }
                    pnp.setFramePosition((int)order);
                    pnp.setValue((int)order);
                }
                public void mouseEntered(MouseEvent e){
                    pop = new Pop();
                    pop.show();
                }
                public void mouseExited(MouseEvent e){
                    pop.hide();
                }
            }
        );
        addMouseMotionListener(
            new MouseMotionAdapter(){
                public void mouseDrugged(MouseEvent e){
                    if(getMousePosition()!=null){
                       double order = (getMousePosition().getX()-5)*(double)pnp.maxMicro/(double)(getWidth()-10)/1000000;
                       if(order<0){
                           order=0;
                       }
                       else if((double)pnp.maxMicro<order*1000000){
                           order=(double)pnp.maxMicro/1000000.0;
                       }
                       label.setText(String.valueOf((int)order/60)+":"+(order%60<10?"0":"")+String.valueOf((int)order%60));
                    }else{System.out.print("アイウエオ");}
                }
                public void mouseMoved(MouseEvent e){
                    if(getMousePosition()!=null){
                       double order = (getMousePosition().getX()-5)*(double)pnp.maxMicro/(double)(getWidth()-10)/1000000;
                       if(order<0){
                           order=0;
                       }
                       else if((double)pnp.maxMicro<order*1000000){
                           order=(double)pnp.maxMicro/1000000.0;
                       }
                       label.setText(String.valueOf((int)order/60)+":"+(order%60<10?"0":"")+String.valueOf((int)order%60));
                    }else{System.out.print("やっぱりな");}
                }
            }
        );
    }
    
    class Pop extends Popup{
        public Pop(){
            super(pn,label,pn.getX()+pn.getParent().getX()+pn.getParent().getParent().getX(),pn.getY()+pn.getParent().getY()+pn.getParent().getParent().getY()-label.getHeight());
        }
    }
    
    public void paint(Graphics g){
        g.drawLine(5,getHeight()/2,getWidth()-5,getHeight()/2);//横線
        int x = (int) ( ( pnp.valueFrame * (getWidth()-10.0) / pnp.maxFrame ) + 5.0  );
        g.drawLine((int)x,5,(int)x,getHeight()-5);//縦線
    }
}