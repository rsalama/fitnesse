l:(("name";"pass?";"msgs";"eval";"pass"); ("T1";"true";"Foo1";"1+1";"{2~x}"); ("T2";"true";"Foo2";"(`a`b`c)!(1 2 3)";"{x}"); ("T3";"true";"Foo3";"til 10";"{count x}"); ("T4";"true";"Foo4";"([] dt: 2010.01.01+til 365; tkr: 365?`3; tp: 365?100. )";"{x}"))
T: update NAME:`$NAME, MSGS:`$MSGS from flip (`$upper l[0])! flip l[1+til (-1+count l)]
\p 5000

C:()
cb:{C::C,0N! .z.w; if[(count C)=count T;arun[C;T]]}

result:{
  h: hopen 0N! ` sv (`:/tmp/rs; x[`MSGS]);
  (neg h) "{{{";
  (neg h) "Name: ", string x`NAME;
  (neg h) "Date: ", string .z.d;
  (neg h) "----";
  (neg h) "Results:\n";
  (neg h) .Q.s first x`evr;
  (neg h) "----";
  (neg h) "Pass?:\n", .Q.s first x`pr;
  (neg h) "----";
  (neg h) "}}}"; }

mkproc:{system 0N! " " sv("screen -d -m -S"; string x; "/home/rs/bin/q"; "/home/rs/q/cb.q"; "-p"; string x; string y; "& "); }

setup:{ps: mkproc[;5000] each (1+5000) + til count T;}

arun:{[ps;jt]
  ps {[h;x] (neg h)({(neg .z.w)(y;(`rp`NAME`MSGS`evr`pr)!(.z.i;x`NAME;x`MSGS;enlist u;enlist (value x[`PASS])@u:(value x[`EVAL])))};x;`result)}' jt; }

teardown:{hclose each C; C::();}