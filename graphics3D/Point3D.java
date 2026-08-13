package graphics3D;

/*//可変の
class MutPoint3D{
}*/

//3D座標をdoubleで表す
public class Point3D{
  public final double x,y,z;

  public Point3D(){
    x = y = z = 0;
  }

  public Point3D(double x,double y,double z){
    this.x = x;
    this.y = y;
    this.z = z;
  }

  //単位ベクトルを返す
  public Point3D normalize(){
    double norm = Math.sqrt(dot(this));

    return new Point3D(x / norm,y / norm,z / norm);
  }

  //内積
  public double dot(Point3D p){
    return x * p.x + y * p.y + z * p.z;
  }

  //ベクトル加算した新しいインスタンスを返す
  public Point3D add(Point3D p){
    return new Point3D(x + p.x,y + p.y,z + p.z);
  }

  //減算
  public Point3D sub(Point3D p){
    return new Point3D(x - p.x,y - p.y,z - p.z);
  }

  @Override
  public String toString(){
    return "Point3D(" + x + " , " + y + " , " + z + ")";
  }
}