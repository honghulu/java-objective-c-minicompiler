#include <stdio.h>
#include <inttypes.h>

int main(int argc, char **argv){
int64_t reserved = 0;
int64_t r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0, va = 0;
int64_t stack[100];
int64_t *sp = &stack[99];
int64_t *fp = &stack[99];
int64_t *ra = &&exit;
goto mainEntry;
five:
sp = sp - 2;
*(sp+2) = fp;
*(sp+1) = ra;
fp = sp;
sp = sp - 4;
r1 = *(fp+7);
r2 = *(fp+6);
r3 = r1 + r2;
*(fp-1) = r3;
r1 = *(fp-1);
r2 = *(fp+5);
r3 = r1 + r2;
*(fp-2) = r3;
r1 = *(fp-2);
r2 = *(fp+4);
r3 = r1 + r2;
*(fp-3) = r3;
r1 = *(fp-3);
r2 = *(fp+3);
r3 = r1 + r2;
*(fp-4) = r3;
va = *(fp-4);
sp = sp + 4;
fp = *(sp+2);
ra = *(sp+1);
sp = sp + 2;
goto *ra;
main:
sp = sp - 2;
*(sp+2) = fp;
*(sp+1) = ra;
fp = sp;
sp = sp - 3;
sp = sp - 1;
*(sp+1) = *(fp+7);
sp = sp - 1;
*(sp+1) = *(fp+6);
sp = sp - 1;
*(sp+1) = *(fp+5);
sp = sp - 1;
*(sp+1) = *(fp+4);
sp = sp - 1;
*(sp+1) = *(fp+3);
ra = &&retLabel0;
goto five;
retLabel0:
sp = sp + 5;
*(fp-1) = va;
r1 = 5;
*(fp-2) = r1;
r1 = *(fp-1);
r2 = *(fp-2);
r3 = r1 + r2;
*(fp-3) = r3;
va = *(fp-3);
sp = sp + 3;
fp = *(sp+2);
ra = *(sp+1);
sp = sp + 2;
goto *ra;
mainEntry:
sp = sp - 2;
*(sp+2) = fp;
*(sp+1) = ra;
fp = sp;
sp = sp - 1;
r1 = 1;
*(fp-1) = r1;
sp = sp - 1;
*(sp+1) = *(fp-1);
r1 = 2;
*(fp-1) = r1;
sp = sp - 1;
*(sp+1) = *(fp-1);
r1 = 3;
*(fp-1) = r1;
sp = sp - 1;
*(sp+1) = *(fp-1);
r1 = 4;
*(fp-1) = r1;
sp = sp - 1;
*(sp+1) = *(fp-1);
r1 = 5;
*(fp-1) = r1;
sp = sp - 1;
*(sp+1) = *(fp-1);
ra = &&retLabel1;
goto main;
retLabel1:
sp = sp + 5;
*(fp-1) = va;
r1 = *(fp-1);
reserved = r1;
sp = sp + 1;
fp = *(sp+2);
ra = *(sp+1);
sp = sp + 2;
goto *ra;
exit:
return reserved;
}
