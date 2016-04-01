package de.sciss.icons

import java.awt.geom.{AffineTransform, Area, GeneralPath, Path2D}
import java.awt.{Color, Component, Graphics, Graphics2D, LinearGradientPaint, Paint, RenderingHints, Shape}
import javax.swing.UIManager

package object raphael {
  /** Creates a shape from a given shape function. */
  def Apply(fun: Path2D => Unit): Shape = {
    val p = new GeneralPath(Path2D.WIND_EVEN_ODD)
    fun(p)
    p.closePath()
    p
  }

  /** Special paint indicating no painting (fully translucent) */
  val NoPaint     = new Color(0, 0, 0, 0): Paint
  /** Standard OS X style textured icon shadow color */
  val WhiteShadow = new Color(255, 255, 255, 127): Paint

  private[this] lazy val isDarkSkin: Boolean = UIManager.getBoolean("dark-skin")

  /** Standard OS X style textured icon foreground color. Uses a vertical gradient from black to gray. */
  def TexturePaint(extent: Int = 32): Paint =
    new LinearGradientPaint(0f, 0f, 0f, extent, Array(0f, 1f), Array(Color.black, new Color(96, 96, 96)))

  /** Creates an icon from a given shape function. */
  def Icon(extent: Int = 32, fill: Paint = Color.black, shadow: Paint = NoPaint)
          (fun: Path2D => Unit): javax.swing.Icon = {
    val p = new GeneralPath(Path2D.WIND_EVEN_ODD)
    fun(p)
    p.closePath()
    val shape = if (extent == 32) p else {
      val scale = extent/32f
      AffineTransform.getScaleInstance(scale, scale).createTransformedShape(p)
    }

    val hasShadow = shadow match {
      case c: Color if c.getAlpha == 0 => false
      case _ => true
    }

    val shadowInset = if (!hasShadow) null else {
      val a1 = new Area(shape)
      val tr = AffineTransform.getTranslateInstance(0, 1)
      val a2 = new Area(tr.createTransformedShape(shape))
      a1.subtract(a2)
      a1
    }

    val darkShadow = if (!hasShadow) null else {
      shadow match {
        case c: Color => new Color(255 - c.getRed, 255 - c.getGreen, 255 - c.getBlue)
        case _ => Color.black // whatever
      }
    }

    new IconImpl(extent, shape, fill, hasShadow, shadowInset, shadow, darkShadow)
  }

  private final class IconImpl(extent: Int, shape: Shape, fill: Paint, hasShadow: Boolean,
                               shadowInset: Shape, lightShadow: Paint, darkShadow: Paint) extends javax.swing.Icon {
    def paintIcon(c: Component, g: Graphics, x: Int, y: Int): Unit = {
      val g2 = g.asInstanceOf[Graphics2D]
      val hints = g2.getRenderingHints
      val at    = g2.getTransform
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING  , RenderingHints.VALUE_ANTIALIAS_ON)
      g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE )
      g2.translate(x, y)
      if (hasShadow) {
        g2.setPaint(lightShadow)
        g2.translate(0, 1)
        g2.fill(shape)
        g2.translate(0, -1)
      }
      g2.setPaint(fill)
      g2.fill(shape)
      if (hasShadow) {
        g2.setPaint(darkShadow)
        g2.fill(shadowInset)
      }
      g2.setTransform(at)
      g2.setRenderingHints(hints)
    }

    def getIconWidth : Int = extent
    def getIconHeight: Int = extent
  }
}
