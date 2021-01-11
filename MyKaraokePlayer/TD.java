import java.awt.*;

public class TD extends Panel implements PlayStateDisplay{
    Label label = new Label("0:00/0:00",Label.CENTER);
    PlayState state;
    
    TD(){
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
    String secToTime(long a){
        return a/60+":"+(((a%60)<10)?"0":"")+a%60;
    }
    
    public void setPlayState(PlayState state){
        this.state=state;
    }
}