import java.awt.*;

public class TimeDisplay extends Panel implements PlayStateDisplay{
    private Label label = new Label("0:00/0:00",Label.CENTER);
    PlayState state;
    
    public TimeDisplay(){
        setLayout(null);
        setBackground(Color.white);
        add(label);
        label.setBounds(1,3,65,23);
    }
    
    public void paint(Graphics g){
        if(state==null){
          return;
        }
        label.setText(secToTime(state.microPosition/1000000)+"/"+secToTime(state.maxMicro/1000000));
    }
    static private String secToTime(long sec){
        return sec/60+":"+(((sec%60)<10)?"0":"")+sec%60;
    }
    
    public void setPlayState(PlayState state){
        this.state=state;
    }
}