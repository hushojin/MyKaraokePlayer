public class ListData extends Data{
    final int id;
    ListData(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    public String getName(){
        return DataLibrary.getListName(id);
    }
    public int size(){
      return DataLibrary.getListSize(id);
    }
    
    public SongData[] getSongs(){
        return DataLibrary.getListSongs(id);
    }
    
    public boolean isSong(){return false;}
}