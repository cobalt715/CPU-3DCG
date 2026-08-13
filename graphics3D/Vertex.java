package graphics3D;

//頂点として必要な情報をいくつか保持する
public class Vertex{
  public final Point3D pos;
  public final Point3D normal;//フォンシェーディングのためこの頂点の法線を持つ
  public final double u,v;//テクスチャマッピングのためにテクスチャ上の座標を保持する

  public Vertex(Point3D pos,Point3D normal){
    this.pos = pos;
    this.normal = normal;
    u = v = 0;
  }

  public Vertex(Point3D pos,Point3D normal,double u,double v){
    this.pos = pos;
    this.normal = normal;
    this.u = u;
    this.v = v;
  }
}