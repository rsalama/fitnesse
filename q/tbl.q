t:`vd`id xkey ([] vd: 2010.01.01 + til 10; id: 10#`aa; op: 10. + til 10; cp: 20. + til 10)
u:`vd`id xkey ([] vd: 2010.01.05 + til 10; id: 10#`aa; op: 10. + til 10; cp: 20. + til 10)
v:`vd`id xkey ([] vd: 2010.01.05 + til 10; id: 10#`aa; op: 10.1 + til 10; cp: 20 + til 10)

yyyymmdd:{x except "."}
vv: update vd:yyyymmdd each vd from string () xkey v

toSymbol:{"S"$ssr[;"\"";""] each x}
toDate:{"D"$ {"." sv (x[0+til 4];x[4 5];x[6 7])} each x}
toInt:{"I"$x}
toReal:{"E"$x}
toFloat:{"F"$x}
/ cnv:(`vd`id`cp`op)!(toDate;toSymbol;toInt;toReal)
cnv:(`vd`id`cp`op)!(`toDate;`toSymbol;`toInt;`toReal)
`vd`id xkey ![vv;();0b;(cols vv)!{(cnv x;x)} each cols vv]

/ ![vv;();0b;`vd`id`cp`op!((`toDate;`vd);(`toSymbol;`id);(`toInt;`cp);(`toReal;`op))];
((=).'')t,''u

TOL:1e-9
getOrElse:{[d;k;v] $[k in key d; d k; v}
fuzzyCmp:{[tol;x;y] tol>abs x-y}
cmpFuncs:(0 8 9h)!(=;fuzzyCmp[TOL];fuzzyCmp[TOL)
cmp:{$[any null x;0b;getOrElse[cmpFuncs;type x;=] . x]}

mkempty:{(key x) xcol (valuex;1#",") 0: enlist ""}

time:{[f;a]
  st:.z.n;
  r: f . a;
  `time`result!(.z.n - st; r)}

cmpTables:{[left;right]
  v:left,''right;
  tt:(cmp'') v;
  rmCols:where all tt;
  ne:$[0<count rmCols;![tt;();0b;rmCols];tt];        / Remove columns where all cells are 1b
  ne:(where not (all') ne) # ne;                     / Remove rows where all cells are 1b
  r:(key ne) # ?[v;();k!k:cols key ne;a!a:cols value ne];  / Use ne to cut v by row and column
  `cmpTable`truthTable`results`resultsTruthTable!(v; tt; r; ne)}

N:100
SYMS:`intc`amd`msft`goog
t:([dt:2025.01.01+til N; sym:N?SYMS] price:100.0 + til N; vol:1000 + 10*til N)
v:([dt:2025.01.01+til N; sym:N?SYMS] price:100.0 + til N; vol:1000 + 10*til N)

p:([dt:2025.01.01 + til 3;sym:3 # `ibm] price:100. 101. 102.; vol: 1000 1001 1002)
q:([dt:2025.01.01 + til 3;sym:3 # `ibm] price:100. 101. 102.; vol: 1000 1001 1003)


