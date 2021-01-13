import java.awt.*;
import java.awt.event.*;

public class PlayerPanel extends Panel implements PlayStateDisplay{
    Button btPlay = new Button("||");
    SeekBar sb = new SeekBar();
    TimeDisplay td = new TimeDisplay();
    Button btPrev = new Button("|<");
    Button btNext = new Button(">|");
    PlayState state;
    
    PlayerPanel(){
        setBackground(Color.gray);
        setLayout(null);
        add(btPlay);
        btPlay.setEnabled(false);
        btPlay.addActionListener(
            (e)->{
                if(state.isPlaying){
                    DataPlayer.pause();
                }else{
                    DataPlayer.start();
                }
            }
        );
        btPlay.addKeyListener(
            new KeyAdapter(){
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode()==KeyEvent.VK_LEFT){
                        DataPlayer.shiftSecond(-5);
                    }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                        DataPlayer.shiftSecond(5);
                    }
                }
            }
        );
        add(sb);
        add(td);
        add(btPrev);
        btPrev.setFocusable(false);
        btPrev.addActionListener(
            (e)-> {
                DataPlayer.prev();
                if(btPlay.isEnabled()){
                    btPlay.requestFocusInWindow();
                }
            }
        );
        add(btNext);
        btNext.setFocusable(false);
        btNext.addActionListener(
            (e)->{
                DataPlayer.next();
                if(btPlay.isEnabled()){
                    btPlay.requestFocusInWindow();
                }
            }
        );
        DataPlayer.setPlayStateDisplay(this);
    }
    
    public void selfLayout(){
        btPlay.setBounds(getParent().getInsets().left+5,5,getHeight()-10,getHeight()-10);
        if(getWidth()>260){
            btNext.setBounds(getWidth()-getParent().getInsets().right-5-getHeight()+10,5,getHeight()-10,getHeight()-10);
            btPrev.setBounds(btNext.getX()-getHeight()+10,5,getHeight()-10,getHeight()-10);
            btNext.setVisible(true);
            btPrev.setVisible(true);
        }else{
            btPrev.setBounds(getWidth()-this.getParent().getInsets().right-5,0,0,0);
            btNext.setVisible(false);
            btPrev.setVisible(false);
        }
        td.setBounds(btPrev.getX()-67-5,(getHeight()-27)/2,67,27);
        sb.setBounds(btPlay.getX()+btPlay.getWidth()+5,5,td.getX()-btPlay.getX()-btPlay.getWidth()-10,getHeight() -10);
        sb.repaint();
    }
    public void ke(KeyEvent e){
        if(e.getKeyCode()==e.VK_LEFT){
            DataPlayer.shiftSecond(-5);
        }else if(e.getKeyCode()==e.VK_RIGHT){
            DataPlayer.shiftSecond(5);
        }else if(e.getKeyCode()==e.VK_SPACE){
            if(btPlay.isEnabled()){
                if(state.isPlaying){
                    DataPlayer.pause();
                }else{
                    DataPlayer.start();
                }
            }
        }
    }
    public void setPlayState(PlayState state){
        this.state=state;
        btPlay.setEnabled(true);
        if(state.isPlaying){
            btPlay.setLabel("||");
        }else{
            btPlay.setLabel(">");
        }
        sb.setPlayState(state);
        td.setPlayState(state);
        sb.repaint();
        td.repaint();
    }
}