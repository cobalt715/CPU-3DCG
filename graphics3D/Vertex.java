package graphics3D;

//座標、uv座標、法線などPolygon3Dの頂点の役割をする
public class Vertex{
  public Point3D pos;
  public Point3D absPos;//絶対座標
  public double u,v;
  public Point3D normal;


  public Vertex(Point3D pos,Point3D normal){
    this.pos = pos;
    this.normal = normal;
  }

  public Vertex(Point3D pos,double u,double v,Point3D normal){
    this.pos = pos;
    this.u = u;
    this.v = v;
    this.normal = normal;
  }
}