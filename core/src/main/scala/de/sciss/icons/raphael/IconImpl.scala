/*
 *  IconImpl.java
 *  (RaphaelIcons)
 *
 *  Copyright (c) 2013-2016 Hanns Holger Rutz. All rights reserved.
 *
 *	This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package de.sciss.icons.raphael

import java.awt.{Component, Graphics, Graphics2D, Paint, RenderingHints, Shape}
import javax.swing.{Icon => JIcon}

private[raphael] final class IconImpl(extent: Int, shape: Shape, fill: Paint, hasShadow: Boolean,
                             shadowInset: Shape, lightShadow: Paint, darkShadow: Paint)
  extends JIcon {

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
