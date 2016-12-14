/ (5 5 5 7 7 9 9 9 11 15 15)\[0]
s: " " vs "-a 3 -b foo -longopt host -init"
() {$[y like "-*"; y; x," ",y]}\ s

l:(("name";"pass?";"msgs";"eval";"pass");
("T1";"true";"Foo1";"1+1";"{2~x}");
("T2";"true";"Foo2";"2+2";"{4~x}");
("T3";"true";"Foo";"1+1";"{2~x}"))

/ Fixture data struct
l1:(("name";"pass?";"msgs";"eval";"pass"); ("T1";"true";"Foo1";"1+1";"{2~x}"); ("T2";"true";"Foo2";"2+2";"{4~x}"); ("T3";"true";"Foo3";"4+4";"{8~x}"))
l:(("name";"pass?";"msgs";"eval";"pass"); ("T1";"true";"Foo1";"1+1";"{2~x}"); ("T2";"true";"Foo2";"(`a`b`c)!(1 2 3)";"{x}"); ("T3";"true";"Foo3";"til 10";"{count x}"); ("T4";"true";"Foo4";"([] dt: 2010.01.01+til 365; tkr: 365?`3; tp: 365?100. )";"{x}"))
t: update NAME:`$NAME, MSGS:`$MSGS from flip (`$upper l[0])! flip l[1+til (-1+count l)]

{(value x[`PASS])@value x[`EVAL]} each t

/ callbacks
echo:{0N! (.z.i;x)}
(neg h) ({(neg .z.w) (z; x*y)}; 6; 7; `echo)

r:t[0]
/ value executed on client
(neg h) ({(neg .z.w) (z; (u;x@u:y))}; (value r`PASS); (value r`EVAL); `echo)

/ value executed on server
(neg h) ({(neg .z.w) (z; (u;(value x)@u:(value y)))}; r`PASS; r`EVAL; `echo)

p {[h;x] (neg h) ({(neg .z.w) (z; (.z.i;u;(value x)@u:(value y)))}; x`PASS; x`EVAL; `echo)}' t

p {[h;x] (neg h) ({(neg .z.w) (y; (.z.i;x`NAME;u;(value x[`PASS])@u:(value x[`EVAL])))}; x; `echo)}' t

echo:{show .z.i; show x}
result:{`R insert x}

p: hopen each (`::5000;`::5010;`::5020;`::5040)
p {[h;x] (neg h) ({(neg .z.w) (y; (`rp`NAME`evr`pr)!(.z.i;x`NAME;enlist u;enlist (value x[`PASS])@u:(value x[`EVAL])))}; x; `result)}' t

R:([] NAME:(); `int$rp:();  evr:(); pr:())
t^R

// .z.ps:{echo x@1;(x@0)[x@1]}

t:([] vd:2010.01.01 + til 10;id:10#`IBM.N;cp: 10?100.0)
u:([] vd:2010.01.01 + til 10;id:10#`GS.N;cp: 10?100.)
v:`vd xasc `vd`id xkey t uj u
ids:([] id:n; mid:(neg count n:exec distinct id from v)?`3)
d: (ids[`id])!ids[`mid]
update mid:d[id] from v
![v;();0b;(enlist `mid)!enlist (d;`id)]
![v;();0b;(enlist `mid)!enlist ((ids[`id])!ids[`mid];`id)]

d:(exec id from ids)!exec mid from ids

`:/tmp/file.dat 0: string 200000?`8

tab:`vd`id xkey ([] vd: 2010.01.01+til 360; id: 360?`ibm`csco`intc`sunw;ex:360?`n`l;p_n:360?11.;p_l:20+360?10.)
update p:?[ex=`l;p_l;p_n] from t
l1 {(x;y)}'l2
zip:{flip(x;y)}

tab2:`vd`id xkey ([] vd: 2010.01.01+til 360; id: 360?`ibm`csco`intc`sunw;ex:360?`n`l`u;p_n:360?11.;p_l:20+360?10.;p_u:30+360?10.)
update p:?[ex=`l;p_l;?[ex=`n;p_n;p_u]] from tab2

n:10
t:() xkey tab3:`vd`id xkey ([] vd: 2010.01.01+til n; id: n?`ibm`csco`intc`sunw;ex:n?`n`l`u;p_n:n?11.;p_l:20+n?10.;p_u:30+n?10.;p:40+n?10.;a_n:n?11.;a_l:20+n?10.;a_u:30+n?10.;a:40+n?10.)
update pR:?[ex=`l;p_l;?[ex=`n;p_n;?[ex=`u;p_u;p]]] from tab3
u:flip (`n`l`u) {?[x=t`ex;y;(count t)#0n]}' t[`p_n`p_l`p_u]
v:({where x<>0n} each u) {y x}' u

u: (`n`l`u) {?[x=t`ex;y;t`p]}' t[`p_n`p_l`p_u]
