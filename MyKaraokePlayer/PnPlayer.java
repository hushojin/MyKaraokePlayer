import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PnPlayer extends Panel implements Runnable{
    PnPlayer pnp = this;
    CDPlayer cdp;
    Button btPlay = new Button("||");
    SB sb = new SB(this);
    TD td = new TD(this);
    Button btPrev = new Button("|<");
    Button btNext = new Button(">|");
    int maxFrame;
    int valueFrame;
    int maxMicro;
    int valueMicro;
    
    PnPlayer(CDPlayer cdp){
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
        this.cdp = cdp;
        
        add(btPlay);
        btPlay.setEnabled(false);
        btPlay.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(cdp.isRunning()){
                        cdp.stop();
                    }
                    else{
                        cdp.start();
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
                    if(cdp.getMicrosecondPosition()<2000000){
                        if(!cdp.prev()){
                            cdp.setFramePosition(0);
                        }
                    }
                    else{
                        cdp.setFramePosition(0);
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
                    cdp.next();
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
        cdp.setFramePosition(frame);
    }
    
    public void setValue(int value){
        valueFrame = value;
        valueMicro = (int)((double)maxMicro*(double)valueFrame/(double)maxFrame);
        sb.repaint();
        td.repaint();
    }
    
    void henka(int frameMax,int microMax){
        btPlay.setEnabled(true);
        if(cdp.isRunning()){
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
            cdp.setMicrosecondPosition(cdp.getMicrosecondPosition()-5000000);//外面
            valueMicro = valueMicro>=5000000 ? valueMicro-5000000 : 0;
            valueFrame = (int)((double)maxFrame*valueMicro/maxMicro);
            sb.repaint();
            td.repaint();
        }
        if(e.getKeyCode()==e.VK_RIGHT){
            cdp.setMicrosecondPosition(cdp.getMicrosecondPosition()+5000000);//外面
            valueMicro = valueMicro+5000000<=maxMicro ? valueMicro+5000000 : maxMicro;
            valueFrame = (int)((double)maxFrame*valueMicro/maxMicro);
            sb.repaint();
            td.repaint();
        }
        if(e.getKeyCode()==e.VK_SPACE){
            if(btPlay.isEnabled()){
                if(cdp.isRunning()){
                    cdp.stop();
                }
                else{
                    cdp.start();
                }
            }
        }
    }
    
    public void run(){
        while(cdp.isRunning()){
            try {                               // 例外処理
                Thread.sleep(100);               // 100ミリ秒休眠
            } catch (InterruptedException e) {
            }
            valueFrame = (int)cdp.getFramePosition();
            valueMicro = (int)cdp.getMicrosecondPosition();
            sb.repaint();
            td.repaint();
        }
    }
}