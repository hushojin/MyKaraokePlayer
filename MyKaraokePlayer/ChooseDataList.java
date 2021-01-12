public class ChooseDataList extends ChooseData{
    final int id;
    ChooseDataList(int id){
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
    
    public ChooseDataSong[] getSongs(){
        return CDFLibrary.getSongsInList(id);
    }
    
    public boolean isSong(){return false;}
}