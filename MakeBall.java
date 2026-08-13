import java.util.*;
import graphics3D.*;

public class MakeBall{
  public List<Polygon3D> pols = new ArrayList<>();

  public MakeBall(Point3D point, int r){
    Point3D upLeft = new Point3D(0,0,0);
    Point3D upRight = new Point3D(0,0,0);
    Point3D downLeft = new Point3D(0,0,0);
    Point3D downRight = new Point3D(0,0,0);

    for(int i = 900;i < 2700;i += 100){
      double upRadius = Utility.COS[i] * r;
      double downRadius = Utility.COS[i + 100] * r;

      double upY = Utility.SIN[i] * r;
      double downY = Utility.SIN[i + 100] * r;

      upLeft.y = upY;
      upRight.y = upY;
      downLeft.y = downY;
      downRight.y = downY;

      for(int j = 0;j < 3600;j += 100){
        int jplus = (j + 100) % 3600;
        upLeft.x = Utility.COS[j] * upRadius;
        upLeft.z = Utility.SIN[j] * upRadius;
        upRight.x = Utility.COS[jplus] * upRadius;
        upRight.z = Utility.SIN[jplus] * upRadius;

        downLeft.x = Utility.COS[j] * downRadius;
        downLeft.z = Utility.SIN[j] * downRadius;
        downRight.x = Utility.COS[jplus] * downRadius;
        downRight.z = Utility.SIN[jplus] * downRadius;

        Polygon3D po1 = new Polygon3D(new Point3D(upLeft),new Point3D(upRight),new Point3D(downRight));
        po1.setNormal(new Point3D(upLeft),new Point3D(upRight),new Point3D(downRight));

        Polygon3D po2 = new Polygon3D(new Point3D(upLeft),new Point3D(downRight),new Point3D(downLeft));
        po2.setNormal(new Point3D(upLeft),new Point3D(downRight),new Point3D(downLeft));

        pols.add(po1);
        pols.add(po2);
      }
    }

    for(Polygon3D polygon:pols){
      for(Vertex point3:polygon.verts){
        point3.pos.x += point.x;
        point3.pos.y += point.y;
        point3.pos.z += point.z;
      }
    }
  }
}