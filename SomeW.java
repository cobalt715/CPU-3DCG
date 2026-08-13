import java.util.*;
import graphics3D.*;

public class SomeW{
  public List<Polygon3D> pols = new ArrayList<>();

  public SomeW(Point3D point, int r){
    r = Math.abs(r);
    double x1 = 0;
    double y1 = point.y - r;
    double z1 = 0;
    double x2 = 0;
    double y2 = point.y + r;
    double z2 = 0;
    for(int i = 0;i < 3600;i += 200){
      x1 = Utility.SIN[i] * r;
      z1 = Utility.COS[i] * r;
      x2 = Utility.SIN[i + 200] * r;
      z2 = Utility.COS[i + 200] * r;

      Polygon3D p1 = new Polygon3D(new Point3D(x1,y1,z1),new Point3D(x2,y1,z2),new Point3D(x1,y2,z1));
      p1.setNormal(new Point3D(x1,0,z1),new Point3D(x2,0,z2),new Point3D(x1,0,z1));

      Polygon3D p2 = new Polygon3D(new Point3D(x1,y2,z1),new Point3D(x2,y1,z2),new Point3D(x2,y2,z2));
      p2.setNormal(new Point3D(x1,0,z1),new Point3D(x2,0,z2),new Point3D(x2,0,z2));

      pols.add(p1);
      pols.add(p2);
    }

    for(Polygon3D polygon:pols){
      for(Vertex point3:polygon.verts){
        point3.pos.x += point.x;
        point3.pos.z += point.z;
      }
    }
  }
}