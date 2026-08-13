import java.util.*;
import javax.swing.*;
import java.awt.*;
import graphics3D.*;

//javac *.java graphics3D/*.java

public class Main{
  public static void main(String[] args){
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(null);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    MyPanel panel = new MyPanel();
    frame.add(panel);

    KeyInput keyInput = new KeyInput();
    frame.addKeyListener(keyInput);

    MouseInput mouseInput = new MouseInput();

    Camera camera = new Camera(new Point3D(0,0,0),0,0);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Rasterizer rasterizer = new Rasterizer(screenSize.width,screenSize.height,new Pipeline());

    Point3D p0 = new Point3D(0,0,-1000);
    Point3D p1 = new Point3D(100,100,-1000);
    Point3D p2 = new Point3D(100,0,-1000);

    java.util.List<Polygon3D> polygons = new ArrayList<>();
    polygons.add(new Polygon3D(p0,p1,p2));

    var timer = new javax.swing.Timer(32,e->{
      mouseInput.update(frame,camera,keyInput.getKeyAlt());
      keyInput.update(camera);
      panel.setImage(rasterizer.render(polygons,camera));
      frame.repaint();
    });

    timer.start();

    frame.setVisible(true);
  }
}