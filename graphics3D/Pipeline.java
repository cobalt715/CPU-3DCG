package graphics3D;

import java.util.*;
import java.awt.*;

//3Dから2Dに変換する機能を純粋関数としてまとめる
//基本的に相対座標を受け取り相対座標で返す
public class Pipeline{
  public static PipelineData transformFull(Polygon3D seed,Camera camera){
    PipelineData data = new PipelineData(seed,camera);

    //相対化、回転
    Point3D[] relative = new Point3D[3];
    Point3D[] rotated = new Point3D[3];

    for(int i = 0;i < 3;i++){
      relative[i] = relative(seed.verts[i].pos,data.camera);
      rotated[i] = rotate(relative[i],data.camera);
    }

    data.relative = new Polygon3D(relative[0],relative[1],relative[2]);
    data.rotated = new Polygon3D(rotated[0],rotated[1],rotated[2]);
    for(int i = 0;i < 3;i++){
      data.rotated.verts[i].absPos = seed.verts[i].pos;
      data.rotated.verts[i].u = seed.verts[i].u;
      data.rotated.verts[i].v = seed.verts[i].v;
      data.rotated.verts[i].normal = seed.verts[i].normal;
    }

    //表裏確認
    if(isFrontFace(data.rotated)){
      return data;
    }

    //クリッピング
    data.clipped = clip(data.rotated);
    if(data.clipped.length == 0){
      return data;
    }

    //投影
    Polygon3D[] projected = new Polygon3D[data.clipped.length];

    for(int i = 0;i < data.clipped.length;i++){
      double[] u = new double[]{data.clipped[i].verts[0].u,data.clipped[i].verts[1].u,data.clipped[i].verts[2].u};
      double[] v = new double[]{data.clipped[i].verts[0].v,data.clipped[i].verts[1].v,data.clipped[i].verts[2].v};

      projected[i] = new Polygon3D(project(data.clipped[i].verts[0].pos),
                                   project(data.clipped[i].verts[1].pos),
                                   project(data.clipped[i].verts[2].pos),
                                   seed.texture,u,v);

      projected[i].setNormal(data.clipped[i].verts[0].normal,data.clipped[i].verts[1].normal,data.clipped[i].verts[2].normal);

      projected[i].setAbsPos(data.clipped[i].verts[0].absPos,data.clipped[i].verts[1].absPos,data.clipped[i].verts[2].absPos);
    }

    data.projected = projected;

    return data;
  }

  //影のため表裏確認をしない
  public static PipelineData transformShadow(Polygon3D seed,Camera camera){
    PipelineData data = new PipelineData(seed,camera);

    //相対化、回転
    Point3D[] relative = new Point3D[3];
    Point3D[] rotated = new Point3D[3];

    for(int i = 0;i < 3;i++){
      relative[i] = relative(seed.verts[i].pos,data.camera);
      rotated[i] = rotate(relative[i],data.camera);
    }

    data.relative = new Polygon3D(relative[0],relative[1],relative[2]);
    data.rotated = new Polygon3D(rotated[0],rotated[1],rotated[2]);
    for(int i = 0;i < 3;i++){
      data.rotated.verts[i].absPos = seed.verts[i].pos;
      data.rotated.verts[i].u = seed.verts[i].u;
      data.rotated.verts[i].v = seed.verts[i].v;
      data.rotated.verts[i].normal = seed.verts[i].normal;
    }

    //クリッピング
    data.clipped = clip(data.rotated);
    if(data.clipped.length == 0){
      return data;
    }

    //投影
    Polygon3D[] projected = new Polygon3D[data.clipped.length];

    for(int i = 0;i < data.clipped.length;i++){
      double[] u = new double[]{data.clipped[i].verts[0].u,data.clipped[i].verts[1].u,data.clipped[i].verts[2].u};
      double[] v = new double[]{data.clipped[i].verts[0].v,data.clipped[i].verts[1].v,data.clipped[i].verts[2].v};

      projected[i] = new Polygon3D(project(data.clipped[i].verts[0].pos),
                                   project(data.clipped[i].verts[1].pos),
                                   project(data.clipped[i].verts[2].pos),
                                   seed.texture,u,v);

      projected[i].setNormal(data.clipped[i].verts[0].normal,data.clipped[i].verts[1].normal,data.clipped[i].verts[2].normal);

      projected[i].setAbsPos(data.clipped[i].verts[0].absPos,data.clipped[i].verts[1].absPos,data.clipped[i].verts[2].absPos);
    }

    data.projected = projected;

    return data;
  }

  //Cameraを中心にした相対座標にする
  public static Point3D relative(Point3D seed,Camera camera){
    return new Point3D(seed.x - camera.getX(),seed.y - camera.getY(),seed.z - camera.getZ());
  }

  //回転
  public static Point3D rotate(Point3D relative,Camera camera){
    //Y軸回転
    int thetaY = (int)Math.round(camera.getBdire());
    double cosY = Utility.COS[thetaY];
    double sinY = Utility.SIN[thetaY];

    double rotatedX = relative.x * cosY + relative.z * sinY;
    double rotatedZ = -relative.x * sinY + relative.z * cosY;

    //X軸縦回転
    int thetaX = (int)Math.round(camera.getVdire());
    double cosX = Utility.COS[thetaX];
    double sinX = Utility.SIN[thetaX];

    double rotatedY = relative.y * cosX - rotatedZ * sinX;
    rotatedZ = relative.y * sinX + rotatedZ * cosX;

    return new Point3D(rotatedX,rotatedY,rotatedZ);
  }

  //表裏確認、時計回りの時に映す
    public static boolean isFrontFace(Polygon3D rotated){
    Point3D normal = rotated.normal();
    double dot = normal.dot(rotated.verts[0].pos);
    return dot > 0;
  }

  //クリッピング
  public static Polygon3D[] clip(Polygon3D rotated){
    int near = -10;//クリッピングする距離
    Point3D[] clipped = new Point3D[4];
    Point3D[] absPos = new Point3D[4];
    double[] u = new double[4];
    double[] v = new double[4];
    Point3D[] normals = new Point3D[4];

    //nearより後ろかどうかを保持する
    boolean[] isItNear = new boolean[]{false,false,false};

    //近いものの数を数える
    int countNear = 0;

    for(int i = 0;i < 3;i++){
      if(rotated.verts[i].pos.z > near){
        isItNear[i] = true;
        countNear++;
      }
    }

    //すべての頂点がすべてnearより後ろなら何もしない
    if(countNear >= 3){
      return new Polygon3D[0];
    }

    int count = 0;//toClipに追加したものの数を数えている
    boolean different1 = false;//クリッピング後にねじれるパターン
    boolean different2 = false;//1-2間と2-0間でクリッピングするとねじれる

    for(int i = 0;i < 3;i++){
      int j = (i + 1) % 3;
      if(isItNear[i] !=isItNear[j]){
        double vectorX = rotated.verts[i].pos.x - rotated.verts[j].pos.x;
        double vectorY = rotated.verts[i].pos.y - rotated.verts[j].pos.y;
        double vectorZ = rotated.verts[i].pos.z - rotated.verts[j].pos.z;

        double t = (near - rotated.verts[j].pos.z) / vectorZ;

        clipped[count] = new Point3D(rotated.verts[j].pos.x + vectorX * t,rotated.verts[j].pos.y + vectorY * t,near);

        //法線を線形補完
        double x = Utility.lerp(rotated.verts[j].normal.x,rotated.verts[i].normal.x,t);
        double y = Utility.lerp(rotated.verts[j].normal.y,rotated.verts[i].normal.y,t);
        double z = Utility.lerp(rotated.verts[j].normal.z,rotated.verts[i].normal.z,t);
        normals[count] = new Point3D(x,y,z);

        //絶対座標を計算
        x = Utility.lerp(rotated.verts[j].absPos.x,rotated.verts[i].absPos.x,t);
        y = Utility.lerp(rotated.verts[j].absPos.y,rotated.verts[i].absPos.y,t);
        z = Utility.lerp(rotated.verts[j].absPos.z,rotated.verts[i].absPos.z,t);
        absPos[count] = new Point3D(x,y,z);

        //テクスチャ参照座標を計算
        u[count] = Utility.lerp(rotated.verts[j].u,rotated.verts[i].u,t);
        v[count] = Utility.lerp(rotated.verts[j].v,rotated.verts[i].v,t);

        count++;
        // 交差したエッジを記録
        if(i == 1) different1 = true;//edge(1,2)
        if(i == 2) different2 = true;//edge(2,0)
      }
    }

    if(different1 && different2){
      for(int i = 0;i < 3;i++){
        if(!isItNear[i]){
          clipped[count] = rotated.verts[i].pos;
          absPos[count] = rotated.verts[i].absPos;
          u[count] = rotated.verts[i].u;
          v[count] = rotated.verts[i].v;
          normals[count] = rotated.verts[i].normal;
          count++;
        }
      }
    }else{
      for(int i = 2;i >= 0;i--){
        if(!isItNear[i]){
          clipped[count] = rotated.verts[i].pos;
          absPos[count] = rotated.verts[i].absPos;
          u[count] = rotated.verts[i].u;
          v[count] = rotated.verts[i].v;
          normals[count] = rotated.verts[i].normal;

          count++;
        }
      }
    }

    //クリッピングした結果頂点が3つになったら
    if(count == 3){
      Polygon3D pol = new Polygon3D(clipped[0],clipped[1],clipped[2]);
      pol.setAbsPos(absPos[0],absPos[1],absPos[2]);
      pol.setUV(Arrays.copyOf(u,3),Arrays.copyOf(v,3));
      pol.setNormal(normals[0],normals[1],normals[2]);
      return new Polygon3D[]{pol};
    }

    Polygon3D pol1 = new Polygon3D(clipped[0],clipped[1],clipped[2]);
    pol1.setAbsPos(absPos[0],absPos[1],absPos[2]);
    pol1.setUV(new double[]{u[0],u[1],u[2]},new double[]{v[0],v[1],v[2]});
    pol1.setNormal(normals[0],normals[1],normals[2]);

    Polygon3D pol2 = new Polygon3D(clipped[0],clipped[2],clipped[3]);
    pol2.setAbsPos(absPos[0],absPos[2],absPos[3]);
    pol2.setUV(new double[]{u[0],u[2],u[3]},new double[]{v[0],v[2],v[3]});
    pol2.setNormal(normals[0],normals[2],normals[3]);

    return new Polygon3D[]{pol1,pol2};
  }

  //投影
  public static Point3D project(Point3D clipped){
    return new Point3D(-clipped.x * 1500.0 / clipped.z,-clipped.y * 1500.0 / clipped.z,clipped.z);
  }
}