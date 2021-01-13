import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SeekBar extends Panel implements PlayStateDisplay{
    PlayState state;
    Label label = new Label("0:00:00");
    Popup pop;
    SeekBar(){
        setBackground(Color.white);
        setFocusable(false);
        label.setSize(55,23);
        addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    double order = 1.0*(getMousePosition().getX()-5)/(getWidth()-10)*state.maxFrame;
                    order=order<0?0:state.maxFrame<order?state.maxFrame:order;
                    DataPlayer.setFramePosition((int)order);
                }
                public void mouseEntered(MouseEvent e){
                    Point p=getLocationOnScreen();
                    pop=PopupFactory.getSharedInstance().getPopup(SeekBar.this,label,p.x,p.y-label.getHeight());
                    pop.show();
                }
                public void mouseExited(MouseEvent e){
                    pop.hide();
                    pop=null;
                }
            }
        );
        addMouseMotionListener(
            new MouseMotionAdapter(){
                public void mouseDrugged(MouseEvent e){
                    if(getMousePosition()!=null&&state!=null){
                       double order = 1.0*(getMousePosition().getX()-5)*state.maxMicro/(getWidth()-10)/1000000;
                       order=order<0?0:state.maxFrame<order?state.maxFrame:order;
                       label.setText(secToTime((int)order));
                    }
                }
                public void mouseMoved(MouseEvent e){
                    if(getMousePosition()!=null&&state!=null){
                       double order = 1.0*(getMousePosition().getX()-5)*state.maxMicro/(getWidth()-10)/1000000;
                       order=order<0?0:state.maxMicro/1000000.0<order?state.maxMicro/1000000.0:order;
                       label.setText(secToTime((int)order));
                    }
                }
            }
        );
    }
    static private String secToTime(int sec){
      return (sec/3600)+":"+(sec/60%60<10?"0":"")+(sec/60%60)+":"+(sec%60<10?"0":"")+(sec%60);
    }
    
    public void paint(Graphics g){
        g.drawLine(5,getHeight()/2,getWidth()-5,getHeight()/2);//‰¡ü
        int x = 5;
        if(state!=null){
            x = (int) ( ( state.framePosition * (getWidth()-10.0) / state.maxFrame ) + 5.0  );
        }
        g.drawLine((int)x,5,(int)x,getHeight()-5);//cü
    }
    
    public void setPlayState(PlayState state){
        this.state=state;
    }
}