public class ChooseDataSong extends ChooseData{
    final int id;
    
    ChooseDataSong(int id){
        this.id=id;
    }
    
    public int getId(){
      return id;
    }
    public String getFname(){
        return CDFLibrary.getSongFile(id);
    }
    public String getName(){
        return CDFLibrary.getSongName(id);
    }
    public int getGrade(){
        return CDFLibrary.getSongEval(id);
    }
    public String getComment(){
        return CDFLibrary.getSongComment(id);
    }
    public String getDate(){
        return CDFLibrary.getSongDate(id);
    }
    public String getWith(){
        return CDFLibrary.getSongWith(id);
    }
    public String getScore(){
        return CDFLibrary.getSongScore(id);
    }
    public boolean isSong(){return true;}
}