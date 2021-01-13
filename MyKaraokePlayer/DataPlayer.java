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
        player.start();
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
            player.start();
            sDisp.setString((index+1)+"/"+sds.length+" "+sd.getName()+" MyKarakePlayer");
        }
    }
    public void prev(){
        if(!hasSong && 1<= index && player.startFromSecond(2)){
            index--;
            SongData sd=sds[index];
            player.setFile(new File(SONG_FILE_DIRECTORY,sd.getFname()));
            player.start();
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
        player.start();
    }
    public void pause(){
        player.pause();
    }
/*
    public void endPlay(){
        if(hasSong){
            player.setFramePosition(0);
         }else{
            next();
         }
    }
*/
    
    private static class FilePlayer{
        private AudioInputStream ais;
        private AudioInputStream dais;
        private AudioFormat af;
        private Clip clip;
        private Timer timer=new Timer(100,(e)->running());
        FilePlayer(){}
        
        public void setFile(File file){
            stop();
            prepare(file);
        }
        private void prepare(File file){
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                AudioFormat baseFormat = ais.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,baseFormat.getSampleRate(),16,baseFormat.getChannels(),baseFormat.getChannels() * 2,baseFormat.getSampleRate(),false);
                dais = AudioSystem.getAudioInputStream(decodedFormat,ais);
                rawplayclip(decodedFormat,dais);
                ais.close();
            }catch (Exception e){
                System.out.println(e);
            }
        }
        private void rawplayclip(AudioFormat targetFormat, AudioInputStream din){
            try {
                DataLine.Info info = new DataLine.Info(Clip.class, targetFormat);
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
                clip.open(din);
            } catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
            }finally {
                try {
                    din.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        public void stop(){
            try{
                if(clip!=null&&clip.isOpen()){
                    clip.stop();
                    clip.close();
                    clip=null;
                }
                if(ais!=null){
                    ais.close();
                }
                if(dais!=null){
                    dais.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        public void start(){
            if(clip==null){
                return;
            }
            clip.start();
            manipulated();
        }
        public void pause(){
            if(clip==null){
                return;
            }
            clip.stop();
            manipulated();
        }
        
        public void shiftSecond(int sec){
            if(clip==null){
                return;
            }
            clip.setMicrosecondPosition(clip.getMicrosecondPosition()+sec*1000000L);
            manipulated();
        }
        public boolean startFromSecond(int sec){
           return clip.getMicrosecondPosition() < sec*1000000L; 
        }
        public void setFramePosition(int frame){
            if(clip==null){
                return;
            }
            clip.setFramePosition(frame);
            manipulated();
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