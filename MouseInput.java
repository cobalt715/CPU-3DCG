import java.awt.*;
import javax.swing.*;
import graphics3D.*; 

public class MouseInput {
  private JFrame frame;
  private Robot robot;
  private Point center;
  private double sensitivity = 1.0;
  private boolean first = true;
  private int lastX, lastY;
  private boolean enter = false;

  public MouseInput(JFrame frame){
    this.frame = frame;
    try{
      robot = new Robot();
    }catch (AWTException e){
      e.printStackTrace();
    }
  }

  //毎フレーム画面中心からの相対座標を求めカーソルを中心に戻す
  public void update(){
    //Altでマウスロック解除
    if(Input.keyAlt){
      frame.setCursor(Cursor.getDefaultCursor());//通常カーソル表示
      enter = true;
      return;
    }
    //画面中央に戻す
    if(enter){
      frame.setCursor(Main.blankCursor);//ゲーム中は透明カーソル
      robot.mouseMove(center.x, center.y);
      enter = false;
    }
    if(first){
      //画面中央を記録
      Point loc = frame.getLocationOnScreen();
      center = new Point(loc.x + frame.getWidth() / 2,loc.y + frame.getHeight() / 2);
      robot.mouseMove(center.x, center.y);
      first = false;
      return;
    }

    //現在位置を取得
    PointerInfo info = MouseInfo.getPointerInfo();
    if (info == null) return; // 安全対策
    Point pos = info.getLocation();

    int dx = pos.x - center.x;
    int dy = pos.y - center.y;

    // マウスが動いていなければ無視
    if (dx == 0 && dy == 0) return;

    //視点の向きを変える
    Camera p = Main.renderer.camera;
    p.setBdire(p.getBdire() + dx * sensitivity);
    p.setVdire(p.getVdire() - dy * sensitivity);

    robot.mouseMove(center.x, center.y);
  }
}
