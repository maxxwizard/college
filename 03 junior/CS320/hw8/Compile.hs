----------------------------------------------------------------
-- Computer Science 320 (Fall, 2009)
-- Concepts of Programming Languages
--
-- Assignments 6, 7, and 8
--   Compile.hs

----------------------------------------------------------------
-- Compiler for Mini-Haskell.

module Compile  where --(compile)

import Monad
import Env
import Err
import Exp
import Machine

memlocs :: [MemLoc]
memlocs = locs 0 where locs n = n:locs (n+1)

labels :: [Label]
labels = labs 0 where labs n = ("L"++show n):labs (n+1)

compile e = (ml,mc)
  where (_,(ml,mc)) = (\(Fresh f) -> 
                      f (emptyEnv, memlocs, labels)) $ (comp e)

data Fresh a = 
  Fresh ((Env MemLoc, [MemLoc], [Label]) 
          -> ((Env MemLoc, [MemLoc], [Label]), a))

comp :: Exp -> Fresh (MemLoc, [Instruction])
comp _ = Fresh (\(env,ms,ls) -> ((env,ms,ls), (-1, [Pass])))

--eof
