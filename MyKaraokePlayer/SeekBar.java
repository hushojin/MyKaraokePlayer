import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SeekBar extends Panel implements PlayStateDisplay{
    private PlayState state;
    private Label label;
    private Popup pop;
    public SeekBar(DataPlayer player){
        setBackground(Color.white);
        setFocusable(false);
        label = new Label("0:00",Label.CENTER);
        label.setSize(55,23);
        label.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));
        addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    double cursorFrame = 1.0*(e.getX()-5)/(getWidth()-10)*state.maxFrame;
                    cursorFrame=Math.max(Math.min(cursorFrame,state.maxFrame),0);
                    player.setFramePosition((int)cursorFrame);
                }
                public void mouseEntered(MouseEvent e){
                    Point p=e.getLocationOnScreen();
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
                    double cursorMicro = 1.0*(e.getX()-5)*state.maxMicro/(getWidth()-10);
                    cursorMicro=Math.max(Math.min(cursorMicro,state.maxMicro),0)/1000000;
                    label.setText(secToTime((int)cursorMicro));
                }
                public void mouseMoved(MouseEvent e){
                    if(state!=null){
                       double cursorMicro = 1.0*(e.getX()-5)*state.maxMicro/(getWidth()-10);
                       cursorMicro=Math.max(Math.min(cursorMicro,state.maxMicro),0)/1000000;
                       label.setText(secToTime((int)cursorMicro));
                    }
                }
            }
        );
    }
    static private String secToTime(int sec){
      return (sec/60)+":"+(sec%60<10?"0":"")+(sec%60);
    }
    
    public void paint(Graphics g){
        g.drawLine(5,getHeight()/2,getWidth()-5,getHeight()/2);//横線
        int x = 5;
        if(state!=null){
            x = (int) ( ( state.framePosition * (getWidth()-10.0) / state.maxFrame ) + 5.0  );
        }
        g.drawLine((int)x,5,(int)x,getHeight()-5);//縦線
    }
    
    public void setPlayState(PlayState state){
        this.state=state;
        repaint();
    }
}