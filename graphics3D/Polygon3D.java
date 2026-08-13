package graphics3D;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//3Dの三角形とテクスチャ
public class Polygon3D{
  public final Vertex v0;
  public final Vertex v1;
  public final Vertex v2;

  //デフォルト＿テクスチャ
  private final static BufferedImage DEFAULT_TEXTURE;

  public final BufferedImage texture;

  static{
    BufferedImage image = null;

    try{
      image = ImageIO.read(new File("graphics3D/DEFAULT_TEXTURE.png"));

      if(image == null){
        throw new RuntimeException("Polygon3D staticブロック DEFAULT_TEXTURE.pngがnullでした");
      }
    }catch(Exception e){
      e.printStackTrace();
    }

    DEFAULT_TEXTURE = image;
  }

  public Polygon3D(Point3D p0,Point3D p1,Point3D p2){
    Point3D nor = normal(p0,p1,p2);

    v0 = new Vertex(p0,nor,0,0);
    v1 = new Vertex(p1,nor,0,1);
    v2 = new Vertex(p2,nor,1,0);

    texture = DEFAULT_TEXTURE;
  }

  public Polygon3D(Vertex v0,Vertex v1,Vertex v2,BufferedImage texture){
    this.v0 = v0;
    this.v1 = v1;
    this.v2 = v2;

    if(texture == null){
      this.texture = DEFAULT_TEXTURE;
    }else{
      this.texture = texture;
    }
  }

  //正規化した法線を返す
  public static Point3D normal(Point3D p0,Point3D p1,Point3D p2){
    Point3D vec0 = p1.sub(p0);
    Point3D vec1 = p2.sub(p0);

    return new Point3D(vec0.y * vec1.z - vec0.z * vec1.y,
                       vec0.z * vec1.x - vec0.x * vec1.z,
                       vec0.x * vec1.y - vec0.y * vec1.x).normalize();
  }

  public Point3D normal(){
    return normal(v0.pos,v1.pos,v2.pos);
  }
}