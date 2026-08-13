import java.awt.event.*;
import java.awt.*;
import graphics3D.*; 

//見テ分カレ
public class Input implements KeyListener{
  private boolean keyW = false;
  private boolean keyA = false;
  private boolean keyS = false;
  private boolean keyD = false;
  private boolean keyUp = false;
  private boolean keyDown = false;
  private boolean keyLeft = false;
  private boolean keyRight = false;
  private boolean keySpace = false;
  private boolean keyShift = false;

  public static boolean keyAlt = false;//Altを押している間カーソルを動かせるようにするため

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
      case KeyEvent.VK_UP:
        keyUp = true;
        break;
      case KeyEvent.VK_DOWN:
        keyDown = true;
        break;
      case KeyEvent.VK_LEFT:
        keyLeft= true;
        break;
      case KeyEvent.VK_RIGHT:
        keyRight = true;
        break;
      case KeyEvent.VK_SPACE:
        keySpace = true;
        break;
      case KeyEvent.VK_SHIFT:
        keyShift = true;
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
      case KeyEvent.VK_UP:
        keyUp = false;
        break;
      case KeyEvent.VK_DOWN:
        keyDown = false;
        break;
      case KeyEvent.VK_LEFT:
        keyLeft= false;
        break;
      case KeyEvent.VK_RIGHT:
        keyRight = false;
        break;
      case KeyEvent.VK_SPACE:
        keySpace = false;
        break;
      case KeyEvent.VK_SHIFT:
        keyShift = false;
        break;
      case KeyEvent.VK_ALT:
        keyAlt = false;
        break;
    }
  }

  @Override
  public void keyTyped(KeyEvent e){}

  public void keyMove(){
    Camera p = Main.renderer.camera;
    if(keyW){
      p.toFront();
    }
    if(keyA){
      p.toLeft();
    }
    if(keyS){
      p.toBack();
    }
    if(keyD){
      p.toRight();
    }
    if(keySpace){
      p.setY(p.getY() - p.speed);
    }
    if(keyShift){
      p.setY(p.getY() + p.speed);
    }
  }
}