import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PnPlayer extends Panel implements Runnable{
    PnPlayer pnp = this;
    Button btPlay = new Button("||");
    SB sb = new SB(this);
    TD td = new TD(this);
    Button btPrev = new Button("|<");
    Button btNext = new Button(">|");
    int maxFrame;
    int valueFrame;
    int maxMicro;
    int valueMicro;
    
    PnPlayer(){
        maxFrame = 1;
        setLayout(null);
//        setFocusable(true);
        addKeyListener(
            new KeyAdapter(){
                public void keyPressed(KeyEvent e){
                    pnp.ke(e);
                }
            }
        );
        
        add(btPlay);
        btPlay.setEnabled(false);
        btPlay.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(CDPlayer.isRunning()){
                        CDPlayer.stop();
                    }
                    else{
                        CDPlayer.start();
                    }
                }
            }
        );
        btPlay.addKeyListener(
            new KeyAdapter(){
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode()==e.VK_LEFT || e.getKeyCode()==e.VK_RIGHT){
                        pnp.ke(e);
                    }
                }
            }
        );
        add(sb);sb.setBackground(Color.white);
        sb.setFocusable(false);
        add(td);td.setBackground(Color.white);
        add(btPrev);
        btPrev.setFocusable(false);
        btPrev.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(CDPlayer.getMicrosecondPosition()<2000000){
                        if(!CDPlayer.prev()){
                            CDPlayer.setFramePosition(0);
                        }
                    }
                    else{
                        CDPlayer.setFramePosition(0);
                    }
                    if(btPlay.isEnabled()){
                        btPlay.requestFocusInWindow();
                    }
                }
            }
        );
        add(btNext);
        btNext.setFocusable(false);
        btNext.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    CDPlayer.next();
                    if(btPlay.isEnabled()){
                        btPlay.requestFocusInWindow();
                    }
                }
            }
        );
    }
    
    public void haichi(){
        btPlay.setBounds(this.getParent().getInsets().left+5,5,getHeight()-10,getHeight()-10);
        if(getWidth()>260){
            btNext.setBounds(getWidth()-this.getParent().getInsets().right-5-getHeight()+10,5,getHeight()-10,getHeight()-10);
            btPrev.setBounds(btNext.getX()-getHeight()+10,5,getHeight()-10,getHeight()-10);
            btNext.setVisible(true);
            btPrev.setVisible(true);
        }
        else{
            btPrev.setBounds(getWidth()-this.getParent().getInsets().right-5,0,0,0);
            btNext.setVisible(false);
            btPrev.setVisible(false);
        }
        td.setBounds(btPrev.getX()-67-5,(getHeight()-27)/2,67,27);
        sb.setBounds(btPlay.getX()+btPlay.getWidth()+5,5,td.getX()-btPlay.getX()-btPlay.getWidth()-10,getHeight() -10);
         sb.repaint();
    }
    
    void setFramePosition(int frame){
        CDPlayer.setFramePosition(frame);
    }
    
    public void setValue(int value){
        valueFrame = value;
        valueMicro = (int)((double)maxMicro*(double)valueFrame/(double)maxFrame);
        sb.repaint();
        td.repaint();
    }
    
    void henka(int frameMax,int microMax){
        btPlay.setEnabled(true);
        if(CDPlayer.isRunning()){
            btPlay.setLabel("||");
            Thread trd = new Thread(this);
            trd.start();
        }
        else{
            btPlay.setLabel(">");
        }
        this.maxFrame = frameMax;
        this.maxMicro = microMax;
        //未記入
    }
    
    
    public void ke(KeyEvent e){
        if(e.getKeyCode()==e.VK_LEFT){
            CDPlayer.setMicrosecondPosition(CDPlayer.getMicrosecondPosition()-5000000);//外面
            valueMicro = valueMicro>=5000000 ? valueMicro-5000000 : 0;
            valueFrame = (int)((double)maxFrame*valueMicro/maxMicro);
            sb.repaint();
            td.repaint();
        }
        if(e.getKeyCode()==e.VK_RIGHT){
            CDPlayer.setMicrosecondPosition(CDPlayer.getMicrosecondPosition()+5000000);//外面
            valueMicro = valueMicro+5000000<=maxMicro ? valueMicro+5000000 : maxMicro;
            valueFrame = (int)((double)maxFrame*valueMicro/maxMicro);
            sb.repaint();
            td.repaint();
        }
        if(e.getKeyCode()==e.VK_SPACE){
            if(btPlay.isEnabled()){
                if(CDPlayer.isRunning()){
                    CDPlayer.stop();
                }
                else{
                    CDPlayer.start();
                }
            }
        }
    }
    
    public void run(){
        while(CDPlayer.isRunning()){
            try {                               // 例外処理
                Thread.sleep(100);               // 100ミリ秒休眠
            } catch (InterruptedException e) {
            }
            valueFrame = (int)CDPlayer.getFramePosition();
            valueMicro = (int)CDPlayer.getMicrosecondPosition();
            sb.repaint();
            td.repaint();
        }
    }
}