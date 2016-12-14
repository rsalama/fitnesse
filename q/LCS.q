/ Implements Longest Common Sequence for comparing tables
/ tdiff[t1;t2] compares two tables; assumes both tables have the same columns, prepends an `op column which contains:
/ `+ if a row needs to be added
/ `- if a row needs to be removed
/ `  if a row is part of the longest common sequence

/ init matrix
C:()

LCS:{[L;R;x;y] i:x+1;j:y+1; C[i;j]: $[L[x]~R[y]; C[i-1;j-1]+1; C[i;j-1] | C[i-1;j]];C[i;j] }

printTDiff:{[C;L;R;i;j]
  x:i-1; y:j-1;
  / 0N! ((),L[x]; (),R[y]; (i;j),(x;y); C[i;j]);
  $[all (i>0;j>0;L[x]~R[y]);
    [ printTDiff[C;L;R;i-1;j-1 ],  (L[(),x]+([] op:(),`)) ];
    (j>0) & ((i=0) | C[i;j-1] >= C[i-1;j]);
      [ printTDiff[C;L;R;i;j-1],  (R[(),y]+([] op:(),`$"+")) ];
    (i>0) & ((j=0) | C[i;j-1] < C[i-1;j]);
      [ printTDiff[C;L;R;i-1;j], (L[(),x]+([] op:(),`$"-")) ];
    [ (delete from L)+([] op:`symbol$()) ] ]
  }

tdiff:{[L;R]
  C::(til 1+count L) {[x;y] 0}/:\: (til 1+count R) ;
  (til (count L)) LCS[L;R;;]/:\: (til (count R));
  show C;
  `op xcols printTDiff[C;L;R;count L; count R]
  }

/ test
t1:([] k:(-20?`2); c1:(-20?100); c2:(10 + -20?100))
t2: t1 _/  (-10?10)
t2,: ([] k:(-5?`2); c1:(-5?100); c2:(10 + -5?100))

\c 50 120
show tdiff[t1;t2]

