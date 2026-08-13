package graphics3D;

public class Camera{
  private double x,y,z;//位置
  private double moveSpeed = 6.0;//動く速さ

  //ラジアン
  private double yaw;//横
  private double pitch;//縦
  private double rotateSpeed = Math.PI / 180.0;//回転する速さ

  public Camera(){}

  public Camera(Point3D pos,double yaw,double pitch){
    x = pos.x;
    y = pos.y;
    z = pos.z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public Point3D getPosition(){
    return new Point3D(x,y,z);
  }

  public double getX(){
    return x;
  }

  public double getY(){
    return y;
  }

  public double getZ(){
    return z;
  }

  public void setX(double x){
    this.x = x;
  }

  public void setY(double y){
    this.y = y;
  }

  public void setZ(double z){
    this.z = z;
  }

  public double getYaw(){
    return yaw;
  }

  public double getPitch(){
    return pitch;
  }

  public void setYaw(double yaw){
    this.yaw = yaw;
  }

  public void setPitch(double pitch){
    this.pitch = pitch;
  }

  public double getMoveSpeed(){
    return moveSpeed;
  }

  public double getRotateSpeed(){
    return rotateSpeed;
  }
}