-- Matthew Huynh
-- CS320 - Kfoury
-- Assignment #5

module Natural where
	import Unify
	import Data.List
	import Equation
	
	data Natural = Zero | Succ Natural | Var String
		deriving (Eq, Show)
	
	--4.a
	instance Substitutable Natural where
		subst s Zero = Zero
		subst s (Succ n) = Succ (subst s n)
		subst s (Var x) = case get x s of
							Nothing -> Var x
							Just val -> val
		vars Zero = []
		vars (Succ n) = vars n
		vars (Var x) = [x]
		solved [] = True
		solved subs = if (concatMap vars [snd s | s <- subs] == []) then True else False
		reduce subs = [ ((fst y), subst s' (snd y)) | y <- subs]
			where s' = [s | s <- subs, vars (snd s) == []]
		
	--4.b
	conflictExists :: Subst Natural -> Bool
	conflictExists subs = foldr (||) False [(fst x == fst y) && (snd x /= snd y) | x <- subs, y <- subs]
	multiReduce :: Subst Natural -> Subst Natural
	multiReduce s = if (nub s) == (reduce (nub s)) then (nub s) else (reduce (nub s))
	
	instance Unifiable Natural where
		unify (Var x) Zero	= Just (sub x Zero)
		unify Zero	(Var x)	= Just (sub x Zero)
		unify (Var x) (Succ n) = Just (sub x (Succ n))
		unify (Succ n) (Var x) = Just (sub x (Succ n))
		unify (Succ x) (Succ y) = unify x y
		unify x 	y		= if x == y then Just emp else Nothing
		combine Nothing _ = Nothing
		combine _ Nothing = Nothing
		combine (Just s1) (Just s2) = Just (s1 ++ s2)
		--5
		resolve Nothing = Nothing
		resolve (Just subs) | (not (conflictExists sol)) && solved sol = Just sol
							| otherwise = Nothing
							where sol = (multiReduce subs)