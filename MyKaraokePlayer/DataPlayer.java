import java.io.*;
import datalibrary.*;

public class DataPlayer{
    private static final String SONG_FILE_DIRECTORY="C:/\\Users\\Owner\\Desktop\\Karaokewavs\\";
    private StringDisplay sDisp;
    private PlayStateDisplay psDisp;
    private boolean hasSong;
    private SongData sd = null;
    private ListData ld = null;
    private SongData[] sds = null;
    private int index;
    private FilePlayer player;
    
    public DataPlayer(){
        player=new FilePlayer(){
            public void manipulated(){
                psDisplayUpdate();
            }
            public void running(){
                psDisplayUpdate();
            }
            public void endPlay(){
                if(hasSong){
                    player.setFramePosition(0);
                }else{
                    next();
                }
            }
        };
    }
    
    public void setStringDisplay(StringDisplay sDisp){
        this.sDisp = sDisp;
    }
    public void setPlayStateDisplay(PlayStateDisplay psDisp){
        this.psDisp = psDisp;
    }
    private void psDisplayUpdate(){
        if(psDisp==null){
            return;
        }
        psDisp.setPlayState(player.getPlayState());
    }
    public void setSongData(SongData sd){
        hasSong=true;
        this.sd=sd;
        player.setFile(new File(SONG_FILE_DIRECTORY,sd.getFname()));
        sDisp.setString(sd.getName()+" MyKarakePlayer");
        player.play();
    }
    public void setListData(ListData ld){
        hasSong=false;
        this.ld=ld;
        this.sds=ld.getSongs();
        index = 0;
        if(sds.length<1){
            sDisp.setString("0/0 MyKarakePlayer");
            return;
        }
        player.setFile(new File(SONG_FILE_DIRECTORY,sds[index].getFname()));
        sDisp.setString((index+1)+"/"+sds.length+" "+sds[index].getName()+" MyKarakePlayer");
        player.play();
    }
    public void next(){
        if( !hasSong && index+1 < sds.length ){
            index++;
            SongData sd=sds[index];
            player.setFile(new File(SONG_FILE_DIRECTORY,sd.getFname()));
            player.play();
            sDisp.setString((index+1)+"/"+sds.length+" "+sd.getName()+" MyKarakePlayer");
        }
    }
    public void prev(){
        if(!hasSong && 1<= index && player.startFromSecond(2)){
            index--;
            SongData sd=sds[index];
            player.setFile(new File(SONG_FILE_DIRECTORY,sd.getFname()));
            player.play();
            sDisp.setString((index+1)+"/"+sds.length+" "+sd.getName()+" MyKarakePlayer");
        }else{
            setFramePosition(0);
        }
    }
    
    public void setFramePosition(int frame){
        player.setFramePosition(frame);
        psDisplayUpdate();
    }
    public void shiftSecond(int sec){
        player.shiftSecond(sec);
        psDisplayUpdate();
    }
    public void play(){
        player.play();
    }
    public void pause(){
        player.pause();
    }
}