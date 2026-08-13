package graphics3D;

//3次元の頂点を格納する
//ベクトルの計算も一部格納する
public class Point3D{
  public double x;
  public double y;
  public double z;

  //長さ1に正規化する
  public Point3D normalize(){
    double len = Math.sqrt(x * x + y * y + z * z);
    if(len == 0){
      return new Point3D(0, 0, 0);
    }
    double scale = 1.0 / len;
    return new Point3D(x * scale, y * scale, z * scale);
  }

  //内積
  public double dot(Point3D vec){
    return x * vec.x + y * vec.y + z * vec.z;
  }

  public Point3D(Point3D p3){
    this.x = p3.x;
    this.y = p3.y;
    this.z = p3.z;
  }

  public Point3D(double x,double y,double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}