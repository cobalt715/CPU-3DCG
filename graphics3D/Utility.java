package graphics3D;

public class Utility{
  //線形補完　始点、終点、補完割合の順に受け取る
  public static double lerp(double s,double e,double t){
    return s + (e - s) * t;
  }

  //状態は嫌いです
  private Utility(){}
}