package graphics3D;

import java.util.*;
import java.awt.*;
import java.awt.image.*;

public class Renderer{
  public Camera camera;
  public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private int w = screenSize.width;
  private int h = screenSize.height;
  private int halfW = w / 2;
  private int halfH = h / 2;

  private final double CON = 1.5;
  private int ssw = (int)(w * CON);
  private int ssh = (int)(h * CON);
  private int ssHalfW = ssw / 2;
  private int ssHalfH = ssh / 2;

  private java.util.List<Polygon3D> pol3D = new ArrayList<>();
  private Color defaultColor = Color.WHITE;

  private PipelineData data;

  private BufferedImage img = new BufferedImage(ssw,ssh,BufferedImage.TYPE_INT_RGB);

  private double[][] zDistance = new double[ssw + 1][ssh + 1];//x,y座標の順

  //光源
  private java.util.List<Light> lights = new ArrayList<>();

  public void addPolygon3D(Polygon3D polygon3D){
    pol3D.add(polygon3D);
  }

  public void update(){
    for(Light l:lights){
      l.update(pol3D);
    }

    Graphics2D g2 = img.createGraphics();
    g2.setColor(defaultColor);
    g2.fillRect(0,0,ssw,ssh);
    g2.dispose();
    zDistance = new double[ssw + 1][ssh + 1];

    for(Polygon3D projected:pol3D){
      data = Pipeline.transformFull(projected,camera);

      if(data.projected != null){
        for(Polygon3D p3:data.projected){
          for(int i = 0;i < 3;i++){
            p3.verts[i].pos.x *= CON;
            p3.verts[i].pos.y *= CON;
            p3.verts[i].pos.x += ssHalfW;
            p3.verts[i].pos.y += ssHalfH;
          }
          setImage(p3);
        }
      }
    }
  }

  //なんかやる
  protected void setImage(Polygon3D pol){
    //頂点が高い順にソート
    Arrays.sort(pol.verts,(a,b) -> Double.compare(a.pos.y,b.pos.y));

    //アクセスしやすいように取り出す
    //透視補正のために/zするものは割っておく
    double[] xpoint = new double[3];
    double[] ypoint = new double[3];
    //エッジ関数のため
    double[] edgeX = new double[3];
    double[] edgeY = new double[3];
    Point3D[] absPos = new Point3D[3];//絶対座標
    double[] zz = new double[3];//zで割る
    double[] uz = new double[3];//zで割る
    double[] vz = new double[3];//zで割る
    Point3D[] normals = new Point3D[3];

    for(int i = 0;i < 3;i++){
      xpoint[i] = pol.verts[i].pos.x;
      ypoint[i] = pol.verts[i].pos.y;
      edgeX[i] = xpoint[i];
      edgeY[i] = ypoint[i];
      absPos[i] = pol.verts[i].absPos;
      zz[i] = 1 / pol.verts[i].pos.z;
      absPos[i] = new Point3D(pol.verts[i].absPos.x * zz[i],pol.verts[i].absPos.y * zz[i],pol.verts[i].absPos.z * zz[i]);
      uz[i] = pol.verts[i].u * zz[i];
      vz[i] = pol.verts[i].v * zz[i];
      normals[i] = pol.verts[i].normal;
    }

    double cross = (xpoint[1] - xpoint[0]) * (ypoint[2] - ypoint[0]) - (ypoint[1] - ypoint[0]) * (xpoint[2] - xpoint[0]);

    if(cross < 0){
      double co = edgeX[1]; edgeX[1] = edgeX[2]; edgeX[2] = co;
      co = edgeY[1]; edgeY[1] = edgeY[2]; edgeY[2] = co;
    }

    //最も高い頂点から最も低い頂点まで
    for(int y = Math.max(0,(int)(ypoint[0]));y < Math.min(ssh,(int)(ypoint[2]) + 1);y++){
      //0-2間(最も高い頂点から最も低い頂点)の辺での補正
      double rightT = (ypoint[2] - ypoint[0]) > 1 ? (y - ypoint[0]) / (ypoint[2] - ypoint[0]):0;
      double rightX = Utility.lerp(xpoint[0],xpoint[2],rightT);
      double rightZ = Utility.lerp(zz[0],zz[2],rightT);
      Point3D rightAbs = new Point3D(Utility.lerp(absPos[0].x,absPos[2].x,rightT),
                                     Utility.lerp(absPos[0].y,absPos[2].y,rightT),
                                     Utility.lerp(absPos[0].z,absPos[2].z,rightT));
      double rightU = Utility.lerp(uz[0],uz[2],rightT);
      double rightV = Utility.lerp(vz[0],vz[2],rightT);
      Point3D rightN = new Point3D(Utility.lerp(normals[0].x,normals[2].x,rightT),
                                   Utility.lerp(normals[0].y,normals[2].y,rightT),
                                   Utility.lerp(normals[0].z,normals[2].z,rightT));

      //途中で使う辺を変える
      int high = 0;
      int low = 1;
      if(ypoint[1] <= y){
        high = 1;
        low = 2;
      }
      double leftT = (ypoint[low] - ypoint[high]) > 1 ? (y - ypoint[high]) / (ypoint[low] - ypoint[high]):0;
      double leftX = Utility.lerp(xpoint[high],xpoint[low],leftT);
      double leftZ = Utility.lerp(zz[high],zz[low],leftT);
      Point3D leftAbs = new Point3D(Utility.lerp(absPos[high].x,absPos[low].x,leftT),
                                    Utility.lerp(absPos[high].y,absPos[low].y,leftT),
                                    Utility.lerp(absPos[high].z,absPos[low].z,leftT));
      double leftU = Utility.lerp(uz[high],uz[low],leftT);
      double leftV = Utility.lerp(vz[high],vz[low],leftT);
      Point3D leftN = new Point3D(Utility.lerp(normals[high].x,normals[low].x,leftT),
                                  Utility.lerp(normals[high].y,normals[low].y,leftT),
                                  Utility.lerp(normals[high].z,normals[low].z,leftT));

      //始点終点を決める
      if(leftX > rightX){
        double co;
        Point3D pco;
        co = rightX; rightX = leftX; leftX = co;
        co = rightZ; rightZ = leftZ; leftZ = co;
        pco = rightAbs; rightAbs = leftAbs; leftAbs = pco;
        co = rightU; rightU = leftU; leftU = co;
        co = rightV; rightV = leftV; leftV = co;
        pco = rightN; rightN = leftN; leftN = pco;
      }

      int left = (int)(leftX);
      int right = (int)(rightX + 1);

      for(int x = Math.max(0,left);x < Math.min(ssw,right);x++){
        double t = (rightX - leftX) > 1 ? (x - leftX) / (rightX - leftX):0;
        double z = Utility.lerp(leftZ,rightZ,t);
        if(zDistance[x][y] < z)continue;

        //三角形の内部にあるか判別する
        double edge1 = (edgeY[0] - edgeY[1]) * (x - edgeX[1]) + (edgeX[1] - edgeX[0]) * (y - edgeY[1]);
        double edge2 = (edgeY[1] - edgeY[2]) * (x - edgeX[2]) + (edgeX[2] - edgeX[1]) * (y - edgeY[2]);
        double edge3 = (edgeY[2] - edgeY[0]) * (x - edgeX[0]) + (edgeX[0] - edgeX[2]) * (y - edgeY[0]);
        if(edge1 < -100||edge2 < -100||edge3 < -100) continue;

        zDistance[x][y] = z;

        Point3D abs = new Point3D(Utility.lerp(leftAbs.x,rightAbs.x,t) / z,
                                  Utility.lerp(leftAbs.y,rightAbs.y,t) / z,
                                  Utility.lerp(leftAbs.z,rightAbs.z,t) / z);

        double u = Math.min(pol.texture.getWidth() - 1,Math.max(0,Utility.lerp(leftU,rightU,t) / z));
        double v = Math.min(pol.texture.getHeight() - 1,Math.max(0,Utility.lerp(leftV,rightV,t) / z));
        Point3D nor = new Point3D(Utility.lerp(leftN.x,rightN.x,t),
                                  Utility.lerp(leftN.y,rightN.y,t),
                                  Utility.lerp(leftN.z,rightN.z,t));

        double intensity = 0.3;//環境光
        for(Light l:lights){
          intensity += l.getLightIntensity(abs,nor);
        }

        img.setRGB(x,y,Utility.computeLighting(pol.texture.getRGB((int)Math.round(u),(int)Math.round(v)),intensity));
      }
    }
  }
  
  public void draw(Graphics2D g2){
    g2.drawImage(img,0,0,w,h,null);
  }

  public Renderer(Camera camera){
   this.camera = camera;
   lights.add(new Light(new Camera(new Point3D(2000,-2000,2000),3200,3200)));

   Light sub = new Light(new Camera(new Point3D(-800,-1000,600),600,3200));
   sub.strength = 0.5;
   lights.add(sub);
  }

  public Renderer(Camera camera,int w,int h){
    this.camera = camera;
    this.w = w;
    this.h = h;
    halfW = w / 2;
    halfH = h / 2;  
  }
}