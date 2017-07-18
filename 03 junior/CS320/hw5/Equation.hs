-- Matthew Huynh
-- CS320 - Kfoury
-- Assignment #5

module Equation where
	import Unify
	
	data Equation a = a `Equals` a
	
	--6.a
	solveEqn :: (Unifiable a, Eq a, Substitutable a) => Equation a -> Maybe (Subst a)
	solveEqn (eq1 `Equals` eq2) = resolve (unify eq1 eq2)
	
	--6.b
	--solveSystem :: (Unifiable a, Eq a, Substitutable a) => [Equation a] -> Maybe (Subst a)
	--solveSystem eqs =  [solve' e1 e2 | e1 <- eqs, e2 <- eqs]