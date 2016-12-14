x: 1 2 3

/Server
\p 5000
marshal:{(neg .z.w) (z; (value x) . y)}
add3:{x+y+z}
echo:{0N! x;}


/Client
h: hopen `::5000
echo:{0N! x;}
(neg h) (`marshal; `add3; 1 2 3; `echo)


s:{.cx.cl[("S"$x)]:`h`cb!(.z.w; y)

m:.cx.cl[`.cx.foo]
(neg m.h) (m.cb;(value `.cx.foo))

h "s[\".cx.foo\";`a]"

f:{$[x in key d;d[x],:y;d[x]:enlist y]}

upd:{m:.cx.cl[x];echo[m];(neg m.h) (m.cb;(value x));echo[value x]}
f:{$[x in key d;d[x],:y;d[x]:enlist y]}

mkObserver:{`h`cb!(.z.w; x)}
addObserver:{o:mkObserver[y];$[x in key .cx.cl;.cx.cl[x],:o;.cx.cl[x]:enlist o];}
notify:{(neg y[`h]) (y[`cb];(value x));}
notifyAll:{notify[x] each .cx.cl[x];}

h "addObserver[ `.cx.foo;`a]"
h "addObserver[ `.cx.foo;`b]"

s:(1 3 -5;"xyzw") 
.[`s;0 2;+;3] 

/ Server (Observable & Container)
createObservers:{.cx.observers:([] subject:`symbol$(); h:`int$(); cb:`symbol$())}
addObserver:{insert[`.cx.observers;(x;.z.w;y)]}
notify:{(neg x[`h]) (x[`cb];x[`subject];(value x[`subject]));}
notifyAll:{notify each (select from .cx.observers where subject=x)}
changeValue:{.[x;();:;y];notifyAll[x];}
dropObserver:{delete from .cx.observers where subject=x,h=y}

/ Client (Observer)
subscribe:{subscribeWithCallback[x;y;`cb]}
subscribeWithCallback:{y "addObserver[`", (string x), ";`", (string z), "]"}
cb:{.[x;();:;y]}

/ Primes
isPrime:{not (|/) {{0.=(y%x) - floor y%x}[;x] 2.0 + til ceiling sqrt x}[x]}
isPrime:{not (|/) {{0.=(y%x) - floor y%x}[;x] 2.0 + til ceiling sqrt x}[x]}
isPrime:{not (|/) {{0.=r - floor r:y%x}[;x] 2.0 + til ceiling sqrt x}[x]}
isPrime:{not {0.=(y%x) - floor y%x}[;x] |/ [2.0 + til ceiling sqrt x]}

/ update stats
timing:([u:`symbol$(); r:`symbol$()] t:`timespan$())
`timing upsert (.z.u;`a;(exec t from select from timing where u=`rs,r=`a)[0]+.z.N-t)
$[null f:(exec t from select from timing where u=`rs,r=`d)[0];0;f]

/ LCS
X:"AGCAT"
Y:"GAC"
C:(til 1+count Y) {[x;y] 0}/:\: (til 1+count X)
LCS:{[C;X;Y;i;j] 0N! (X[i];Y[j]); $[X[i]=Y[j];C[i-1;j-1]+1;C[i;j-1]|C[i-1;j]]}

/ search path
c:{[d;f] not (count fl) = (fl:key d) ? f}
` sv (p[first where c[;`FOO]@'p];`FOO)
fnd:{[sp;f]
  has:{[d;f] not (count fl) = (fl:key d) ? f};
  ` sv (sp[first where has[;f]@'sp];f)
  }

fnd2:{[sp;f] ` sv (sp[first where {not (count fl)=(fl:key x)?y}[;f]@'sp];f) }
fnd3:{[sp;f] $[not ()~key 0N! p:` sv (sp[first where {not ()~key ` sv (x;y)}[;f]@'sp];f)];p;0N! `fail }
ld:{[sp;f] system "l ", 1_string fnd3[sp;f]}

fnd3:{[sp;f] ` sv (sp[first where {not ()~key ` sv (x;y)}[;f]@'sp];f)}
ld:{[sp;f] if[not ()~key p:fnd3[sp;f]; system "l ", 1_string p]}

fnd4:{[sp;f] $[not () ~ key p:` sv (sp[first where {not ()~key ` sv (x;y)}[;f]@'sp];f);p;()]}
ld4:{[sp;f] if[not ()~ p:fnd4[sp;f]; system "l ", 1_string p]}

