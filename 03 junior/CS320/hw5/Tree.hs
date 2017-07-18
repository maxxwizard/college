-- Matthew Huynh
-- CS320 - Kfoury
-- Assignment #5

module Tree where
	import Unify
	import Data.List
	import Equation
	
	data Tree = Leaf | Node Tree Tree | Var String
		deriving (Eq, Show)
	
	--4.c
	instance Substitutable Tree where
		subst s Leaf = Leaf
		subst s (Node l r) = Node (subst s l) (subst s r)
		subst s (Var x) = case get x s of
							Nothing -> Var x
							Just val -> val
		vars Leaf = []
		vars (Node l r) = (vars l) ++ (vars r)
		vars (Var x) = [x]
		solved subs = if (concatMap vars [snd s | s <- subs] == []) then True else False
		reduce subs = [ ((fst y), subst s' (snd y)) | y <- subs]
			where s' = [s | s <- subs, vars (snd s) == []]
	
	--4.d
	conflictExists :: Subst Tree -> Bool
	conflictExists subs = foldr (||) False [(fst x == fst y) && (snd x /= snd y) | x <- subs, y <- subs]
	multiReduce :: Subst Tree -> Subst Tree
	multiReduce s = if (nub s) == (reduce (nub s)) then (nub s) else (reduce (nub s))
	
	instance Unifiable Tree where
		unify (Node t1 t2) (Node t3 t4) = Just ( extract (unify t1 t3) ++ extract (unify t2 t4) )
			where extract (Just n) = n
			      extract Nothing = []
		unify (Var x) n = Just (sub x n)
		unify n (Var x) = Just (sub x n)
		unify t1 t2 = if t1 == t2 then Just emp else Nothing
		combine Nothing _ = Nothing
		combine _ Nothing = Nothing
		combine (Just s1) (Just s2) = Just (s1 ++ s2)
		--5
		resolve Nothing = Nothing
		resolve (Just subs) | (not (conflictExists sol)) && solved sol = Just sol
							| otherwise = Nothing
							where sol = (multiReduce subs)
	
	e0 = Node (Node (Node (Var "x") (Var "y")) (Node (Var "y") (Var "x"))) (Var "z")
		`Equals`
		 Node (Node (Node Leaf (Var "z")) (Node Leaf (Var "y"))) (Var "x")
	e1 = let f b 0 = b
		 f b n = Node (f b (n-1)) (f b (n-1))
		 in f (Var "x") 10 `Equals` f Leaf 13
	e2 = [ (Var "z") `Equals` Leaf
		 , Node (Var "y") Leaf `Equals` Node Leaf (Var "x")
		 , (Var "x") `Equals` Node (Var "z") (Var "z") ]
	e3 = Leaf `Equals` Leaf
	e4 = Node Leaf Leaf `Equals` Var "x"
	
		--not enough information to solve
	e5 = Node Leaf (Var "y") `Equals` (Var "x")
	
		--misalignment, so this can't be solved
	e6 = Node (Node Leaf Leaf) Leaf `Equals` Node Leaf (Var "x")
	
		--any solution has conflict, so this can't be solved
	e7 = Node (Node Leaf Leaf) (Var "x") `Equals` Node (Var "x") Leaf
	e8 = Node (Node (Var "z") (Node Leaf Leaf)) Leaf `Equals` Node (Node (Node Leaf Leaf) (Var "z")) Leaf
	e9 = Node (Var "x") (Node (Var "y") (Var "z")) `Equals` Node (Var "z") (Node Leaf (Var "y"))
	e10 = (Node (Var "x") (Node (Var "y") (Node (Var "z") Leaf))) `Equals` (Node (Node (Var "y") (Leaf)) (Node Leaf (Node Leaf Leaf)))
	
	--solutions
	s0 = Nothing
	s1 = Just [("x",Node (Node (Node Leaf Leaf) (Node Leaf Leaf)) (Node (Node Leaf Leaf) (Node Leaf Leaf)))]
	--s2 = 