import java.awt.event.*;
import graphics3D.*;

//キー入力を受け取る
public class KeyInput implements KeyListener{
  //前後左右移動、WASD
  private boolean keyW = false;
  private boolean keyA = false;
  private boolean keyS = false;
  private boolean keyD = false;

  //上下
  private boolean keySpace = false;
  private boolean keyShift = false;

  //視点移動、十字キー
  private boolean keyUp = false;
  private boolean keyDown = false;
  private boolean keyRight = false;
  private boolean keyLeft = false;

  private boolean keyAlt = false;//Altを押している間カーソルの制限をなくす

  public boolean getKeyAlt(){
    return keyAlt;
  }

  //渡されたCameraを動かす
  public void update(Camera camera){
    if(keyW){
      camera.setX(camera.getX() + camera.getMoveSpeed() * Math.sin(camera.getYaw()));
      camera.setZ(camera.getZ() - camera.getMoveSpeed() * Math.cos(camera.getYaw()));
    }
    if(keyA){
      camera.setX(camera.getX() + camera.getMoveSpeed() * Math.sin(camera.getYaw() - Math.PI / 2));
      camera.setZ(camera.getZ() - camera.getMoveSpeed() * Math.cos(camera.getYaw() - Math.PI / 2));
    }
    if(keyS){
      camera.setX(camera.getX() + camera.getMoveSpeed() * Math.sin(camera.getYaw() + Math.PI));
      camera.setZ(camera.getZ() - camera.getMoveSpeed() * Math.cos(camera.getYaw() + Math.PI));
    }
    if(keyD){
      camera.setX(camera.getX() + camera.getMoveSpeed() * Math.sin(camera.getYaw() + Math.PI / 2));
      camera.setZ(camera.getZ() - camera.getMoveSpeed() * Math.cos(camera.getYaw() + Math.PI / 2));
    }

    if(keySpace){
      camera.setY(camera.getY() - camera.getMoveSpeed());
    }
    if(keyShift){
      camera.setY(camera.getY() + camera.getMoveSpeed());
    }

    if(keyUp){
      camera.setPitch(camera.getPitch() + camera.getRotateSpeed());
    }
    if(keyDown){
      camera.setPitch(camera.getPitch() - camera.getRotateSpeed());
    }
    if(keyRight){
      camera.setYaw(camera.getYaw() + camera.getRotateSpeed());
    }
    if(keyLeft){
      camera.setYaw(camera.getYaw() - camera.getRotateSpeed());
    }
  }

  @Override
  public void keyPressed(KeyEvent e){
    switch(e.getKeyCode()){
      case KeyEvent.VK_W:
        keyW = true;
        break;
      case KeyEvent.VK_A:
        keyA = true;
        break;
      case KeyEvent.VK_S:
        keyS = true;
        break;
      case KeyEvent.VK_D:
        keyD = true;
        break;

      case KeyEvent.VK_SPACE:
        keySpace = true;
        break;
      case KeyEvent.VK_SHIFT:
        keyShift = true;
        break;

      case KeyEvent.VK_UP:
        keyUp = true;
        break;
      case KeyEvent.VK_DOWN:
        keyDown = true;
        break;
      case KeyEvent.VK_RIGHT:
        keyRight = true;
        break;
      case KeyEvent.VK_LEFT:
        keyLeft = true;
        break;

      case KeyEvent.VK_ALT:
        keyAlt = true;
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e){
    switch(e.getKeyCode()){
      case KeyEvent.VK_W:
        keyW = false;
        break;
      case KeyEvent.VK_A:
        keyA = false;
        break;
      case KeyEvent.VK_S:
        keyS = false;
        break;
      case KeyEvent.VK_D:
        keyD = false;
        break;

      case KeyEvent.VK_SPACE:
        keySpace = false;
        break;
      case KeyEvent.VK_SHIFT:
        keyShift = false;
        break;

      case KeyEvent.VK_UP:
        keyUp = false;
        break;
      case KeyEvent.VK_DOWN:
        keyDown = false;
        break;
      case KeyEvent.VK_RIGHT:
        keyRight = false;
        break;
      case KeyEvent.VK_LEFT:
        keyLeft = false;
        break;

      case KeyEvent.VK_ALT:
        keyAlt = false;
        break;
    }
  }

  @Override
  public void keyTyped(KeyEvent e){}
}