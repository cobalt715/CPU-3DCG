package graphics3D;

//三角関数を0.1度ごとにテーブル化する
public final class Utility{
  public static final int RESOLUTION = 3601;//0.1度刻み
  public static final double[] SIN = new double[RESOLUTION];
  public static final double[] COS = new double[RESOLUTION];

  //線形補完　始点、終点、補完位置の順に受け取る
  public static double lerp(double s,double e,double t){
    return s + (e - s) * t;
  }

  //明度を計算し色に対して補正を掛ける
  public static int computeLighting(int c,double intensity){
    int a = (c >>> 24) & 0xFF;
    int r = (c >>> 16) & 0xFF;
    int g = (c >>> 8)  & 0xFF;
    int b = c & 0xFF;

    //intensityを掛けて0-255にクランプ
    r = Math.max(0,Math.min(255,(int)(r * intensity)));
    g = Math.max(0,Math.min(255,(int)(g * intensity)));
    b = Math.max(0,Math.min(255,(int)(b * intensity)));

    // 元のアルファ値と合わせて 32bit に戻す
    return (a << 24) | (r << 16) | (g << 8) | b;
  }

  static{
    for (int i = 0; i < RESOLUTION; i++) {
      double rad = Math.toRadians(i / 10.0);
      SIN[i] = Math.sin(rad);
      COS[i] = Math.cos(rad);
    }
  }

  private Utility(){}//インスタンス化禁止
}