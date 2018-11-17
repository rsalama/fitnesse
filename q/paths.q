qscript_path:"/home/rs/q:/home/rs/tmp"
.sp.path:hsym `$":" vs qscript_path
.sp.fnd:{[sp;f] first sp where f in' key each sp}
.sp.ld:{[f] system "l ", 0N!1_string $[not null p:.sp.fnd[.sp.path;f];` sv (p;f);'"FileNotFound:",string f]}

/ 
q).sp.fnd[.sp.path;`ps.q]
q).sp.ld `ps.q
q).sp.ld `ops.q - exception
