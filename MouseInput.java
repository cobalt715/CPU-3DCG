import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import graphics3D.*;

public class MouseInput{
  private Robot robot;

  private final static BufferedImage blankCursor;
  private final static Cursor invisibleCursor;//透明カーソル

  private static final int centerX,centerY;

  private boolean first = true;//特定のタイミングでカーソルを画面真ん中にしないといけないため

  static{
    blankCursor = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
    blankCursor.setRGB(0,0,0);

    invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursor,new Point(0, 0),"invisible");

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    centerX = screenSize.width / 2;
    centerY = screenSize.height / 2;
  }

  public MouseInput(){
    try{
      robot = new Robot();
    }catch(AWTException e){
      e.printStackTrace();
    }
  }

  public void update(JFrame frame,Camera camera,boolean keyAlt){
    //Altでマウスを動かせるようにする
    if(keyAlt){
      frame.setCursor(Cursor.getDefaultCursor());
      first = true;
      return;
    }

    if(first){
      frame.setCursor(invisibleCursor);//透明カーソルにする
      robot.mouseMove(centerX,centerY);//画面中央にカーソルを移動
      first = false;
      return;
    }

    PointerInfo info = MouseInfo.getPointerInfo();
    if(info == null){
      return;
    }

    Point cursorLocation = info.getLocation();

    int dx = cursorLocation.x - centerX;
    int dy = cursorLocation.y - centerY;

    camera.setYaw(camera.getYaw() + dx / 200.0);
    camera.setPitch(camera.getPitch() - dy / 200.0);

    robot.mouseMove(centerX,centerY);
  }
}