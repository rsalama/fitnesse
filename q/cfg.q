xlate: (`mskey`skey`port`stype`grpID)!("S";"S";"I";"S";"S")
split: {[str;pat] (first l)!(trim last l:("S",pat,";" ) 0: str)}
xsplit:{[str;pat;xlt] 
  l:raze ("S",pat,";" ) 0: str; 
  k: first l; v: last l;
  (enlist k)!(enlist $[xlt[k] in ("C";" "); trim v; xlt[k]$v]) }

rdConfig:{[fn;vals;xlt]
  z: trim read0 fn;
  z: z where all not z like/: ("#*";""); / Read file, skip comments and blank lines
  v: raze split[;"="] @' (system "env"), read0 vals;   / env + properties file into dict 
  v: flip (key v;value v);                             / convert back to list of key value pairs
  z: z {ssr[; "${",(string y@0),"}"; y@1] @' x}/ v;    / substitute ${*}'s
  inds: where any z like/: ("Start";"End"),\: "Config*";/ find block start/end
  inds: ((count inds) div 2;2)#inds;                   / reshape inds, point to blocks
  inds: .[inds;(til count inds;0);+;1];                / index start and end of blocks
  z: z[{x[0] + til -/[reverse x]} @' inds];            / create list of blocks
  / merge lines not starting with = with previous line (scan {...}\)
  / apply split to each line (returns a dict), use xlt to xlate to right type
  / merge all dicts into a table (raze)
  t: {[x; xlt] raze xsplit[;"=";xlt] @' {$[not y like "*=*"; x," ",y; y]}\ [x]}[;xlt] @' z; 
  t }

/ t:raze {rdConfig[x;`:config.properties;xlate]} @' `$system "ls config*.txt"