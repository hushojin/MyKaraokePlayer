import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ListPanel extends Panel{
    Data[] cda;
    TextField searchField=new TextField();
    Panel pnSwitch = new Panel(null);
    CheckboxGroup cbg = new CheckboxGroup();
    Checkbox cbSong = new Checkbox("単曲",cbg,true);
    Checkbox cbPlayList = new Checkbox("プレイリスト",cbg,false);
    
    List list;
    PopListInfo popList;
    PopSongInfo popSong;
    
    ListPanel(){
        setLayout(null);
        setBackground(Color.black);
        cda=DataLibrary.getMatchCDSs("");
        list=createListFrom(cda);
        add(list);
        searchField.addTextListener((e)->update(searchField.getText()));
        add(searchField);
        pnSwitch.setBackground(Color.gray);
        pnSwitch.add(cbSong);
        pnSwitch.add(cbPlayList);
        add(pnSwitch);
        ItemListener il=(e)->update(cbSong.getState());
        cbSong.addItemListener(il);
        cbPlayList.addItemListener(il);
        addMouseMotionListener(
            new MouseMotionListener(){
                public void mouseDragged(MouseEvent e){
                    hideListInfo();
                    hideSongInfo();
                }
                public void mouseMoved(MouseEvent e){
                    hideListInfo();
                    hideSongInfo();
                }
            }
        );
    }
    
    public void selfLayout(){
        searchField.setBounds(40,5,getWidth()-80,20);
        pnSwitch.setBounds(0,searchField.getY()+searchField.getHeight()+5,getWidth(),30);
        cbSong.setBounds(40,5,50,20);
        cbPlayList.setBounds(90,5,100,20);
        list.setBounds(40,pnSwitch.getY()+pnSwitch.getHeight()+5,getWidth()-80,getHeight()-70);
    }

    void update(boolean isSong){
        update(searchField.getText(),isSong);
    }
    void update(String searchField){
        update(searchField,cbSong.getState());
    }
    void update(String searchField,boolean isSong){
        if(isSong){
            cda=DataLibrary.getMatchCDSs(searchField);
        }else{
            cda=DataLibrary.getMatchCDLs(searchField);
        }
        List l=createListFrom(cda);
        remove(list);
        list=l;
        add(list);
        selfLayout();
    }
    
    private List createListFrom(Data[] cda){
        List res=new List();
        for(Data cd:cda){
            res.add(cd.getName());
        }
        res.addActionListener(
            (e)->{
                DataPlayer.setCD(cda[list.getSelectedIndex()]);
                transferFocus();
            }
        );
        res.addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    if(e.getButton()==MouseEvent.BUTTON3&&list.getSelectedIndex()!=-1){
                        Data cd=cda[list.getSelectedIndex()];
                        if(cd.isSong()){
                            showSongInfo(MouseInfo.getPointerInfo().getLocation(),(SongData)cd);
                        }else{
                            showListInfo(MouseInfo.getPointerInfo().getLocation(),(ListData)cd);
                        }
                    }
                }
            }
        );
        return res;
    }
    
    private void showSongInfo(Point p,SongData cds){
        hideSongInfo();
        popSong = new PopSongInfo(p,cds);
        popSong.show();
    }
    private void showListInfo(Point p,ListData cdl){
        hideListInfo();
        popList = new PopListInfo(p,cdl);
        popList.show();
    }
    private void hideSongInfo(){
        if(popSong != null){
            popSong.hide();
            popSong = null;
        }
    }
    private void hideListInfo(){
        if(popList != null){
            popList.hide();
            popList = null;
        }
    }
    
    class PopSongInfo extends Popup{
        public PopSongInfo(Point p,SongData cds){
            super(ListPanel.this,new PnSongInfo(cds),(int)(p.getX()),(int)(p.getY()));
        }
    }
    class PopListInfo extends Popup{
        public PopListInfo(Point p,ListData cdl){
            super(ListPanel.this,new ListPanelInfo(cdl),(int)(p.getX()),(int)(p.getY()));
        }
    }
    
    class PnSongInfo extends Panel{
        PnSongInfo(SongData cds){
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            Label name = new Label(cds.getName());
            Label grade = new Label(String.valueOf(cds.getGrade()));
            Label comment = new Label(cds.getComment());
            Label date = new Label(cds.getDate());
            Label with = new Label(cds.getWith());
            Label score = new Label(cds.getScore());
            Button btEdit = new Button("編集");
            btEdit.addActionListener(
                (e)->{
                    hideListInfo();
                    hideSongInfo();
                    new SongDataEditDialog(cds);
                }
            );
            add(name);
            add(grade);
            add(comment);
            add(date);
            add(with);
            add(score);
            add(btEdit);
        }
    }
    class ListPanelInfo extends Panel{
        ListPanelInfo(ListData cdl){
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            SongData[] cdsa=cdl.getSongs();
            List songList = new List(cdl.size(),false);
            songList.addMouseListener(
                new MouseAdapter(){
                    public void mouseReleased(MouseEvent e){
                        if(e.getButton()==MouseEvent.BUTTON3&&songList.getSelectedIndex()!=-1){
                            SongData cds=cdsa[songList.getSelectedIndex()];
                            showSongInfo(MouseInfo.getPointerInfo().getLocation(),cds);
                        }
                    }
                }
            );
            for(SongData cds:cdsa){
                songList.add(cds.getFname());
            }
            Button editButton = new Button("編集");
            editButton.addActionListener(
                (e)->{
                    hideListInfo();
                    hideSongInfo();
                    new ListDataEditDialog(cdl);
                }
            );
            Button deleteButton = new Button("削除");
            deleteButton.addActionListener(
                (e)->{
                    hideListInfo();
                    hideSongInfo();
                    DataLibrary.deleteList(cdl.getId());
                }
            );
            add(songList);
            add(editButton);
            add(deleteButton);
        }
    }
}