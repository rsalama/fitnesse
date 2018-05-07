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
