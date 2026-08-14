package graphics3D;

import java.util.*;
import java.awt.*;
import java.awt.image.*;

//画像を作る
public class Rasterizer{
  private Pipeline pipeline;
  private BufferedImage image;
  private int[] pixels;
  private double[] zDistance;
  private int width,height;
  private int centerX,centerY;

  public BufferedImage render(java.util.List<Polygon3D> polygons,Camera camera){
    Arrays.fill(pixels,0xeeeeeeee);
    Arrays.fill(zDistance,0.0);

    for(Polygon3D polygon3D:polygons){
      for(Polygon3D projected:pipeline.process(polygon3D,camera)){
        setImage(projected);
      }
    }

    return image;
  }

  protected void setImage(Polygon3D projected){
    //Y軸が高い順にする
    //画面上にある方が高いとする
    Vertex[] vertexs = new Vertex[]{projected.v0,projected.v1,projected.v2};

    Arrays.sort(vertexs,(a,b) -> Double.compare(a.pos.y,b.pos.y));

    Vertex top = vertexs[0];
    Vertex mid = vertexs[1];
    Vertex bot = vertexs[2];

    //描画座標に移動しておく
    final double topX = top.pos.x + centerX;
    final double midX = mid.pos.x + centerX;
    final double botX = bot.pos.x + centerX;

    final double topY = top.pos.y + centerY;
    final double midY = mid.pos.y + centerY;
    final double botY = bot.pos.y + centerY;

    //そのまま線形補完すると歪むので透視補完するためにzで割る
    final double topRecZ = 1.0 / top.pos.z;
    final double midRecZ = 1.0 / mid.pos.z;
    final double botRecZ = 1.0 /bot.pos.z;

    final double topUZ = (double)top.u / top.pos.z;
    final double topVZ = (double)top.v / top.pos.z;
    final double midUZ = (double)mid.u / mid.pos.z;
    final double midVZ = (double)mid.v / mid.pos.z;
    final double botUZ = (double)bot.u / bot.pos.z;
    final double botVZ = (double)bot.v / bot.pos.z;

    //三角形の上の端から下の端かつ画面内を走査する
    final int beginY = Math.max(0,(int)Math.round(topY));
    final int endY = Math.min(height - 1,(int)Math.round(botY));

    for(int y = beginY;y < endY;y++){
      //三角形の左の端から右の端かつ画面内を走査する
      double leftT = 0.0;
      double rightT = 0.0;

      //辺の両端
      double leftX = 0;
      double rightX = 0;

      //forで走査する分
      int beginX = 0;
      int endX = 0;

      double leftRecZ = 0.0;
      double rightRecZ = 0.0;

      double leftUZ = 0.0;
      double leftVZ = 0.0;
      double rightUZ = 0.0;
      double rightVZ = 0.0;

      final double magic_number = 2.0;//分母がこれより小さいなら

      double den = botY - topY;

      leftT = (den < magic_number) ? 0.0:(y - topY) / den;

      leftX = Utility.lerp(topX,botX,leftT);

      beginX = (int)Math.max(0,Math.min(width - 1,Math.round(leftX)));

      leftRecZ = Utility.lerp(topRecZ,botRecZ,leftT);

      leftUZ = Utility.lerp(topUZ,botUZ,leftT);
      leftVZ = Utility.lerp(topVZ,botVZ,leftT);

      if(y < midY){
        den = midY - topY;

        rightT = (den < magic_number) ? 0.0:(y - topY) / den;

        rightX = Utility.lerp(topX,midX,rightT);

        endX = (int)Math.max(0,Math.min(width - 1,Math.round(rightX)));

        rightRecZ = Utility.lerp(topRecZ,midRecZ,rightT);

        rightUZ = Utility.lerp(topUZ,midUZ,rightT);
        rightVZ = Utility.lerp(topVZ,midVZ,rightT);
      }else{
        den = botY - midY;

        rightT = (den < magic_number) ? 0.0:(y - midY) / den;

        rightX = Utility.lerp(midX,botX,rightT);

        endX = (int)Math.max(0,Math.min(width - 1,Math.round(rightX)));

        rightRecZ = Utility.lerp(midRecZ,botRecZ,rightT);

        rightUZ = Utility.lerp(midUZ,botUZ,rightT);
        rightVZ = Utility.lerp(midVZ,botVZ,rightT);
      }

      //leftを小さくするようにswap
      if(leftX > rightX){
        double swapd = rightT;
        rightT = leftT;
        leftT = swapd;

        swapd = rightX;
        rightX = leftX;
        leftX = swapd;

        int swapi = endX;
        endX = beginX;
        beginX = swapi;

        swapd = rightRecZ;
        rightRecZ = leftRecZ;
        leftRecZ = swapd;

        swapd = rightUZ;
        rightUZ = leftUZ;
        leftUZ = swapd;

        swapd = rightVZ;
        rightVZ = leftVZ;
        leftVZ = swapd;
      }

      for(int x = beginX;x < endX;x++){
        den = rightX - leftX;

        double t = (den < magic_number) ? 0.0:(x - leftX) / den;

        double recZ = Utility.lerp(leftRecZ,rightRecZ,t);

        double uz = Utility.lerp(leftUZ,rightUZ,t);
        double vz = Utility.lerp(leftVZ,rightVZ,t);

        pixels[y * width + x] = projected.texture.getRGB((int)Math.max(0,Math.min(projected.textureWidth - 1,Math.round(uz / recZ))),
                                                         (int)Math.max(0,Math.min(projected.textureHeight - 1,Math.round(vz / recZ))));
      }
    }
  }

  public Rasterizer(int width,int height,Pipeline pipeline){
    this.pipeline = pipeline;
    this.width = width;
    this.height = height;
    centerX = width / 2;
    centerY = height / 2;

    image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

    pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

    zDistance = new double[pixels.length];
  }
}