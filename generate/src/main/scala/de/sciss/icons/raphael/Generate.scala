package de.sciss.icons.raphael

import java.io.StringWriter

import org.apache.batik.parser.{PathHandler, PathParser}

object Generate extends App {
  final val LOG_ENABLED = false

  def log(what: => String): Unit = if (LOG_ENABLED) println(s"[log] $what")

  class Handler extends PathHandler {
    private val out     = new StringWriter()
    // private var gpIdx   = 0
    // private var path    = ""

    private var x       = 0f
    private var y       = 0f
    private var cx      = 0f
    private var cy      = 0f

    var indent          = "    p."
    var eol             = ";\n"

    private var ended   = false

    private var isFirst = false
    private var startX  = 0f
    private var startY  = 0f

    def result(): String = {
      require(ended, "Path has not yet ended")
      out.toString
    }

    def startPath(): Unit = {
      log("startPath()")
      // newPath()
      x   = 0
      y   = 0
      cx  = 0
      cy  = 0
      isFirst = true
    }

    //    private def newPath(): Unit = {
    //      gpIdx += 1
    //      path   = s"p$gpIdx"
    //      // out.write(s"val $path = new GeneralPath(Path2D.WIND_EVEN_ODD)\n")
    //    }

    def endPath(): Unit = {
      log("endPath()")
      require(!ended, "Path has already ended")
      // out.write(s"$path.closePath()")
      ended = true
    }

    def movetoRel(x3: Float, y3: Float): Unit = {
      log(s"movetoRel($x3, $y3)")
      x  += x3
      y  += y3
      cx  = cx
      cy  = cy
      pathMoveTo(x, y)
    }

    def movetoAbs(x3: Float, y3: Float): Unit = {
      log(s"movetoAbs($x3, $y3)")
      cx  = x3
      cy  = y3
      x   = x3
      y   = y3
      pathMoveTo(x, y)
    }

    def closePath(): Unit = {
      log("closePath()")
      //      x   = 0
      //      y   = 0
      //      cx  = 0
      //      cy  = 0
      linetoAbs(startX, startY)

      //      x   = startX
      //      y   = startY
      //      cx  = startX
      //      cy  = startY
      // out.write(s"${indent}closePath();\n")
      isFirst = true

      //      out.write(s"g2.fill($path)\n")
      //      newPath()
    }

    def linetoRel(x3: Float, y3: Float): Unit = {
      log(s"linetoRel($x3, $y3)")
      x += x3
      y += y3
      cx = x
      cy = y
      pathLineTo(x, y)
    }

    def linetoAbs(x3: Float, y3: Float): Unit = {
      log(s"linetoAbs($x3, $y3)")
      x   = x3
      y   = y3
      cx  = x
      cy  = y
      pathLineTo(x, y)
    }

    def linetoHorizontalRel(x3: Float): Unit = {
      log(s"linetoHorizontalRel($x3)")
      x += x3
      cx = x
      cy = y
      pathLineTo(x, y)
    }

    def linetoHorizontalAbs(x3: Float): Unit = {
      log(s"linetoHorizontalAbs($x3)")
      x  = x3
      cx = x
      cy = y
      pathLineTo(x, y)
    }

    def linetoVerticalRel(y3: Float): Unit = {
      log(s"linetoVerticalRel($y3)")
      cx = x
      y += y3
      cy = y
      pathLineTo(x, y)
    }

    def linetoVerticalAbs(y3: Float): Unit = {
      log(s"linetoVerticalAbs($y3)")
      cx = x
      y  = y3
      cy = y
      pathLineTo(x, y)
    }

    def curvetoCubicRel(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
      log(s"curvetoCubicRel($x1, $y1, $x2, $y2, $x3, $y3)")
      val x0  = x + x1
      val y0  = y + y1
      cx      = x + x2
      cy      = y + y2
      x      += x3
      y      += y3
      pathCurveTo(x0, y0, cx, cy, x, y)
    }

    def curvetoCubicAbs(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
      log(s"curvetoCubicAbs($x1, $y1, $x2, $y2, $x3, $y3)")
      cx = x2
      cy = y2
      x  = x3
      y  = y3
      pathCurveTo(x1, y1, cx, cy, x, y)
    }

    def curvetoCubicSmoothRel(x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
      log(s"curvetoCubicSmoothRel($x2, $y2, $x3, $y3)")
      val x1 = x * 2 - cx
      val y1 = y * 2 - cy
      cx     = x + x2
      cy     = y + y2
      x     += x3
      y     += y3
      pathCurveTo(x1, y1, cx, cy, x, y)
    }

    def curvetoCubicSmoothAbs(x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
      log(s"curvetoCubicSmoothAbs($x2, $y2, $x3, $y3)")
      val x1  = x * 2 - cx
      val y1  = y * 2 - cy
      cx      = x2
      cy      = y2
      x       = x3
      y       = y3
      pathCurveTo(x1, y1, cx, cy, x, y)
    }

    def curvetoQuadraticRel(p1: Float, p2: Float, p3: Float, p4: Float): Unit = {
      log(s"curvetoQuadraticRel($p1, $p2, $p3, $p4)")
      ???
    }

    def curvetoQuadraticAbs(p1: Float, p2: Float, p3: Float, p4: Float): Unit = {
      log(s"curvetoQuadraticAbs($p1, $p2, $p3, $p4)")
      ???
    }

    def curvetoQuadraticSmoothRel(p1: Float, p2: Float): Unit = {
      log(s"curvetoQuadraticSmoothRel($p1, $p2)")
      ???
    }

    def curvetoQuadraticSmoothAbs(p1: Float, p2: Float): Unit = {
      log(s"curvetoQuadraticSmoothAbs($p1, $p2)")
      ???
    }

    def arcRel(p1: Float, p2: Float, p3: Float, p4: Boolean, p5: Boolean, p6: Float, p7: Float): Unit = {
      log(s"arcRel($p1, $p2, $p3, $p4, $p5, $p6, $p7)")
      ???
    }

    def arcAbs(p1: Float, p2: Float, p3: Float, p4: Boolean, p5: Boolean, p6: Float, p7: Float): Unit = {
      log(s"arcAbs($p1, $p2, $p3, $p4, $p5, $p6, $p7)")
      ???
    }

    private def pathMoveTo(x: Float, y: Float): Unit = {
      if (isFirst) {
        startX  = x
        startY  = y
        isFirst = false
      }
      out.write(s"${indent}moveTo(${x}f, ${y}f)$eol")
    }

    private def pathLineTo(x: Float, y: Float): Unit = {
      out.write(s"${indent}lineTo(${x}f, ${y}f)$eol")
    }

    private def pathCurveTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
      out.write(s"${indent}curveTo(${x1}f, ${y1}f, ${x2}f, ${y2}f, ${x3}f, ${y3}f)$eol")
    }
  }

  val parser  = new PathParser
  val is      = getClass.getResourceAsStream("paths.txt")
  val source  = io.Source.fromInputStream(is, "UTF-8")
  val entries = source.getLines().flatMap {
    case s if s.nonEmpty && !s.startsWith("#") =>
      val t = s.trim()
      val Array(key, value) = t.split("=")
      Some(key -> value)

    case _ => None
  } .toIndexedSeq
  is.close()

  print(
    """package de.sciss.icons.raphael;
      |
      |import java.awt.Shape;
      |import java.awt.geom.GeneralPath;
      |import java.awt.geom.Path2D;
      |
      |public class Shapes {
      |  private Shapes() {}
      |""".stripMargin
  )

  entries.zipWithIndex.foreach { case ((name, path), idx) =>
    //    if (idx % 14 == 0) {
    //      if (idx > 0) println("lineFeed()")
    //    } else {
    //      println("moveRight()")
    //    }

    val h = new Handler
    parser.setPathHandler(h)
    parser.parse(path)
    println()
    println(s"  public static void ${name.capitalize}(Path2D p) {")
    print  (h.result())
    println( "  }")
  }
  println("}")

  //  println("\n///////////////////// DEMO\n")
  //  entries.foreach { case (name, _) =>
  //    println(s"")
  //  }
}