# RaphaelIcons

Translation of the Raphael icon suite by Dmitry Baranovskiy into a Java2D-based Scala library. Icon source made available from [raphaeljs.com/icons](http://raphaeljs.com/icons) under the [MIT License](http://raphaeljs.com/license.html). Library code (C)opyright 2013 by Hanns Holger Rutz. All rights reserved. Released under the GNU LGPL v2.1+.

## linking

Use the following dependency:

    "de.sciss" %% "raphael-icons" % v

The current version `v` is `"1.0.+"`.

## Building

Uses Scala 2.10 and sbt 0.13 for building. The actual icon class is synthesised into sub-project `core`, using the source generator provided in sub-project `generate`.

To see the source generation, run `sbt generate/run`. To compile the core project, use `sbt raphael-icons/compile`. To run a demo, use `sbt raphael-icons/test:run`. There are two demos. `Demo` shows a matrix of all icons. Mouse mouse hover to see the icon names (method names in `Shapes.java`). `IconDemo` shows how to create a custom sized icon.

## Reminder to Self

To publish, `sbt raphael-icons/publish-signed`.