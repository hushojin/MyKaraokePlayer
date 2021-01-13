import java.awt.*;
import java.awt.event.*;

public class PlayerPanel extends Panel implements PlayStateDisplay{
    private Button playButton = new Button("||");
    private SeekBar seekBar;
    private TimeDisplay timeDisplay = new TimeDisplay();
    private Button previousButton = new Button("|<");
    private Button nextButton = new Button(">|");
    private PlayState state;
    
    PlayerPanel(DataPlayer player){
        seekBar = new SeekBar(player);
        
        setBackground(Color.gray);
        setLayout(null);
        add(playButton);
        playButton.setEnabled(false);
        playButton.addActionListener(
            (e)->{
                if(state.isPlaying){
                    player.pause();
                }else{
                    player.start();
                }
            }
        );
        playButton.addKeyListener(
            new KeyAdapter(){
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode()==KeyEvent.VK_LEFT){
                        player.shiftSecond(-5);
                    }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                        player.shiftSecond(5);
                    }
                }
            }
        );
        add(seekBar);
        add(timeDisplay);
        add(previousButton);
        previousButton.setFocusable(false);
        previousButton.addActionListener(
            (e)-> {
                player.prev();
                if(playButton.isEnabled()){
                    playButton.requestFocusInWindow();
                }
            }
        );
        add(nextButton);
        nextButton.setFocusable(false);
        nextButton.addActionListener(
            (e)->{
                player.next();
                if(playButton.isEnabled()){
                    playButton.requestFocusInWindow();
                }
            }
        );
        player.setPlayStateDisplay(this);
    }
    
    public void selfLayout(){
        playButton.setBounds(getParent().getInsets().left+5,5,getHeight()-10,getHeight()-10);
        if(getWidth()>260){
            nextButton.setBounds(getWidth()-getParent().getInsets().right-5-getHeight()+10,5,getHeight()-10,getHeight()-10);
            previousButton.setBounds(nextButton.getX()-getHeight()+10,5,getHeight()-10,getHeight()-10);
            nextButton.setVisible(true);
            previousButton.setVisible(true);
        }else{
            previousButton.setBounds(getWidth()-this.getParent().getInsets().right-5,0,0,0);
            nextButton.setVisible(false);
            previousButton.setVisible(false);
        }
        timeDisplay.setBounds(previousButton.getX()-67-5,(getHeight()-27)/2,67,27);
        seekBar.setBounds(playButton.getX()+playButton.getWidth()+5,5,timeDisplay.getX()-playButton.getX()-playButton.getWidth()-10,getHeight() -10);
        seekBar.repaint();
    }
    public void setPlayState(PlayState state){
        this.state=state;
        if(state==null){
          playButton.setEnabled(false);
          previousButton.setEnabled(false);
          nextButton.setEnabled(false);
          seekBar.setEnabled(false);
        }
        playButton.setEnabled(true);
        if(state.isPlaying){
            playButton.setLabel("||");
        }else{
            playButton.setLabel(">");
        }
        seekBar.setPlayState(state);
        timeDisplay.setPlayState(state);
        seekBar.repaint();
        timeDisplay.repaint();
    }
}