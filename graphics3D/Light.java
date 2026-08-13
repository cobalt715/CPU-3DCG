package graphics3D;

import java.util.*;
import java.awt.Color;

//放射範囲制限光源(1面)
public class Light{
  public Camera camera;
  private int w = 512;
  private int h = 512;
  private int halfW = w / 2;
  private int halfH = h / 2;

  public double strength = 1;//光の強さを決める

  private PipelineData data;

  private double[][] depth = new double[w + 1][h + 1];//x,y座標の順

  public void update(List<Polygon3D> pol3D){
    depth = new double[w + 1][h + 1];

    for(Polygon3D projected:pol3D){
      data = Pipeline.transformShadow(projected,camera);

      if(data.projected != null){
        for(Polygon3D p3:data.projected){
          for(int i = 0;i < 3;i++){
            p3.verts[i].pos.x += halfW;
            p3.verts[i].pos.y += halfH;
          }
          setDepth(p3);
        }
      }
    }
  }

  protected void setDepth(Polygon3D pol){
    //頂点が高い順にソート
    Arrays.sort(pol.verts,(a,b) -> Double.compare(a.pos.y,b.pos.y));

    //アクセスしやすいように取り出す
    //透視補正のために/zするものは割っておく
    double[] xpoint = new double[3];
    double[] ypoint = new double[3];
    //エッジ関数のため
    double[] edgeX = new double[3];
    double[] edgeY = new double[3];
    double[] zz = new double[3];//zで割る

    for(int i = 0;i < 3;i++){
      xpoint[i] = pol.verts[i].pos.x;
      ypoint[i] = pol.verts[i].pos.y;
      edgeX[i] = xpoint[i];
      edgeY[i] = ypoint[i];
      zz[i] = 1 / pol.verts[i].pos.z;
    }

    double cross = (xpoint[1] - xpoint[0]) * (ypoint[2] - ypoint[0]) - (ypoint[1] - ypoint[0]) * (xpoint[2] - xpoint[0]);

    if(cross < 0){
      double co = edgeX[1]; edgeX[1] = edgeX[2]; edgeX[2] = co;
      co = edgeY[1]; edgeY[1] = edgeY[2]; edgeY[2] = co;
    }

    //最も高い頂点から最も低い頂点まで
    for(int y = Math.max(0,(int)(ypoint[0]));y < Math.min(h,(int)(ypoint[2]) + 1);y++){
      //0-2間(最も高い頂点から最も低い頂点)の辺での補正
      double rightT = (ypoint[2] - ypoint[0]) > 1 ? (y - ypoint[0]) / (ypoint[2] - ypoint[0]):0;
      double rightX = Utility.lerp(xpoint[0],xpoint[2],rightT);
      double rightZ = Utility.lerp(zz[0],zz[2],rightT);

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

      //始点終点を決める
      if(leftX > rightX){
        double co;
        co = rightX; rightX = leftX; leftX = co;
        co = rightZ; rightZ = leftZ; leftZ = co;
      }

      int left = (int)(leftX);
      int right = (int)(rightX + 1);

      for(int x = Math.max(0,left);x < Math.min(w,right);x++){
        double t = (rightX - leftX) > 1 ? (x - leftX) / (rightX - leftX):0;
        double z = Utility.lerp(leftZ,rightZ,t);
        if(depth[x][y] < z)continue;

        //三角形の内部にあるか判別する
        double edge1 = (edgeY[0] - edgeY[1]) * (x - edgeX[1]) + (edgeX[1] - edgeX[0]) * (y - edgeY[1]);
        double edge2 = (edgeY[1] - edgeY[2]) * (x - edgeX[2]) + (edgeX[2] - edgeX[1]) * (y - edgeY[2]);
        double edge3 = (edgeY[2] - edgeY[0]) * (x - edgeX[0]) + (edgeX[0] - edgeX[2]) * (y - edgeY[0]);
        if(edge1 < -100||edge2 < -100||edge3 < -100) continue;

        depth[x][y] = z;
      }
    }
  }

  //絶対座標、ポリゴンの法線を受け取る
  //明度を返す
  public double getLightIntensity(Point3D point,Point3D normal){
    int near = -10;//クリッピングする距離

    Point3D ray = new Point3D(camera.getX() - point.x,camera.getY() - point.y,camera.getZ() - point.z).normalize();
    double dot = normal.dot(ray);

    Point3D p3 = Pipeline.relative(point,camera);
    p3 = Pipeline.rotate(p3,camera);
    if(p3.z > near){
      return 0;
    }

    p3 = Pipeline.project(p3);

    p3.x += halfW;
    p3.y += halfH;

    int x = (int)p3.x;
    int y = (int)p3.y;
    double z = p3.z;

    if(x < 0 || x >= w || y < 0 || y >= h){
      return 0;
    }

    double bias = 1 - dot * dot * 0.03 - 1e-3;

    int count = 0;
    for(int i = -1;i < 2;i++){
      for(int j = -1;j < 2;j++){
        int rx = x + i;
        int ry = y + j;
        if(rx < 0 || rx >= w || ry < 0 || ry >= h){
          count++;
        }else if(1 / depth[rx][ry] >= z * bias){
          count ++;
        }
      }
    }

    if(count < 4)count = 0;

    double intensity = computeBrightness(normal,ray) * strength * ((9-count) / 9);

    return intensity;
  }

  public double computeBrightness(Point3D p3,Point3D ray){
   double brightness = Math.max(0,p3.normalize().dot(ray));
   //double ambient = 0.4; // 環境光
   //return ambient + (1 - ambient) * brightness;
   return brightness;
  }

  public Light(Camera camera){
   this.camera = camera;
  }

  public Light(Camera camera,int w,int h){
    this.camera = camera;
    this.w = w;
    this.h = h;
    halfW = w / 2;
    halfH = h / 2;  
  }
}