Solar System Java Library Calculator
========================

This library is the continuation of Kurt Zoglmann work available in [Github] (https://github.com/zoglmannk/Sunrise-sunset-reference) in November 2013 where he was made a translation of a calculation algorithm Sunrise-Sunset-reference of an old C program. 
The original implementation came from an ancient GW-BASIC program published by Sky & Telescope in the August 1994
issue on Page 84. Sky & Telescope has made the source code available for download 
[here](http://media.skyandtelescope.com/binary/sunup.bas). The mathetmatical algorithm was published in
1979 in the Astrophysical Journal Supplement Series, vol. 41, p. 391-411, which you can view
[here](http://articles.adsabs.harvard.edu//full/1979ApJS...41..391V/0000391.000.html). For other related algorithms, you may find this [page](http://aa.usno.navy.mil/faq/docs/rs_algor.php) useful. 
Basically the work Zoglmann performs calculations on the orbit of the Moon and the Sun, gather all information necessary to know the position (right Ascention, declination, longitudeEcliptic and distance), and the time that each rises and sets in your area (latitude Longitude x) This library aims to implement the algorithms and coordinates the other planets of the solar systems and as well as the sun and the moon to know the time that each rises and sets with the help of the functions implemented by Sunrise-sunset-reference design. It also aims to map the elements of the Zodiac / Constellations, proposing an astrological view of the elements of our solar system. In the future it will be possible to detect date and time of lunar and solar eclipse. Also will be possible to verify phenomena Moon Perigee and Apogee as.

## Technologies Used
- Java 1.8