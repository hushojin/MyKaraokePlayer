public class ListData extends Data{
    final int id;
    ListData(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    public String getName(){
        return CDFLibrary.getListName(id);
    }
    public int size(){
      return CDFLibrary.getListSize(id);
    }
    
    public SongData[] getSongs(){
        return CDFLibrary.getListSongs(id);
    }
    
    public boolean isSong(){return false;}
}