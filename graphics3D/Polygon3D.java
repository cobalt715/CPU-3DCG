package graphics3D;

import java.awt.*;
import java.awt.image.*;

//3次元の3角形を格納する
public class Polygon3D{
  public Vertex[] verts;
  public BufferedImage texture;

  //デフォルトカラー
  private static Color c = Color.BLUE;

  //正規化した方線を返す
  public Point3D normal(){
    return normal(verts[0].pos,verts[1].pos,verts[2].pos);
  }

  public static Point3D normal(Point3D p1,Point3D p2,Point3D p3){
    Point3D vec1 = new Point3D(p2.x - p1.x,p2.y - p1.y,p2.z - p1.z);
    Point3D vec2 = new Point3D(p3.x - p1.x,p3.y - p1.y,p3.z - p1.z);
    double x = vec1.y * vec2.z - vec1.z * vec2.y;
    double y = vec1.z * vec2.x - vec1.x * vec2.z;
    double z = vec1.x * vec2.y - vec1.y * vec2.x;
    return new Point3D(x,y,z).normalize();
  }

  public void setAbsPos(Point3D p1,Point3D p2,Point3D p3){
    verts[0].absPos = p1;
    verts[1].absPos = p2;
    verts[2].absPos = p3;
  }

  public void setUV(double[] u,double[] v){
    verts[0].u = u[0];
    verts[0].v = v[0];
    verts[1].u = u[1];
    verts[1].v = v[1];
    verts[2].u = u[2];
    verts[2].v = v[2];
  }

  public void setNormal(Point3D p1,Point3D p2,Point3D p3){
    verts[0].normal = p1;
    verts[1].normal = p2;
    verts[2].normal = p3;
  }

  //テクスチャを単色で初期化する
  public static BufferedImage createSolidTexture(Color c){
    BufferedImage img = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
    img.setRGB(0,0,c.getRGB());
    return img;
  }

  public void setTexture(BufferedImage image){
    this.texture = image;
  }

  // 全てはこれに集約
  private Polygon3D(Point3D p1,Point3D p2,Point3D p3,BufferedImage texture,double[] u,double[] v,Point3D[] customNormals){
    if(customNormals == null){
      Point3D n = normal(p1,p2,p3);
      customNormals = new Point3D[]{n,n,n};
    }

    Vertex v1 = new Vertex(p1,customNormals[0]);
    Vertex v2 = new Vertex(p2,customNormals[1]);
    Vertex v3 = new Vertex(p3,customNormals[2]);

    if(u != null && v != null){
      v1.u = u[0];
      v1.v = v[0];
      v2.u = u[1];
      v2.v = v[1];
      v3.u = u[2];
      v3.v = v[2];
    }

    this.verts = new Vertex[]{v1,v2,v3};

    this.texture = (texture != null) ? texture : createSolidTexture(c);
  }

  //公開コンストラクタ 
  public Polygon3D(Point3D p1,Point3D p2,Point3D p3){
    this(p1,p2,p3,null,null,null,null);
  }

  public Polygon3D(Point3D p1,Point3D p2,Point3D p3,Color color){
    this(p1,p2,p3,createSolidTexture(color),null,null,null);
  }

  public Polygon3D(Point3D p1,Point3D p2,Point3D p3,BufferedImage tex,double[] u,double[] v){
    this(p1,p2,p3,tex,u,v,null);
  }
}