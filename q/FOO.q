\d .util
CONFROOT:"/home/rs/q";
\d .

rdConfig:{[hdr;dir;fname] (hdr;enlist ",") 0: `$"/" sv (dir;fname) }
rdConfig:{.[x;(y;.util.CONFROOT;z);`invalid]}[rdConfig]

entl:rdConfig["SSSS"; "entitlements.csv"];
actgrps:rdConfig["SS"; "actiongroups.csv"];
usrgrps:rdConfig["SSB"; "usergroups.csv"];

/ dm:`domain`entitlements`actiongroups`usergroups!(`TEST; entl;actgrps;usrgrps)
/ dm:`domain`entitlements`actiongroups`usergroups!(`TEST; rdConfig["SSSS"; "entitlements.csv"]; rdConfig["SS"; "actiongroups.csv"]; rdConfig["SSB"; "usergroups.csv"])
dm:`domain!(`TEST)
dm,:`entitlements(rdConfig["SSSS"; "entitlements.csv"])
dm,:`actiongroups(rdConfig["SS"; "actiongroups.csv"])
dm,:`usergroups:rdConfig["SSB"; "usergroups.csv"];
