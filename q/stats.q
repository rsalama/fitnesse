\d .kdbstats

statistics:([user:`symbol$(); resource:`symbol$()] cnt:`int$(); time:`timespan$())
fname:{[h;p;dir] `$ "" sv (enlist ":"; dir;enlist "/"; "Stats-"; string h; enlist ":"; string p; ".bin")};

// Update user Statistics
/ updateStats:{[reArgs;res]
updateStats:{[u;r;t]
  incr:{$[null x[0]; y; x[0]+y]}; / TODO -- get rid of [0]
  / u:reArgs[`user];
  / r:reArgs[`calltree][1]; / TODO - bug handle when `targ is `
  f:exec cnt,time from select from .kdbstats.statistics where user=u,resource=r;
  / 0N! ("f: "; f);
  `.kdbstats.statistics upsert ret:(u; r; incr[f[`cnt]; 1]; incr[f[`time]; t]);
  / 0N! "<<< updateStats"; 
  }

/ Persist statistics -- use port number to compose file name
persistStats:{
  0N! ">>> persistStats"; / TODO -- pass directory?
  dir:"/tmp";
  (fn:fname[.z.h;value "\\p";dir]) set .kdbstats.statistics; 
  0N! fn;
  0N! "<<< persistStats";
  }

readStats:{
  dir:"/tmp";
  get (fn:fname[.z.h;value "\\p";dir])  }

clearStats:{ / Only for testing, needs to be blocked
  delete from `.kdbstats.statistics where resource in exec distinct resource from .kdbstats.statistics;}

mergeStats: {
  0+/{get `$x} each raze { enlist ":", x} each system "ls /tmp/Stat-*"
  }  

usageReport: {
  }

rpcReport: {
  }

/ {.kdbstats.updateStats[.z.u;x;.z.N-t]} each l[ 10?count l:`a`b`c]
/ d:{.kdbstats.fname[.z.h;x;"/tmp"]} each 6010 + til 4
/ {x set .kdbstats.statistics} each d
/ 0+/{get x} each d
doall:{
  t:.z.N;
  rsrc:((`a`b`c);(`i`j`k);(`x`y`z));
  pbase:(6010;6020;6030);
  rpmap:(6010;6020;6030)!((`a`b`c);(`i`j`k);(`x`y`z));
  genstats:{{.kdbstats.updateStats[.z.u;x;.z.N-t]} each x[ 10?count x]};
  genfiles:{{.kdbstats.fname[.z.h;x;"/tmp"]} each x + til y};
  savestats:{{x set .kdbstats.statistics} each x};

  / {clearStats[]; {genstats[x]} each x} each rsrc;
  {genstats[x]} each  rsrc;
  }

dooneA:{
  t:.z.N;
  N:4;

  clearStats[];
  {.kdbstats.updateStats[.z.u;x;.z.N-t]} each x;
  {x set .kdbstats.statistics} each {.kdbstats.fname[.z.h;x;"/tmp"] } each y + til N;
  }

doone:{
  t:.z.N;

  clearStats[];
  {.kdbstats.updateStats[.z.u;x;.z.N-t]} each x;
  {x set .kdbstats.statistics} each {.kdbstats.fname[.z.h;x;"/tmp"] } each y;
  }

t:.z.N;

rsrc:((`a`b`c);(`i`j`k);(`x`y`z));
/
g:{{ x[10?count x] } each x}
f:{.kdbstats.updateStats[.z.u;x;y]} 
ff:{{f[x;.z.N-t]} each x}
{ff[x]} each g[rsrc]
{{.kdbstats.updateStats[.z.u;x;.z.N-t]} each x} each {x[10?count x]} each rsrc
rpmap:(6010;6020;6030)!((`a`b`c`d);(`i`j`k`l`m`n);(`x`y`z));
rpmap:(6010;6020;6030;6040)!((`a`b`c`d);(`i`j`k`l`m`n);(`x`y`z);(`a;`i;`x));
(value rpmap) {.kdbstats.doone1[x;y]}' key rpmap

(value rpmap) {.kdbstats.doone[x;y]}' {x + til 4} each key rpmap

{`$ ":", x} each  system "ls /tmp/Stats-*"
0+/{get x} each d

t:0+/{get x} each {.kdbstats.fname[.z.h;x;"/tmp"]} each raze {x + til 4} each key rpmap

(count t)~count distinct raze value rpmap
(exec resource from t) ~ distinct raze value rpmap

\