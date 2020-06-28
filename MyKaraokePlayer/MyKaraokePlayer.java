  public class MyKaraokePlayer{
    public static void main(String args[]) {
        CDFLibrary cdfl = new CDFLibrary();
        CDPlayer cdp = new CDPlayer(cdfl);
        PnList pnlist = new PnList(cdp,cdfl);
        cdfl.setPnList(pnlist);
        PnPlayer pnplayer = new PnPlayer(cdp);
        cdp.setPnPlayer(pnplayer);
        Gamen gam = new Gamen(pnlist,pnplayer);
        cdp.setGamen(gam);
        MB mb = new MB(cdp,cdfl,gam);
        gam.setMB(mb);
        gam.haichi();
    }
}