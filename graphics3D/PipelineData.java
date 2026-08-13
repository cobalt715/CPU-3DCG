package graphics3D;

//2Dに変換する際に出てくるデータを保管する
public class PipelineData{
  public Camera camera;
  public Polygon3D seed;//元の座標
  public Polygon3D relative;//視点を中心にした相対座標
  public Polygon3D rotated;//回転後の座標
  public Polygon3D[] clipped;//クリッピング後の座標
  public Polygon3D[] projected;//投影後の座標

  public PipelineData(Camera camera,Polygon3D seed){
    this.seed = seed;
    this.camera = camera;
  }

  public PipelineData(Polygon3D seed,Camera camera){
    this.seed = seed;
    this.camera = camera;
  }
}