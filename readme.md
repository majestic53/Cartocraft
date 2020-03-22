Cartocraft
=

[![License: Unlicense](https://img.shields.io/badge/license-Unlicense-blue.svg)](https://unlicense.org/)

About
=

Cartocraft is a top-down, orthogonal Minecraft map renderer with ambient occlusion and lighting. Use Cartocraft to quickly render full color maps of your creations, or use it to create elevation/height maps for use in other applications.

Changelog
=

Version 0.1.4
-

* Documentation/license cleanup
* Updated block types for version 1.1

Version 0.1.3
-

* Initial release

Features
=

* Render color, elevation, and height maps at various heights (0 - Map Height) as png files
* Rendering process is multi-threaded
* Screen-space ambient occlusion (SSAO) included
* Screen-space indirect lighting (SSIL) included
* Ability to set various rendering parameters (such as exposure)
* Add new block types, and customize colors of blocks

Usage
=

```
java -jar Cartocraft.jar [Region File Directory] [Height] [Output]
```

```
[Region File Directory]: The directory where a given set of mcr files is location
[Height]: Render height (0 - Map Height)
[Output]: The name of the output png file
```

Known Bugs
=

None at the moment

License
=

This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
