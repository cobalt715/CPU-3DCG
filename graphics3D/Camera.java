package graphics3D;

//視点のx,y,z見ている方向を格納する
public class Camera{
  private Point3D position;
  private double bdire = 0.0;//横
  private double vdire = 0.0;//縦

  public static int speed = 6;

  public void setBounds(Point3D position){
    this.position = position;
  }

  public void setX(double x){
    this.position.x = x;
  }

  public void setY(double y){
    this.position.y = y;
  }

  public void setZ(double z){
    this.position.z = z;
  }

  public void setBVdire(int b,int v){
    bdire = b;
    vdire = v;
  }

  //1/10°づつ必要になるから3600になっている
  public void setBdire(double b){
    bdire = ((b % 3600) + 3600) % 3600;
  }

  public void setVdire(double v){
    vdire = ((v % 3600) + 3600) % 3600;
  }

  public double getX(){
    return position.x;
  }

  public double getY(){
    return position.y;
  }

  public double getZ(){
    return position.z;
  }

  public double getBdire(){
    return bdire;
  }

  public double getVdire(){
    return vdire;
  }

  public void toFront(){
    position.z = getZ() - (int)Math.round(Utility.COS[(int)Math.round(getBdire())] * speed);
    position.x = getX() + (int)Math.round(Utility.SIN[(int)Math.round(getBdire())] * speed);
  }

  public void toBack(){
    position.z = getZ() + (int)Math.round(Utility.COS[(int)Math.round(getBdire())] * speed);
    position.x = getX() - (int)Math.round(Utility.SIN[(int)Math.round(getBdire())] * speed);
  }

  public void toRight(){
    int theta = (int)Math.round((getBdire() + 900) % 3600);
    position.z = getZ() - (int)Math.round(Utility.COS[theta] * speed);
    position.x = getX() + (int)Math.round(Utility.SIN[theta] * speed);
  }

  public void toLeft(){
    int theta = (int)Math.round((getBdire() + 900) % 3600);
    position.z = getZ() + (int)Math.round(Utility.COS[theta] * speed);
    position.x = getX() - (int)Math.round(Utility.SIN[theta] * speed);
  }

  public Camera(Point3D position,double bdire,double vdire){
    this.position = position;
    this.bdire = bdire;
    this.vdire = vdire;
  }

  @Override
  public String toString() {
    String s = "<html>" +
    "x:" + getX() + "<br>" +
    "y:" + getY() + "<br>" +
    "z:" + getZ() + "<br>" +
    "b:" + getBdire() / 10 + "<br>" +
    "v:" + getVdire() / 10 +
    "</html>";
    return s;
  }
}