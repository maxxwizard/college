Matthew Huynh
CS320 Fall 2009
Assignment #1

1.a

> prefix 1 (x : xs) = [x]
> prefix n (x : xs) = x : (prefix (n-1) xs)
> prefix _ []  = []

1.b

> suffix _ []  = []
> suffix 0 lst = lst
> suffix n lst = suffix (n-1) (tail lst)

1.c

> split 0 x xs = xs
> split n x [] = []
> split n x xs = (prefix n xs) ++ [x] ++ (split n x (suffix n xs))

2.a

> plane r = [ (x/r, y/r) | y <- [-1*r .. 1*r], x <- [-2*r .. 1*r] ]

2.b

> bar (a,b) f = (a,b) : bar (f (a,b)) f
> pp (x,y) (u,v) = (u*u - v*v + x, 2*u*v + y)
> orbit (x,y) = bar (0,0) (pp (x,y))

2.c

> disp d [] = ' '
> disp d xs = if (fst (head xs) > d) then (snd (head xs))
>			  else disp d (tail xs)

2.d

> norm (x,y) = x*x + y*y
> distances r i = map norm [ (orbit x) !! i | x <- plane r ]
> mandelbrot r i l = split ((3*r)+1) '\n' list
>				     where list = [ disp d l | d <- (distances r i) ]

3.a
to compile: javac Mandelbrot.java
to execute: java Mandelbrot r i

3.b
In Java, I had to create a custom class named Pair in order to replicate
the built-in tuple functionality in Haskell. This led to much more code
and errors as I had to make sure my custom class was working properly.

When I was coding in Java, I no longer thought in terms of recursion - or
rather, it was hard to. I was frustrated at not being able to simply
type a function's definition using the pattern matching form for easy
recursion programming.

Thirdly, in Java, I could not test and debug easily. There was no interpreter
for me to send test calls to my methods/functions easily.
