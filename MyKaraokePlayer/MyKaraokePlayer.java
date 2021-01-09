public class MyKaraokePlayer{
    public static void main(String args[]) {
        PnPlayer pnPlayer = new PnPlayer();
        CDPlayer.setPnPlayer(pnPlayer);
        Gamen gam = new Gamen(pnPlayer);
        CDPlayer.setGamen(gam);
        MB mb = new MB(gam);
        gam.setMB(mb);
        gam.haichi();
    }
}