package datalibrary;

public class SongData extends Data{
    private final DataLibrary library;
    private final int id;
    
    SongData(DataLibrary library,int id){
        this.library=library;
        this.id = id;
    }
    
    public int getId(){
      return id;
    }
    public String getFname(){
        return library.getSongFile(id);
    }
    public String getName(){
        return library.getSongName(id);
    }
    public int getGrade(){
        return library.getSongEval(id);
    }
    public String getComment(){
        return library.getSongComment(id);
    }
    public String getDate(){
        return library.getSongDate(id);
    }
    public String getWith(){
        return library.getSongWith(id);
    }
    public String getScore(){
        return library.getSongScore(id);
    }
    public boolean isSong(){return true;}
}