class PlayState{
    final public boolean isPlaying;
    final public int maxFrame;
    final public int framePosition;
    final public long maxMicro;
    final public long microPosition;
    PlayState(boolean isPlaying,int maxFrame,int framePosition,long maxMicro,long microPosition){
        this.isPlaying=isPlaying;
        this.maxFrame=maxFrame;
        this.framePosition=framePosition;
        this.maxMicro=maxMicro;
        this.microPosition=microPosition;
    }
    public String toString(){
      return isPlaying+
      "\n["+framePosition+"/"+maxFrame+"]"+
      "\n["+microPosition+"/"+maxMicro+"]";
    }
}