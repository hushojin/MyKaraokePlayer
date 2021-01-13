public class ListData extends Data{
    private final DataLibrary library;
    private final int id;
    ListData(DataLibrary library,int id){
        this.library=library;
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    public String getName(){
        return library.getListName(id);
    }
    public int size(){
      return library.getListSize(id);
    }
    
    public SongData[] getSongs(){
        return library.getListSongs(id);
    }
    
    public boolean isSong(){return false;}
}