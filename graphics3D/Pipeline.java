package graphics3D;

//Polygon3Dを受け取りCameraの情報に合わせて投影する
public class Pipeline{
  public Polygon3D[] process(Polygon3D seed,Camera camera){
    //seedとcameraの情報から相対位置にし回転したPolygon3Dを作る
    Polygon3D rotated = toCameraSpace(seed,camera);

    //表裏確認
    if(rotated.normal().z > 0) return new Polygon3D[0];

    //クリッピング
    Polygon3D[] clippeds = clipping(rotated);

    //クリッピングしたものを投影し完全なPolygon3D[]にして返す
    Polygon3D[] projecteds = new Polygon3D[clippeds.length];

    for(int i = 0;i < clippeds.length;i++){
      Vertex v0 = clippeds[i].v0;
      Vertex v1 = clippeds[i].v1;
      Vertex v2 = clippeds[i].v2;

      //posのz座標はそのまま
      projecteds[i] = new Polygon3D(new Vertex(project(v0.pos),v0.normal,v0.u,v0.v),
                                    new Vertex(project(v1.pos),v1.normal,v1.u,v1.v),
                                    new Vertex(project(v2.pos),v2.normal,v2.u,v2.v),
                                    seed.texture);
    }

    return projecteds;
  }

  //相対位置にし回転したPolygon3Dを作る
  public static Polygon3D toCameraSpace(Polygon3D seed,Camera camera){
    Point3D cameraPosition = camera.getPosition();

    Point3D relative0 = seed.v0.pos.sub(cameraPosition);
    Point3D relative1 = seed.v1.pos.sub(cameraPosition);
    Point3D relative2 = seed.v2.pos.sub(cameraPosition);

    Point3D rotated0 = rotate(relative0,camera);
    Point3D rotated1 = rotate(relative1,camera);
    Point3D rotated2 = rotate(relative2,camera);

    Vertex v0 = new Vertex(rotated0,seed.v0.normal,seed.v0.u,seed.v0.v);
    Vertex v1 = new Vertex(rotated1,seed.v1.normal,seed.v1.u,seed.v1.v);
    Vertex v2 = new Vertex(rotated2,seed.v2.normal,seed.v2.u,seed.v2.v);

    return new Polygon3D(v0,v1,v2,seed.texture);
  }

  //相対化した位置とCameraをうけとり回転したPolygon3Dを返す
  public static Point3D rotate(Point3D relative,Camera camera){
    //y軸回転
    double sinY = Math.sin(camera.getYaw());
    double cosY = Math.cos(camera.getYaw());

    double rotatedX = relative.x * cosY + relative.z * sinY;
    double rotatedZ = -relative.x * sinY + relative.z * cosY;

    //X軸回転
    double sinX = Math.sin(camera.getPitch());
    double cosX = Math.cos(camera.getPitch());

    double rotatedY = relative.y * cosX - rotatedZ * sinX;
    rotatedZ = relative.y * sinX + rotatedZ * cosX;

    return new Point3D(rotatedX,rotatedY,rotatedZ);
  }

  public static Polygon3D[] clipping(Polygon3D rotated){
    double near = -10.0;//クリッピングする距離
    boolean[] isNear = new boolean[]{false,false,false};
    int countNear = 0;

    //どの点がクリッピングする距離より手前か判定する
    if(rotated.v0.pos.z > near){
      isNear[0] = true;
      countNear++;
    }
    if(rotated.v1.pos.z > near){
      isNear[1] = true;
      countNear++;
    }
    if(rotated.v2.pos.z > near){
      isNear[2] = true;
      countNear++;
    }

    //すべて手前の時
    if(countNear == 3){
      return new Polygon3D[0];
    }else if(countNear == 0){//すべて奥の時
      return new Polygon3D[]{rotated};
    }

    if(countNear == 2){
      Vertex near0 = null;
      Vertex near1 = null;
      Vertex far0 = null;

      if(!isNear[0]){
        far0 = rotated.v0;
        near0 = rotated.v1;
        near1 = rotated.v2;
      }else if(!isNear[1]){
        far0 = rotated.v1;
        near0 = rotated.v2;
        near1 = rotated.v0;
      }else if(!isNear[2]){
        far0 = rotated.v2;
        near0 = rotated.v0;
        near1 = rotated.v1;
      }

      double t0 = (near - near0.pos.z) / (far0.pos.z - near0.pos.z);
      double t1 = (near - near1.pos.z) / (far0.pos.z - near1.pos.z);

      Polygon3D clipped = new Polygon3D(far0,
                                        lerpVertex(near0,far0,t0),
                                        lerpVertex(near1,far0,t1),
                                        null);

      return new Polygon3D[]{clipped};
    }else{
      Vertex far0 = null;
      Vertex far1 = null;
      Vertex near0 = null;

      if(isNear[0]){
        near0 = rotated.v0;
        far0 = rotated.v1;
        far1 = rotated.v2;
      }else if(isNear[1]){
        near0 = rotated.v1;
        far0 = rotated.v2;
        far1 = rotated.v0;
      }else if(isNear[2]){
        near0 = rotated.v2;
        far0 = rotated.v0;
        far1 = rotated.v1;
      }

      double t0 = (near - near0.pos.z) / (far0.pos.z - near0.pos.z);
      double t1 = (near - near0.pos.z) / (far1.pos.z - near0.pos.z);

      Vertex boundary0 = lerpVertex(near0,far0,t0);
      Vertex boundary1 = lerpVertex(near0,far1,t1);

      Polygon3D clipped0 = new Polygon3D(far0,far1,boundary0,null);
      Polygon3D clipped1 = new Polygon3D(boundary0,boundary1,far1,null);

      return new Polygon3D[]{clipped0,clipped1};
    }
  }

  //Vertexの線形補完
  public static Vertex lerpVertex(Vertex s, Vertex e, double t){
    Point3D pos = new Point3D(Utility.lerp(s.pos.x,e.pos.x,t),
                              Utility.lerp(s.pos.y,e.pos.y,t),
                              Utility.lerp(s.pos.z,e.pos.z,t));

    Point3D normal = new Point3D(Utility.lerp(s.normal.x,e.normal.x,t),
                                 Utility.lerp(s.normal.y,e.normal.y,t),
                                 Utility.lerp(s.normal.z,e.normal.z,t));

    double u = Utility.lerp(s.u,e.u,t);
    double v = Utility.lerp(s.v,e.v,t);

    return new Vertex(pos,normal,u,v);
  }

  //z座標はそのまま
  public static Point3D project(Point3D clipped){
    return new Point3D(-clipped.x * 1500.0 / clipped.z,-clipped.y * 1500.0 / clipped.z,clipped.z);
  }
}