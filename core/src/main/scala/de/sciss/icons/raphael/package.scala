/*
 *  package.java
 *  (RaphaelIcons)
 *
 *  Copyright (c) 2013-2020 Hanns Holger Rutz. All rights reserved.
 *
 *	This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package de.sciss.icons

import java.awt.geom.{AffineTransform, Area, GeneralPath, Path2D}
import java.awt.{Color, GradientPaint, LinearGradientPaint, Paint, Shape}
import javax.swing.{UIManager, Icon => JIcon}

package object raphael {
  /** Creates a shape from a given shape function. */
  def Apply(fun: Path2D => Unit): Shape = {
    val p = new GeneralPath(Path2D.WIND_EVEN_ODD)
    fun(p)
    p.closePath()
    p
  }

  /** Special paint indicating no painting (fully translucent) */
  val NoPaint            : Paint = new Color(0x00, 0x00, 0x00, 0x00)
  val WhiteShadow        : Paint = new Color(0xFF, 0xFF, 0xFF, 0x7F)
  val BlackShadow        : Paint = new Color(0x00, 0x00, 0x00, 0x7F)
  val WhiteShadowDisabled: Paint = new Color(0xFF, 0xFF, 0xFF, 0x2F)
  val BlackShadowDisabled: Paint = new Color(0x00, 0x00, 0x00, 0x2F)

  private[this] lazy val isDarkSkin: Boolean = UIManager.getBoolean("dark-skin")

  private[this] val colrPlainLight    = Color.black
  private[this] val colrPlainDark     = new Color(220, 200, 200)

  private[this] val colrDimLight      = Color.darkGray
  private[this] val colrDimDark       = new Color(160, 160, 160)

  private[this] val colrTexTopLight   = Color.black
  private[this] val colrTexBotLight   = new Color(96, 96, 96)
  private[this] val colrTexTopDark    = new Color(140, 140, 140)
  private[this] val colrTexBotDark    = new Color(240, 240, 240)
  private[this] val colrTexTopLightD  = new Color(0x30, 0x30, 0x30, 0x50)
  private[this] val colrTexBotLightD  = new Color(96, 96, 96, 0x50)
  private[this] val colrTexTopDarkD   = new Color(140, 140, 140, 0x50)
  private[this] val colrTexBotDarkD   = new Color(200, 200, 200, 0x50)
  private[this] val colrPlainLightD   = colrTexTopLightD
  private[this] val colrPlainDarkD    = new Color(200, 200, 200, 0x60)

  def Shadow        : Paint = if (isDarkSkin) BlackShadow         else WhiteShadow
  def ShadowDisabled: Paint = if (isDarkSkin) BlackShadowDisabled else WhiteShadowDisabled

  /** Standard OS X style textured icon foreground color.
    * Uses a vertical gradient from black to gray. On dark skin
    * look uses whitish.
    */
  def TexturePaint(extent: Int = 32): Paint = {
    val ct = if (isDarkSkin) colrTexTopDark else colrTexTopLight
    val cb = if (isDarkSkin) colrTexBotDark else colrTexBotLight
    new GradientPaint(0f, 0f, ct, 0f, extent, cb)
  }

  def TexturePaintDisabled(extent: Int = 32): Paint = {
    val ct = if (isDarkSkin) colrTexTopDarkD else colrTexTopLightD
    val cb = if (isDarkSkin) colrTexBotDarkD else colrTexBotLightD
    new GradientPaint(0f, 0f, ct, 0f, extent, cb)
  }

  def PlainPaint        : Paint = if (isDarkSkin) colrPlainDark  else colrPlainLight
  def PlainPaintDisabled: Paint = if (isDarkSkin) colrPlainDarkD else colrPlainLightD
  def DimPaint          : Paint = if (isDarkSkin) colrDimDark    else colrDimLight

  def DisabledIcon(extent: Int = 32, fill: Paint = PlainPaintDisabled, shadow: Paint = NoPaint)
                  (fun: Path2D => Unit): JIcon =
    Icon(extent = extent, fill = fill, shadow = shadow)(fun)

  def TexturedIcon(extent: Int = 32)(fun: Path2D => Unit): JIcon =
    Icon(extent = extent, fill = TexturePaint(extent), shadow = Shadow)(fun)

  def TexturedDisabledIcon(extent: Int = 32)(fun: Path2D => Unit): JIcon =
    Icon(extent = extent, fill = TexturePaintDisabled(extent), shadow = ShadowDisabled)(fun)

  /** Creates an icon from a given shape function. */
  def Icon(extent: Int = 32, fill: Paint = PlainPaint, shadow: Paint = NoPaint)
          (fun: Path2D => Unit): JIcon = {
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
//      shadow match {
//        case c: Color => new Color(255 - c.getRed, 255 - c.getGreen, 255 - c.getBlue)
//        case _ => Color.black // whatever
//      }
      fill match {
        case c: Color => c.darker()
        case p: GradientPaint => p.getColor1.darker()
        case p: LinearGradientPaint => p.getColors.apply(0).darker()
        case _ => Color.black // whatever
      }
    }

    new IconImpl(extent, shape, fill, hasShadow, shadowInset, shadow, darkShadow)
  }
}
