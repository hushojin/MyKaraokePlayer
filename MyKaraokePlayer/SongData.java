public class SongData extends Data{
    final int id;
    
    SongData(int id){
        this.id=id;
    }
    
    public int getId(){
      return id;
    }
    public String getFname(){
        return DataLibrary.getSongFile(id);
    }
    public String getName(){
        return DataLibrary.getSongName(id);
    }
    public int getGrade(){
        return DataLibrary.getSongEval(id);
    }
    public String getComment(){
        return DataLibrary.getSongComment(id);
    }
    public String getDate(){
        return DataLibrary.getSongDate(id);
    }
    public String getWith(){
        return DataLibrary.getSongWith(id);
    }
    public String getScore(){
        return DataLibrary.getSongScore(id);
    }
    public boolean isSong(){return true;}
}