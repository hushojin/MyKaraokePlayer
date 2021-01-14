import java.io.*;
import javax.swing.Timer;
import javax.sound.sampled.*;
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
    public void setData(Data d){
        if(d.isSong()){
            setSong((SongData)d);
        }else{
            setList((ListData)d);
        }
        player.play();
    }
    public void setSong(SongData sd){
        hasSong=true;
        this.sd=sd;
        player.setFile(new File(SONG_FILE_DIRECTORY,sd.getFname()));
    }
    public void setList(ListData ld){
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
    public void start(){
        player.play();
    }
    public void pause(){
        player.pause();
    }
    
    private static class FilePlayer{
        private Clip clip;
        private Timer timer=new Timer(100,(e)->running());
        
        void setFile(File file){
            stop();
            try(AudioInputStream ais= AudioSystem.getAudioInputStream(file) ){
                AudioFormat baseFormat = ais.getFormat();
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,baseFormat.getSampleRate(),16,baseFormat.getChannels(),baseFormat.getChannels() * 2,baseFormat.getSampleRate(),false);
                try( AudioInputStream dais = AudioSystem.getAudioInputStream(targetFormat,ais) ){
                    DataLine.Info info = new DataLine.Info(Clip.class,targetFormat);
                    clip = (Clip)AudioSystem.getLine(info);
                    clip.addLineListener(
                        (e)->{
                            if(e.getType()==LineEvent.Type.START){
                                timer.start();
                            }else if(e.getType()==LineEvent.Type.STOP){
                                timer.stop();
                                if(clip.getFramePosition()>=clip.getFrameLength()){
                                    endPlay();
                                }
                            }
                        }
                    );
                    clip.open(dais);
                }catch(LineUnavailableException | IOException e){
                    clip=null;
                    throw e;
                }
            }catch(LineUnavailableException | UnsupportedAudioFileException | IOException e){
                e.printStackTrace();
            }
        }
        
        void stop(){
            if(clip==null){
                return;
            }
            if(clip.isOpen()){
                clip.stop();
                clip.close();
            }
            clip=null;
            manipulated();
        }
        void play(){
            if(clip==null){
                return;
            }
            clip.start();
            manipulated();
        }
        void pause(){
            if(clip==null){
                return;
            }
            clip.stop();
            manipulated();
        }
        
        void setFramePosition(int frame){
            if(clip==null){
                return;
            }
            clip.setFramePosition(frame);
            manipulated();
        }
        void shiftSecond(int sec){
            if(clip==null){
                return;
            }
            clip.setMicrosecondPosition(clip.getMicrosecondPosition()+sec*1000000L);
            manipulated();
        }
        boolean startFromSecond(int sec){
           return clip.getMicrosecondPosition() < sec*1000000L; 
        }
        public PlayState getPlayState(){
          if(clip==null){
              return null;
          }
          return new PlayState(clip.isRunning(),clip.getFrameLength(),clip.getFramePosition(),clip.getMicrosecondLength(),clip.getMicrosecondPosition());
        }
        public void manipulated(){}
        public void endPlay(){}
        public void running(){}
    }
}