l:(("name";"pass?";"msgs";"eval";"pass"); ("T1";"true";"Foo1";"1+1";"{2~x}"); ("T2";"true";"Foo2";"(`a`b`c)!(1 2 3)";"{x}"); ("T3";"true";"Foo3";"til 10";"{count x}"); ("T4";"true";"Foo4";"([] dt: 2010.01.01+til 365; tkr: 365?`3; tp: 365?100. )";"{x}"))
t: update NAME:`$NAME, MSGS:`$MSGS from flip (`$upper l[0])! flip l[1+til (-1+count l)]

fresult:{(0N! ` sv (`:/tmp; x[`MSGS])) 0: enlist .Q.s x; hclose .z.w; }

mkproc:{system 0N! " " sv("screen -d -m -S"; string x; "/home/rs/bin/q"; "-p"; string x; "& "); system "sleep 1"; h: hopen `$"::",string x; h ".z.pc:{exit 0}"; h}

frun:{[jt]
  ps: mkproc each 5000 + til count jt;
  ps {[h;x] (neg h)({(neg .z.w)(y;(`rp`NAME`MSGS`evr`pr)!(.z.i;x`NAME;x`MSGS;enlist u;enlist (value x[`PASS])@u:(value x[`EVAL])))};x;`fresult)}' jt; }

d: (`NAME`PC`MSGS`EVAL`PASS)!(`T4;`true;`Foo4;"([] dt: 2010.01.01+til 365; tkr: 365?`3; tp: 365?100. )";"{x}")

execute:{[d]
  u:(value string d[`EVAL]);
  p:(value string d[`PASS])@u;
  / r:(`NAME`S)!(d`NAME;p);
  r:(`NAME`EVAL`PASS`S)!(d`NAME;string d`EVAL;string d`PASS;p);
  r }