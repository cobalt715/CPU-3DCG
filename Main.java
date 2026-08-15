import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import graphics3D.*;

//javac *.java graphics3D/*.java

public class Main{
  private static long lastTime = System.nanoTime();

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

    BufferedImage kanagawa = null;

    try{
      //出展https://ja.wikipedia.org/wiki/%E7%A5%9E%E5%A5%88%E5%B7%9D%E6%B2%96%E6%B5%AA%E8%A3%8F#/media/%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB:The_Great_Wave_off_Kanagawa.jpg
      kanagawa = ImageIO.read(new File("Kanagawa.jpg"));

      if(kanagawa == null){
        throw new RuntimeException("Kanagawa.jpgがnullでした");
      }
    }catch(Exception e){
      e.printStackTrace();
    }

    Point3D p0 = new Point3D(0,0,-5000);
    Point3D p1 = new Point3D(0,882,-5000);
    Point3D p2 = new Point3D(1279,0,-5000);
    Point3D p3 = new Point3D(1279,882,-5000);

    Point3D normal = Polygon3D.normal(p0,p1,p2);

    Vertex v0 = new Vertex(p0,normal,0,0);
    Vertex v1 = new Vertex(p1,normal,0,882);
    Vertex v2 = new Vertex(p2,normal,1279,0);
    Vertex v3 = new Vertex(p3,normal,1279,882);

    java.util.List<Polygon3D> polygons = new ArrayList<>();
    polygons.add(new Polygon3D(v0,v1,v2,kanagawa));
    polygons.add(new Polygon3D(v2,v1,v3,kanagawa));

    var timer = new javax.swing.Timer(16,e->{
      mouseInput.update(frame,camera,keyInput.getKeyAlt());
      keyInput.update(camera);
      panel.setImage(rasterizer.render(polygons,camera));
      frame.repaint();

      long now = System.nanoTime();
      long delta = now - lastTime; // ナノ秒単位
      double deltaMillis = delta / 1_000_000.0; // ミリ秒に変換
      lastTime = now;
      System.out.println("1フレーム前との経過: " + deltaMillis + " ms");
    });

    timer.start();

    frame.setVisible(true);
  }
}