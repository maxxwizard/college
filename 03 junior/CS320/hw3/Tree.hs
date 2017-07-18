-- Matthew Huynh
-- CS320 - Kfoury
-- Assignment #3

--1.a, 1.b
data Tree a = Leaf | Node a (Tree a) (Tree a)
	deriving (Show, Eq)
	
testTree = Leaf
testTree1 = Node 1 Leaf Leaf
testTree2 = Node 1 Leaf (Node 2 Leaf Leaf)
testTree3 = Node 1 Leaf (Node 2 Leaf (Node 3 Leaf Leaf))
testTree4 = Node 1 (Node 2 Leaf Leaf) (Node 3 Leaf Leaf)

--1.d
foldT :: (a -> b -> b -> b) -> b -> Tree a -> b
foldT f g (Leaf) = g
foldT f g (Node x t1 t2) = f x (foldT f g t1) (foldT f g t2)

--1.c
mapT :: (a -> b) -> Tree a -> Tree b
mapT f = foldT (Node . f) (Leaf)

--2.a
leafCount :: Tree a -> Integer
leafCount = foldT step 1
			where
			step x m n = m + n
			
--2.b
nodeCount :: Tree a -> Integer
nodeCount = foldT step 0
			where
			step x m n = (1 + m) + n
			
--2.c
height :: Tree a -> Integer
height = foldT step 0
	 where
	 step x m n = 1 + (max m n)

--3.a
perfect :: Tree a -> Bool
perfect Leaf = True
perfect (Node x m n) = (leafCount t + nodeCount t) == ((2^((height t)+1))-1)
						where t = (Node x m n)

--3.b
degenerate :: Tree a -> Bool
degenerate Leaf = False
degenerate (Node x m n) = (nodeCount (Node x m n) <= height (Node x m n))

--3.c
list :: Tree a -> Maybe [a]
list (Node x m n) 	| degenerate (Node x m n) = Just (foldT (+++) [] (Node x m n))		      
					| otherwise = Nothing
					where (+++) x y z = [x] ++ y ++ z

--4.c
--1) foldT allows for shorter and more elegant code to collapse the tree
--   (i.e. performing an operation that involves all the nodes in the tree).
--2) Higher-functions can be used in distributed/parallel computing because
--   the inner function is repeated many times so different machines can
--   process different parts of the computation.