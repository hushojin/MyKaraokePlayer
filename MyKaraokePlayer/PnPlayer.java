import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PnPlayer extends Panel implements PlayStateDisplay{
    Button btPlay = new Button("||");
    SB sb = new SB();
    TD td = new TD();
    Button btPrev = new Button("|<");
    Button btNext = new Button(">|");
    PlayState state;
    
    PnPlayer(){
        setBackground(Color.gray);
        setLayout(null);
        add(btPlay);
        btPlay.setEnabled(false);
        btPlay.addActionListener(
            (e)->{
                if(state.isPlaying){
                    CDPlayer.pause();
                }else{
                    CDPlayer.start();
                }
            }
        );
        btPlay.addKeyListener(
            new KeyAdapter(){
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode()==KeyEvent.VK_LEFT){
                        CDPlayer.shiftSecond(-5);
                    }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                        CDPlayer.shiftSecond(5);
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
                CDPlayer.prev();
                if(btPlay.isEnabled()){
                    btPlay.requestFocusInWindow();
                }
            }
        );
        add(btNext);
        btNext.setFocusable(false);
        btNext.addActionListener(
            (e)->{
                CDPlayer.next();
                if(btPlay.isEnabled()){
                    btPlay.requestFocusInWindow();
                }
            }
        );
        CDPlayer.setPlayStateDisplay(this);
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
            CDPlayer.shiftSecond(-5);
        }else if(e.getKeyCode()==e.VK_RIGHT){
            CDPlayer.shiftSecond(5);
        }else if(e.getKeyCode()==e.VK_SPACE){
            if(btPlay.isEnabled()){
                if(state.isPlaying){
                    CDPlayer.pause();
                }else{
                    CDPlayer.start();
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