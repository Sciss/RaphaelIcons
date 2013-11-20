package de.sciss.svgpath

import org.apache.batik.parser.{PathHandler, PathParser}
import java.io.StringWriter

object Generate extends App {
  val LOG_ENABLED = true

  val pathEye   =
    "M16,8.286C8.454,8.286,2.5,16,2.5,16s5.954,7.715,13.5,7.715c5.771,0,13.5-7.715,13.5-7.715S21.771,8.286,16,8.286" +
    "zM16,20.807c-2.649,0-4.807-2.157-4.807-4.807s2.158-4.807,4.807-4.807s4.807,2.158,4.807,4.807" +
    "S18.649,20.807,16,20.807zM16,13.194c-1.549,0-2.806,1.256-2.806,2.806c0,1.55,1.256,2.806,2.806,2.806" +
    "c1.55,0,2.806-1.256,2.806-2.806C18.806,14.451,17.55,13.194,16,13.194z"

  val pathInfo  =
    "M16,1.466C7.973,1.466,1.466,7.973,1.466,16c0,8.027,6.507,14.534,14.534,14.534c8.027,0,14.534-6.507,14.534-14.534" +
    "C30.534,7.973,24.027,1.466,16,1.466z M14.757,8h2.42v2.574h-2.42V8z M18.762,23.622H16.1" +
    "c-1.034,0-1.475-0.44-1.475-1.496v-6.865c0-0.33-0.176-0.484-0.484-0.484h-0.88V12.4h2.662" +
    "c1.035,0,1.474,0.462,1.474,1.496v6.887c0,0.309,0.176,0.484,0.484,0.484h0.88V23.622z"

  def log(what: => String): Unit = if (LOG_ENABLED) println(s"[log] $what")

  class Handler extends PathHandler {
    private val out     = new StringWriter()
    private var gpIdx   = 0
    private var path    = ""

    private var x       = 0f
    private var y       = 0f
    private var cx      = 0f
    private var cy      = 0f

    private var ended   = false

    def result(): String = {
      require(ended, s"Path has not yet ended")
      out.toString
    }

    def startPath(): Unit = {
      log("startPath()")
      newPath()
      x   = 0
      y   = 0
      cx  = 0
      cy  = 0
    }

    private def newPath(): Unit = {
      gpIdx += 1
      path   = s"p$gpIdx"
      out.write(s"val $path = new GeneralPath(Path2D.WIND_EVEN_ODD)\n")
    }

    def endPath(): Unit = {
      log("endPath()")
      require(!ended, s"Path has already ended")
      out.write(s"$path.closePath()")
      ended = true
    }

    def movetoRel(p1: Float, p2: Float): Unit = {
      log(s"movetoRel($p1, $p2)")
    }

    def movetoAbs(x1: Float, y1: Float): Unit = {
      log(s"movetoAbs($x1, $y1)")
      cx  = x1
      cy  = y1
      x   = x1
      y   = y1
      out.write(s"$path.moveTo(${cx}f, ${cy}f)\n")
    }

    def closePath(): Unit = {
      log("closePath()")
      //      out.write(s"$path.closePath()\n")
      //      out.write(s"g2.fill($path)\n")
      //      newPath()
    }

    def linetoRel(p1: Float, p2: Float): Unit = {
      log(s"linetoRel($p1, $p2)")
      ???
    }

    def linetoAbs(p1: Float, p2: Float): Unit = {
      log(s"linetoAbs($p1, $p2)")
      ???
    }

    def linetoHorizontalRel(x3: Float): Unit = {
      log(s"linetoHorizontalRel($x3)")
      x += x3
      cx = x
      cy = y
      out.write(s"$path.lineTo(${x}f, ${y}f)\n")
    }

    def linetoHorizontalAbs(x3: Float): Unit = {
      log(s"linetoHorizontalAbs($x3)")
      x  = x3
      cx = x
      cy = y
      out.write(s"$path.lineTo(${x}f, ${y}f)\n")
    }

    def linetoVerticalRel(y3: Float): Unit = {
      log(s"linetoVerticalRel($y3)")
      cx = x
      y += y3
      cy = y
      out.write(s"$path.lineTo(${x}f, ${y}f)\n")
    }

    def linetoVerticalAbs(y3: Float): Unit = {
      log(s"linetoVerticalAbs($y3)")
      cx = x
      y  = y3
      cy = y
      out.write(s"$path.lineTo(${x}f, ${y}f)\n")
    }

    def curvetoCubicRel(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
      log(s"curvetoCubicRel($x1, $y1, $x2, $y2, $x3, $y3)")
      val x0  = x + x1
      val y0  = y + y1
      cx      = x + x2
      cy      = y + y2
      x      += x3
      y      += y3
      out.write(s"$path.curveTo(${x0}f, ${y0}f, ${cx}f, ${cy}f, ${x}f, ${y}f)\n")
    }

    def curvetoCubicAbs(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
      log(s"curvetoCubicAbs($x1, $y1, $x2, $y2, $x3, $y3)")
      cx = x2
      cy = y2
      x  = x3
      y  = y3
      out.write(s"$path.curveTo(${x1}f, ${y1}f, ${cx}f, ${cy}f, ${x}f, ${y}f)\n")
    }

    def curvetoCubicSmoothRel(x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
      log(s"curvetoCubicSmoothRel($x2, $y2, $x3, $y3)")
      val x1 = x * 2 - cx
      val y1 = y * 2 - cy
      cx     = x + x2
      cy     = y + y2
      x     += x3
      y     += y3
      out.write(s"$path.curveTo(${x1}f, ${y1}f, ${cx}f, ${cy}f, ${x}f, ${y}f)\n")
    }

    def curvetoCubicSmoothAbs(x2: Float, y2: Float, x3: Float, y3: Float): Unit = {
      log(s"curvetoCubicSmoothAbs($x2, $y2, $x3, $y3)")
      val x1  = x * 2 - cx
      val y1  = y * 2 - cy
      cx      = x2
      cy      = y2
      x       = x3
      y       = y3
      out.write(s"$path.curveTo(${x1}f, ${y1}f, ${cx}f, ${cy}f, ${x}f, ${y}f)\n")
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
  }

  val parser  = new PathParser
  val h       = new Handler
  parser.setPathHandler(h)
  parser.parse(pathInfo)
  println(h.result())
}
