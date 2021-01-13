import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ListPanel extends Panel{
    private Data[] datas;
    private TextField searchField=new TextField();
    private Panel checkboxPanel = new Panel(null);
    private CheckboxGroup cbg = new CheckboxGroup();
    private Checkbox songCheckbox = new Checkbox("単曲",cbg,true);
    private Checkbox listCheckbox = new Checkbox("プレイリスト",cbg,false);
    private List list;
    private PopListInfo popList;
    private PopSongInfo popSong;
    private DataPlayer player;
    
    public ListPanel(DataPlayer player){
        this.player=player;
        setLayout(null);
        setBackground(Color.black);
        datas=DataLibrary.getMatchSongs("");
        list=createListFrom(datas);
        add(list);
        searchField.addTextListener((e)->update());
        add(searchField);
        checkboxPanel.setBackground(Color.gray);
        checkboxPanel.add(songCheckbox);
        checkboxPanel.add(listCheckbox);
        add(checkboxPanel);
        ItemListener il=(e)->update();
        songCheckbox.addItemListener(il);
        listCheckbox.addItemListener(il);
        addMouseMotionListener(
            new MouseMotionListener(){
                public void mouseDragged(MouseEvent e){
                    if(popSong != null){
                        popSong.hide();
                    }
                    if(popList != null){
                        popList.hide();
                    }
                }
                public void mouseMoved(MouseEvent e){
                    if(popSong != null){
                        popSong.hide();
                    }
                    if(popList != null){
                        popList.hide();
                    }
                }
            }
        );
    }
    
    public void selfLayout(){
        searchField.setBounds(40,5,getWidth()-80,20);
        checkboxPanel.setBounds(0,searchField.getY()+searchField.getHeight()+5,getWidth(),30);
        songCheckbox.setBounds(40,5,50,20);
        listCheckbox.setBounds(90,5,100,20);
        list.setBounds(40,checkboxPanel.getY()+checkboxPanel.getHeight()+5,getWidth()-80,getHeight()-70);
    }
    
    private void update(){
        if(songCheckbox.getState()){
            datas=DataLibrary.getMatchSongs(searchField.getText());
        }else{
            datas=DataLibrary.getMatchLists(searchField.getText());
        }
        List l=createListFrom(datas);
        remove(list);
        list=l;
        add(list);
        selfLayout();
    }
    
    private List createListFrom(Data[] datas){
        List res=new List();
        for(Data cd:datas){
            res.add(cd.getName());
        }
        res.addActionListener(
            (e)->{
                player.setData(datas[list.getSelectedIndex()]);
                transferFocus();
            }
        );
        res.addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    if(e.getButton()==MouseEvent.BUTTON3&&list.getSelectedIndex()!=-1){
                        Data cd=datas[list.getSelectedIndex()];
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
        if(popSong != null){
            popSong.hide();
        }
        popSong = new PopSongInfo(p,cds);
        popSong.show();
    }
    private void showListInfo(Point p,ListData cdl){
        if(popList != null){
            popList.hide();
        }
        popList = new PopListInfo(p,cdl);
        popList.show();
    }
    
    private class PopSongInfo extends Popup{
        public PopSongInfo(Point p,SongData cds){
            super(ListPanel.this,new PnSongInfo(cds),(int)(p.getX()),(int)(p.getY()));
        }
    }
    private class PopListInfo extends Popup{
        public PopListInfo(Point p,ListData cdl){
            super(ListPanel.this,new ListPanelInfo(cdl),(int)(p.getX()),(int)(p.getY()));
        }
    }
    
    private class PnSongInfo extends Panel{
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
                    if(popSong != null){
                        popSong.hide();
                    }
                    if(popList != null){
                        popList.hide();
                    }
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
    private class ListPanelInfo extends Panel{
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
                    if(popSong != null){
                        popSong.hide();
                    }
                    if(popList != null){
                        popList.hide();
                    }
                    new ListDataEditDialog(cdl);
                }
            );
            Button deleteButton = new Button("削除");
            deleteButton.addActionListener(
                (e)->{
                    if(popSong != null){
                        popSong.hide();
                    }
                    if(popList != null){
                        popList.hide();
                    }
                    DataLibrary.deleteList(cdl.getId());
                }
            );
            add(songList);
            add(editButton);
            add(deleteButton);
        }
    }
}